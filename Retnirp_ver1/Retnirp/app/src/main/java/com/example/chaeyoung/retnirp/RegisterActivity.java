package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * Created by Chaeyoung on 2016-11-23.
 */

public class RegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }
    public void mOnclick(View v){
        Intent intent = new Intent();

        switch(v.getId()) {
            // Mmenu button
            case R.id.image_button_menu_reg :
                intent.setClass(this, MenuActivity.class);
                startActivity(intent);
                break;

            case R.id.image_button_add :
                break;
        }

    }
}
