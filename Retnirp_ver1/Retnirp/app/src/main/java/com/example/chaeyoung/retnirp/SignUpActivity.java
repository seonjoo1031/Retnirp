package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Chaeyoung on 2016-11-22.
 *
 * Modified by Seonjoo on 2016-11-30.
 * -> 그런데 응답 결과가 duplicated, noDuplicated, JoinCompletion
 */

public class SignUpActivity extends Activity {
    String userID = null;
    String userPW = null;
    TextView msg;
    boolean checkid = false;
    EditText edit_id, edit_pw, edit_confirm;
    Toast toast;
    String server;

    //id 체크인지 가입인지 구별
    boolean join_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        edit_id = (EditText)findViewById(R.id.edittext_userid);
        edit_pw = (EditText) findViewById(R.id.edittext_password);
    }

    public void checkID(View v){
        if(edit_id.equals(null)){ // edit text is empty
            //give toast : please enter id

            return;
        }

        //connect with server
        //이때 서버는 중복체크 jsp
        server = "http://192.168.43.143:8080/retnirpWeb5/Join2.jsp";
        String result = SendByHttp(server);


        //check id duplication
        String[][] parsedData = jsonParserList(result); // 받은 메시지를 json 파싱

        if(parsedData[0][0].equals("CanJoin"))
        {
            //success
            checkid = true;
            //give toast : your id is  available
            toast.makeText(this, "This ID is available", Toast.LENGTH_SHORT).show();
        }

        else{
            //fail
            checkid = false;
            // give toast : please enter another id
            toast.makeText(this, "This ID already exists", Toast.LENGTH_SHORT).show();

        }

        // please give an id

    }

    // Submit button
    public void confirmOnclick(View v){
        //check id

        if(!checkid){//did not check id
            //give toast :  please check id first
            toast.makeText(this, "Please check ID first", Toast.LENGTH_SHORT);
            toast.show();

            return ;
        }

        //chceck password comfirmation
        if( !(edit_pw.equals(edit_confirm.toString()))){// do not match password and confirmation
            //give toast : please check both passwords
            msg = (TextView) findViewById(R.id.message);
            msg.setVisibility(View.VISIBLE);
            return ;
        }

        // if all steps are completed
        //connect with server
        //이때 서버는 등록 jsp
        //server = "http://192.168.1.201:8001/test/JSONServer.jsp";
        //mc
        //server = "http://192.168.0.40:8001/test/JSONServer.jsp";

        //태민
        server = "http://192.168.43.143:8080/retnirpWeb5/Join.jsp";

        join_mode = true;
        String result = SendByHttp(server);
        String[][] parsedData = jsonParserList(result); // 받은 메시지를 json 파싱

        //register id and password on DB

        if(parsedData[0][0].equals("JoinCompletion"))
        {
            //give toast : your id and password is registered
            toast.makeText(this, "SUCESSFUL", Toast.LENGTH_SHORT).show();

            // go to sign in page
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }

        //else : give toast
        else{
            toast.makeText(this, "Join fail", Toast.LENGTH_SHORT).show();
        }

    }

    private String SendByHttp(String server) {

        // 서버를 설정해주세요!!!
        String URL = server;

        //String URL = "http://192.168.43.143:8080/retnirpWeb5/Join.jsp";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DefaultHttpClient client = new DefaultHttpClient();
        try {
			/* 체크할 id와 pwd값 서버로 전송 */
            //HttpPost post = new HttpPost(URL+"?msg="+msg);
            HttpPost post;
            if(join_mode){
                post = new HttpPost(URL+"?userId="+edit_id.getText().toString()+"&userPw="+edit_pw.getText().toString());
            }

            else{
                post = new HttpPost(URL+"?userId="+edit_id.getText().toString());
            }

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

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
