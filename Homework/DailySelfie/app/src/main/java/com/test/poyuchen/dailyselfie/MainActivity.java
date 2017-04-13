package com.test.poyuchen.dailyselfie;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private CustomListAdapter adapter;
    private ListView listView;
    private File storageDir;
    private final int finalTimeToTakePhoto = 120000;
    private int timeToTakePhoto = finalTimeToTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // action bar show camara icon
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // list view show the pictures
        adapter = new CustomListAdapter(this, initPhoto());
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // show the single picture that be chosen by user
                // TODO
                //Locate and create the image
                File imgFile = new File(adapter.getItem(position).get("image").toString());

                if (imgFile.exists()) {
                    //Create a bitmap so the ImageView can load it
                    String myBitmap = imgFile.getAbsolutePath();

                    Intent myIntent = new Intent(MainActivity.this, ImageActivity.class);
                    myIntent.putExtra("imagePath", myBitmap);
                    startActivity(myIntent);
                }
            }
        });

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                // do stuff then
                // can call h again after work!
                if (timeToTakePhoto <= 0) {
                    sendNotification();
                    timeToTakePhoto = finalTimeToTakePhoto;
                } else {
                    timeToTakePhoto -= 1000;
                }
                h.postDelayed(this, 1000);
            }
        }, 1000); // 1 second delay (takes millis)
    }

    private void sendNotification() {
        // Sets an ID for the notification
        int mNotificationId = 001;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_camera)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie");
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private List<Map<String, Object>> initPhoto() {
        List<Map<String, Object>> photoList = new ArrayList<>();
        File[] fileList = storageDir.listFiles();
        Arrays.sort(fileList);
        for (File file : fileList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", file.getName().split("\\.")[0]);
            map.put("image", file.getAbsolutePath());
            photoList.add(map);
        }
        return photoList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        dispatchTakePictureIntent();
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bitmap newPhotoData = (Bitmap) data.getExtras().get("data");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File newPhotoFile = new File(storageDir, timeStamp + ".png");
            FileOutputStream outStream;
            try {
                outStream = new FileOutputStream(newPhotoFile);
                newPhotoData.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                /* 100 to keep full quality of the image */
                outStream.flush();
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("name", newPhotoFile.getName().split("\\.")[0]);
            map.put("image", newPhotoFile.getAbsolutePath());
            adapter.add(map);

            timeToTakePhoto = finalTimeToTakePhoto;
        }
    }
}
