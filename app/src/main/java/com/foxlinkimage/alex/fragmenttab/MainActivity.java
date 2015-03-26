package com.foxlinkimage.alex.fragmenttab;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity{
    final static String DEBUG_TAG = "HIDDEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG,"Main onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menuContainer, new MenuFragment())
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


    @Override
    protected void onStop() {
        Log.d(DEBUG_TAG,"Main onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(DEBUG_TAG,"Main onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(DEBUG_TAG,"Main onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(DEBUG_TAG,"Main onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(DEBUG_TAG,"Main onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(DEBUG_TAG,"Main onRestart()");
        super.onRestart();
    }
}
