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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MemberPageActivity extends AppCompatActivity {

    ListView memberJobListView;

    public void backToJobList(View view) {
        Intent intent = new Intent(getApplicationContext(), JobListingsActivity.class);
        startActivity(intent);
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

        if (item.getItemId() == R.id.adminDashboard) {
            Toast.makeText(this, "You Do Not Have Admin Rights", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.logOut) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);

        memberJobListView = (ListView) findViewById(R.id.memberJobListView);

        Intent intent = getIntent();
        String message = intent.getStringExtra("validated");

        final String memberJobs = "https://pardg-cs496-junktruck.appspot.com/member/" + message;

        MemberJobTask task = new MemberJobTask();
        task.execute(memberJobs);


    }

    public class MemberJobTask extends AsyncTask<String, Void, String> {

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
                String keyInfo = jsonObject.getString("jobs");
                final ArrayList<String> arrList = new ArrayList<>();

                JSONArray arr = new JSONArray(keyInfo);

                for (int i = 0; i < arr.length(); i++) {
                    Log.i("Extracted: ", arr.get(i).toString());
                    arrList.add(arr.get(i).toString());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplication(), android.R.layout
                        .simple_list_item_1, arrList);
                memberJobListView.setAdapter(arrayAdapter);

                memberJobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("Tapped: ", arrList.get(i));
                        String keyClicked = arrList.get(i);
                        Intent intent = new Intent(getApplicationContext(), MemberJobDetailsActivity.class);
                        intent.putExtra("keyClicked", keyClicked);
                        startActivity(intent);
                    }
                });

                Log.i("Keys: ", keyInfo);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }




}
