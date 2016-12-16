package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Chaeyoung on 2016-11-23.
 */

public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void mOnClick_menu(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);

        switch (v.getId()) {
            // register button
            case R.id.image_button_close :
                finish();
                break;

            case R.id.image_menu_search :
                intent.setClass(this, SearchActivity.class);
                finish();
                startActivity(intent);
                break;

            case R.id.image_menu_register :
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.image_menu_signout : // log out
                intent.setClass(this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.image_menu_delete :
                intent.setClass(this, UnsubscribeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
