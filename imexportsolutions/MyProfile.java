package com.imexportsolutions.imexportsolutions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import utility.HttpManager;
import utility.RequestPackage;

import static android.os.Build.ID;

public class MyProfile extends AppCompatActivity {
ActionBar actionBar;
ImageView ivprofile;
TextView tvname,tvnumber,tvemail,tvuname,tvcountry;
int FCAID,ICAID;
String Country;
String imgname,finaluri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
       /* actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");*/
        findallviews();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyProfile.this);
        Country  = sp.getString("Country","");

            ICAID  = sp.getInt("ICAID",0);

            FCAID  = sp.getInt("FCAID",0);



        new ProfileTask().execute();
    }

    private void findallviews() {
        ivprofile=findViewById(R.id.ivprofile);
            tvname = findViewById(R.id.tvname);
        tvnumber = findViewById(R.id.tvnumber);
        tvemail = findViewById(R.id.tvemail);
        tvuname = findViewById(R.id.tvuname);
        tvcountry = findViewById(R.id.tvcountry);
         }


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    class ProfileTask extends AsyncTask<String,String,String> {
        ProgressDialog pd;

        String ID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MyProfile.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            if(Country.equals("INDIA")){
                ID = ICAID + "";
            }
            else{
                ID = FCAID + "";
            }
            RequestPackage rp = new RequestPackage();
            rp.setMethod("GET");
            rp.setUri(HttpManager.URL+"ProfileCA");
            rp.setParam("ID", ID);

            String ans = HttpManager.getData(rp);

            return ans;
        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);


            try {

                JSONObject obj = new JSONObject(ans);
                tvname.setText(obj.getString("Name"));
                tvcountry.setText(obj.getString("Country"));
                tvemail.setText(obj.getString("Email"));
                tvnumber.setText(obj.getString("Mobile"));
                tvuname.setText(obj.getString("Username"));
                imgname=obj.getString("Photo");
                finaluri="http://192.168.43.161:51539/ProfilePhotos/CustomsAgentPics/"+imgname;

                new DownloadImageFromInternet ((ImageView) findViewById(R.id.ivprofile)).execute(finaluri);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();


        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String,Void, Bitmap>
    {
        ImageView ivprofile;

        public DownloadImageFromInternet(ImageView imageView){
            this.ivprofile=imageView;

        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);

            }catch (Exception e){

                Log.e("Error Message",e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result){
            ivprofile.setImageBitmap(result);
        }
    }
}
