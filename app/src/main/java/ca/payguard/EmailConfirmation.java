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

import ca.payguard.R;

public class EmailConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);

        ((TextView)findViewById(R.id.ConfirmationMessage)).setText(getResources().getString(
                R.string.confirmation_message,
                getIntent().getIntExtra("preAuthAmount", 0)));
    }

    @Override
    public void onBackPressed() {

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

    public void onButtonClick(View v){
        Customer c = getIntent().getParcelableExtra("customer");
        if(((EditText)findViewById(R.id.phone_num)).getText().toString().matches(
                "^\\D?(\\d{3})\\D?\\D?(\\d{3})\\D?(\\d{4})$"))
            c.setPhoneNum(Integer.parseInt(
                    ((EditText)findViewById(R.id.phone_num)).getText().toString()));
        Intent myIntent = new Intent(getBaseContext(),   MainActivity.class);
        myIntent.putExtra("customer", c);
        myIntent.putExtra("tableNum", getIntent().getStringExtra("tableNum"));
        startActivity(myIntent);
    }
}
