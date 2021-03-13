package com.example.mls;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignInActivity extends AppCompatActivity {
   String TAG= SignInActivity.class.getCanonicalName();
   private EditText name;
   private EditText password;
   private EditText email;
   private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        name=(EditText)findViewById(R.id.et2_data_uname);
        password=(EditText)findViewById(R.id.et2_data_upass);
        email=(EditText)findViewById(R.id.et2_data_umail);
        send=(Button)findViewById(R.id.btn2_login);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            loginPOST();

            }
        });
    }

    public void loginPOST(){
        new Thread(postRun).start();
    }
    Runnable postRun=new Runnable() {
        @Override
        public void run() {
            requestPost();
        }
    };
    private void requestPost() {
        try {
            String baseUrl = "http://47.107.62.150:8080/reg";
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
            obj.put("stuId",Integer.parseInt(name.getText().toString()));
            obj.put("password",password.getText().toString());
            obj.put("email",email.getText().toString());

            System.out.println(obj);
          //  String json = java.net.URLEncoder.encode(obj.toString(), "utf-8");
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