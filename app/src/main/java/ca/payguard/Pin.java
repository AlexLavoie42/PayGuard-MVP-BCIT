package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ca.payguard.R;

public class Pin extends AppCompatActivity {

    // The pin-code for the authorized card.
    private ArrayList<Integer> pin;
    // The TextView to show the # symbols in.
    private TextView pin_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        pin = new ArrayList<Integer>();
    }

    /** Updates the pin_code variable with the amount of #'s that is appropriate. */
    private void pinScreenUpdate(){
        pin_code = (TextView) findViewById(R.id.pin_total);
        String pin_pounds = "";
        for(int i = 0; i < pin.size(); i++){
            pin_pounds += "#";
        }
        pin_code.setText(pin_pounds);
    }

    /** Appends number to the end of the current pin. */
    private void pinUpdate(int number){
        pin.add(number);
        pinScreenUpdate();
    }

    //TODO: Refactor name to be more descriptive.
    //TODO: Pass pin data to next intent.

    /**
     * Goes to the next Intent (EmailConfirmation).
     */
    public void onButtonClick(View v){
        Intent myIntent = new Intent(getBaseContext(),   EmailConfirmation.class);
        startActivity(myIntent);
    }

    //Might refactor this into a switch function pending member review.
    public void onButton1(View v){
        pinUpdate(1);
    }

    public void onButton2(View v){
        pinUpdate(2);
    }

    public void onButton3(View v){
        pinUpdate(3);
    }

    public void onButton4(View v){
        pinUpdate(4);
    }

    public void onButton5(View v){
        pinUpdate(5);
    }

    public void onButton6(View v){
        pinUpdate(6);
    }

    public void onButton7(View v){
        pinUpdate(7);
    }

    public void onButton8(View v){
        pinUpdate(8);
    }

    public void onButton9(View v){
        pinUpdate(9);
    }

    public void onButton0(View v){
        pinUpdate(0);
    }

    /**
     * If the pin is empty, go back to previous intent. If pin is not empty, delete last
     * entry.
     */
    public void onButtonBack(View v){
        if(pin.isEmpty()){
            Intent myIntent = new Intent(getBaseContext(),   Payment.class);
            startActivity(myIntent);
        }else{
            pin.remove(pin.size() - 1);
            pinScreenUpdate();
        }
    }

    //TODO: Add tap feature
    private void card_tapped(){};

    //TODO: Add insert/swipe feature
    private void other_pay(){};
}
