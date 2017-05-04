package com.antonioramos.classtracker1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * TitleActivity class will allow user to select action from available options
 * Created by antonio on 4/8/2017.
 */

public class TitleActivity extends Activity implements View.OnClickListener{
    private int idButton [] ={R.id.map_button,R.id.saveLocation_button,R.id.lookupClass_button,
            R.id.lookupLocations_button};

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.title_activity);

        for (int id:idButton) {
            Button b = (Button)findViewById(id);
           // b.performHapticFeedback(1);
            b.setOnClickListener(this);
        }
    }
    @Override
    public void onClick(View view) {
        //adds vibration feedback to buttons
        if(view != null){
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);
        }

        if(view.getId() == R.id.lookupClass_button){

            Intent intent = new Intent(getApplicationContext(), LocationListActivity.class);
            intent.putExtra(CurrentLocationActivity.LIST_KEY, "APSU");
            startActivity(intent);
        }
        else if(view.getId() == R.id.saveLocation_button){
            Intent intent = new Intent(getApplicationContext(), CurrentLocationActivity.class);
            startActivity(intent);
        }

        // This will change once I create the intents
        //just demo
        else if(view.getId() == R.id.map_button){
          //  view.performHapticFeedback(1);

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.lookupLocations_button) {
            Intent intent = new Intent(getApplicationContext(), LocationListActivity.class);
            intent.putExtra(CurrentLocationActivity.LIST_KEY, "Saved");
            startActivity(intent);
        }
        else {
            Log.i("onClick", "Title Activity **************");
        }

    }
}
