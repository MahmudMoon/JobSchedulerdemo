package com.example.moon.jobschedulerdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class ScanForContacts extends JobService {
    private static final String TAG = "ScanForContacts";

    @Override
    public boolean onStartJob(JobParameters params) {
        getContactList();
        return true;
    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if(!MainActivity.db_helper.checkExistanceOFNumber(phoneNo)) {
                            MainActivity.db_helper.insertIntoContactTable(name, phoneNo, 0);
                        }else{
                            Log.i(TAG, "NUMBER EXISTS : " + name + "\n" + phoneNo);
                        }

                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        MainActivity.scheduleJob(getApplicationContext(),MainActivity.JOB_SCHEDULER_ID);

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
