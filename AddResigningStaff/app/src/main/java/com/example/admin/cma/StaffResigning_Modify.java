package com.example.admin.cma;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffResigning_Modify extends AppCompatActivity {


    Toolbar toolbar;
    StaffResigning staff;
    String PreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_resigning__modify);
        Intent intent=getIntent();
        staff=(StaffResigning) intent.getSerializableExtra("chuandi");


        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar3);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        final EditText editText1=(EditText) findViewById(R.id.edit_text1);
        editText1.setText(staff.getName());
        final EditText editText2=(EditText)findViewById(R.id.edit_text2);
        editText2.setText(staff.getTitle());
        final EditText editText3=(EditText)findViewById(R.id.edit_text3);
        editText3.setText(staff.getDegree());
        //editText3.setText(bundle.getString("position"));
        final EditText editText4=(EditText)findViewById(R.id.edit_text4);
        editText4.setText(staff.getMajor());
        //editText4.setText(bundle.getString("id"));
        final EditText editText5=(EditText)findViewById(R.id.edit_text5);
        editText5.setText(staff.getDepartment());
       // editText5.setText(bundle.getString("location"));
        final EditText editText6=(EditText)findViewById(R.id.edit_text6);
        editText6.setText(staff.getPosition());

        final EditText editText7=(EditText)findViewById(R.id.edit_text7);
        editText7.setText(staff.getResignDate());


         PreName=staff.getName();

        //Bitmap bitmap = BitmapFactory.decodeFile(staff.getFileImage());

        //得到application对象
        //ApplicationInfo appInfo = getApplicationInfo();
        //得到该图片的id(name 是该图片的名字，"mipmap" 是该图片存放的目录，appInfo.packageName是应用程序的包)
        //int resID = getResources().getIdentifier(staff.getFileImage(), "mipmap-xxxhdpi", appInfo.packageName);
        //Bitmap bitmap=BitmapFactory.decodeResource(getResources(), resID);

        //对于从网站下载的图片 TODO
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的","here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url("http://172.25.185.143/get_staff.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的","here is json22");
                    InputStream responseData = response.body().byteStream();
                    Bitmap bitmap=BitmapFactory.decodeStream(responseData);
                    showResponse(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();*/



        //在xml的时候，光标是不可见的，但是当用户编辑的时候，光标要可见。
        ShowCursor(editText1);
        ShowCursor(editText2);
        ShowCursor(editText3);
        ShowCursor(editText4);
        ShowCursor(editText5);
        ShowCursor(editText6);
        ShowCursor(editText7);

        Button button=(Button)findViewById(R.id.delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StaffResigning_Modify.this);
                dialog.setTitle("确定删除此人的档案吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //将信息提交到数据库
                    }
                });
                dialog.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //从数据库删除这个人的档案 TODO
                       new Thread(new Runnable() {
                            @Override
                            public void run() {
                                postDelete(staff);
                            }
                        }).start();


                        finish();
                    }
                });
                dialog.show();
            }
        });

        //对 保存 按钮监听
        toolbar.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StaffResigning_Modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                //需要保存数据到数据库 TODO

               final String s1=editText1.getText().toString();
               final String s2=editText2.getText().toString();
               final  String s3=editText3.getText().toString();
               final String s4=editText4.getText().toString();
               final String s5=editText5.getText().toString();
                final String s6=editText6.getText().toString();
                final String s7=editText7.getText().toString();
                if((s1==null||s1.equals("")) ||(s2==null||s2.equals("")) || (s3==null||s3.equals("")) || (s4==null||s4.equals("")) ||(s5==null||s5.equals(""))||(s6==null||s6.equals(""))||(s7==null||s7.equals("")))
                    Toast.makeText(StaffResigning_Modify.this,"请全部填满",Toast.LENGTH_SHORT).show();
                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            postSave(PreName,s1,s2,s3,s4,s5,s6,s7);
                        }
                    }).start();
                }

                finish();
            }
        });

    }


    //设置光标可见
    private void ShowCursor(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
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


    private void postDelete(StaffResigning temp){
        Gson gson=new Gson();
        String jsonStr=gson.toJson(temp);
        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=RequestBody.create(JSON,jsonStr);

        //或者传一个name
        RequestBody requestBody1=new FormBody.Builder().add("name",temp.getName()).build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffResigning/delete")//url的地址
                .post(requestBody)
                .build();
        /*try {
            Response response=okHttpClient.newCall(request).execute();
            Log.i("StaffFile_Modify"," Here here11111");
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                //   Log.i("StaffFile_Add"," Here here");
                // Log.i("StaffFile_Add",response.body().string());
                handler.sendEmptyMessage(0);
            }
        } catch (IOException e) {
            Log.d("JSONstr:","Delete unsuccessfully!");
            e.printStackTrace();
        }*/
        //同步上传
        //异步post
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffResigning_Modify.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffResigning_Modify.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void postSave(String preName,String s1,String s2,String s3,String s4,String s5,String s6,String s7){
        Gson gson=new Gson();
        StaffResigning temp=new StaffResigning(0,s1,s2,s3,s4,s5,s6,s7);
        String jsonStr=gson.toJson(temp);
        String result="{\"name\":\""+preName+"\",\"staffResigning\":"+jsonStr+"}";
        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=RequestBody.create(JSON,result);

        //或者传一个name
        RequestBody requestBody1=new FormBody.Builder().add("name",temp.getName()).build();

        Log.d("生成的json:",result);
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffResigning/modify")//url的地址
                .post(requestBody)
                .build();
        /*try {
            Response response=okHttpClient.newCall(request).execute();
            Log.i("StaffFile_Modify"," Here here11111");
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                //   Log.i("StaffFile_Add"," Here here");
                // Log.i("StaffFile_Add",response.body().string());
                handler.sendEmptyMessage(0);
            }
        } catch (IOException e) {
            Log.d("JSONstr:","Delete unsuccessfully!");
            e.printStackTrace();
        }*/
        //同步上传
        //异步post
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffResigning_Modify.this, "保存失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffResigning_Modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }



    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Toast.makeText(StaffResigning_Modify.this, "Delete successfully!", Toast.LENGTH_SHORT).show();

        }
    };

}
