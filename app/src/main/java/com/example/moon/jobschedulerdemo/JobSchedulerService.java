package com.example.moon.jobschedulerdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.database.Cursor;
import android.util.Log;

public class JobSchedulerService extends JobService {
    private static final String TAG = "JobSchedulerService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob: ");
        doInBackground(params);
        return true;
    }

    private void doInBackground(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor allContacts = MainActivity.db_helper.getAllContacts();
                if(allContacts!=null && allContacts.getCount()>0){
                    allContacts.moveToFirst();
                    Log.i(TAG, "run: "+allContacts.getCount());
                    int count = 1;
                    while (allContacts.moveToNext()){
                        // need to check if it is uploaded or not
                        Log.i(TAG, "Uploading . . . .:" + count +allContacts.getString(0) + " --> " + allContacts.getString(1));
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        count++;
                    }
                    allContacts.close();
                }



                Cursor allImages = MainActivity.db_helper.getAllImages();
                if(allImages!=null && allImages.getCount()>0){
                    allImages.moveToFirst();
                    while (allImages.moveToNext()){
                        // need to check if it is uploaded or not
                        Log.i(TAG, "Uploading . . . .: "+allImages.getString(0));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    allImages.close();
                }

                jobFinished(params,false);
                MainActivity.scheduleJobForImageSearch(getApplicationContext(),MainActivity.JOB_IMAGE_SEARCH);
            }
        }).start();
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob: ");
        return true;
    }
}
