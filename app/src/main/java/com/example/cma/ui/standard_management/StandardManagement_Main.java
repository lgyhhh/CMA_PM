package com.example.cma.ui.standard_management;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.adapter.standard_management.StandardManagementAdapter;
import com.example.cma.model.standard_management.StandardManagement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StandardManagement_Main extends AppCompatActivity {


    public List<StandardManagement> dangAns = new ArrayList<StandardManagement>();
    private StandardManagementAdapter adapter;
    private ListView listView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standard_management__main);
        toolbar = (Toolbar) findViewById(R.id.mToolbar2);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view);


        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //设置搜索文本监听
        listView.setTextFilterEnabled(true);

        showResponse();//对于从数据库获取的数据重新开UI线程否则出错。

        //长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(StandardManagement_Main.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DatePickerDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StandardManagement temp=(StandardManagement) listView.getItemAtPosition(position);
                        postDelete(temp.getFileId());
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                return true ;
            }
        });


        //点击listview中的一个元素
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StandardManagement d=(StandardManagement) listView.getItemAtPosition(i);
                Intent intent=new Intent(StandardManagement_Main.this,StandardManagement_Info.class);
                intent.putExtra("chuandi",d);
                startActivity(intent);
            }
        });

        //添加按钮
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StandardManagement_Main.this,StandardManagement_Add.class);
                startActivity(intent);
            }
        });

    }

    public void postDelete(long fileId){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("fileId",Long.toString(fileId)).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StandardManagement/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StandardManagement_Main.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StandardManagement_Main.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        sendRuquestWithOkHttp();
                    }
                });

            }
        });

    }

    //这里是获取json数据
    private void sendRuquestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的", "here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url("http://119.23.38.100:8080/cma/StandardManagement/getAll")
                            .build();
                    Response response = client.newCall(request).execute();
                    // Log.d("点击获取的", "here is json22");
                    String responseData = response.body().string();
                    //Log.d("点击获取的", "here is json44");
                    //Log.d("获得的数据:", responseData);
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        System.out.print(jsonData);
        String[] strings=jsonData.split("\\[");
        String[] strings1=strings[1].split("\\]");
        Log.d("sss:",strings1[0]);
        String string="["+strings1[0]+"]";

        Gson gson = new Gson();


        dangAns = gson.fromJson(string, new TypeToken<List<StandardManagement>>(){}.getType());
        Log.d("点击获取的", "here is json33");
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter = new StandardManagementAdapter(StandardManagement_Main.this, R.layout.internal_document_item, dangAns);
                listView.setAdapter(adapter);
            }
        });
    }


    private void initDangan() {
        dangAns.clear();
        Log.d("点击获取的", "init");
        sendRuquestWithOkHttp();
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


    @Override
    protected void onResume() {
        super.onResume();
        initDangan();
    }


}

