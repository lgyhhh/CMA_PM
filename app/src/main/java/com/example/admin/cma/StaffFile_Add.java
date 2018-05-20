package com.example.admin.cma;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

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

public class StaffFile_Add extends AppCompatActivity {

    Toolbar toolbar;
    private Uri imageUri;
    private ImageView imageView;
    private File outputImage;
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private Boolean flag=false;//判断拍照

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stafffile_add);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageView=(ImageView)findViewById(R.id.imageview4_1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StaffFile_Add.this);
                dialog.setTitle("请选择上传方式");
                dialog.setCancelable(false);
                dialog.setPositiveButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //将信息提交到数据库 TODO
                        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent,CHOOSE_PHOTO);
                        flag=true;
                    }
                });
                dialog.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        outputImage = new File(getExternalStorageDirectory(),"output_image.jpg");

                        //启动相机程序
                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(Build.VERSION.SDK_INT>=24){
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            imageUri= FileProvider.getUriForFile(StaffFile_Add.this,"com.example.admin.cma.fileprovider",outputImage);
                        }else {
                            imageUri= Uri.fromFile(outputImage);
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PHOTO);
                        flag=true;
                    }
                });
                dialog.show();
            }
        });

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
                if((s1==null||s1.equals("")) ||(s2==null||s2.equals("")) || (s3==null||s3.equals("")) || (s4==null||s4.equals("")) ||(s5==null||s5.equals("")))
                    isfull=false;
                if(isfull==true && flag==true){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(StaffFile_Add.this);
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
                                    postJson(0,s1,s2,s3,s4,s5,aa);
                                }
                            }).start();

                            //传递图片
                           /* new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postImage(outputImage);
                                }
                            }).start();*/

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(StaffFile_Add.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    if(isfull==false)
                       Toast.makeText(StaffFile_Add.this,"请全部填满",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(StaffFile_Add.this,"请上传扫描件",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void postJson(int key,String s1,String s2,String s3,String s4,String s5,String fileImage){
       // String name,String department,String position,String id,String location
        Log.i("StaffFile_Add"," Here here11111");
        StaffFile temp=new StaffFile(key,s1,s2,s3,s4,s5,fileImage);
        Gson gson=new Gson();
        String jsonStr=gson.toJson(temp);
       // String str="{\"id\":\"++\",\"name\":\""+input_name+"\",\"age\":"+input_age+"\"}";
        String result="{\"name\":\""+s1+"\",\"department\":\""+s2+"\",\"position\":\""+s3+"\",\"fileID\":\""+s4+"\",\"location\":\""+s5+"\",\"fileImage\":\""+fileImage+"\"}";
        Log.d("result",result);

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=RequestBody.create(JSON,result);
        RequestBody requestBody1= new FormBody.Builder().add("name",s1).add("department",s2).add("position",s3).add("fileId",s4).add("location",s5).add("fileImage",fileImage).build();
        Log.d("传输的数据",result);
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffFile/addStaff")//url的地址
                .post(requestBody1)
                .build();
        Log.d("StaffFile_Add"," Here here2222");
        /*try {
            Response response=okHttpClient.newCall(request).execute();
            Log.i("StaffFile_Add"," Here here333");
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
               Log.i("StaffFile_Add"," Here here");
                Log.i("StaffFile_Add",response.body().string());
                handler.sendEmptyMessage(0);
            }else {
                Log.i("StaffFile_Add"," Here here444");
            }
        } catch (IOException e) {
            Log.d("JSONstr:","Submit unsuccessfully!");
            e.printStackTrace();
        }*/

        //同步上传
        //异步上传
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffFile_Add.this, "上传失败！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffFile_Add.this, "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void postImage(File file){
        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
      //  RequestBody requestBody=RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody=RequestBody.create(MediaType.parse("image/png"),file);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/addPicture")//url的地址
                .post(requestBody)
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            Log.i("StaffFile_Add"," Here here11111");
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                //   Log.i("StaffFile_Add"," Here here");
                // Log.i("StaffFile_Add",response.body().string());
                handler.sendEmptyMessage(0);
            }
        } catch (IOException e) {
            Log.d("JSONPicture:","Submit unsuccessfully!");
            e.printStackTrace();
        }
    }


    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Toast.makeText(StaffFile_Add.this, "Submit successfully!", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case TAKE_PHOTO://take_photo
                if(resultCode==RESULT_OK)
                {
                    //try {
                        //将拍摄的照片显示出来
                        //Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getPath());
                        imageView.setImageBitmap(bitmap);
                    //}catch (FileNotFoundException e) {
                        //e.printStackTrace();
                    //}
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    selectPicture(data);
                }
            default:
                break;
        }
    }

    private void selectPicture(Intent intent){
        String path=null;
        Uri uri=intent.getData();
        //通过uri和selection(倒数第三个
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        if(path!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            outputImage=new File(path);//add new
            imageView.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
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
