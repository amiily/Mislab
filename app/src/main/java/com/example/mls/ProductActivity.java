package com.example.mls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
public class ProductActivity extends AppCompatActivity {
    EditText edt1;
    EditText edt2;
    EditText edt3;
    int type=2;
    private static final int RESULT_LOAD_IMG =1 ;
    private String urlString ="http://47.107.62.150:8080/admin/add";
    private AsyncTask asyncTask;
    public static StringBuffer s3;
    String TAG=InforActivity.class.getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_infor);
        Button button0 = (Button) findViewById(R.id.btn_0);
        Button button1 = (Button) findViewById(R.id.btn_1);
        Button button2 = (Button) findViewById(R.id.btn_2);
        Button button3=(Button)findViewById(R.id.btn_3);
        Button button4 = (Button) findViewById(R.id.btn_4);
        edt1=(EditText)findViewById(R.id.edt_1);
        edt2=(EditText)findViewById(R.id.edt_2);
        edt3=(EditText)findViewById(R.id.edt_3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findInfor();

            }
        });
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传图片
                initTask();
                asyncTask.execute((Object) null);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tpid();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeInfo();
            }
        });
    }




    public void findInfor(){
        new Thread(postRun).start();
    }
    Runnable postRun=new Runnable() {
        @Override
        public void run() {
            request();
        }
    };
    public void Tpid(){
        new Thread(Tpid).start();
    }
    Runnable Tpid=new Runnable() {
        @Override
        public void run() {
            requestTpid();
        }
    };
    public void ChangeInfo(){
        new Thread(ChangeInfo).start();
    }
    Runnable ChangeInfo=new Runnable() {
        @Override
        public void run() {
            change();
        }
    };
    private void change(){
        try {
            String baseUrl = "http://47.107.62.150:8080/admin/upproIntro";
            URL url = new URL(baseUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(5 * 1000);
            urlConn.setReadTimeout(5 * 1000);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST");
            urlConn.setInstanceFollowRedirects(true);
            urlConn.connect();
            Log.i("Sign_in","开始连接");

            // 发送请求参数
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("proIntro",edt3.getText().toString());
            obj.put("proImgUrl",edt2.getText().toString());
            System.out.println(obj);
            String json=String.valueOf(obj);
            System.out.println(json);
            out.write(json.getBytes());
            out.flush();
            out.close();
            //读取参数
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);


            }
            System.out.println(sb);

            if (urlConn.getResponseCode() == 200) {
                Log.i("Sign_in","Post方式请求成功");
            } else {
                Log.i("Sign_in", "Post方式请求失败");
            }
            reader.close();
            urlConn.disconnect();
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }
    private void request() {
        try {
            String baseUrl = "http://47.107.62.150:8080/admin/All/Pro";
            URL url = new URL(baseUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(5 * 1000);
            urlConn.setReadTimeout(5 * 1000);
            // urlConn.setRequestProperty("Content-Type", "multipart/form-data");
            urlConn.setRequestMethod("POST");
            urlConn.connect();
            Log.i("Sign_in","开始连接");
            //读取参数
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String lines;
            //StringBuilder response=new StringBuilder();
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                // lines = URLDecoder.decode(lines, "utf-8");
                sb.append(lines);
                // response.append(lines);
            }
            //textView1.setText(sb);
            // showResponse(sb.toString());
            s3=sb;
            System.out.println(sb);
            // System.out.println(response.toString());
            Intent intent=new Intent(ProductActivity.this,InMessageActivity.class);
            intent.putExtra("inform", (Serializable) s3);
            startActivity(intent);
            if (urlConn.getResponseCode() == 200) {
                Log.i("Sign_in","Post方式请求成功");
            } else {
                Log.i("Sign_in", "Post方式请求失败");
            }
            reader.close();
            urlConn.disconnect();
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }




  /*  private void showResponse(final String sb){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView  textView1=(TextView)findViewById(R.id.tv_1);
                textView1.setText(sb);
            }
        });
    }*/


    public void loadImage() {
        //这里就写了从相册中选择图片，相机拍照的就略过了
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // 获取游标
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgPath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageView);
                imgView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                Log.i("Infor","看戏"+selectedImage+"   "+filePathColumn+"  "+imgPath);
            } else {
                Toast.makeText(this, "你还没有选择图片",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "出错啦", Toast.LENGTH_LONG).show();
        }
    }

    private void initTask(){
        asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                urlString="http://47.107.62.150:8080/admin/add";
               // String response = UploadImage.uploadFile(new File("imgPath"), urlString);
              //  System.out.println(response);
              //  return response;
                return 0;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                urlString = "http://47.107.62.150:8080/admin/add";
                Toast.makeText(ProductActivity.this, "开始上传", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                // Toast.makeText(InforActivity.this, (o == null ? "" : o.toString()), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void requestTpid() {
        try {
            String baseUrl = "http://47.107.62.150:8080/admin/toupproIntro";
            URL url = new URL(baseUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(5 * 1000);
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置格式为json
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            //配置请求Content-Type
            // 开始连接
            urlConn.connect();
            Log.i("Sign_in","开始连接");

            // 发送请求参数
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("id",edt1.getText().toString());
            System.out.println(obj);
            String json=String.valueOf(obj);
            System.out.println(json);
            out.write(json.getBytes());
            out.flush();
            out.close();
            //读取参数
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String lines;
            //StringBuilder response=new StringBuilder();
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                // lines = URLDecoder.decode(lines, "utf-8");
                sb.append(lines);
                // response.append(lines);
            }
            System.out.println(sb);
            // System.out.println(response.toString());

            if (urlConn.getResponseCode() == 200) {
                Log.i("Sign_in","Post方式请求成功");
            } else {
                Log.i("Sign_in", "Post方式请求失败");
            }
            reader.close();
            urlConn.disconnect();
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }
}
