package com.imexportsolutions.imexportsolutions;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import utility.HttpManager;
import utility.RequestPackage;


public class WaitingPODetails extends AppCompatActivity {

    String ID1;

    int FCAID,ICAID;
    String Country;
    TextView tvpoid,tvdispatchdate,tvsuppliername,tvfcaname,tvmaterial,tvquantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_po_details);
        ID1 = getIntent().getStringExtra("ID");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WaitingPODetails.this);
        Country  = sp.getString("Country",null);
        ICAID  = sp.getInt("ICAID",0);
        FCAID  = sp.getInt("FCAID",0);

        findAllViews();
        new FetchPODetails().execute();

    }

    private void findAllViews() {
        tvpoid = findViewById(R.id.tvpoid);
        tvdispatchdate = findViewById(R.id.tvdispatchdate);
        tvsuppliername = findViewById(R.id.tvsuppliername);
        tvfcaname = findViewById(R.id.tvfcaname);
        tvmaterial = findViewById(R.id.tvmaterial);
        tvquantity = findViewById(R.id.tvquantity);
    }

    class FetchPODetails extends AsyncTask<String ,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(WaitingPODetails.this);
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
                rp.setUri(HttpManager.URL+"FetchPODetails");
                rp.setParam("ID", ID1);

                String ans = HttpManager.getData(rp);
                return ans.trim();

        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);

            String strPO = ans.split("#####--#####")[0];
            String strPOI = ans.split("#####--#####")[1];

            try {
                JSONArray arr1 = new JSONArray(strPO);
                JSONObject obj = arr1.getJSONObject(0);
                tvpoid.setText(obj.getString("PurchaseOrderID"));
                if(Country.equals("INDIA")) {
                    tvdispatchdate.setText(obj.getString("DispatchdatebyFCA").split("#")[0]);
                }else
                {
                    tvdispatchdate.setText(obj.getString("DispatchdatebySupp").split("#")[0]);
                }
                tvfcaname.setText(obj.getString("FCAName"));
                tvsuppliername.setText(obj.getString("SupplierName"));

                JSONArray arr2 = new JSONArray(strPOI);
                JSONObject obj1 = arr2.getJSONObject(0);

                tvquantity.setText(obj1.getString("Quantity"));
                tvmaterial.setText(obj1.getString("MaterialName"));



            } catch (JSONException e) {
                e.printStackTrace();
            }


            pd.dismiss();

        }

    }
}
