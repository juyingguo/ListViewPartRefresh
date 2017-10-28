package net.mobctrl.listviewdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import net.mobctrl.listviewdemo.IMyAidlInterface;

// 服务端
public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    IMyAidlInterface.Stub iBinder = new IMyAidlInterface.Stub() {
        // 我们在aidl文件中定义的通信规则
        @Override
        public String getMessage(String str) throws RemoteException {
            return str + "&我来自MyService";
        }

    };

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
}