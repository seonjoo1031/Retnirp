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

        //show results
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);



        finding_text = (TextView)findViewById(R.id.finding_text);

        //get ingredients from searchActivity
        Intent intent = getIntent();
        ing_harmful = intent.getStringExtra("harmful");
        ing_allergic = intent.getStringExtra("allergic");
        System.out.println("result : "+ ing_harmful + "/" + ing_allergic);

        //call setIngredients function
        View view_reg1  = findViewById(R.id.reg_ing_1);
        //View view_reg2  = findViewById(R.id.reg_ing_2);
        //View view_reg3  = findViewById(R.id.reg_ing_3);
        View view_new1  = findViewById(R.id.new_ing_1);
        TextView text_reg1 = (TextView)findViewById(R.id.textview_reg_ing_1);
        //TextView text_reg2 = (TextView)findViewById(R.id.textview_reg_ing_2);
        //TextView text_reg3 = (TextView)findViewById(R.id.textview_reg_ing_3);
        TextView text_new1 = (TextView)findViewById(R.id.textview_new_ing_1);

        //StringTokenizer tokens_h = new StringTokenizer(ing_harmful,",");

        text_reg1.setText(ing_harmful.toString());
        System.out.println("ing hARMFUL1: "+ text_reg1.getText().toString());
        //text_reg2.setText(tokens_h.nextToken());
        //System.out.println("ing hARMFUL2: "+ text_reg2.toString());
        //text_reg3.setText(tokens_h.nextToken());
        //System.out.println("ing hARMFUL3: "+ text_reg3.toString());

        //StringTokenizer tokens_a = new StringTokenizer(ing_allergic,",");
        text_new1.setText(ing_allergic.toString());
        System.out.println("ing a1: "+ text_new1.getText().toString());


        //view_reg2.setVisibility(View.VISIBLE);
        //view_reg3.setVisibility(View.VISIBLE);

        int num=0;
        StringTokenizer tokens_h = new StringTokenizer(ing_harmful,",");
        StringTokenizer tokens_a = new StringTokenizer(ing_allergic,",");
        System.out.println("Counting start");

        for(int i = 0 ; tokens_h.hasMoreElements() ; i++){
            //make lists
            System.out.println("counting harmful "+tokens_h.nextToken().toString() + Integer.toString(num));
            num++;
        }
        System.out.println("harmful ingredients num"+Integer.toString(num));

        try {
            for (int i = 0; tokens_a.hasMoreElements(); i++) {
                //make lists
                System.out.println("counting allergic " + tokens_a.nextToken().toString() + Integer.toString(num));
                num++;
            }
        }catch(Exception e){
            num++;
        }
        System.out.println("total ingredients num"+Integer.toString(num));

        finding_text.setText( "There are " + Integer.toString(num) + " Ingredients!" );
        view_new1.setVisibility(View.VISIBLE);
        view_reg1.setVisibility(View.VISIBLE);


        



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
        View view_reg1  = (View)findViewById(R.id.reg_ing_1);
        //View view_reg2  = (View)findViewById(R.id.reg_ing_2);
        //View view_reg3  = (View)findViewById(R.id.reg_ing_3);
        View view_new1  = (View)findViewById(R.id.new_ing_1);
        TextView text_reg1 = (TextView)findViewById(R.id.textview_reg_ing_1);
        //TextView text_reg2 = (TextView)findViewById(R.id.textview_reg_ing_2);
        //TextView text_reg3 = (TextView)findViewById(R.id.textview_reg_ing_3);
        TextView text_new1 = (TextView)findViewById(R.id.textview_new_ing_1);

        StringTokenizer tokens_h = new StringTokenizer(harmful,",");
        text_reg1.setText(tokens_h.nextToken());
        //text_reg2.setText(tokens_h.nextToken());
        //text_reg3.setText(tokens_h.nextToken());

        StringTokenizer tokens_a = new StringTokenizer(harmful,",");
        text_new1.setText(tokens_a.nextToken());
        
        view_new1.setVisibility(View.VISIBLE);
        view_reg1.setVisibility(View.VISIBLE);
        //view_reg2.setVisibility(View.VISIBLE);
        //view_reg3.setVisibility(View.VISIBLE);
        //make token with harmful ingredients
        /*
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
        }*/

        num = 4;
        finding_text.setText( Integer.toString(num) );
    }




}
