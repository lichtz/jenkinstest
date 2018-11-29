package com.example.licht.idcardtestss.jobscheduler;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licht.idcardtestss.BuildConfig;
import com.example.licht.idcardtestss.R;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

/**
 * @author licht
 * @version v 0.1 2018/11/29 Thu 14:39
 */
public class JobSchedulerActivity extends AppCompatActivity {
    public static final int MSG_START = 0;
    public static final int MSG_STOP = 1;
    private int mJobId = 0;
    private ComponentName componentName;
    public static final String MESSAGE_INTENT_KEY= BuildConfig.APPLICATION_ID+".MESSAGE_INTENT_KEY";
    private InCommingMessageHandler messageHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobscheduler_layout);
        componentName = new ComponentName(this, TTJobService.class);
        messageHandler = new InCommingMessageHandler(this);


    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, TTJobService.class);
        Messenger messenger = new Messenger(messageHandler);
        intent.putExtra(MESSAGE_INTENT_KEY,messenger);
        startService(intent);


    }

    public void finishJob(View view) {
        JobScheduler jobSchedule = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            jobSchedule = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobSchedule != null) {
                List<JobInfo> allPendingJobs = jobSchedule.getAllPendingJobs();
                if (allPendingJobs.size() > 0) {
                    int id = allPendingJobs.get(0).getId();
                    jobSchedule.cancel(id);
                    Toast.makeText(this, String.format(Locale.CHINA,"取消 %d 任务", id), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, String.format(Locale.CHINA,"没有任务了"), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void cancel(View view) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        Toast.makeText(this, "取消全部任务", Toast.LENGTH_SHORT).show();

    }

    public void add(View view) {
        JobInfo.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder = new JobInfo.Builder(mJobId++, componentName);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            // 可以设置省电 充电闲置
//            builder.setRequiresDeviceIdle();
//            builder.setRequiresCharging();
            PersistableBundle persistableBundle = new PersistableBundle();
            persistableBundle.putString("KAKA","KAKAK~~~");
            builder.setExtras(persistableBundle);
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());

        }


    }


    private static class InCommingMessageHandler extends Handler {

        private final WeakReference<JobSchedulerActivity> jobSchedulerActivityWeakReference;

        public InCommingMessageHandler(JobSchedulerActivity jobSchedulerActivity) {
            jobSchedulerActivityWeakReference = new WeakReference<>(jobSchedulerActivity);

        }


        @Override
        public void handleMessage(Message msg) {
            JobSchedulerActivity jobSchedulerActivity = jobSchedulerActivityWeakReference.get();
            if (jobSchedulerActivity == null) {
                return;
            }
            TextView startTv = jobSchedulerActivity.findViewById(R.id.job_start);
            TextView stopEnd = jobSchedulerActivity.findViewById(R.id.job_stop);

            switch (msg.what) {
                case MSG_START:
                    String obj = (String) msg.obj;
                    startTv.setText(obj);
                    startTv.setBackgroundColor(Color.GREEN);
                    stopEnd.setBackgroundColor(Color.GRAY);

                    break;
                case MSG_STOP:
                    startTv.setBackgroundColor(Color.GRAY);
                    stopEnd.setBackgroundColor(Color.RED);
                    break;
                default:
                    break;
            }

        }
    }
}
