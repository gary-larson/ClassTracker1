package com.antonioramos.classtracker1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by antonio on 4/8/2017.
 */

public class TitleActivity extends AppCompatActivity implements View.OnClickListener{
    private int idButton [] ={R.id.map_button,R.id.saveLocation_button,R.id.lookUpClass_button,
            R.id.lookupLoctions_button};

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.title_activity);

        for (int id:idButton) {
            Button b = (Button)findViewById(id);
            b.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.lookUpClass_button){
            Intent intent = new Intent(getApplicationContext(), LocationListActivity.class);
            startActivity(intent);


        }
        else if(view.getId() == R.id.saveLocation_button){
            Intent intent = new Intent(getApplicationContext(), CurrentLocationActivity.class);
            startActivity(intent);
        }

        // This will change once I create the intents
        //just demo
        else if(view.getId() == R.id.map_button){

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.lookupLoctions_button) {
            Intent intent = new Intent(getApplicationContext(), LocationListActivity.class);
            startActivity(intent);
        }

    }
}
