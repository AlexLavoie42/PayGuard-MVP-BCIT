package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.payguard.R;

public class TapCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_card);
    }

    public void onButtonClick(View v){
        Intent myIntent = new Intent(getBaseContext(),   Pin.class);
        startActivity(myIntent);
    }

    //TODO: Add tap feature
    private void card_tapped(){};

    //TODO: Add insert/swipe feature
    private void other_pay(){};
}
