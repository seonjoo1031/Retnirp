package com.example.chaeyoung.retnirp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by Chaeyoung on 2016-11-23.
 *
 * Modified by Seonjoo on 2016-11-30.
 * -> 이미지를 서버로 전달하고 유해성분 있을 경우 수신하는 기능 구현
 */


public class SearchActivity extends Activity {
    ImageView btn_camera = null;
    ImageView btn_album = null;
    ImageView photo = null;
    View searchPage1, searchPage2;
    public static final int CAMERA = 1;
    public static final int ALBUM = 101;
    public int status = 0;

    String userID;
    public String filePath=null;

    // 마쉬멜로우 이상부터는 manifest 뿐만 아니라 runtime에서도 permission 허용해주는 것이 필요하기 때문에 추가
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //permission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        System.out.println("11" + userID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        verifyStoragePermissions(this);

        searchPage1 = findViewById(R.id.searchpage);
        searchPage2 = findViewById(R.id.searchpage2);

        btn_camera = (ImageView)findViewById(R.id.image_button_takepic);
        btn_album = (ImageView)findViewById(R.id.image_button_album);
        photo = (ImageView)findViewById(R.id.photo_ingredient);
    }

    //close button
    public  void close(View v){
        finish();
    }

    public void useCamera(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Uri image;

        if(requestCode == CAMERA){
            if(resultCode==Activity.RESULT_OK){
                try {
                    image = getLastCaptureImageUri();
                    photo.setImageURI(image);
                    filePath = getLastCaptureImageUri().toString();
                    status = CAMERA;
                    searchPage2.setVisibility(View.VISIBLE);
                    searchPage1.setVisibility(View.INVISIBLE);

                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }
        else if(requestCode == ALBUM) {// gallery
            if(resultCode==Activity.RESULT_OK)
            {
                try {
                    status = ALBUM;
                    image = data.getData();
                    System.out.println("album uri : " + image);

                    filePath = getRealImagePath(image);
                    photo.setImageURI(image);


                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            searchPage2.setVisibility(View.VISIBLE);
            searchPage1.setVisibility(View.INVISIBLE);
        }
    }

    // 가장 최근에 찍은 사진의 경로를 받아오는 함수
    // 테스트 기기의 경우 사진을 찍으면 intent를 null로 반환하기 때문에 필요
    private Uri getLastCaptureImageUri(){
        Uri uri =null;
        String[] IMAGE_PROJECTION = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns._ID,
        };
        try {
            Cursor cursorImages = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, null, null,null);
            if (cursorImages != null && cursorImages.moveToLast()) {
                uri = Uri.parse(cursorImages.getString(0)); //경로
                int id = cursorImages.getInt(1); //아이디
                cursorImages.close(); // 커서 사용이 끝나면 꼭 닫아준다.
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return uri;
    }


    // 선택 이미지의 실제 경로를 받아오는 함수
    private String getRealImagePath(Uri uriPath) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uriPath, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String fullPath = cursor.getString(index);  // 파일의 실제 경로
        cursor.close();
        return fullPath;
    }

    public void useGallery(View v){
        //choose image

        System.out.print(v.toString());

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ALBUM);

    }
    public void doAgain(View v){
        System.out.print(v.toString());
        if(status == CAMERA){
            useCamera(btn_camera);
        }
        else if(status == ALBUM){
            useGallery(btn_album);
        }
    }

    //send Image to server
    public void send(View v){

        // send Image to server
        if(filePath != null){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String result = SendByHttp(); // 메시지를 서버에 보냄
            String[][] parsedData = jsonParserList(result); // 받은 메시지를 json 파싱
            // get ingredients
            String ing_harmful = parsedData[0][0]; //쉽표로 구분하여 하나로 붙이기
            String ing_allergic = parsedData[0][1]; //쉼표로 구분하여 하나로 붙이기

            //go to result page
            Intent intent = new Intent();
            intent.setClass(this, ResultActivity.class);
            intent.putExtra("harmful", ing_harmful);
            intent.putExtra("allergic", ing_allergic);
            startActivity(intent);
            finish();
        }



    }

    // 이미지를 서버로 보내는 함수
    private String SendByHttp() {
        //String server = "http://192.168.1.201:8001/test/image.jsp";
        //my wifi
        //String server = "http://192.168.43.89:8001/test/image.jsp";

        //태민
        String server = "http://192.168.43.143:8080/retnirpWeb5/ReceivePhoto1.jsp";

        // 파일을 서버로 보내는 부분
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server);
        post.setHeader("Connection", "Keep-Alive");
        post.setHeader("Accept-Charset", "UTF-8");
        post.setHeader("ENCTYPE", "multipart/form-data");

        System.out.println("1");
        File glee = new File(filePath);
        System.out.println("2");
        FileBody bin = new FileBody(glee);
        System.out.println("3");


        MultipartEntityBuilder meb = MultipartEntityBuilder.create();
        //meb.setCharset(Charset.forName("UTF-8"));
        meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        meb.addTextBody("id", userID, ContentType.create("Multipart/related", "UTF-8"));
        System.out.println(userID);
        meb.addPart("image", bin);
        HttpEntity entity = meb.build();

        post.setEntity(entity);
        System.out.println("4");

        try {
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
            String[] jsonName = {"msg1", "msg2"};
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
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][1]);
            }

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
