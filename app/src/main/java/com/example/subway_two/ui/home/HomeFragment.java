package com.example.subway_two.ui.home;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.subway_two.ListAdapter;
import com.example.subway_two.cashier_general;
import com.example.subway_two.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment {
  public  ListView list;
    private HomeViewModel homeViewModel;
    ProgressDialog pd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
         list = (ListView) root.findViewById(R.id.listviewdriver);
        new JsonTask_active().execute("https://dev.das-360.com/subway_two/wp-json/restos/v3/active-drivers/","https://dev.das-360.com/subway_two/wp-json/restos/v3/busy-drivers/");
        return root;
    }
    private class JsonTask_active extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getContext());
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            HttpURLConnection connection2 = null;
            BufferedReader reader2 = null;
            try {
                URL url1 = new URL(params[0]);
                URL url2 = new URL(params[1]);
                connection = (HttpURLConnection) url1.openConnection();
                connection.connect();
                connection2 = (HttpURLConnection) url2.openConnection();
                connection2.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
             /*   NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext(), "001")
                                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                .setContentTitle("My notification")
                                .setContentText("Hello World!");
                NotificationManager mNotificationManager =  (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.notify(001, mBuilder.build());*/
                InputStream stream2 = connection2.getInputStream();
                reader2 = new BufferedReader(new InputStreamReader(stream2));
                StringBuffer buffer2 = new StringBuffer();
                String line2 = "";
                while ((line2 = reader2.readLine()) != null) {
                    buffer2.append(line2+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }

            if (result.isEmpty()){
                Toast toast = Toast.makeText(getContext(), "empty response", Toast.LENGTH_SHORT);
                toast.show();
            }
            try {
                JSONArray jArray = new JSONArray(result);
                ArrayList<JSONObject> listItems=getArrayListFromJSONArray(jArray);
                ListAdapter adapter=new ListAdapter(getContext(),R.layout.list_driver,R.id.txtid,listItems);
                list.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){

            ArrayList<JSONObject> aList=new ArrayList<JSONObject>();

            try {

                if (jsonArray != null) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        aList.add(jsonArray.getJSONObject(i));

                    }

                }

            }catch (JSONException je){je.printStackTrace();}

            return  aList;

        }
    }
}
