package com.manhthong.chatsocketio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.manhthong.chatsocketio.Fragment.Active_Status_Fragment;
import com.manhthong.chatsocketio.Fragment.Message_Fragment;
import com.manhthong.chatsocketio.Fragment.Setting_Fragment;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String uniqueId;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://172.168.10.233:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //connect to socketIO
        mSocket.connect();
        //nhan data from login
        uniqueId = getIntent().getStringExtra("id");
        Log.d("tdn", uniqueId);
        bottomNav.getMenu().findItem(R.id.nav_message).setChecked(true);

        if(savedInstanceState == null){
            loadFragment(new Message_Fragment());
        }
        //I added this if statement to keep the selected fragment when rotating the device
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Message_Fragment()).commit();
        }
    }
    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_message:
                            selectedFragment = new Message_Fragment();
                            break;
                        case R.id.nav_activeStatus:
                            selectedFragment = new Active_Status_Fragment();
                            break;
                        case R.id.nav_setting:
                            selectedFragment = new Setting_Fragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}