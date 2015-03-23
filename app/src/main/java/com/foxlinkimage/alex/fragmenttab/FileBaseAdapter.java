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
    ArrayList<File> alFiles;
    LayoutInflater mInflater;
    Context context;
    SharedPreferences spDefaultSetting;
    String strRootFolder;


    FileBaseAdapter(Context context, ArrayList<File> files) {
        this.alFiles = files;
        this.context = context;
    }

    @Override
    public int getCount() {
        return alFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return alFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //使用ViewHolder Pattern(Google強烈建議使用) ---> setTag() getTag(),
        // 因為在上下滑動時會一直執行getView()動作, 如果沒有使用這個Pattern, 就每次都會去佈局文件中去拿到你的View
        // 導致效率低下, 利用viewHolder方法, 可以暫存view起來, 如果滑動過程發現該view存在, 就直接取出使用
        //可參考文章說明 http://lp43.blogspot.tw/2011_02_01_archive.html
        ViewHolder viewHolder;

        final File fileJudgement = alFiles.get(position); //用以判斷是icon或者是folder
        spDefaultSetting = context.getSharedPreferences(SettingFrag.SHARED_PREF, 0);
        strRootFolder = spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder");

        if (convertView == null) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.file_item, viewGroup, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.filename = (TextView) convertView.findViewById(R.id.filename);
            viewHolder.cropimg = (Button) convertView.findViewById(R.id.crop);
            viewHolder.viewimg = (Button) convertView.findViewById(R.id.view);
            viewHolder.multiselect = (CheckBox) convertView.findViewById(R.id.multiSelect);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Folder
        if (fileJudgement.isDirectory()) {
            if (fileJudgement.getPath().equals(strRootFolder)) {   //如果資料夾是Root資料夾, 就顯示... , 否則顯示該資料夾名稱
                viewHolder.filename.setText("...");
            }else{
                viewHolder.filename.setText(fileJudgement.getName());
            }
            viewHolder.icon.setImageResource(R.drawable.icon_folder);
            viewHolder.viewimg.setVisibility(View.INVISIBLE);
            viewHolder.cropimg.setVisibility(View.INVISIBLE);
            viewHolder.multiselect.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setImageResource(R.drawable.icon_image);
            viewHolder.filename.setText(alFiles.get(position).getName());
            viewHolder.viewimg.setVisibility(View.VISIBLE);
            viewHolder.cropimg.setVisibility(View.VISIBLE);
            viewHolder.multiselect.setVisibility(View.VISIBLE);
            //檢視圖片
            viewHolder.viewimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(fileJudgement), "image/png");
                    context.startActivity(intent);
                }
            });
            //切割圖片
            viewHolder.cropimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //使用第三方library, 幫助切割
                    Intent it = new Intent(context, CropImage.class);
                    String file_path = fileJudgement.getPath();
                    it.putExtra(CropImage.IMAGE_PATH, file_path);
                    it.putExtra(CropImage.SCALE, true);
                    it.putExtra(CropImage.ASPECT_X, 3);
                    it.putExtra(CropImage.ASPECT_Y, 2);
                    ((Activity) context).startActivityForResult(it, REQUEST_CODE_CROP_IMAGE);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView filename;
        Button viewimg;
        Button cropimg;
        CheckBox multiselect;
    }

}
