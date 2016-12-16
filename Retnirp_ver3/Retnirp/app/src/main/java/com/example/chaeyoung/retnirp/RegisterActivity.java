package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Chaeyoung on 2016-11-23.
 */

public class RegisterActivity extends Activity {

    String userID=null;
    String allergic;
    private ListView              ListView_allergic;
    private ArrayAdapter<String>   Adapter_allergic;
    //get allergic ingredients from server
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent thisintent = getIntent();
        userID = thisintent.getStringExtra("userID");
        System.out.println("Register  : " + userID);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //서버사용

        if(userID != null){
            String result = SendByHttp(); // 메시지를 서버에 보냄
            String[][] parsedData = jsonParserList(result); // 받은 메시지를 json 파싱
        }


        //test
        //allergic = "new,one,hello";
        ;


        //Allergic adpater
        Adapter_allergic = new ArrayAdapter<String>(getApplicationContext(),R.layout.allergicitem, R.id.textview_allergic_name);
        ListView_allergic = (ListView) findViewById(R.id.list_allergic_reg);
        ListView_allergic.setAdapter(Adapter_allergic);

        StringTokenizer tokens_a = new StringTokenizer(allergic,",");
        System.out.println("registered allergic : " + allergic);


        for(int i = 0 ; tokens_a.hasMoreElements() ; i++){
            //make lists
            // ListView에 아이템 추가
            Adapter_allergic.add(tokens_a.nextToken().toString());
            Adapter_allergic.notifyDataSetChanged();
        }
        listViewHeightSet(Adapter_allergic , ListView_allergic);

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

    public void mOnclick(View v){
        Intent intent = new Intent();

        switch(v.getId()) {
            // Mmenu button
            case R.id.image_button_menu_reg :
                intent.setClass(this, MenuActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.image_button_add :
                intent.setClass(this, PutIngredientActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                finish();
                break;
        }

    }

    private String SendByHttp() {

        // 서버를 설정해주세요!!!
        //String URL = "http://192.168.1.201:8001/test/JSONServer.jsp";

        //my wifi
        //String URL = "http://192.168.43.89:8001/test/JSONServer.jsp";

        //String URL = "http://192.168.43.143:8080/retnirpWeb5/Join.jsp";

        //mc
        //String URL = "http://192.168.0.40:8001/test/JSONServer.jsp";

        //태민
        String URL = "http://192.168.43.143:8080/retnirpWeb5/ViewAllergicIngredient.jsp";

        DefaultHttpClient client = new DefaultHttpClient();
        try {
			/* 체크할 id와 pwd값 서버로 전송 */
            //HttpPost post = new HttpPost(URL+"?msg="+msg);
            HttpPost post = new HttpPost(URL+"?userId="+userID);

			/* 지연시간 최대 3초 */
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 3000);
            HttpConnectionParams.setSoTimeout(params, 3000);

			/* 데이터 보낸 뒤 서버에서 데이터를 받아오는 과정 */
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(),
                            "utf-8"));

            String line = null;
            String result = "";

            while ((line = bufreader.readLine()) != null) {
                result += line;
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            client.getConnectionManager().shutdown();	// 연결 지연 종료
            return "";
        }

    }

    public String[][] jsonParserList(String pRecvServerPage) {

        Log.i("서버에서 받은 전체 내용 : ", pRecvServerPage);

        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");


            // 받아온 pRecvServerPage를 분석하는 부분
            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if(json != null) {
                    for(int j = 0; j < jsonName.length; j++) {
                        parseredData[i][j] = json.getString(jsonName[j]);
                    }
                }
            }


            // 분해 된 데이터를 확인하기 위한 부분
            for(int i=0; i<parseredData.length; i++){
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][0]);
            }
            allergic = parseredData[0][0];

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
