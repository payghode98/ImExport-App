package com.imexportsolutions.imexportsolutions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import utility.HttpManager;
import utility.RequestPackage;

public class forgotpswd extends AppCompatActivity implements View.OnClickListener {

    EditText etemail;
    Button btnsubmit;
    String Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpswd);
        etemail = findViewById(R.id.etemailFP);
        btnsubmit = findViewById(R.id.btnsubmit);

        btnsubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Email = etemail.getText().toString();

        new ForgotPassword().execute();
    }


    class ForgotPassword extends AsyncTask<String ,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(forgotpswd.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            RequestPackage rp = new RequestPackage();
            rp.setMethod("GET");
            rp.setUri(HttpManager.URL+"ForgetPswdCustomsAgent");
            rp.setParam("Email", Email);
            String ans = HttpManager.getData(rp);
            return ans.trim();

        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);

            if(ans.equals("Success"))
            {
                Toast.makeText(forgotpswd.this, "You will get your Username and Password on your registered Email address!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(forgotpswd.this,LogInPage.class);
                startActivity(i);
            }
            else{
                Toast.makeText(forgotpswd.this, "Entered email didn't match any records!! Please enter the email properly", Toast.LENGTH_SHORT).show();
            }
            if(pd!=null){
                pd.dismiss();
            }

        }

    }
}