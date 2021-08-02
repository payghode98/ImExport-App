package com.imexportsolutions.imexportsolutions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import utility.HttpManager;
import utility.RequestPackage;

public class DocDownload extends AppCompatActivity {
String id;
    String finaluri;
WebView browser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_download);

        Intent i = getIntent();
        id = i.getStringExtra("id");


        new MyDocDownload().execute();
    }

    private class MyDocDownload extends AsyncTask<String,String,String> {

        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(DocDownload.this);
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
            rp.setUri(HttpManager.URL+"InvoicePDF");
            //rp.setParam("id",id);
            String ans = HttpManager.getData(rp);

            return ans.trim();
            }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);
            pd.dismiss();
            //String url = "http://www.example.com/abc.pdf";
            final String googleDocsUrl = "http://docs.google.com/viewer?url=";
             String pdfname=ans;
           final  String finaluri="http://192.168.43.105:51539/invoice/"+pdfname;


            WebView mWebView=new WebView(DocDownload.this);
            mWebView.getSettings().setJavaScriptEnabled(true);
           // mWebView.getSettings().setPluginsEnabled(true);

            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url){

                    view.loadUrl(finaluri);
                    return false; // then it is not handled by default action
                }
            });


            mWebView.loadUrl((googleDocsUrl + finaluri));

            setContentView(mWebView);
           /* DocumentsContract.Document doc = new DocumentsContract.Document();
            try {

                Log.e("PDFCreator", "PDF Path: " + path);
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                file = new File(dir, "Trinity PDF" + sdf.format(Calendar.getInstance().getTime()) + ".pdf");
*/


           /* String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            File file = new File(dir,"POID"+id+ ".pdf");
            String filename=file.getName();
            String  filepath=file.getAbsolutePath();
            try{
                Base64.decodeToFile(ans,filepath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            //String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

            File file1 = new File(path, filename);

            intent.setDataAndType( Uri.fromFile(file1), "application/pdf" );
            startActivity(intent);
*/

        }

    }
}



