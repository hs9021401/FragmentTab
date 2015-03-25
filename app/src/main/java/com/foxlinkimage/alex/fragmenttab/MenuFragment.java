package com.foxlinkimage.alex.fragmenttab;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

    public MenuFragment()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, viewGroup, false);

        Button btnScan = (Button)view.findViewById(R.id.MenuBtnScan);
        Button btnFolder = (Button)view.findViewById(R.id.MenuBtnFolder);
        Button btnSetting = (Button)view.findViewById(R.id.MenuBtnSetting);

        fragScan = new ScanFrag();
        fragFolder = new FolderFrag();
        fragSetting = new SettingFrag();

        //一開始load Scan的fragment進來, 並設計一個旗標strTagNowFragment用來記錄目前的Fragment是什麼
        strTagNowFragment = "Tag_ScanFrag";
        fragmentTransaction = getFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out).add(R.id.container, fragScan, strTagNowFragment);
        fragmentTransaction.commit();

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
        Fragment fragNow = getFragmentManager().findFragmentByTag(strTagNowFragment);
        FragmentTransaction transaction = getFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        if(fragNow != to)    //表示要跳轉其他frag
        {
            if(!to.isAdded())   //如果該frag未被add過, 將上一個frag, hide, 執行add
            {
                transaction.hide(fragNow).add(R.id.container, to, tag).commit();
            }else   //否則hide上一個, show目的frag
            {
                transaction.hide(fragNow).show(to).commit();
            }
        }

    }

}


