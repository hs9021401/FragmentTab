package com.foxlinkimage.alex.fragmenttab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alex on 2015/3/11.
 */
public class FolderFrag extends Fragment {
    public FolderFrag()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_folder,container,false);
        
        return rootView;
    }
}
