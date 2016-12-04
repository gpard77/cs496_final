package com.pardg.junkmovers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JobListingsActivity extends AppCompatActivity {
    private static final String TAG = "JobListingsActivity";

    private CurrentJobList currentJobList;

    ListView jobListView;

//    public void createJobLIst(View view) {
//
//    }

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

        if (item.getItemId() == R.id.memberActiveJobs) {
            Intent intent = new Intent(getApplicationContext(), MemberPageActivity.class);
            intent.putExtra("validated", MainActivity.loggedIn);
            startActivity(intent);
        }


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_listings);

        jobListView = (ListView) findViewById(R.id.jobListView);


        final String jobList = "https://pardg-cs496-junktruck.appspot.com/job";

        DownloadTask task = new DownloadTask();
        task.execute(jobList);

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

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
                String keyInfo = jsonObject.getString("job keys");
                final ArrayList<String> arrList = new ArrayList<>();

                JSONArray arr = new JSONArray(keyInfo);

                for (int i = 0; i < arr.length(); i++) {
                    Log.i("Extracted: ", arr.get(i).toString());
                    arrList.add(arr.get(i).toString());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplication(), android.R.layout
                        .simple_list_item_1, arrList);
                jobListView.setAdapter(arrayAdapter);

                jobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("Tapped: ", arrList.get(i));
                        String keyClicked = arrList.get(i);
                        Intent intent = new Intent(getApplicationContext(), JobDetailsActivity.class);
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

    private CurrentJobList getCurrentDetails(String jsonData) throws JSONException{

        JSONObject jobItems = new JSONObject(jsonData);
        JSONArray keyList = jobItems.getJSONArray("key");
        //String[] jobKey = jobItems.getString("key");
        ArrayList<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < keyList.length(); i++) {
            tempList.add(i);
        }

        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout
                .simple_list_item_1, tempList);
        jobListView.setAdapter(arrayAdapter);

        Log.i(TAG, "getCurrentDetails: JSON KEY" + keyList.getString(0));

        return new CurrentJobList();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manger = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manger.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
