package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    public void onButtonClick(View v){
        Intent myIntent = new Intent(getBaseContext(),   MainActivity.class);
        myIntent.putExtra("customer", getIntent().getParcelableExtra("customer"));
        myIntent.putExtra("tableNum", getIntent().getStringExtra("tableNum"));
        startActivity(myIntent);
    }
}
