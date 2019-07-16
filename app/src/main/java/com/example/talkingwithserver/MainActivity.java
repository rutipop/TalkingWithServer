package com.example.talkingwithserver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    String logged_user_name;
    String ServerUrl = "http://hujipostpc2019.pythonanywhere.com/";
    SharedPreferences sp;
    int reqCodeFirstLogin =1;
    Server myServer;
    TextView uNameView;
    EditText pNameView;
    ImageView imageurlView;
    TextView welcoming ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uNameView = (TextView) findViewById(R.id.userName);
        pNameView = (EditText) findViewById(R.id.preetyName);
        imageurlView = (ImageView) findViewById(R.id.MyImage);
        welcoming = (TextView) findViewById(R.id.welcome_msg);

        // loading from shared preferences:
        sp = getSharedPreferences("saved_username", Context.MODE_PRIVATE);
        loadNameFromSP();

        if (logged_user_name == null || logged_user_name.isEmpty()){
            //no username yet, so we ask for one:
            Intent intent = new Intent(this, firstLoginActivity.class);
            startActivityForResult(intent, reqCodeFirstLogin);

        }
        else {
            //connect to our server:
            myServer = new Server(logged_user_name, this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK||data==null) return;
        if(requestCode== reqCodeFirstLogin){
            //if we are here it means we got a legit username:
            logged_user_name =data.getStringExtra("filled user name");

            //saving to SP:
            saveNameToSP();

            //connect to our server:
            myServer = new Server(logged_user_name, this);
        }
    }


    public  void  loadNameFromSP(){
       String result =sp.getString("saved_username",null);
        logged_user_name =result;

    }

    public void saveNameToSP(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("saved_username", logged_user_name);
        editor.apply();

    }


    public void showInfo(User user){
        if (user == null){
            return;
        }
        String userHeadline = user.username;
        if(user.pretty_name == null || user.pretty_name.isEmpty()){
            welcoming.setText("welcome "+ user.username);
        }
        else{
            welcoming.setText("welcome again "+ user.pretty_name);
        }

        uNameView.setText(userHeadline);
        pNameView.setText(user.pretty_name);
        Picasso.get().load(ServerUrl+user.image_url).into(imageurlView);
    }


   public void pNameButtonHandler (View view){
       String pname=((EditText)(findViewById(R.id.preetyName))).getText().toString();
       myServer.updatePrettyName(pname);
   }




}
