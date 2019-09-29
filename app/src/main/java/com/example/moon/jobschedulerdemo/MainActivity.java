package com.example.moon.jobschedulerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int JOB_SCHEDULER_ID = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scheduleJob(getApplicationContext(),JOB_SCHEDULER_ID);
        finish();

    }

    public static void scheduleJob(Context applicationContext,int jobId) {
        ComponentName componentName = new ComponentName(applicationContext,JobSchedulerService.class);
        JobInfo jobInfo = new JobInfo.Builder(jobId,componentName)
                .setPeriodic(15*60*1000)
                .build();
        JobScheduler jobScheduler = (JobScheduler)applicationContext.getSystemService(JOB_SCHEDULER_SERVICE);
        int schedule_result = jobScheduler.schedule(jobInfo);
        if(schedule_result==JobScheduler.RESULT_SUCCESS){
            Log.i(TAG, "Job Schedule Successfully: ");
        }else{
            Log.i(TAG, "Job Schedule Failed: ");
        }
    }
}
