package com.imexportsolutions.imexportsolutions;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import org.json.JSONException;
import org.json.JSONObject;

import utility.HttpManager;
import utility.RequestPackage;

public class LogInPage extends AppCompatActivity implements View.OnClickListener {

    EditText etusername,etpassword;
    Button btnlogin;
    String Country,ID1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_log_in_page);


            etusername = findViewById(R.id.etusername);
            etpassword = findViewById(R.id.etpassword);
            btnlogin = findViewById(R.id.btnlogin);
            btnlogin.setOnClickListener(this);
           /* setTitle("Star Inc.");*/

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.customPrimaryDark));
        }
    }

    @Override
    public void onClick(View v) {
        new MyLoginTask().execute();
    }

    public void forgotp(View view) {
        Intent i = new Intent(LogInPage.this, forgotpswd.class);
        startActivity(i);

    }

    class MyLoginTask extends AsyncTask<String,String,String>{
        ProgressDialog pd;
        String username ,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            username = etusername.getText().toString();
            password = etpassword.getText().toString();

            pd = new ProgressDialog(LogInPage.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestPackage rp = new RequestPackage();
            rp.setMethod("GET");
            rp.setUri(HttpManager.URL+"Login");
            rp.setParam("Username",username);
            rp.setParam("Password",password);
            String ans = HttpManager.getData(rp);

            return ans.trim();
        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);



            try {

                JSONObject obj = new JSONObject(ans);
                ID1 = obj.getString("CustomsAgentID");
                Country = obj.getString("Country");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            int ID = Integer.parseInt(ID1);

            if (ID > 0) {
                if (Country.equals("INDIA")){
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LogInPage.this);
                sp.edit().putInt("ICAID", ID).apply();
                sp.edit().putString("Country","INDIA").apply();
                sp.edit().putString("Logged","Logged").apply();

                }
                else{
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LogInPage.this);
                    sp.edit().putInt("FCAID", ID).apply();
                    sp.edit().putString("Country", Country).apply();
                    sp.edit().putString("Logged","Logged").apply();

                }

                Toast.makeText(LogInPage.this, "Login Successful !!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(LogInPage.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(LogInPage.this, ans+"Invalid username or password !!", Toast.LENGTH_LONG).show();

            }
            if(pd!=null){
                pd.dismiss();
            }
       }
    }
}
