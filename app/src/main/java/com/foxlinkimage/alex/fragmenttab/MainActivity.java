package com.foxlinkimage.alex.fragmenttab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menuContainer, new MenuFragment())
                    .commit();
        }

        //利用SharedPreference物件, 設定預設的值, 第二個參數設定如下:
        //MODE_PRIVATE 只有這個程式可以用,  其實也就只是個常數 0
        //MODE_WORLD_READABLE 所有程式可以讀取
        //MODE_WORLD_WRITEABLE 所有程式可以修改
        SharedPreferences spDefaultSetting = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        if(spDefaultSetting.getString("IP","NODATA").equals("NODATA"))      //取不到資料
        {
            spDefaultSetting.edit()         //之後可以放入更多值, 如掃描的參數值
                    .putString("IP", "10.1.20.85")
                    .putString("ROOTFOLDER", "/storage/emulated/0/Pictures/MyPicFolder")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
