package com.foxlinkimage.alex.fragmenttab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alex on 2015/3/11.
 */
public class ScanFrag extends Fragment {
    static final String CMD_GET_STATUS = "getstatus";
    static final String CMD_DO_SCAN = "doscan";
    static final String CMD_GET_IMAGE_COUNT = "getimagecount";
    static final String CMD_GET_IMAGE = "getimage";

    static final String RESP_NotReady = "NotReady";
    static final String RESP_Busy = "Busy";
    static final String RESP_Ready = "Ready";
    static final String RESP_OK = "ok";

    final static String DEBUG_TAG = "HIDDEN";

    HttpClient httpClient;
    HttpGet httpGet;
    HttpParams httpParams;  //存放連線的設定參數
    HttpResponse httpResponse;
    HttpEntity httpEntity;

    int iImgCount;

    SharedPreferences spDefaultSetting;
    String strIP;
    String strRootFolderPath;
    String strResultMsgSend;
    TextView tvResult;
    Button btnScan;
    ProgressBar progressBar;

    public ScanFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "Scan onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_scan, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "Scan onViewCreated()");
    /*
        * The method getSharedPreferences is a method of the Context object, so just calling getSharedPreferences from a Fragment will not work...
        * because it is not a Context! (Activity is an extension of Context, so we can call getSharedPreferences from it).
        */
        spDefaultSetting = getActivity().getSharedPreferences(SettingFrag.SHARED_PREF, 0);
        strIP = spDefaultSetting.getString("IP", "192.168.1.1"); //192.168.1.1為如果抓不到資料所設定的預設值

        strRootFolderPath = spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder");

        tvResult = (TextView) view.findViewById(R.id.txtResult);
        btnScan = (Button) view.findViewById(R.id.btnScan);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setProgress(0);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoScanTask GetStatus = new DoScanTask();
                GetStatus.execute(CMD_GET_STATUS, CMD_DO_SCAN, CMD_GET_IMAGE_COUNT);

                if(iImgCount != 0){
                    DownloadImageTask downloadImageTask = new DownloadImageTask();
                    downloadImageTask.execute(CMD_GET_IMAGE);
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    class DoScanTask extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... params) {
            /// /取得手機的WIFI是否有開啟
            ConnectivityManager mConnMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnMgr.getActiveNetworkInfo() == null) {
                Toast.makeText(getActivity(), "No network service...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            } else {
                //執行工作:  getstatus, doscan, getimagecount
                for (int i = 0; i < params.length; i++) {
                    if (!isCancelled()) {
                        httpClient = new DefaultHttpClient();
                        //執行非doscan的判斷式 (httpGet status, getimagecount)
                        if (!params[i].equals(CMD_DO_SCAN)) {
                            strResultMsgSend = getReturnMessage(params[i]);
                            if (strResultMsgSend.equals(RESP_NotReady) || strResultMsgSend.equals(RESP_Busy)) {
                                publishProgress(strResultMsgSend + "機台可能正在掃描中或未插入文件, 請檢查無誤後再次按下Scan...");
                                this.cancel(true);  //發現機器沒紙或者是正在掃描, 將task停止
                                break;
                            } else if (strResultMsgSend.equals(RESP_Ready)) {
                                publishProgress(strResultMsgSend);
                            }
                            //執行getimagecount判斷式
                            if (params[i].equals(CMD_GET_IMAGE_COUNT)) {
                                iImgCount = Integer.valueOf(strResultMsgSend);
                            }
                        } else //執行doscan的判斷式
                        {
                            strResultMsgSend = getReturnMessage(params[i]);
                            if (strResultMsgSend.equals(RESP_OK)) {
                                publishProgress(strResultMsgSend + " : 掃描開始");
                                Boolean tag = true;
                                while (tag) {
                                    //每隔兩秒, poll status一次
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException ex) {
                                        publishProgress(ex.getMessage());
                                    }
                                    //httpGet status
                                    strResultMsgSend = getReturnMessage(params[0]);
                                    publishProgress(strResultMsgSend + " : 裝置正在掃描中.....");
                                    if (!strResultMsgSend.equals(RESP_Busy))    //如果裝置已經不是非Busy狀態, 表示掃完狀態 ,停止polling
                                    {
                                        tag = false;
                                        //需delay一段時間(設定8秒), 等待掃描器儲存完所有圖片, 才可以取得張數, 否則都會得到0
                                        try {
                                            Thread.sleep(8000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                publishProgress(strResultMsgSend + " , 停止掃描工作");
                                this.cancel(true);
                                break;
                            }

                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tvResult.setText(tvResult.getText() + "\n" + values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tvResult.setText(tvResult.getText() + "\n" + "開始下載圖片, 請耐心等待...");
        }
    }

    String getReturnMessage(String params) {
        String strReturnMessage;
        try {
            httpGet = new HttpGet("http://" + strIP + "/cgi-bin/" + params + ".cgi?");
            httpParams = httpGet.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            strReturnMessage = EntityUtils.toString(httpEntity).trim();
        } catch (ClientProtocolException ex) {
            strReturnMessage = ex.getMessage() + " Timeout!! 請檢查IP設定或裝置連線狀態.";
        } catch (IOException ex) {
            strReturnMessage = ex.getMessage();
        }
        return strReturnMessage;
    }

    class DownloadImageTask extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //建立子task資料夾
            String timestamp = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN).format(new Date());
            File fileRootFolder = new File(strRootFolderPath);
            File fileSubTaskFolder = new File(fileRootFolder + "/" + timestamp);
            if (!fileSubTaskFolder.exists()) {
                fileSubTaskFolder.mkdirs();
            }

            //下載
            for (int i = 0; i < iImgCount; i++) {
                httpClient = new DefaultHttpClient();
                try {
                    httpGet = new HttpGet("http://" + strIP + "/cgi-bin/" + params[0] + ".cgi?" + String.valueOf(i));
                    httpParams = httpGet.getParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 5000);

                    httpResponse = httpClient.execute(httpGet);
                    httpEntity = httpResponse.getEntity();

                    if (httpEntity != null) {
                        long length = httpEntity.getContentLength();
                        InputStream input = new BufferedInputStream(httpEntity.getContent());
                        timestamp = new SimpleDateFormat("HHmmss", Locale.TAIWAN).format(new Date());
                        OutputStream output = new FileOutputStream(fileSubTaskFolder + "/" + timestamp + ".jpg");

                        byte data[] = new byte[1024];
                        long total = 0;
                        int count;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            publishProgress((int) ((total * 100) / length));
                            output.write(data, 0, count);
                        }
                        output.flush();
                        output.close();
                        input.close();
                    }
                } catch (ClientProtocolException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tvResult.setText(tvResult.getText() + "\n" + "圖片已儲存完畢");
        }
    }


    @Override
    public void onDetach() {
        Log.d(DEBUG_TAG, "Scan onDetach()");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d(DEBUG_TAG,"Scan onPause()");
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(DEBUG_TAG,"Scan onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Scan onActivityCreated()");


        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(DEBUG_TAG,"Scan onResume()");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(DEBUG_TAG,"Scan onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG,"Scan onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.d(DEBUG_TAG,"Scan onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(DEBUG_TAG,"Scan onStop()");
        super.onStop();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Scan onCreate()");
        super.onCreate(savedInstanceState);
    }
}
