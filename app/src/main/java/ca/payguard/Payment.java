package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.payguard.R;

public class Payment extends AppCompatActivity{

    private Customer newCustomer;
    private Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        table = getIntent().getParcelableExtra("table");
        newCustomer = new Customer(table.getAllCustomers().size(), table);
    }

    /**
     * Forwards a string $ amount to the TapCard activity. Will pass an int to TapCard called
     * 'preAuthAmount'.
     * @param amount: Must start with $ and must be convertible to an Int.
     */
    protected void forwardAmount(String amount){
        int dollars = Integer.parseInt(amount.substring(1));
        newCustomer.setPreAuthTotal(dollars);
        table.addCustomer(newCustomer);

        Intent myIntent = new Intent(getBaseContext(),   TapCard.class);
        myIntent.putExtra("table", table);
        myIntent.putExtra("preAuthAmount", dollars);
        startActivity(myIntent);
    }

    /** Method to handle entering a custom amount for a pre-auth. */
    public void onCustomAmount(View v){
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.popupLayout, PreAuthAmountFragment.newInstance());
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
        blur.setBlurRadius(6);
        blur.setAlpha(0.8f);
        blur.setOverlayColor(1);
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
