package com.pardg.junkmovers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddJobActivity extends AppCompatActivity {

    private String caption;
    private String street;
    private String city;
    private String zip_code;
    private String drop_location;
    private String offer_amount;

    EditText captionEditText;
    EditText streetEditText;
    EditText cityEditText;
    EditText zipEditText;
    EditText dropEditText;
    EditText offerEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        captionEditText = (EditText) findViewById(R.id.captionEditText);
        streetEditText = (EditText) findViewById(R.id.streetEditText);
        cityEditText = (EditText) findViewById(R.id.cityEditText);
        zipEditText = (EditText) findViewById(R.id.zipEditText);
        dropEditText = (EditText) findViewById(R.id.dropEditText);
        offerEditText = (EditText) findViewById(R.id.offerEditText);

    }

    public void handleFormData(View view) {
        caption = captionEditText.getText().toString();
        street = streetEditText.getText().toString();
        city = cityEditText.getText().toString();
        zip_code = zipEditText.getText().toString();
        drop_location = dropEditText.getText().toString();
        offer_amount = offerEditText.getText().toString();
        Log.i("Caption: ", caption);
        Log.i("Street: ", street);
        Log.i("City: ", city);
        Log.i("Zip Code: ", zip_code);
        Log.i("Drop Location: ", drop_location);
        Log.i("Offer: ", offer_amount);


        RequestQueue addJobQueue = Volley.newRequestQueue(this);
        String url = "https://pardg-cs496-junktruck.appspot.com/job";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Toast.makeText(AddJobActivity.this, "Response: ", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast.makeText(AddJobActivity.this, "Error in Response", Toast.LENGTH_SHORT).show();
            }
        }){
            public Map<String, String>getParams() {
                Map<String, String> myData = new HashMap<String, String>();
                myData.put("caption", caption);
                myData.put("street", street);
                myData.put("city", city);
                myData.put("zip_code", zip_code);
                myData.put("drop", drop_location);
                myData.put("offer", offer_amount);

                return myData;
            }
        };

        addJobQueue.add(stringRequest);

        Intent intent = new Intent(getApplicationContext(), AddJobConfActivity.class);
        startActivity(intent);

    }
}
