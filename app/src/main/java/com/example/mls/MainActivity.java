package com.example.mls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getCanonicalName();
    public static int target;
    private EditText name;
    private EditText password;
    final String REMEMBER_PWD_PREF = "rememberPwd";
    final String ACCOUNT_PREF = "name";
    final String PASSWORD_PREF = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //从 SharedPreferences 中获取【是否记住密码】参数

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        name = (EditText) findViewById(R.id.et_data_uname);
        password = (EditText) findViewById(R.id.et_data_upass);
        Button button = (Button) findViewById(R.id.sign);//注册按钮
       // target=loginPost();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = preference.getBoolean(REMEMBER_PWD_PREF, false);
        final CheckBox rememberPwd = (CheckBox) findViewById(R.id.remember_pwd);

        if (isRemember) {
            name.setText(preference.getString(ACCOUNT_PREF, ""));
            password.setText(preference.getString(PASSWORD_PREF, ""));
            rememberPwd.setChecked(true);
        }

        Button btnLogin = (Button) findViewById(R.id.login);//登录按钮
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                SharedPreferences.Editor editor = preference.edit();
                if (rememberPwd.isChecked()) {//记住账号与密码
                    editor.putBoolean(REMEMBER_PWD_PREF, true);
                    editor.putString(ACCOUNT_PREF, name.getText().toString());
                    editor.putString(PASSWORD_PREF, password.getText().toString());
                } else {//清空数据
                    editor.clear();
                }
                editor.apply();
            }
        });
    }

    public void login() {
        new Thread(postRun).start();
    }

    Runnable postRun = () -> loginPost();//开启线程

    private void loginPost() {
        try {
            String baseUrl = "http://47.107.62.150:8080/login";
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

            // 发送请求参数
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("stuId", Integer.parseInt(name.getText().toString()));
            obj.put("password", password.getText().toString());

            //System.out.println(obj);

            String json = String.valueOf(obj);
            System.out.println("发出的数据是：" + json);
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

            String s1 = new String(sb);
            System.out.println("这是s1" + s1);
            parseJSONWithJSONObject(s1);
            System.out.println("s1后的"+target);
           // int i=parseJSONWithJSONObject(s1);

            Log.i("MainActivity", "读取结果");

            if (urlConn.getResponseCode() == 200) {
                Log.i("Sign_in", "Post方式请求成功");
            } else {
                Log.i("Sign_in", "Post方式请求失败");
            }
            reader.close();
            urlConn.disconnect();
            //return i;
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
      //  return 0;
    }
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        };
    };
    public void parseJSONWithJSONObject(String jsonData) {

        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            String msg = jsonObject.getString("msg");
            int age = jsonObject.optInt("stuId");
            Log.i("MainAcvitity", "stuId is " + age);
            Log.i("MainAcvitity", "msg is " + msg);
            System.out.println("msg的值"+msg);
             if(msg.equals("success"))
             {target = -1;}
                 Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                 if(target!=-1)

                 {

                /*     Message m = new Message();
                     m.obj = "密码输入有误或账号未注册";
                     handler.sendMessage(m); */
                     Toast.makeText(MainActivity.this,"密码输入有误或账号未注册!",Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     startActivity(intent);
                     Toast.makeText(MainActivity.this,"登陆成功!",Toast.LENGTH_SHORT).show();

                    }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }





    }



