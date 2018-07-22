package com.example.cma.ui.staff_management;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffTraining;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StaffTraining_Add extends AppCompatActivity {

    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_training_add);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final TextView editText=(TextView) findViewById(R.id.edit_text2);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(StaffTraining_Add.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });


        Button button=(Button)findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                EditText editText1=(EditText)findViewById(R.id.edit_text1);
                final String s1=editText1.getText().toString();


                final String s2=editText.getText().toString();

                EditText editText3=(EditText)findViewById(R.id.edit_text3);
                final String s3=editText3.getText().toString();

                EditText editText4=(EditText)findViewById(R.id.edit_text4);
                final String s4=editText4.getText().toString();

                EditText editText5=(EditText)findViewById(R.id.edit_text5);
                final String s5=editText5.getText().toString();

                EditText editText6=(EditText)findViewById(R.id.edit_text5);
                final String s6=editText6.getText().toString();
                if((s1==null||s1.equals("")) ||(s2==null||s2.equals("")) || (s3==null||s3.equals("")) || (s4==null||s4.equals("")) ||(s5==null||s5.equals("")))
                    isfull=false;
                if(isfull==true){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(StaffTraining_Add.this);
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
                                    String aa="jjj";
                                    postJson(s1,s2,s3,s4,s5,s6);
                                }
                            }).start();


                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(StaffTraining_Add.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    if(isfull==false)
                        Toast.makeText(StaffTraining_Add.this,"请全部填满",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(StaffTraining_Add.this,"请上传扫描件",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void postJson(String s1,String s2,String s3,String s4,String s5,String s6){
        // String name,String department,String position,String id,String location
        Log.i("StaffFile_Add"," Here here11111");


        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody1= new FormBody.Builder().add("program",s1)
                .add("trainingDate",s2)
                .add("place",s3)
                .add("presenter",s4)
                .add("content",s5)
                .add("note",s6)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffTraining/addOne")//url的地址
                .post(requestBody1)
                .build();
        Log.d("StaffFile_Add"," Here here2222");

        //同步上传
        //异步上传
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffTraining_Add.this, "上传失败！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffTraining_Add.this, "考核结果添加成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
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
