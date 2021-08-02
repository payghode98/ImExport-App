package com.imexportsolutions.imexportsolutions;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import utility.HttpManager;
import utility.RequestPackage;

public class DocUpload extends AppCompatActivity {

    ImageView iv1;
    Button btnpic;
    Uri file;
    int CAMERA_REQUEST;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_upload);

        btnpic = (Button) findViewById(R.id.btnpic);
        iv1 = (ImageView) findViewById(R.id.iv1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btnpic.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnpic.setEnabled(true);
            }
        }
    }


    public void takePicture(View view) {

        CAMERA_REQUEST = 1;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        photo = (Bitmap) data.getExtras().get("data");
        iv1.setImageBitmap(photo);
    }

    public void upload(View view) {
        //String photoupload = convertBitmapToString(photo);
        new MyDocUpload().execute();
    }

    public String convertBitmapToString(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 1000, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        String imageStr = Base64.encodeBytes(byte_arr);
        return imageStr;

    }

    private class MyDocUpload extends AsyncTask<String,String,String > {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(DocUpload.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            RequestPackage rp = new RequestPackage();
            rp.setMethod("POST");
            rp.setUri(HttpManager.URL+"PortClearanceDocs");
            String photoupload = convertBitmapToString(photo);
            rp.setParam("photoupload",photoupload);

            String ans = HttpManager.getData(rp);

            return ans.trim();
        }
        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);




           // int ID = Integer.parseInt(ans);

            if (ans.equals("Success")) {

                Toast.makeText(DocUpload.this, "Upload Successful !!", Toast.LENGTH_LONG).show();
               /* Intent i = new Intent(DocUpload.this, MainActivity.class);
                startActivity(i);
                finish();*/
            } else {
                Toast.makeText(DocUpload.this, ans+"Upload Unsuccessful", Toast.LENGTH_LONG).show();

            }
            if(pd!=null){
                pd.dismiss();
            }
        }

    }
}
