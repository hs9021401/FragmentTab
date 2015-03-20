package com.foxlinkimage.alex.fragmenttab;

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
    SharedPreferences spDefaultSetting;
    EditText edtIP, edtRootFolder;
    public SettingFrag()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spDefaultSetting = getActivity().getSharedPreferences("SETTINGS", 0);
        edtIP = (EditText)view.findViewById(R.id.IP);
        edtRootFolder = (EditText)view.findViewById(R.id.RootFolder);

        //當lose focus時把information存入SharedPreference
        edtIP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        edtRootFolder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
    }
}
