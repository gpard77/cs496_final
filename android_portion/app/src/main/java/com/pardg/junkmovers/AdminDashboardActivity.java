package com.pardg.junkmovers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AdminDashboardActivity extends AppCompatActivity {

    public void addJobToJobList(View view) {
        Intent intent = new Intent(getApplicationContext(), AddJobActivity.class);
        startActivity(intent);
    }

    public void toActiveJobList(View view) {
        Intent intent = new Intent(getApplicationContext(), JobListingsActivity.class);
        startActivity(intent);
    }

    public void toAdminDeleteJob(View view) {
        Intent intent = new Intent(getApplicationContext(), AdminDeleteActivity.class);
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

        if (item.getItemId() == R.id.logOut) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
    }
}
