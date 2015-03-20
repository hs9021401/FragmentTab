package com.foxlinkimage.alex.fragmenttab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    TextView location;
    ListView fileList;

    public FolderFrag()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_folder, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spDefaultSetting = getActivity().getSharedPreferences("SETTINGS", 0);
        strRootFolderPath = spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder");

        location = (TextView)view.findViewById(R.id.location);
        location.setText(strRootFolderPath);

        //取得資料夾內所有的檔案(包含子資料夾)
        final ArrayList<File> FilesInFolder = GetFiles(strRootFolderPath);
        fileList = (ListView)getActivity().findViewById(R.id.fileList);
        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File tmp = FilesInFolder.get(position);
                String tmp_path = tmp.getPath();
                if(tmp.isDirectory())
                {
                    location.setText(tmp.getPath());
                    FilesInFolder.clear();
                    ArrayList<File> getSubFolderFile = GetFiles(tmp.getPath());
                    if(!tmp_path.equals(strRootFolderPath)) {
                        FilesInFolder.add(tmp.getParentFile());
                    }
                    for(int i=0;i<getSubFolderFile.size();i++)
                    {
                        FilesInFolder.add(getSubFolderFile.get(i));
                    }

                    fileList.setAdapter(fileBaseAdapter);
                }
            }
        });

        //如果資料夾內是空的話, 顯示no file訊息
        if(FilesInFolder== null)
        {
            fileList.setEmptyView(getActivity().findViewById(R.id.empty));
        }else{
        /**
                * 將Context也傳過去, 因為使用inflater時會需要, Google文件如下
                * LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                * */
            fileBaseAdapter = new FileBaseAdapter(getActivity(), FilesInFolder);
            fileList.setAdapter(fileBaseAdapter);
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
}
