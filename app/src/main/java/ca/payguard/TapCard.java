package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;

import ca.payguard.R;

public class TapCard extends AppCompatActivity {

    private Table test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_card);
    }

    public void onButtonClick(View v){
        Intent myIntent = new Intent(getBaseContext(), EmailConfirmation.class);
        myIntent.putExtra("customer", getIntent().getParcelableExtra("customer"));
        myIntent.putExtra("tableNum", getIntent().getStringExtra("tableNum"));
        myIntent.putExtra("preAuthAmount", getIntent().getIntExtra("preAuthAmount", 0));
        startActivity(myIntent);
    }

    //TODO: Add tap feature
    private void card_tapped(){};

    //TODO: Add insert/swipe feature
    private void other_pay(){};
}
