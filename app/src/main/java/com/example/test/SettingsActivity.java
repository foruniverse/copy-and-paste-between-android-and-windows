package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    EditText ip;
    EditText port;
    String ip_input;
    String port_input;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.comment_item:
                Log.d("third","test1");
                Intent intent = new Intent(SettingsActivity.this, CommentActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("设置");
        Log.d("third","fanhui1");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button commit=(Button) findViewById(R.id.commit);
        ip=(EditText) findViewById(R.id.default_ip);
        ip.setInputType(InputType.TYPE_CLASS_NUMBER);
        String digits="0123456789.";
        ip.setKeyListener(DigitsKeyListener.getInstance(digits));

        port=(EditText) findViewById(R.id.default_port);
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        String name = pref.getString("ip_address","");
        String port1= pref.getString("port","");
        ip.setText(name);
        port.setText(port1);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("ip_address",ip.getText().toString());
                Log.d("third",ip.getText().toString())  ;
                editor.putString("port",port.getText().toString());
                editor.apply();
                Intent intent_return = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent_return);
            }
        });



    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        Log.d("third","fanhui");
//        finish();
//        return super.onSupportNavigateUp();
//    }




}
