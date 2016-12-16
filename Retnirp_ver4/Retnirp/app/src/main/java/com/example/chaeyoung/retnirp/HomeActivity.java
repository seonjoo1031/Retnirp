package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import android.content.Context;
import android.support.v4.content.ContextCompat;
/**
 * Created by Chaeyoung on 2016-11-23.
 */

public class HomeActivity extends Activity {
    String userID;
    private ListView  ListView_history;
    private HistoryAdapter Adapter_history;
    final String COLOR_SAFE =  "#4FD2C2";
    final String COLOR_DETECTED =  "#F9CA9C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get userID from previous page
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        System.out.println("home : " +userID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //Recent history
        //get image and id\

        Adapter_history = new HistoryAdapter();
        ListView_history = (ListView) findViewById(R.id.list_history);
        ListView_history.setAdapter(Adapter_history);

        //test
        Adapter_history.addItem(ContextCompat.getDrawable(this, R.drawable.profile3),"Safe","Tuesday",COLOR_SAFE);
        Adapter_history.addItem(ContextCompat.getDrawable(this, R.drawable.profile3),"Detected","Wednesday",COLOR_DETECTED);
        Adapter_history.addItem(ContextCompat.getDrawable(this, R.drawable.profile3),"Detected","Friday",COLOR_DETECTED);
        Adapter_history.addItem(ContextCompat.getDrawable(this, R.drawable.profile3),"Safe","Tuesday",COLOR_SAFE);
        Adapter_history.notifyDataSetChanged();

        //History 생성
        for(int i =0 ;i<3 ;i++){
            //통신으로 하나씩 가져오기?

            //Bitmap을 drawable로 변환 방법
            //Drawable drawable = new BitmapDrawable(bitmap);

            //Adapter_history.addItem(ContextCompat.getDrawable(this, R.drawable.profile3),"Safe","Tuesday",COLOR_SAFE);
            Adapter_history.notifyDataSetChanged();
        }
        //fix the height of theh list
        listViewHeightSet(Adapter_history , ListView_history);

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
