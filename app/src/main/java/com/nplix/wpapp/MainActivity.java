package com.nplix.wpapp;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.FrameLayout;
public class MainActivity extends AppCompatActivity implements  BackHandledFragment.BackHandlerInterface,LifecycleOwner{
    public static String TAG="WPAPP";
    private FrameLayout container;
    private BackHandledFragment backHandledFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        container= (FrameLayout) findViewById(R.id.container);
        setListPostFrag();

    }
    private void setListPostFrag() {
        FragmentManager fm=getSupportFragmentManager();
        ListFragment listFragment=new ListFragment();
        FragmentTransaction ft=fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.container, listFragment, ListFragment.TAG);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        this.backHandledFragment=backHandledFragment;
    }
}
