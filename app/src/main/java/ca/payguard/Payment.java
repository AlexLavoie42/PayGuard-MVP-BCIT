package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ca.payguard.R;

public class Payment extends AppCompatActivity{

    private Customer newCustomer;
    private String tableNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        tableNum = getIntent().getStringExtra("tableNum");
        newCustomer = new Customer();
    }

    /**
     * Forwards a string $ amount to the TapCard activity. Will pass an int to TapCard called
     * 'preAuthAmount'.
     * @param amount: Must start with $ and must be convertible to an Int.
     */
    protected void forwardAmount(String amount){
        if(!amount.isEmpty()) {
            int dollars = Integer.parseInt(amount.substring(1));
            newCustomer.setPreAuthTotal(dollars);

            Intent myIntent = new Intent(getBaseContext(), ManualCardInput.class);
            myIntent.putExtra("tableNum", tableNum);
            myIntent.putExtra("customer", newCustomer);
            myIntent.putExtra("preAuthAmount", "" + dollars);
            startActivity(myIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter limit",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /** Method to handle entering a custom amount for a pre-auth. */
    public void onCustomAmount(View v){
        //Begin the transaction
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        final PreAuthAmountFragment fragment = PreAuthAmountFragment.newInstance();
        ft.replace(R.id.popupLayout, fragment);
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
        blur.setBlurRadius(6);
        blur.setAlpha(0.8f);
        blur.setOverlayColor(1);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft.detach(fragment);
            }
        });
    }

    /** Method for handling dollarAmount1 button's push. Will use button's text in forwardAmount. */
    public void onAmount1(View v){
        Button button = (Button)findViewById(R.id.dollarAmount1);
        String amount = button.getText().toString();
        forwardAmount(amount);
    }

    /** Method for handling dollarAmount2 button's push. Will use button's text in forwardAmount. */
    public void onAmount2(View v){
        Button button = (Button)findViewById(R.id.dollarAmount2);
        String amount = button.getText().toString();
        forwardAmount(amount);
    }

    /** Method for handling dollarAmount3 button's push. Will use button's text in forwardAmount. */
    public void onAmount3(View v){
        Button button = (Button)findViewById(R.id.dollarAmount3);
        String amount = button.getText().toString();
        forwardAmount(amount);
    }
}
