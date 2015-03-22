package com.foxlinkimage.alex.fragmenttab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Alex on 2015/3/11.
 */
public class SettingFrag extends Fragment {
    static final String PREF = "SETTINGS";
    SharedPreferences spDefaultSetting;
    EditText edtIP, edtRootFolder;

    public SettingFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtIP = (EditText) view.findViewById(R.id.IP);
        edtRootFolder = (EditText) view.findViewById(R.id.RootFolder);

        //利用SharedPreference物件, 參數1: 文件名稱, 參數2:操作模式
        // 第2個參數設定如下:
        //MODE_PRIVATE 只有這個程式可以用,  其實也就只是個常數 0
        //MODE_WORLD_READABLE 所有程式可以讀取
        //MODE_WORLD_WRITEABLE 所有程式可以修改
        spDefaultSetting = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);

        edtIP.setText(spDefaultSetting.getString("IP", "10.1.20.85"));
        edtRootFolder.setText(spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder"));

        //當lose focus時把information存入SharedPreference
        edtIP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                spDefaultSetting.edit()
                        .putString("IP", edtIP.getText().toString())
                        .commit();
            }
        });

        edtRootFolder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                spDefaultSetting.edit()
                        .putString("ROOTFOLDER", edtRootFolder.getText().toString())
                        .commit();
            }
        });
    }
}
