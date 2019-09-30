package com.example.moon.jobschedulerdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

public class ScanForMediaFiles extends JobService {
    private static final String TAG = "ScanForMediaFiles";
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob: Image Search");
        doInBackground(params);
        return true;
    }

    private void doInBackground(final JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] STAR = { "*" };
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        , STAR, null, null, null);

                if (cursor != null)
                {
                    if (cursor.moveToFirst())
                    {
                        do
                        {
                            String path = cursor.getString(cursor
                                    .getColumnIndex(MediaStore.Images.Media.DATA));
                            if(!MainActivity.db_helper.checkExistance(path)){
                                boolean b = MainActivity.db_helper.insertIntoDB(path, 0);
                                if(b)
                                {
                                    Log.i(TAG, "doInBackground: "+"ADDED "+ path);
                                }
                            }else{
                                Log.i(TAG, "doInBackground: "+ "Already ADDED " + path);
                            }

                        }while (cursor.moveToNext());

                    }
                    cursor.close();
                }
                jobFinished(params,false);
                MainActivity.scheduleForContacts(getApplicationContext(),MainActivity.JOB_FOR_CONTACTS);
            }
        }).start();


    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob: Image Search");
        return true;
    }
}
