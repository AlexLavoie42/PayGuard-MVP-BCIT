package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ca.payguard.R;

public class Pin extends AppCompatActivity {

    private ArrayList<Integer> pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        pin = new ArrayList<Integer>();
    }

    public void onButtonClick(View v){
        Intent myIntent = new Intent(getBaseContext(),   EmailConfirmation.class);
        startActivity(myIntent);
    }

    public void onButton1(View v){
        pin.add(1);
    }

    public void onButton2(View v){
        pin.add(2);
    }

    public void onButton3(View v){
        pin.add(3);
    }

    public void onButton4(View v){
        pin.add(4);
    }

    public void onButton5(View v){
        pin.add(5);
    }

    public void onButton6(View v){
        pin.add(6);
    }

    public void onButton7(View v){
        pin.add(7);
    }

    public void onButton8(View v){
        pin.add(8);
    }

    public void onButton9(View v){
        pin.add(9);
    }

    public void onButton0(View v){
        pin.add(0);
    }

    public void onButtonBack(View v){
        if(pin.isEmpty()){
            Intent myIntent = new Intent(getBaseContext(),   Payment.class);
            startActivity(myIntent);
        }else{
            pin.remove(pin.size() - 1);
        }
    }

    //TODO: Add tap feature
    private void card_tapped(){};

    //TODO: Add insert/swipe feature
    private void other_pay(){};
}
