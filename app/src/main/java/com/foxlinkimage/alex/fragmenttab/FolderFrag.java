package com.foxlinkimage.alex.fragmenttab;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alex on 2015/3/11.
 */
public class FolderFrag extends Fragment{
    public final static String folder_Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+ "/MyPicFolder";
    ListView fileList;

    public FolderFrag()
    {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //分享與刪除的功能callback

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //List all the file in the folder
        ArrayList<File> FilesInFolder = GetFiles(folder_Path);
        fileList = (ListView)getActivity().findViewById(R.id.fileList);

        //如果資料夾內是空的話, 顯示no file訊息
        if(FilesInFolder== null)
        {
            fileList.setEmptyView(getActivity().findViewById(R.id.empty));
        }else{
        /**
                * 將Context也傳過去, 因為使用inflater時會需要, Google文件如下
                * LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                * */
            fileList.setAdapter(new FileBaseAdapter(getActivity(), FilesInFolder));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_folder, container, false);
        return rootView;
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

}
