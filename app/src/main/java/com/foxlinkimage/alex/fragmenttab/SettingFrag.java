package com.foxlinkimage.alex.fragmenttab;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Alex on 2015/3/11.
 */
public class SettingFrag extends Fragment {
    static final String SHARED_PREF = "SETTINGS";
    SharedPreferences spDefaultSetting;
    EditText edtIP, edtRootFolder;
    final static String DEBUG_TAG = "HIDDEN";

    public SettingFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Setting onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Setting onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        edtIP = (EditText) view.findViewById(R.id.IP);
        edtRootFolder = (EditText) view.findViewById(R.id.RootFolder);

        //利用SharedPreference物件, 參數1: 文件名稱, 參數2:操作模式
        // 第2個參數設定如下:
        //MODE_PRIVATE 只有這個程式可以用,  其實也就只是個常數 0
        //MODE_WORLD_READABLE 所有程式可以讀取
        //MODE_WORLD_WRITEABLE 所有程式可以修改
        spDefaultSetting = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        edtIP.setText(spDefaultSetting.getString("IP", "10.1.20.85"));
        edtRootFolder.setText(spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder"));

        //當lose focus時把information存入SharedPreference
        edtIP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                spDefaultSetting.edit()
                        .putString("IP", edtIP.getText().toString())
                        .apply();
            }
        });

        edtRootFolder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                spDefaultSetting.edit()
                        .putString("ROOTFOLDER", edtRootFolder.getText().toString())
                        .apply();
            }
        });
    }


    @Override
    public void onDetach() {
        Log.d(DEBUG_TAG,"Setting onDetach()");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d(DEBUG_TAG,"Setting onPause()");
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(DEBUG_TAG,"Setting onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Setting onActivityCreated()");

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(DEBUG_TAG,"Setting onResume()");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(DEBUG_TAG,"Setting onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG,"Setting onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.d(DEBUG_TAG,"Setting onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(DEBUG_TAG,"Setting onStop()");
        super.onStop();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Setting onCreate()");
        super.onCreate(savedInstanceState);
    }
}
