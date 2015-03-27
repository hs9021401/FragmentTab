package com.foxlinkimage.alex.fragmenttab;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class MainActivity extends ActionBarActivity{
    final static String DEBUG_TAG = "HIDDEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menuContainer, new MenuFragment())
                    .commit();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onStop() {
        Log.d(DEBUG_TAG,"onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(DEBUG_TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(DEBUG_TAG,"onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(DEBUG_TAG,"onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(DEBUG_TAG,"onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(DEBUG_TAG,"onRestart()");
        super.onRestart();
    }
}
