package com.pardg.junkmovers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MemberAddJobConfActivity extends AppCompatActivity {

    public void toMemberPage(View view) {
        Intent intent = getIntent();
        String thisMessage = intent.getStringExtra("message");

        Intent intent2 = new Intent(getApplicationContext(), MemberPageActivity.class);
        intent2.putExtra("validated", MainActivity.loggedIn);
        startActivity(intent2);
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
        setContentView(R.layout.activity_member_add_job_conf);
    }
}
