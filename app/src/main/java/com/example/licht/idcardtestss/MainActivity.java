package com.example.licht.idcardtestss;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.licht.idcardtestss.utils.NotificationUtils;

import java.io.File;

public class MainActivity extends Activity {

    private final String TAG = "Screenshot";

    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + "Pictures"+ File.separator + "Screenshots" + File.separator;
    private CustomFileObserver mFileObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xx);
//      String ss = "one";
//      changeData(ss);
//       Log.i("zyl",PATH);
//        CustomFileObserver customFileObserver = new CustomFileObserver(PATH,FileObserver.ALL_EVENTS);
//        customFileObserver.startWatching();
    }

    private void changeData(String ss) {
        ss = "TTYY";
    }

    public void xxx(View view) {
        NotificationUtils notificationUtils = new NotificationUtils(view.getContext());
        notificationUtils.sendNotification("sssss","eeeeeee",null);
    }

    /**
     * 目录监听器
     */
    private class CustomFileObserver extends FileObserver {

        private String mPath;

        public CustomFileObserver(String path) {
            super(path);
            this.mPath = path;
            Log.i(TAG,path);
        }

        public CustomFileObserver(String path, int mask) {
            super(path, mask);
            this.mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            Log.d(TAG, path + " " + event);
            Log .i("zyl",path);
            // 监听到事件，做一些过滤去重处理操作
        }
    }


}
