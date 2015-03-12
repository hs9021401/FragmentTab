package com.foxlinkimage.alex.fragmenttab;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alex on 2015/3/11.
 */
public class FolderFrag extends Fragment {
    public final static String folder_Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+ "/MyPicFolder";
    protected ListView fileList;

    public FolderFrag()
    {}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //List all the file in the folder
        ArrayList<String> FilesInFolder = GetFiles(folder_Path);
        fileList = (ListView)getActivity().findViewById(R.id.fileList);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, FilesInFolder);
        fileList.setAdapter(myAdapter);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), view.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_folder, container, false);
        return rootView;
    }

    public ArrayList<String> GetFiles(String DirectoryPath)
    {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++)
                MyFiles.add(files[i].getName());
        }

        return MyFiles;
    }
}
