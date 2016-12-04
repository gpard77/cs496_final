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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class JobDetailsActivity extends AppCompatActivity {

    TextView captionSetTextView;
    TextView addressSetTextView;
    TextView dropSetTextView;
    TextView offerSetTextView;

    private String message;

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
        setContentView(R.layout.activity_job_details);

        captionSetTextView = (TextView) findViewById(R.id.captionSetTextView);
        addressSetTextView = (TextView) findViewById(R.id.addressSetTextView);
        dropSetTextView = (TextView) findViewById(R.id.dropSetTextView);
        offerSetTextView = (TextView) findViewById(R.id.offerSetTextView);

        Intent intent = getIntent();
        message = intent.getStringExtra("keyClicked");
        Log.i("From Job List", message);
        Toast.makeText(this, intent.getStringExtra("keyClicked"), Toast.LENGTH_SHORT).show();

        final String jobDetail = "https://pardg-cs496-junktruck.appspot.com/job/" + message;

        DownloadJobDetails task = new DownloadJobDetails();
        task.execute(jobDetail);




    }

    public class DownloadJobDetails extends AsyncTask<String, Void, String> {

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
                String caption = jsonObject.getString("caption");
                String address = jsonObject.getString("street");
                String address2 = jsonObject.getString("city");
                String zip_code = jsonObject.getString("zip_code");
                String dropLocation = jsonObject.getString("drop");
                String offerAmount = jsonObject.getString("offer");
                captionSetTextView.setText(caption);
                addressSetTextView.setText(address + "\r\n" + address2 + "\r\n" + zip_code);
                dropSetTextView.setText(dropLocation);
                offerSetTextView.setText(offerAmount);




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void acceptJob(View view) {

        RequestQueue acceptJobQueue = Volley.newRequestQueue(this);
        String url = "https://pardg-cs496-junktruck.appspot.com/member/" + MainActivity.loggedIn + "/job/" + message;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Toast.makeText(JobDetailsActivity.this, "Response: ", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast.makeText(JobDetailsActivity.this, "Error in Response", Toast.LENGTH_SHORT).show();
            }
        }){
            public Map<String, String> getParams() {
                Map<String, String> myData = new HashMap<String, String>();
                myData.put("jobs", message);
                myData.put("Content-Type", "application/json");

                return myData;
            }
        };

        acceptJobQueue.add(stringRequest);

//        RequestQueue deleteJobQueue = Volley.newRequestQueue(this);
//        String url2 = "https://pardg-cs496-junktruck.appspot.com/job/" + message;
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest2 = new StringRequest(Request.Method.DELETE, url2,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        //mTextView.setText("Response is: "+ response.substring(0,500));
//                        //Toast.makeText(AdminDeleteJobDetailsActivity.this, "Response: ", Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //mTextView.setText("That didn't work!");
//                //Toast.makeText(AdminDeleteJobDetailsActivity.this, "Error in Response", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            public Map<String, String> getParams() {
//                Map<String, String> myData = new HashMap<String, String>();
//                //myData.put("key", message);
//                myData.put("Content-Type", "application/json");
//
//                return myData;
//            }
//        };
//
//        deleteJobQueue.add(stringRequest2);

        Intent intent = new Intent(getApplicationContext(), MemberAddJobConfActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);

    }
}
