package com.pardg.junkmovers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class LogInActivity extends AppCompatActivity {

    EditText userName;
    String keyValue;
    String validated;

    public void toAdminDashboard(View view) {

        String adminLogin = userName.getText().toString();

        if (adminLogin == null) {
            Toast.makeText(this, "Must LOG IN as Admin", Toast.LENGTH_SHORT).show();
        }

        if (adminLogin.equalsIgnoreCase("admin")) {
            Intent intent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Must LOG IN as Admin", Toast.LENGTH_SHORT).show();
        }
    }

    public void toJobListings(View view) {

        String name = null;
        try {
            name = URLEncoder.encode(userName.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String verifyLogIn = "https://pardg-cs496-junktruck.appspot.com/member/search?user_name=" + name;

        LogInTask task = new LogInTask();
        task.execute(verifyLogIn);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logOut) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userName = (EditText) findViewById(R.id.userName);


    }

    public class LogInTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                keyValue = jsonObject.getString("keys");
                //Log.i("First try: ", keyValue);

                JSONArray arr = new JSONArray(keyValue);
                validated = arr.get(0).toString();
                Log.i("Key Extracted: ", validated);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!keyValue.equalsIgnoreCase("")) {
                Intent intent = new Intent(getApplicationContext(), MemberPageActivity.class);
                MainActivity.loggedIn = validated;
                intent.putExtra("validated", validated);
                startActivity(intent);
            } else {
                Toast.makeText(getApplication(), "Invalid Credentials. Please Try Again", Toast.LENGTH_LONG).show();
            }

        }

    }

}
