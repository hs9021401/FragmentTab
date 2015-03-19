package com.foxlinkimage.alex.fragmenttab;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.IOException;

/**
 * Created by Alex on 2015/3/11.
 */
public class ScanFrag extends Fragment {
    static final String CMD_GET_STATUS = "getstatus";
    static final String CMD_DO_SCAN = "doscan";
    static final String CMD_GET_IMAGE_COUNT = "getimagecount";
    static final String CMD_GET_IMAGE = "getimage";

    static final String RESP_NotReadyORBusy = "NotReadyORBusy";

    HttpClient httpClient;
    String strResultMsgSend;
    TextView txtResult;
    Button btnScan;
    int iImgCount;


    public ScanFrag()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan,null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        txtResult = (TextView)view.findViewById(R.id.txtResult);
        btnScan =(Button)view.findViewById(R.id.btnScan);
        super.onViewCreated(view, savedInstanceState);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask GetStatus = new MyAsyncTask();
                GetStatus.execute(CMD_GET_STATUS,CMD_DO_SCAN,CMD_GET_IMAGE_COUNT);
            }
        });
    }

    class MyAsyncTask extends AsyncTask<String,String,Void>
    {
        @Override
        protected void onProgressUpdate(String... values) {
            txtResult.setText(txtResult.getText() + "\n" + values[0]);
        }

        @Override
        protected Void doInBackground(String... params) {
            /// /取得系統資訊的網路狀態
            ConnectivityManager mConnMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(mConnMgr.getActiveNetworkInfo() == null)
            {
                Toast.makeText(getActivity(), "No network service...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }else {

                for (int i = 0; i < params.length; i++) {
                    if(!isCancelled()) {
                        httpClient = new DefaultHttpClient();
                        try {
                            HttpGet get = new HttpGet("http://10.1.20.85/cgi-bin/" + params[i] + ".cgi?");
                            HttpParams httpParams = get.getParams();
                            //設定connection timeout 5秒
                            HttpConnectionParams.setConnectionTimeout(httpParams,5000);

                            HttpResponse response = httpClient.execute(get);
                            HttpEntity entity = response.getEntity();
                            strResultMsgSend = EntityUtils.toString(entity).trim();
                            publishProgress(strResultMsgSend);
                            if(strResultMsgSend.equals(RESP_NotReadyORBusy)) {
                                this.cancel(true);
                                break;
                            }
                        } catch (ClientProtocolException ex) {
                            publishProgress(ex.getMessage());
                            break;
                        }catch(IOException ex)
                        {
                            publishProgress(ex.getMessage());
                            break;
                        }

                    }
                }

            }
            return  null;
        }
    }

}
