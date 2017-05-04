package com.antonioramos.classtracker1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Class will receive destination data and start navigation to requested location
 * using google map app.
 * When user is done with google map app, user can press the back button and will
 * see three different image of requested location to ensure user is at the correct location.
 * Created by Antonio Ramos on 4/18/2017.
 */
public class Navigation extends AppCompatActivity {

    private int nextPicture =1;
    private int currentPicture;
    private int unavailableImage = 5;
    private boolean noPhoto =false;
    private double latitude;
    private double longitude;
    String name;

    int indoorPics[] = {R.drawable.apsu1,R.drawable.apsu3,R.drawable.apsu4,R.drawable.maynard1,
            R.drawable.maynard2,R.drawable.maynard3,R.drawable.tech1,R.drawable.tech2,
            R.drawable.tech3,R.drawable.sun1,R.drawable.sundquist_2, R.drawable.sundquist_3,
            R.drawable.book1,R.drawable.book2,R.drawable.book3, R.drawable.nophoto};

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.navigation_layout);
        ImageView iv = (ImageView)findViewById(R.id.class_imageView);

        if(saveInstanceState == null) {
            //retrieve location data - by Antonio Ramos
            Intent intent = getIntent();
            String name = intent.getStringExtra(CurrentLocationActivity.NAME_KEY);
            longitude = intent.getDoubleExtra(CurrentLocationActivity.LONGITUDE_KEY, 0);
            latitude = intent.getDoubleExtra(CurrentLocationActivity.LATITUDE_KEY, 0);
            int pictureSet = intent.getIntExtra("position", 0);
            boolean checkPhoto = intent.getBooleanExtra("checkPhoto",false);

            //if user just wants to see pictures don't call map intent
            if(!checkPhoto) {
                startGoogleMaps();
            }
            setUpPictures(pictureSet);
        }
        else{
            nextPicture = saveInstanceState.getInt("nextPicture");
            currentPicture = saveInstanceState.getInt("currentPicture");
            noPhoto = saveInstanceState.getBoolean("noPhoto");
            iv.setImageResource(indoorPics[currentPicture]);
        }
    }

    /**
     * Save data when image is rotated.
     * -by Antonio Ramos
     *
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("nextPicture",nextPicture );
        outState.putInt("currentPicture", currentPicture);
        outState.putBoolean("noPhoto", noPhoto);
    }

    /**
     * Set up destination pictures
     * -By Antonio Ramos
     */
    private void setUpPictures(int pictureSet ){

        ImageView iv = (ImageView)findViewById(R.id.class_imageView);
        //if no photo is available for location set pictureSet to unavailableImage
        if(pictureSet >= unavailableImage){
            currentPicture = unavailableImage *3;
            noPhoto = true;
        }
        else {
            //set-up picture group
            currentPicture = pictureSet * 3;
        }
        iv.setImageResource(indoorPics[currentPicture]);
    }

    /**
     * Start google map APP
     * - by Antonio Ramos
     */
    private void startGoogleMaps(){
        //pass location data and start google map app.
        //"&mode=w" will start navigation directions in walk mode
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+ longitude+"&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    /**
     * Allows user to cycle through set of pictures of selected destination.
     * User can touch any part of the window to see next image.
     * - by Antonio Ramos
     */
    @Override
    public boolean onTouchEvent( MotionEvent motionEvent) {
        ImageView iv = (ImageView)findViewById(R.id.class_imageView);

        if( motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            //adds vibration feedback to buttons
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);

            //if else statement allows user to cycle through images as many times as they want
            //if image unavailable display toast message
            if (noPhoto) {
                Toast.makeText(this, "No Image Available", Toast.LENGTH_SHORT).show();
            } else if (nextPicture % 3 != 0) {
                currentPicture++;
                iv.setImageResource(indoorPics[currentPicture]);
                nextPicture++;
            } else {
                currentPicture -= 2;
                nextPicture -= 2;
                iv.setImageResource(indoorPics[currentPicture]);
            }
        }
        return true;
    }
}
