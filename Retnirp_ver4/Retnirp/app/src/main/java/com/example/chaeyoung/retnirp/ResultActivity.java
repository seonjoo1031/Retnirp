package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import java.util.StringTokenizer;

/**
 * Created by Chaeyoung on 2016-11-30.
 */

public class ResultActivity extends Activity {
    String ing_harmful;
    String ing_allergic;
    TextView finding_text;
    private ListView                ListView_harmful, ListView_allergic;
    private ArrayAdapter<String>    Adapter_harmful, Adapter_allergic;

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
/*
        //call setIngredients function
        View view_reg1  = findViewById(R.id.reg_ing_1);
        View view_new1  = findViewById(R.id.new_ing_1);


        TextView text_reg1 = (TextView)findViewById(R.id.textview_reg_ing_1);
        TextView text_new1 = (TextView)findViewById(R.id.textview_new_ing_1);


        text_reg1.setText(ing_harmful.toString());
        System.out.println("ing hARMFUL1: "+ text_reg1.getText().toString());

        //StringTokenizer tokens_a = new StringTokenizer(ing_allergic,",");
        text_new1.setText(ing_allergic.toString());
        System.out.println("ing a1: "+ text_new1.getText().toString());
*/

        //harmful Adapter
        // harmfullayout으로 어댑터 생성
        //Adapter_harmful = new ArrayAdapter<String>(getApplicationContext(), R.layout.harmfulitem);
        Adapter_harmful = new ArrayAdapter<String>(getApplicationContext(),R.layout.harmfulitem, R.id.textview_harmful_name);
        ListView_harmful = (ListView) findViewById(R.id.list_harmful);
        ListView_harmful.setAdapter(Adapter_harmful);

        //Allergic adpater
        Adapter_allergic = new ArrayAdapter<String>(getApplicationContext(),R.layout.allergicitem, R.id.textview_allergic_name);
        ListView_allergic = (ListView) findViewById(R.id.list_allergic);
        ListView_allergic.setAdapter(Adapter_allergic);

        int num=0;
        StringTokenizer tokens_h = new StringTokenizer(ing_harmful,",");
        StringTokenizer tokens_a = new StringTokenizer(ing_allergic,",");
        System.out.println("Counting start");


        for(int i = 0 ; tokens_h.hasMoreElements() ; i++){
            //make lists
            // ListView에 아이템 추가
            Adapter_harmful.add(tokens_h.nextToken().toString());
            Adapter_harmful.notifyDataSetChanged();
            //System.out.println("counting harmful "+tokens_h.nextToken().toString() + Integer.toString(num));
            num++;
        }
        System.out.println("num of harmful item : "+ Integer.toString(Adapter_harmful.getCount()));
        System.out.println("harmful ingredients num"+Integer.toString(num));

        try {
            for (int i = 0; tokens_a.hasMoreElements(); i++) {
                //make lists
                Adapter_allergic.add(tokens_a.nextToken().toString());
                Adapter_allergic.notifyDataSetChanged();
                //System.out.println("counting allergic " + tokens_a.nextToken().toString() + Integer.toString(num));
                num++;
            }
        }catch(Exception e){
            num++;
        }
        System.out.println("total ingredients num"+Integer.toString(num));

        finding_text.setText( "There are " + Integer.toString(num) + " Ingredients!" );


        //set Height of list view
        listViewHeightSet(Adapter_harmful , ListView_harmful);
        listViewHeightSet(Adapter_allergic , ListView_allergic);

        //view_new1.setVisibility(View.VISIBLE);
        //view_reg1.setVisibility(View.VISIBLE);


    }

    private static void listViewHeightSet(BaseAdapter listAdapter, ListView listView){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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






}
