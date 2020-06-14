package com.example.test;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
// 针对Android 10.0 一下系统可以监听剪切板变化，一键传送到桌面端
public class MyService extends Service { // jichen
    public MyService()
    {}
    private ClipboardManager manager;
    private DownloadBinder mBinder = new DownloadBinder();
    class DownloadBinder extends Binder
    {
        public void startDownload(){
            Log.d("MyService","startDownload executed");
        }
        public int getProgress()
        {
            Log.d("MyService","getProgress executed");
            return 0;
        }
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void onCreate()//创建
    {
        super.onCreate();
        Log.d("MyService","onCreate executed");
        // 创建前台
        Intent intent = new Intent (this, SettingsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);

        String channelId="channel_id_01"; // id
       //仅支持安卓8.0以上系统
        String channelName="剪切板同步后台服务";// 用户可见服务名
        int importance= NotificationManager.IMPORTANCE_HIGH;// 重要程度
        NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
        channel.setDescription("channel description");
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(notificationManager!= null)
            notificationManager.createNotificationChannel(channel);//创建信息通道

        Notification notification = new NotificationCompat.Builder(this,channelId)
                .setContentTitle("This a content title")
                .setContentText("this is a content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);
        manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        manager.addPrimaryClipChangedListener(onPrimaryClipChangedListener);
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)//服务开始时调用
    {
        Log.d("MyService","onStartCommand executed");

        return super.onStartCommand(intent,flags,startId);

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(manager != null)
        {
            manager.removePrimaryClipChangedListener(onPrimaryClipChangedListener);
        }//注销监听事件,防止内存泄漏
        Log.d("MyService","onDestroy executed");

    }


    private ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener
            = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                Log.d( "MyService","here");
                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {

                    CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();

                    if (addedText != null) {
                        Log.d( "MyService","yes"+addedText);
                        //TextView textView =(TextView) findViewById(R.id.text_1);
                        //textView.setText();
                        Toast.makeText(MyService.this,addedText,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };



}
