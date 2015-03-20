package com.foxlinkimage.alex.fragmenttab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * Created by Alex on 2015/3/16.
 */
public class FileBaseAdapter extends BaseAdapter {
    public static final int REQUEST_CODE_CROP_IMAGE = 0x01;
    ArrayList<File> files;
    LayoutInflater mInflater;
    Context context;
    SharedPreferences spDefaultSetting;

    FileBaseAdapter(Context context, ArrayList<File> files) {
        this.files = files;
        this.context = context;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //使用ViewHolder Pattern(Google強烈建議使用), 目的在於降低因為在上下滑動時所造成多次getView()動作所導致的效率差現象
        //可參考文章說明 http://lp43.blogspot.tw/2011_02_01_archive.html
        ViewHolder viewHolder;
        //判斷是icon或者是folder
        final File file_judgement = files.get(position);
        spDefaultSetting = context.getSharedPreferences("SETTINGS", 0);

        if (view == null) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.file_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.filename = (TextView) view.findViewById(R.id.filename);
            viewHolder.crop = (Button) view.findViewById(R.id.crop);
            viewHolder.view = (Button) view.findViewById(R.id.view);
            viewHolder.multiselect = (CheckBox) view.findViewById(R.id.multiSelect);
            view.setTag(viewHolder);
        } else {
            //如果在上下滑動時發現該item是已經存在的, 就不需要重新new物件出來
            viewHolder = (ViewHolder) view.getTag();
        }


        if (file_judgement.getPath().equals(spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder"))) {
            viewHolder.filename.setText("...");
            viewHolder.multiselect.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.filename.setText(files.get(position).getName());
        }

        //Folder
        if (file_judgement.isDirectory()) {
            viewHolder.icon.setImageResource(R.drawable.icon_folder);
            viewHolder.crop.setVisibility(View.INVISIBLE);
            viewHolder.view.setVisibility(View.INVISIBLE);
        } else
        {
            viewHolder.icon.setImageResource(R.drawable.icon_image);
            //檢視圖片
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file_judgement), "image/png");
                    context.startActivity(intent);
                }
            });
            //切割圖片
            viewHolder.crop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //使用第三方library, 幫助切割
                    Intent it = new Intent(context, CropImage.class);
                    String file_path = file_judgement.getPath();
                    it.putExtra(CropImage.IMAGE_PATH, file_path);
                    it.putExtra(CropImage.SCALE, true);
                    it.putExtra(CropImage.ASPECT_X, 3);
                    it.putExtra(CropImage.ASPECT_Y, 2);
                    ((Activity) context).startActivityForResult(it, REQUEST_CODE_CROP_IMAGE);
                }
            });
        }
        return view;
    }

    private class ViewHolder {
        ImageView icon;
        TextView filename;
        Button crop;
        Button view;
        CheckBox multiselect;
    }

}
