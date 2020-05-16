package com.example.subway_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class Login extends AppCompatActivity {
    Button login_button;
    TextView test_json;
    ProgressDialog pd;
    EditText username;
    EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_button = (Button) findViewById(R.id.login);
        test_json = (TextView) findViewById(R.id.textView2);
        username = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("https://dev.das-360.com/subway_two/wp-json/restos/v3/all/" + username.getText().toString());
            }
        });
    }
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Login.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
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
                Toast toast = Toast.makeText(getApplicationContext(), "Wrong Username!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject oneObject = jArray.getJSONObject(0);
                    String user = oneObject.getString("user");
                    String password = oneObject.getString("pass");
                    String role = oneObject.getString("role");
                    if (password.equals(pass.getText().toString())) {
                        if (role.equals("cashier")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Hello, " + user + " " + role, Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(Login.this, cashier_general.class);
                            intent.putExtra("username", user);
                            startActivity(intent);

                        } else {
                            Intent intent = new Intent(Login.this, Driver.class);
                            intent.putExtra("username", user);
                            startActivity(intent);
                            Toast toast = Toast.makeText(getApplicationContext(), "Hello, " + user, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
