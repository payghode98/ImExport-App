package com.imexportsolutions.imexportsolutions;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import utility.HttpManager;
import utility.RequestPackage;

public class CompletedListFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvcompleted;
    String[] from={"ID","DispatchDate"};
    int[] to={R.id.tvPOID,R.id.tvDispatchedDate};
    ArrayList<HashMap<String,String>> data= new ArrayList<>();
    int FCAID,ICAID;
    String Country;
    CompletedListDataTask task = new CompletedListDataTask();

    SimpleAdapter sa;
    int datacount = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //instead of above line we return this...

        View Rootview = inflater.inflate(R.layout.fragment_completed_list,container,false);
        lvcompleted = Rootview.findViewById(R.id.lvcompleted);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Country  = sp.getString("Country",null);
        ICAID  = sp.getInt("ICAID",0);
        FCAID  = sp.getInt("FCAID",0);
        sa = new SimpleAdapter(getActivity(), data, R.layout.custom_card_cmlpt_list,from, to);
        new CompletedListDataTask().execute();
        lvcompleted.setOnItemClickListener(this);
        lvcompleted.setAdapter(sa);
        task.execute();
        return  Rootview;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String clickedid = data.get(position).get("ID").split(" : ")[1];
        Intent i = new Intent(getActivity(), CompletedPoDetails.class);
        i.putExtra("ID",clickedid);
        startActivity(i);
    }

    class CompletedListDataTask extends AsyncTask<String ,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            //pd.show();
        }


        @Override
        protected String doInBackground(String... strings) {

            if(Country.equals("INDIA")) {
                RequestPackage rp = new RequestPackage();
                rp.setMethod("GET");
                rp.setUri(HttpManager.URL+"CompletedPOICA");
                rp.setParam("Status", "Import Cleared");
                rp.setParam("ID",ICAID+"");
                String ans = HttpManager.getData(rp);
                return ans;
            }
            else{

                RequestPackage rp = new RequestPackage();
                rp.setMethod("GET");
                rp.setUri(HttpManager.URL+"CompletedPOFCA");
                rp.setParam("Status", "FCA Dispatched");
                rp.setParam("ID",FCAID+"");
                String ans = HttpManager.getData(rp);
                return ans;
            }

        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);

            try {

                JSONArray Waiting = new JSONArray(ans);
                int newdatacount = Waiting.length();
                for(int i = 0; i <Waiting.length(); i++){
                    data.clear();
                    JSONObject obj = Waiting.getJSONObject(i);
                    String ID = obj.getString("PurchaseOrderID");
                    String DispatchDate = obj.getString(("DispatchdatebySupp"));
                    HashMap<String,String> map1=new HashMap<>();
                    map1.put("ID","PurchaseOrder : "+ID);
                    map1.put("DispatchDate","Dispatch Date : " +DispatchDate.split("#")[0]);
                    data.add(map1);
                }
                sa.notifyDataSetChanged();

                if(datacount==0){
                    // do nothing, first time
                }
                else if(newdatacount>datacount){
                    // show notification
                    showNotification();
                }


                datacount = newdatacount;


            } catch (JSONException e) {
                e.printStackTrace();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    task = new CompletedListDataTask();
                    task.execute();
                }
            }, 10000);


            //pd.dismiss();

        }
        private void showNotification() {
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(getActivity())
                    .setSmallIcon(R.drawable.ic_star_new) // notification icon
                    .setContentTitle("Star Inc.") // title for notification
                    .setContentText("You just Release a Purchase Order")// message for notification
                    .setSound(uri)
                    .setVibrate(new long[] { 100,100 })
                    .setAutoCancel(true);

            // clear notification after click
           /* Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
            mBuilder.setContentIntent(pi);
           */ NotificationManager mNotificationManager =
                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }

    }
}
