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

    ArrayList<String> alSelectedFiles;  //用來儲存被選中的檔案
    ArrayList<Boolean> b_arrChecked;//用來儲存check的狀態

    FileBaseAdapter(Context context, ArrayList<File> files) {
        alSelectedFiles = new ArrayList<>();
        this.alFiles = files;
        this.context = context;
        b_arrChecked = new ArrayList<>();
        for(int i=0;i<files.size();i++)
            b_arrChecked.add(false);
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        //ViewHolder Pattern
        //參考文章說明 http://lp43.blogspot.tw/2011_02_01_archive.html
        final ViewHolder viewHolder;
        final File fileJudgement = alFiles.get(position); //用以判斷是icon或者是folder
        spDefaultSetting = context.getSharedPreferences(SettingFrag.SHARED_PREF, 0);
        strRootFolder = spDefaultSetting.getString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder");

        if (convertView == null) {
            mInflater = LayoutInflater.from(context); //或 mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 都一樣
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

        //3-27 用以解決在上下滑動時候, check狀態會亂跑
        viewHolder.multiselect.setChecked(b_arrChecked.get(position));
        viewHolder.multiselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    b_arrChecked.set(position,true);
                    alSelectedFiles.add(getItem(position).toString());
                } else {
                    b_arrChecked.set(position, false);


//                    for (int i = 0; i < alSelectedFiles.size(); i++) {
//                        if (alSelectedFiles.get(i).equals(getItem(position).toString())) {
//                            alSelectedFiles.remove(i);
//                            break;
//                        }
//                    }
//                                        刪除的code用下面兩行替代 簡化程式碼
                    int delete_index = alSelectedFiles.indexOf(getItem(position).toString());
                    alSelectedFiles.remove(delete_index);
                }
            }
        });

        //Folder
        if (fileJudgement.isDirectory()) {
            if (fileJudgement.getPath().equals(strRootFolder)) {   //如果資料夾是Root資料夾, 就顯示... , 否則顯示該資料夾名稱
                viewHolder.filename.setText("...");
            } else {
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

    //公用函式: 提供外部程式呼叫. 傳回被選擇的檔案名字
    public ArrayList<String> getSelectedFiles() {
        ArrayList<String> files;
        files = alSelectedFiles;
        return files;
    }

    //公用函式: 提供外部程式呼叫. 從alFiles刪除要被刪除的檔案名稱
    public void Delete(String deletefile) {
        int delete_index = alFiles.indexOf(new File(deletefile));
        alFiles.remove(delete_index);
        b_arrChecked.remove(delete_index);
    }

    private class ViewHolder {
        ImageView icon;
        TextView filename;
        Button viewimg;
        Button cropimg;
        CheckBox multiselect;
    }

}
