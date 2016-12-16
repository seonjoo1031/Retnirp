package com.example.chaeyoung.retnirp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
 * -> 서버와 통신하여 id, pw 일치하는지 확인하는 기능 구현
 */

public class SignInActivity extends Activity {
    Toast toast;
    private EditText edit_id, edit_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        edit_id = (EditText) findViewById(R.id.edittext_userid);
        edit_pw = (EditText) findViewById(R.id.edittext_password);
    }
    public void signupOnClick(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    public void mainOnClick(View v){

        switch (v.getId()) {
            case R.id.image_button_signin : //if "sign in" button is clicked
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String result = SendByHttp(); // 메시지를 서버에 보냄
                String[][] parsedData = jsonParserList(result); // 받은 메시지를 json 파싱


                // if id and password are correct
                if(parsedData[0][0].equals("LoginYes"))
                {
                    Toast.makeText(this, "Sign in Success", Toast.LENGTH_SHORT).show();

                    //send id to Next page
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("userID", edit_id.getText().toString());
                    System.out.println("sign in : " +edit_id.getText().toString());

                    //start next page
                    startActivity(intent);
                }

                //else : give toast
                else{
                    toast.makeText(this, "Please check ID and Password again", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.image_button_customer : // if "as customer" button is clicked
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("userID", "");
                startActivity(intent);
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
        String URL = "http://192.168.43.143:8080/retnirpWeb5/Login.jsp";

        DefaultHttpClient client = new DefaultHttpClient();
        try {
			/* 체크할 id와 pwd값 서버로 전송 */
            //HttpPost post = new HttpPost(URL+"?msg="+msg);
            HttpPost post = new HttpPost(URL+"?userId="+edit_id.getText().toString()+"&userPw="+edit_pw.getText().toString());

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
