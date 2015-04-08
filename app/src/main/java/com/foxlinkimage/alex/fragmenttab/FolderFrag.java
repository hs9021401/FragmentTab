package com.foxlinkimage.alex.fragmenttab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
public class FolderFrag extends Fragment {
    SharedPreferences spDefaultSetting;
    String strRootFolderPath;
    FileBaseAdapter fileBaseAdapter;
    TextView tvLocation;
    ListView lvFileList;
    ArrayList<String> alSelectedFiles;

    final static String DEBUG_TAG = "HIDDEN";


    public FolderFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "Folder onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_folder, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "Folder onViewCreated()");
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);    //使用自己的option menu
        spDefaultSetting = getActivity().getSharedPreferences(SettingFrag.SHARED_PREF, 0);
        strRootFolderPath = spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder");
        final ArrayList<File> FilesInFolder = GetFiles(strRootFolderPath);        //取得資料夾內所有的檔案(包含子資料夾)
        tvLocation = (TextView) view.findViewById(R.id.location);
        tvLocation.setText(strRootFolderPath);

        lvFileList = (ListView) getActivity().findViewById(R.id.fileList);
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
                    alSelectedFiles = new ArrayList<String>();  //在回上一層資料夾時, 被選取的檔案設為空
                    fileBaseAdapter = new FileBaseAdapter(getActivity(), FilesInFolder); //0327++
                    lvFileList.setAdapter(fileBaseAdapter);

                }
            }
        });

        //如果資料夾內是空的話, 顯示no file訊息
        if (FilesInFolder == null) {
            lvFileList.setEmptyView(getActivity().findViewById(R.id.empty));
        } else {
            fileBaseAdapter = new FileBaseAdapter(getActivity(), FilesInFolder);
            lvFileList.setAdapter(fileBaseAdapter);
        }

    }


    public ArrayList<File> GetFiles(String DirectoryPath) {
        ArrayList<File> MyFiles = new ArrayList<>();
        File f = new File(DirectoryPath);
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i = 0; i < files.length; i++)
                MyFiles.add(files[i]);
        }
        return MyFiles;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        alSelectedFiles = fileBaseAdapter.getSelectedFiles();
        if (alSelectedFiles.size() != 0) {
            switch (item.getItemId()) {
                case R.id.action_share:
                    ArrayList<Uri> alSelectedFilesUri = new ArrayList<>();
                    for(int i=0; i< alSelectedFiles.size();i++)
                    {
                        alSelectedFilesUri.add(Uri.parse(alSelectedFiles.get(i)));
                    }
                    Intent sendIntent = new Intent();
                    sendIntent.setType("image/*");
                    sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, alSelectedFilesUri);
                    startActivity(Intent.createChooser(sendIntent,"share via."));
                    break;

                case R.id.action_delete:
                    //刪除檔案, 並重新bind
                    for(int i=0;i < alSelectedFiles.size();i++)
                    {
                        File deleteFile = new File(alSelectedFiles.get(i));
                        boolean bDel = deleteFile.delete();
                    }
                    //TODO 重新刷新介面部分未完成
                    final ArrayList<File> FilesInFolder = GetFiles(strRootFolderPath);
                    fileBaseAdapter = new FileBaseAdapter(getActivity(), FilesInFolder);
                    lvFileList.setAdapter(fileBaseAdapter);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        Log.d(DEBUG_TAG, "Folder onDetach()");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d(DEBUG_TAG, "Folder onPause()");
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(DEBUG_TAG, "Folder onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "Folder onActivityCreated()");

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(DEBUG_TAG, "Folder onResume()");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(DEBUG_TAG, "Folder onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, "Folder onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.d(DEBUG_TAG, "Folder onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(DEBUG_TAG, "Folder onStop()");
        super.onStop();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "Folder onCreate()");
        super.onCreate(savedInstanceState);
    }

}
