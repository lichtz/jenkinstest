package com.example.licht.idcardtestss.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import static com.example.licht.idcardtestss.jobscheduler.JobSchedulerActivity.MESSAGE_INTENT_KEY;

/**
 * @author licht
 * @version v 0.1 2018/11/29 Thu 15:36
 */
public class TTJobService extends JobService
{

    private Messenger mJobActivityMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("zyl:onCreate ",TTJobService.class.getSimpleName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("zyl:onStartCommand",TTJobService.class.getSimpleName());
        mJobActivityMessenger = intent.getParcelableExtra(MESSAGE_INTENT_KEY);
        return START_NOT_STICKY;

    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        final String cc = "开始任务啦：：：";

        Log.i("zyl:onStartJob","onStartJob");
        String kaka = (String) params.getExtras().get("KAKA");
        Log.i("zyl:onStart",kaka);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();

                message.what =0;
                message.obj= cc+ SystemClock.currentThreadTimeMillis();

                try {
                    if (mJobActivityMessenger != null) {
                        mJobActivityMessenger.send(message);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            jobFinished(params,false);
                        }
                    }).start();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        },1000);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("zyl:onStopJob","onStopJob");
        String kaka = (String) params.getExtras().get("KAKA");
        Log.i("zyl:stop",kaka+"STOP");
        Message obtain = Message.obtain();
        obtain.what=1;
        obtain.obj ="stop";

        try {
            if (mJobActivityMessenger != null) {
                mJobActivityMessenger.send(obtain);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        stopSelf();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("zyl","destroy");
    }
}
