package com.foxlinkimage.alex.fragmenttab;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Alex on 2015/3/11.
 */
public class MenuFragment extends Fragment {
    Fragment fragScan;
    Fragment fragFolder;
    Fragment fragSetting;

    FragmentTransaction fragmentTransaction;
    String strTagNowFragment;

    final static String DEBUG_TAG = "HIDDEN";

    public MenuFragment()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.fragment_menu, viewGroup, false);
        Button btnScan = (Button)view.findViewById(R.id.MenuBtnScan);
        Button btnFolder = (Button)view.findViewById(R.id.MenuBtnFolder);
        Button btnSetting = (Button)view.findViewById(R.id.MenuBtnSetting);

        fragScan = new ScanFrag();
        fragFolder = new FolderFrag();
        fragSetting = new SettingFrag();

        //設計一個旗標strTagNowFragment用來記錄目前的Fragment是什麼
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(fragScan, "Tag_ScanFrag");
                strTagNowFragment = "Tag_ScanFrag";
            }
        });

        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(fragFolder,"Tag_FolderFrag");
                strTagNowFragment = "Tag_FolderFrag";
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(fragSetting,"Tag_SettingFrag");
                strTagNowFragment = "Tag_SettingFrag";
            }
        });
        return view;
    }

    public void switchFragment(Fragment to, String tag)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        if(strTagNowFragment == null)
        {
            transaction.add(R.id.container,to,tag).commit();
        }else {
            Fragment fragNow = getFragmentManager().findFragmentByTag(strTagNowFragment);
            if (fragNow != to)    //表示要跳轉其他frag
            {
                if (!to.isAdded())   //如果該frag未被add過, 將上一個frag, hide, 執行add
                {
                    transaction.hide(fragNow).add(R.id.container, to, tag).commit();
                } else   //否則hide上一個, show目的frag
                {
                    transaction.hide(fragNow).show(to).commit();
                }
            }
        }
    }

    @Override
    public void onDetach() {
        Log.d(DEBUG_TAG,"onDetach()");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d(DEBUG_TAG,"onPause()");
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(DEBUG_TAG,"onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(DEBUG_TAG,"onResume()");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(DEBUG_TAG,"onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.d(DEBUG_TAG,"onStart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(DEBUG_TAG,"onStop()");
        super.onStop();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"onCreate()");
        super.onCreate(savedInstanceState);
    }

}


