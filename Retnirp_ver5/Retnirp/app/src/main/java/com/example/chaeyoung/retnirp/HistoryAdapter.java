package com.example.chaeyoung.retnirp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by Chaeyoung on 2016-12-07.
 */

public class HistoryAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<HistoryItem> historyItemList = new ArrayList<HistoryItem>();
    LayoutInflater inflater;


    //생성자
    public HistoryAdapter(){

    }

    @Override
    public int getCount() {
        return historyItemList.size();
    }
    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        AQuery aq = new AQuery( convertView );

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.historyitem, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView safetyTextView = (TextView) convertView.findViewById(R.id.text_safety) ;
        TextView dateTextView = (TextView) convertView.findViewById(R.id.text_date) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        HistoryItem listViewItem = historyItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        aq.id(iconImageView).image(listViewItem.getImage());
        System.out.println("adapter : " + listViewItem.getImage());
        //iconImageView.setImageDrawable(listViewItem.getImage());
        safetyTextView.setText(listViewItem.getSafety());
        dateTextView.setText(listViewItem.getDate());

        safetyTextView.setTextColor(Color.parseColor(listViewItem.getTextColor()));


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
            return historyItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String image, String safety, String date, String color) {
        HistoryItem item = new HistoryItem();
        item.setImage(image);
        item.setSafety(safety);
        item.setDate(date);
        item.setTextColor(color);

        historyItemList.add(item);
    }
}
