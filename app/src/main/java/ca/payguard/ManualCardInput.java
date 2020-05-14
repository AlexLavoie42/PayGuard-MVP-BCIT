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
import android.widget.TextView;

import org.w3c.dom.Text;

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

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void onNext(View v){
        try{
            Customer customer = (Customer) getIntent().getParcelableExtra("customer");
            String pan = findViewById(R.id.PAN).toString();
            String exp = findViewById(R.id.exp_year).toString() + findViewById(R.id.exp_month).toString();
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
                TransactionService.instance.executeTransaction(customer.getOrderID(), pan, exp, dollarAmount);
            }else{
                Intent intent = new Intent(this, TransactionService.class);
                startService(intent);
                TransactionService.instance.executeTransaction(customer.getOrderID(), pan, exp, dollarAmount);
            }

            Intent myIntent = new Intent(getBaseContext(),   EmailConfirmation.class);
            myIntent.putExtra("customer", getIntent().getParcelableExtra("customer"));
            myIntent.putExtra("tableNum", getIntent().getStringExtra("tableNum"));
            myIntent.putExtra("preAuthAmount", getIntent().getIntExtra("preAuthAmount", 0));
            startActivity(myIntent);
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }
}
