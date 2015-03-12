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
    Fragment frag;
    FragmentTransaction fragmentTransaction;

    public MenuFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_multi,container,false);

        frag = new ScanFrag();
        fragmentTransaction = getFragmentManager().beginTransaction().add(R.id.container, frag);
        fragmentTransaction.commit();

        Button btnScan = (Button)view.findViewById(R.id.buttonScan);
        Button btnFolder = (Button)view.findViewById(R.id.buttonFolder);
        Button btnSetting = (Button)view.findViewById(R.id.buttonSetting);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new ScanFrag();
                fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragmentTransaction.commit();
            }
        });

        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new FolderFrag();
                fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragmentTransaction.commit();

            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new SettingFrag();
                fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragmentTransaction.commit();
            }
        });


        return view;
    }
}
