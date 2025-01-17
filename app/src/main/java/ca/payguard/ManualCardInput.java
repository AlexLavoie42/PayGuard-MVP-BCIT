package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import ca.payguard.paymentUtil.CanadaPreAuth;
import ca.payguard.paymentUtil.Transaction;

public class ManualCardInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_card_input);
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

    @Override
    public void onBackPressed() {

    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void onNext(View v){
        try{
            Customer customer = (Customer) getIntent().getParcelableExtra("customer");
            String pan = ((EditText)findViewById(R.id.PAN)).getText().toString();
            String exp = ((EditText)findViewById(R.id.exp_year)).getText().toString() + ((EditText)findViewById(R.id.exp_month)).getText().toString();
            int dollars;
            String dollarAmount;
            try{
                dollarAmount = getIntent().getStringExtra("preAuthAmount");
            }catch(Exception e){
                dollars = getIntent().getIntExtra("preAuthAmount", 0);
                dollarAmount = "" + dollars;
            }
            //TODO: Put these into transaction.
            if(TransactionService.isRunning ){
                TransactionService.instance.executeTransaction(customer.getOrderID(), pan, exp, dollarAmount +".00");
            }else{
                Intent intent = new Intent(this, TransactionService.class);
                startService(intent);
                TransactionService.instance.executeTransaction(customer.getOrderID(), pan, exp, dollarAmount + ".00");
            }
////
//            Transaction transaction = new Transaction();
//            CanadaPreAuth preAuth = new CanadaPreAuth();
//            preAuth.setOrderId("PG-type1-00009");
//            preAuth.setPan("4242424242424242"); // Test Visa card
//            preAuth.setExpDate("2103"); // YY/MM
//            transaction.newTransaction(preAuth);
//            try{
//                if(!transaction.executeTransaction("1", "5.00")) throw new Exception(); // DO NOT TOUCH AMOUNT
//                if(!transaction.completeTransaction("1", "4.00")) throw new Exception(); //DO NOT TOUCH AMOUNT

            Intent myIntent = new Intent(getBaseContext(),   EmailConfirmation.class);
            myIntent.putExtra("customer", getIntent().getParcelableExtra("customer"));
            myIntent.putExtra("tableNum", getIntent().getStringExtra("tableNum"));
            startActivity(myIntent);
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }
}
