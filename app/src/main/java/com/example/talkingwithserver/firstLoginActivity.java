package com.example.talkingwithserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class firstLoginActivity extends AppCompatActivity {
    String filled_username;
    Button setName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        setName  = (Button)findViewById(R.id.setUserButton);
        setName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filled_username = ((EditText)(findViewById(R.id.userNameEditText))).getText().toString();
                if (filled_username.isEmpty() || !(isAlphaNumeric(filled_username))){
                    // toast bad input:
                    Toast.makeText(firstLoginActivity.this, "user name should be only with letters or numbers!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent =new Intent();
                    intent.putExtra("filled user name",filled_username);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

    }


    public boolean isAlphaNumeric (String s){
        return s != null && s.matches("^[a-zA-Z0-9]*$");
    }




}
