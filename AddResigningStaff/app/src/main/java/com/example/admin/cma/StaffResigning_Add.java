package com.example.admin.cma;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Environment.getExternalStorageDirectory;

public class StaffResigning_Add extends AppCompatActivity {

    Toolbar toolbar;

    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_resigning__add);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }




        Button button=(Button)findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                EditText editText1=(EditText)findViewById(R.id.edit_text1);
                final String s1=editText1.getText().toString();

                EditText editText2=(EditText)findViewById(R.id.edit_text2);
                final String s2=editText2.getText().toString();

                EditText editText3=(EditText)findViewById(R.id.edit_text3);
                final String s3=editText3.getText().toString();

                EditText editText4=(EditText)findViewById(R.id.edit_text4);
                final String s4=editText4.getText().toString();

                EditText editText5=(EditText)findViewById(R.id.edit_text5);
                final String s5=editText5.getText().toString();

                EditText editText6=(EditText)findViewById(R.id.edit_text6);
                final String s6=editText6.getText().toString();

                EditText editText7=(EditText)findViewById(R.id.edit_text7);
                final String s7=editText7.getText().toString();
                if((s1==null||s1.equals("")) ||(s2==null||s2.equals("")) || (s3==null||s3.equals("")) || (s4==null||s4.equals("")) ||(s5==null||s5.equals(""))||(s6==null||s6.equals(""))||(s7==null||s7.equals("")))
                    isfull=false;
                if(isfull==true ){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(StaffResigning_Add.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           //将信息提交到数据库 TODO
                            //传递json数据和图片流
                           new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(0,s1,s2,s3,s4,s5,s6,s7);
                                }
                            }).start();


                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(StaffResigning_Add.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    if(isfull==false)
                       Toast.makeText(StaffResigning_Add.this,"请全部填满",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(StaffResigning_Add.this,"请上传扫描件",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void postJson(int key,String s1,String s2,String s3,String s4,String s5,String s6,String s7){
       // String name,String department,String position,String id,String location
        Log.i("StaffFile_Add"," Here here11111");
        StaffResigning temp=new StaffResigning(key,s1,s2,s3,s4,s5,s6,s7);
        Gson gson=new Gson();
        String jsonStr=gson.toJson(temp);
       // String str="{\"id\":\"++\",\"name\":\""+input_name+"\",\"age\":"+input_age+"\"}";
        String result="{\"name\":\""+s1+"\",\"title\":\""+s2+"\",\"degree\":\""+s3+"\",\"major\":\""+s4+"\",\"department\":\""+s5+"\",\"position\":\"" + s6 + "\",\"resignDate\":\"" + s7 + "\"}";
        Log.d("result",result);

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=RequestBody.create(JSON,result);
        Log.d("传输的数据",result);
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffResigning/addStaff")//url的地址
                .post(requestBody)
                .build();
        Log.d("StaffResigning_Add"," Here here2222");


        //同步上传
        //异步上传
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffResigning_Add.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("androixx.cn", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffResigning_Add.this, "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }



    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Toast.makeText(StaffResigning_Add.this, "Submit successfully!", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){

            default:
                break;
        }
    }


    //监听返回按钮的点击事件，比如可以返回上级Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
