package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.StringTokenizer;

/**
 * Created by Chaeyoung on 2016-11-30.
 */

public class ResultActivity extends Activity {
    String ing_harmful;
    String ing_allergic;
    TextView finding_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        finding_text = (TextView)findViewById(R.id.finding_text);

        //get ingredients from searchActivity
        Intent intent = getIntent();
        ing_harmful = intent.getStringExtra("harmful");
        ing_allergic = intent.getStringExtra("allergic");

        //call setIngredients function

        //show results
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

    }

    public void mOnclickResult(View v){
        Intent intent = new Intent();

        switch(v.getId()) {
            // Mmenu button
            case R.id.image_button_menu :
                intent.setClass(this, MenuActivity.class);
                startActivity(intent);
                break;
        }

    }

    // set text and lists
    public void setIngredients (String harmful, String allergic){
        int num = 0;

        //make token with harmful ingredients
        StringTokenizer tokens_h = new StringTokenizer(harmful,",");
        for(int i = 0 ; tokens_h.hasMoreElements() ; i++){
            //make lists

            num++;
        }

        //make token with allergic ingredients
        StringTokenizer tokens_a = new StringTokenizer(harmful,",");
        for(int i = 0 ; tokens_a.hasMoreElements() ; i++){
            //make lists
            num++;
        }

        finding_text.setText( Integer.toString(num) );
    }




}
