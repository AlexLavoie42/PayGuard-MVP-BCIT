package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * Forwards a string $ amount to the TapCard activity. Will pass an int to TapCard called
     * 'preAuthAmount'.
     * @param amount: Must start with $ and must be convertible to an Int.
     */
    protected void forwardAmount(String amount){
        if(!amount.isEmpty()) {
            float dollars = Float.parseFloat(amount.substring(1));
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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        final PreAuthAmountFragment fragment = PreAuthAmountFragment.newInstance();
        ft.replace(R.id.popupLayout, fragment);
        //Complete changes
        ft.commit();
        final com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
        blur.setVisibility(View.VISIBLE);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(fragment);
                ft.commit();
                blur.setVisibility(View.GONE);
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
