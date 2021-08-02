package com.imexportsolutions.imexportsolutions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ActionBar toolbar;
    ImageView StatusImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = getSupportActionBar();
        StatusImage=findViewById(R.id.StatusImage);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Star Inc.");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new WaitingListFragment()).commit();


        /*mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
    }



    //private TextView mTextMessage;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment=null;

            switch (item.getItemId()) {
                case R.id.navigation_waiting_list:
//                    toolbar.setTitle("Home");
                    selectedFragment=new WaitingListFragment();



                    break;
                case R.id.navigation_received_list:
//                    toolbar.setTitle("Settings");
                    selectedFragment=new ReceivedListFragment();

                    break;
                /*case R.id.navigation_scan:
                    toolbar.setTitle("QR Code Scanner");
                    selectedFragment=new ScanFragment();
                    break;*/
                case R.id.navigation_completed_list:
//                    toolbar.setTitle("Notification");
                    selectedFragment=new CompletedListFragment();

                    break;
               /* case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    selectedFragment=new ProfileFragment();
                    break;*/
            }

            getSupportFragmentManager().beginTransaction()/*.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right)*/.addToBackStack(null).replace(R.id.fragment_container,selectedFragment).commit();
            return true;




        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_qrcode_scanner:
                Intent i = new Intent(this,QrcodeScanner.class);
                this.startActivity(i);
                return true;
            case R.id.action_settings:
                 i = new Intent(this,SettingsActivity.class);
                this.startActivity(i);
                return true;
            case R.id.action_myprofile:
                i = new Intent(this, MyProfile.class);
                this.startActivity(i);
                return true;
            case R.id.action_logout:
                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sp.edit().remove("Logged").apply();
                i= new Intent(this,LogInPage.class);
                this.startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
