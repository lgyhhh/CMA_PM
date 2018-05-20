package com.example.admin.cma;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StaffFile_Main extends AppCompatActivity {

    static public List<StaffFile> dangAns=new ArrayList<StaffFile>();
    ;
    private StaffFileAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stafffile_main);
        initView();
        initDangan();

        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(StaffFile_Main.this,android.R.layout.simple_list_item_1,data);
       // ListView listView=(ListView)findViewById(R.id.list_view);
       // listView.setAdapter(adapter);

      //  StaffFileAdapter adapter=new StaffFileAdapter(StaffFile_Main.this,R.layout.listview_item,dangAns);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar2);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //设置搜索文本监听
        listView.setTextFilterEnabled(true);
        //当点击搜索按钮时触发
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           //当点击搜索按钮触发该方法进行回调
            @Override
            public boolean onQueryTextSubmit(String s) {
                ListAdapter listAdapter=listView.getAdapter();
                if(listAdapter instanceof Filterable){
                    Filter filter=((Filterable)listAdapter).getFilter();
                    if(s==null||s.length()==0){
                        filter.filter(null);
                    }else{
                        filter.filter(s);
                    }
                }
                return false;
            }

            //当搜索内容改变时触发该方法进行回调
            @Override
            public boolean onQueryTextChange(String s) {
                ListAdapter adapter=listView.getAdapter();
                if(adapter instanceof Filterable){
                    Filter filter=((Filterable)adapter).getFilter();
                    if(s==null||s.length()==0){
                        filter.filter(null);
                    }else{
                        filter.filter(s);
                    }
                }
                return true;
            }
        });
      //  adapter=new StaffFileAdapter(StaffFile_Main.this,R.layout.listview_item,dangAns);
        //listView.setAdapter(adapter);
        showResponse();//对于从数据库获取的数据重新开UI线程否则出错。



        //选择listview中的一个元素
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(StaffFile_Main.this,StaffFile_Modify.class);
                StaffFile d=(StaffFile) listView.getItemAtPosition(i);
                intent.putExtra("chuandi",d);
                startActivity(intent);

            }
        });
        //添加按钮
        Button button=(Button)findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StaffFile_Main.this,StaffFile_Add.class);
                startActivity(intent);
            }
        });

    }

    //这里是获取json数据
    private void sendRuquestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的","here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url("http://119.23.38.100:8080/cma/StaffFile/getAll")
                           // .url("http://192.168.43.96/get_staff.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的","here is json22");
                    String responseData = response.body().string();
                    Log.d("点击获取的","here is json44");
                    Log.d("获得的数据:",responseData);
                    parseJSONWithGSON(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        System.out.print(jsonData);
        Gson gson = new Gson();
       dangAns = gson.fromJson(jsonData, new TypeToken<List<StaffFile>>() {}.getType());
       Log.d("点击获取的","here is json33");
       //将获取到的jsonList对应到 dangAns里，dangAns初始化完成
/*        StaffFile_Main temp=dangAns.get(0);
        Log.d("name",temp.getName());
        Log.d("department",temp.getDepartment());
        Log.d("position",temp.getPosition());*/
//        Log.d("id",temp.getFileId());
  //      Log.d("location",temp.getLocation());
    //    Log.d("fileImage",temp.getFileImage());
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter=new StaffFileAdapter(StaffFile_Main.this,R.layout.listview_item,dangAns);
                listView.setAdapter(adapter);
            }
        });
    }


    private  void initDangan(){
         dangAns.clear();
         Log.d("点击获取的","init");
        sendRuquestWithOkHttp();


        //应该从数据库里读取，先自己设置
        //(String name,String department,String position,String id,String location){
        //Resources res=this.getResources();
        //dangAns.clear();

        /*
        Staff d1=new Staff(0,"李贵银","学习部","部长","001","档案室", BitmapFactory.decodeResource(res,R.mipmap.pic1));
        dangAns.add(d1);
        Staff d2=new Staff(0,"杨雨桦","恋爱部","部员","002","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic2));
        dangAns.add(d2);
        PersonnelFile d3=new PersonnelFile(0,"龚茹沁","少女部","部员","003","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic3));
        dangAns.add(d3);
        PersonnelFile d4=new PersonnelFile(0,"钱子珩","小说部","会长","004","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic4));
        dangAns.add(d4);
        PersonnelFile d5=new PersonnelFile(0,"仇盛","臭美部","部员","005","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic5));
        dangAns.add(d5);
        PersonnelFile d6=new PersonnelFile(0,"曹阳","二货部","部员","006","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic6));
        dangAns.add(d6);
        PersonnelFile d7=new PersonnelFile(0,"蒋雨霖","吃货部","会长","007","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic7));
        dangAns.add(d7);
        PersonnelFile d8=new PersonnelFile(0,"马泽坤","杨楠部","会长","008","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic8));
        dangAns.add(d8);
        PersonnelFile d9=new PersonnelFile(0,"王国新","好学部","部员","009","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic9));
        dangAns.add(d9);
        PersonnelFile d10=new PersonnelFile(0,"杨修天","社会部","会长","010","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic10));
        dangAns.add(d10);
        PersonnelFile d11=new PersonnelFile(0,"丁运鹏","可爱部","部员","011","档案室",BitmapFactory.decodeResource(res,R.mipmap.pic11));
        dangAns.add(d11);*/
    }

    private void initView(){
        listView=(ListView)findViewById(R.id.list_view);
        searchView=(SearchView)findViewById(R.id.searchview);
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
