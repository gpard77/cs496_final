package com.pardg.junkmovers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreateNewVolunteerActivity extends AppCompatActivity {

    private String user_name;
    private String passcode;
    private String first_name;
    private String last_name;

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText userNameEditText;
    EditText passcodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_volunteer);

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        passcodeEditText = (EditText) findViewById(R.id.passcodeEditText);

    }

    public void createNewMember(View view) {

        first_name = firstNameEditText.getText().toString();
        last_name = lastNameEditText.getText().toString();
        user_name = userNameEditText.getText().toString();
        passcode = passcodeEditText.getText().toString();

        RequestQueue addMemberQueue = Volley.newRequestQueue(this);
        String url = "https://pardg-cs496-junktruck.appspot.com/member";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Toast.makeText(CreateNewVolunteerActivity.this, "Response: ", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast.makeText(CreateNewVolunteerActivity.this, "Error in Response", Toast.LENGTH_SHORT).show();
            }
        }){
            public Map<String, String> getParams() {
                Map<String, String> myData = new HashMap<String, String>();
                myData.put("first_name", first_name);
                myData.put("last_name", last_name);
                myData.put("user_name", user_name);
                myData.put("passcode", passcode);

                return myData;
            }
        };

        addMemberQueue.add(stringRequest);

        Intent intent = new Intent(getApplicationContext(), CreateNewMemberConfActivity.class);
        startActivity(intent);

    }
}
