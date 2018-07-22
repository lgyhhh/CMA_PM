package com.example.cma.ui.period_check;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.utils.ViewUtil;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PeriodCheckPlan_Add extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_check_plan__add);
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewUtil.ShowCursor((EditText)findViewById(R.id.edit_text1));
        ViewUtil.ShowCursor((EditText)findViewById(R.id.edit_text2));
        ViewUtil.ShowCursor((EditText)findViewById(R.id.edit_text4));




        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.top6);
        final TextView editText=(TextView) findViewById(R.id.select_time);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(PeriodCheckPlan_Add.this,new TimeSelector.ResultHandler() {
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

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(PeriodCheckPlan_Add.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });



        Button button=(Button)findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                EditText editText1=(EditText)findViewById(R.id.edit_text1);
                final String s1=editText1.getText().toString();
                EditText editText2=(EditText) findViewById(R.id.edit_text2);
                final String s2=editText2.getText().toString();
                EditText editText4=(EditText)findViewById(R.id.edit_text4);
                final String s4=editText4.getText().toString();
                TextView time=(TextView)findViewById(R.id.select_time);
                final String s5=time.getText().toString();

                if((s1==null||s1.equals("")) || (s2==null||s2.equals(""))||(s4==null||s4.equals("")) ||(s5==null||s5.equals(""))  )
                    isfull=false;
                if(isfull==true){

                    AlertDialog.Builder dialog=new AlertDialog.Builder(PeriodCheckPlan_Add.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(s1,s2,s4,s5);
                                }
                            }).start();

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(PeriodCheckPlan_Add.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    Toast.makeText(PeriodCheckPlan_Add.this,"请全部填满",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void postJson(String s1,String s2,String s4,String s5){

        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("object",s1)
                .add("content",s4)
                .add("checkDate",s5)
                .add("personInCharge",s2)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/IntermediateChecksPlan/addOne")//url的地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeriodCheckPlan_Add.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("androixx.cn", result);
                Log.d("androixx.cn", "成功！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeriodCheckPlan_Add.this, "上传成功！", Toast.LENGTH_SHORT).show();
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


