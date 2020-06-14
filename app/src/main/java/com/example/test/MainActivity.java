package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Handler mMainHandler;
    public static Socket socket;
    private ExecutorService mThreadPool;//线程池
    private ClipboardManager clipboardManager;
    private  boolean isConnected=false;
    private  boolean islogin = false;
    private  String ip_address ;
    private   String port;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    String response;
    OutputStream os;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.settings_item:
                Log.d("third","test1");
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.comment_item:
                Log.d("third","test1");
                Intent intent_comment = new Intent(MainActivity.this, CommentActivity.class);
                Log.d("third","test2");
                startActivity(intent_comment);

                break;
            default:
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        ip_address= pref.getString("ip_address","");
        port= pref.getString("port","");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("剪切板助手");

        mThreadPool= Executors.newCachedThreadPool();//初始化线程池
        initView();
        clipboardManager=(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        Log.d("third","hello");
        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        receive_message.setText("连接失败");
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this,"正在连接桌面,请稍候",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this,"请检查桌面是否在线",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        editText.setText("");
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "请输入需要同步的内容", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
//        receive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 利用线程池直接开启一个线程 & 执行该线程
//                mThreadPool.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            // 步骤1：创建输入流对象InputStream
//                            while(true) {
//                                if (socket.isConnected()) {
//                                    Log.d("third", "hello");
//                                    is = socket.getInputStream();
//                                    Log.d("third", "hello1");
//                                    // 步骤2：创建输入流读取器对象 并传入输入流对象
//                                    // 该对象作用：获取服务器返回的数据
//                                    isr = new InputStreamReader(is);
//                                    Log.d("third", "hello2");
//                                    br = new BufferedReader(isr);
//                                    Log.d("third", "hello3d");
//                                    // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
//                                    response = br.readLine();// 末尾加上换行符,否则阻塞
//                                    if (response != null) {
//                                        Log.d("third", response + "hello 4jgj");
//                                        // 步骤4:通知主线程,将接收的消息显示到界面
//
//                                        Looper.prepare();// 允许toast
//                                        setClipboardManager(response);
//                                        Log.d("third", response + "hello 4j");
//                                        Message msg = Message.obtain();
//                                        msg.what = 0;
//                                        mMainHandler.sendMessage(msg);
//                                        Looper.loop();
//                                    }
//                                }
//
//                            }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//            }
//        });
        final Intent Intent_1 = new Intent(this,Receive_service.class);//停止服务
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ip_address=="" ||port=="")
                {
                    Toast.makeText(MainActivity.this,"请在右上角设置界面,设置完毕后再启动本服务",Toast.LENGTH_SHORT).show();
                    return ;
                }
                // 利用线程池直接开启一个线程 & 执行该线程
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("third","正在链接ma  ma ");
                            // 创建Socket对象 & 指定服务端的IP 及 端口号
//                            Looper.prepare();
//                            Toast.makeText(ThirdActivity.this,"正在连接,请等待",Toast.LENGTH_SHORT).show();
//
//                            Looper.loop();
//                            Message msg = Message.obtain();
//                            msg.what = 1;
//                            mMainHandler.sendMessage(msg);
                            socket = new Socket(ip_address,  Integer.parseInt(port));


                            //socket.setSoTimeout(500);// 总时间限制
                            // 判断客户端和服务器是否连接成功


                            //System.out.println(socket.isConnected());
                            //Log.d("third","正在链接" +  socket.isConnected());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("third","链接超时");
                            //Looper.prepare();
                            islogin=true;
                            //Toast.makeText(ThirdActivity.this,"连接超时 ,请检测桌面端是否登录",Toast.LENGTH_SHORT).show();
//                            Message msg = Message.obtain();
//                            msg.what = 2;
//                            mMainHandler.sendMessage(msg);Message msg = Message.obtain();
////                            msg.what = 2;
////                            mMainHandler.sendMessage(msg);
                            //Looper.loop();
                            return;

                        }
                        isConnected=true;
                        startForegroundService(Intent_1);
                    }
                });
            }
        });
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(ip_address=="" ||port=="")
                {
                    Toast.makeText(MainActivity.this,"请在右上角设置界面,设置完毕后再启动本服务",Toast.LENGTH_SHORT).show();
                    return ;
                }
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.d("aa","1");
                            os= socket.getOutputStream();// 发送流
                            String message2send=editText.getText().toString();
                            if(message2send=="")
                            {
                                Message msg = Message.obtain();
                                msg.what = 4;
                                mMainHandler.sendMessage(msg);
                                return;
                            }
                            os.write((editText.getText().toString()).getBytes("GBK"));
                            Log.d("third","发送信息:"+editText.getText().toString());
                            os.flush();
                            Message msg = Message.obtain();
                           msg.what = 3;
                              mMainHandler.sendMessage(msg);
                            Log.d("third","fasong");
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
                    if(!isConnected)
                    {
                        Toast.makeText(MainActivity.this,"连接未建立,此操作无效",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    os= socket.getOutputStream();
                    String temp="_is_quited_";
                    os.write(temp.getBytes("GBK"));
                    os.flush();
                    os.close();
                    // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
                    br.close();
                    // 关闭整个Socket连接
                    socket.close();
                    Toast.makeText(MainActivity.this,"您已断开连接",Toast.LENGTH_SHORT).show();
                    // 判断客户端和服务器是否已经断开连接
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isConnected=false;
            }
        });
    }
    private Button connect, disconnect, send;
    private TextView receive,receive_message;
    private EditText editText;
    public void initView()
    {
        connect=(Button) findViewById(R.id.connect);
        disconnect =(Button) findViewById(R.id.disconnect);
        send =(Button) findViewById(R.id.send);
        //receive =(Button) findViewById(R.id.receive);
        editText =(EditText) findViewById(R.id.edittext);
        //receive_message = (TextView) findViewById(R.id.receive_message);
    }

}


//在安卓10.0 以下的设备,可以加入剪切板监听,实现更加无缝的体验,自动监听剪切事件,只要复制,就发送到服务端
