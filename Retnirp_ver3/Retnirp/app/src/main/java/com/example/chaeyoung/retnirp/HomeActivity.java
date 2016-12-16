package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Chaeyoung on 2016-11-23.
 */

public class HomeActivity extends Activity {
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get userID from previous page
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        System.out.println("home : " +userID);


        //set id to customer
        //if(userID.isEmpty()){ userID = "CUSTOMER"; }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void mOnClick(View v){
        Intent intent = new Intent(this, RegisterActivity.class);

        switch(v.getId()){
            // register button
            case R.id.image_button_register :
                intent.putExtra("userID", userID);
                startActivity(intent);
                break;

            // search button
            case R.id.image_button_search :
                intent.setClass(this, SearchActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                break;

            // menu button
            case R.id.image_button_menu_home :
                intent.setClass(this, MenuActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                break;
        }
    }

}
