package com.example.test;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.test.MainActivity.socket;

public class Receive_service extends Service { // jichen
    public Receive_service() {
    }

    private ClipboardManager clipboardManager;
    private Handler mMainHandler;

    @Override
    public IBinder onBind(Intent intent) {
        //return mBinder;
        return null;
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void onCreate()//创建
    {
        super.onCreate();
        Log.d("third", "onCreate executed");
        // 创建前台
        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(Receive_service.this,"桌面已断开连接,请检查桌面程序",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        String channelId = "channel_id_01"; // id
        //仅支持安卓8.0以上系统
        String channelName = "剪切板同步后台服务";// 用户可见服务名
        int importance = NotificationManager.IMPORTANCE_HIGH;// 重要程度
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setDescription("channel description");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.createNotificationChannel(channel);//创建信息通道

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("剪切板后台服务")
                .setContentText("this is a content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //manager.addPrimaryClipChangedListener(onPrimaryClipChangedListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)//服务开始时调用
    {
        Log.d("third", "onStartCommand executed");

                 //利用线程池直接开启一个线程 & 执行该线程
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // 步骤1：创建输入流对象InputStream
                    while(true) {
                            InputStream is;
                            InputStreamReader isr;
                            BufferedReader br;
                            String response;
                            Log.d("third", "hello");
                            is = socket.getInputStream();
                            Log.d("third", "hello1");
                            // 步骤2：创建输入流读取器对象 并传入输入流对象
                            // 该对象作用：获取服务器返回的数据
                            isr = new InputStreamReader(is,"GBK");
                            Log.d("third", "hello2");
                            br = new BufferedReader(isr);
                            Log.d("third", "hello3d");
                            // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据

                            response=br.readLine();
                            if(response=="__is_quited")
                            {
                                Message msg = Message.obtain();
                                msg.what = 0;
                                mMainHandler.sendMessage(msg);
                                return;
                            }

                            Log.d("third", response + "hello 4jgj");
                            // 步骤4:通知主线程,将接收的消息显示到界面
                            // 允许toast
                            setClipboardManager(response);
                            Log.d("third", response + "hello 4j");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            }).start();


        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("third", "onDestroy executed");

    }
    public void setClipboardManager(String data)
    {
        ClipData clipData = ClipData.newPlainText("test",data);
        Log.d("third", "clipstart1");
        clipboardManager.setPrimaryClip(clipData);
        Log.d("third", "clipstart2");
//        Looper.prepare();
        //Toast.makeText(Receive_service.this,"已粘贴:"+data,Toast.LENGTH_SHORT).show();
//        Looper.loop();
    }

}