package com.example.moon.jobschedulerdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int JOB_SCHEDULER_ID = 123;
    public static final int JOB_IMAGE_SEARCH = 124;
    public static final int JOB_FOR_CONTACTS = 125;
    public static DB_Helper db_helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoStartPermissionHelper.getInstance().getAutoStartPermission(getApplicationContext());
        //addAutoStartup();
        db_helper = new DB_Helper(getApplicationContext());
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
          || ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
            {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS},101);
        }else{
            scheduleJobForImageSearch(getApplicationContext(),JOB_IMAGE_SEARCH);
        }

        //scheduleJob(getApplicationContext(),JOB_SCHEDULER_ID);
        finish();

    }


    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode == PackageManager.PERMISSION_GRANTED){
            scheduleJobForImageSearch(getApplicationContext(),JOB_IMAGE_SEARCH);
        }
    }

    public static void scheduleJobForImageSearch(Context context, int jobId){
        ComponentName componentName = new ComponentName(context,ScanForMediaFiles.class);
        JobInfo jobInfo = new JobInfo.Builder(jobId,componentName)
                .setPeriodic(15*60*1000)
                .setPersisted(true)
                .build();
        JobScheduler jobScheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
        int schedule_res = jobScheduler.schedule(jobInfo);
        if(schedule_res==JobScheduler.RESULT_SUCCESS){
            Log.i(TAG, "scheduleJobForImageSearch: Success");
        }else{
            Log.i(TAG, "scheduleJobForImageSearch: Failed");
        }
    }

    public static void scheduleJob(Context applicationContext, int jobId) {
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

    public static void scheduleForContacts(Context context,int jobID){
        ComponentName componentName = new ComponentName(context,ScanForContacts.class);
        JobInfo jobInfo = new JobInfo.Builder(jobID,componentName)
                .setPeriodic(15*60*1000)
                .build();
        JobScheduler jobScheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
        int schedule_result = jobScheduler.schedule(jobInfo);
        if(schedule_result==JobScheduler.RESULT_SUCCESS){
            Log.i(TAG, "schedule for contacts Successfully: ");
        }else{
            Log.i(TAG, "schedule for contacts Failed: ");
        }
    }
}
