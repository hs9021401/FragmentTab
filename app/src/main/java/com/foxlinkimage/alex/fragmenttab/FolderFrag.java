package com.foxlinkimage.alex.fragmenttab;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alex on 2015/3/11.
 */
public class FolderFrag extends Fragment{
    SharedPreferences spDefaultSetting;
    String strRootFolderPath;
    FileBaseAdapter fileBaseAdapter;
    TextView tvLocation;
    ListView lvFileList;
    final static String DEBUG_TAG = "HIDDEN";

    public FolderFrag()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Folder onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_folder, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Folder onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        spDefaultSetting = getActivity().getSharedPreferences(SettingFrag.SHARED_PREF, 0);
        strRootFolderPath = spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder");

        tvLocation = (TextView)view.findViewById(R.id.location);
        tvLocation.setText(strRootFolderPath);

        //取得資料夾內所有的檔案(包含子資料夾)
        final ArrayList<File> FilesInFolder = GetFiles(strRootFolderPath);
        lvFileList = (ListView)getActivity().findViewById(R.id.fileList);
        lvFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File tmp = FilesInFolder.get(position);
                String tmp_path = tmp.getPath();
                if (tmp.isDirectory()) {
                    tvLocation.setText(tmp.getPath());
                    FilesInFolder.clear();
                    ArrayList<File> getSubFolderFile = GetFiles(tmp.getPath());
                    if (!tmp_path.equals(strRootFolderPath)) {
                        FilesInFolder.add(tmp.getParentFile());
                    }

                    if (getSubFolderFile != null) {
                        for (int i = 0; i < getSubFolderFile.size(); i++) {
                            FilesInFolder.add(getSubFolderFile.get(i));
                        }
                    }
                    lvFileList.setAdapter(fileBaseAdapter);
                }
            }
        });

        //如果資料夾內是空的話, 顯示no file訊息
        if(FilesInFolder== null)
        {
            lvFileList.setEmptyView(getActivity().findViewById(R.id.empty));
        }else{
            /**
             * 將Context也傳過去, 因為使用inflater時會需要, Google文件如下
             * LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             * */
            fileBaseAdapter = new FileBaseAdapter(getActivity(), FilesInFolder);
            lvFileList.setAdapter(fileBaseAdapter);
        }

    }


    public ArrayList<File> GetFiles(String DirectoryPath)
    {
        ArrayList<File> MyFiles = new ArrayList<>();
        File f = new File(DirectoryPath);
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++)
                MyFiles.add(files[i]);
        }
        return MyFiles;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //右上角Menu分享與刪除的功能
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        Log.d(DEBUG_TAG, "Folder onDetach()");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d(DEBUG_TAG,"Folder onPause()");
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(DEBUG_TAG,"Folder onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Folder onActivityCreated()");

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(DEBUG_TAG,"Folder onResume()");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(DEBUG_TAG,"Folder onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG,"Folder onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.d(DEBUG_TAG,"Folder onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(DEBUG_TAG,"Folder onStop()");
        super.onStop();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Folder onCreate()");
        super.onCreate(savedInstanceState);
    }

}
