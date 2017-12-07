package yjy.com.commonproject.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import yjy.com.commonproject.ProcessConnection;

/**
 * Created by asus on 2017/7/23.
 * 守护双进程
 */
public class GuardService extends Service {

    private final int GuardId=0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(GuardId,new Notification());
        //绑定建立连接
        bindService(new Intent(this,MessageService.class),mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub(){};
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(GuardService.this,"建立连接",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            startService(new Intent(GuardService.this,MessageService.class));
            bindService(new Intent(GuardService.this,MessageService.class),mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
