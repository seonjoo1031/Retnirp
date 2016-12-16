package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Chaeyoung on 2016-11-23.
 */

public class HomeActivity extends Activity {
    String userID;
    private ListView  ListView_history;
    private HistoryAdapter Adapter_history;
    final String COLOR_SAFE =  "#4FD2C2";
    final String COLOR_DETECTED =  "#F9CA9C";
    public String SERVER = "http://192.168.43.143:8080/retnirpWeb5/viewHistory.jsp";



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

        new Upload(SERVER, this).execute();



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

    //async task to upload image
    private class Upload extends AsyncTask<Void,Void,String> {
        private String server;
        private Context context;
        String result="";

        public Upload(String server, Context context){
            this.server = server;
            this.context=context;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(server);
            post.setHeader("Connection", "Keep-Alive");
            post.setHeader("Accept-Charset", "UTF-8");
            post.setHeader("ENCTYPE", "multipart/form-data");

            post = new HttpPost(server+"?userId="+userID);

            try {

                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(),
                                "utf-8"));

                String line = null;


                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }

                System.out.println("[result]");
                System.out.println(result);



            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // post 형식의 데이터를 서버로 전달

            return "SUCCESS";




        }



        @Override
        protected void onPostExecute(String s) {
            //show image uploaded
            String[][] parsedData = jsonParserList(result); // 받은 메시지를 json 파싱

            Adapter_history = new HistoryAdapter();
            ListView_history = (ListView) findViewById(R.id.list_history);
            ListView_history.setAdapter(Adapter_history);

            /*
            //test
            Adapter_history.addItem(ContextCompat.getDrawable(context, R.drawable.profile3),"Safe","Tuesday",COLOR_SAFE);
            Adapter_history.addItem(ContextCompat.getDrawable(context, R.drawable.profile3),"Detected","Wednesday",COLOR_DETECTED);
            Adapter_history.addItem(ContextCompat.getDrawable(context, R.drawable.profile3),"Detected","Friday",COLOR_DETECTED);
            Adapter_history.addItem(ContextCompat.getDrawable(context, R.drawable.profile3),"Safe","Tuesday",COLOR_SAFE);
            Adapter_history.notifyDataSetChanged();
            */
            //Adapter_history.addItem(parsedData[0][4],"Safe",parsedData[0][1],COLOR_SAFE);


            //History 생성
            for(int i =0 ;i< parsedData.length ;i++){
                //통신으로 하나씩 가져오기?

                //Bitmap을 drawable로 변환 방법
                //Drawable drawable = new BitmapDrawable(bitmap);

                //Adapter_history.addItem(ContextCompat.getDrawable(this, R.drawable.profile3),"Safe","Tuesday",COLOR_SAFE);
                if(parsedData[i][2]!=null || parsedData[i][3]!=null)
                    Adapter_history.addItem(parsedData[i][4],"Detected",parsedData[i][1],COLOR_DETECTED);
                else
                    Adapter_history.addItem(parsedData[i][4],"Safe",parsedData[i][1],COLOR_SAFE);



                Adapter_history.notifyDataSetChanged();
            }
            //fix the height of theh list
            listViewHeightSet(Adapter_history , ListView_history);
        }
    }

    public String[][] jsonParserList(String pRecvServerPage) {

        Log.i("서버에서 받은 전체 내용 : ", pRecvServerPage);

        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");


            // 받아온 pRecvServerPage를 분석하는 부분
            String[] jsonName = {"msg1", "msg2", "msg3", "msg4", "msg5"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if(json != null) {
                    for(int j = 0; j < jsonName.length; j++) {
                        parseredData[i][j] = json.getString(jsonName[j]);
                    }
                }
            }

            System.out.println("object 수 : " + jArr.length());


            // 분해 된 데이터를 확인하기 위한 부분
            for(int i=0; i<parseredData.length; i++){
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][0]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][1]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][2]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][3]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][4]);
            }




            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
