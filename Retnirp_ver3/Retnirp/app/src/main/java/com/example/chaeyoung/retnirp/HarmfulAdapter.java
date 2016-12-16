package com.example.chaeyoung.retnirp;

import android.widget.BaseAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.drawable.Drawable;

/**
 * Created by Chaeyoung on 2016-12-02.
 */

public class HarmfulAdapter extends BaseAdapter {
    // 문자열을 보관 할 ArrayList
    private ArrayList<String>   h_List;
    //private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    private Drawable iconDrawable ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }


    // 생성자
    public HarmfulAdapter() {
        h_List = new ArrayList<String>();
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return h_List.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return h_List.get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        return convertView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(String _msg) {
        System.out.println(_msg + "is in harmful list!");
        h_List.add(_msg);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        h_List.remove(_position);
    }
}
