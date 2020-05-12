package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ManualCardInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_card_input);
    }

    public void onNext(View v){
        try{
            String cvc = findViewById(R.id.CVC).toString();
            String pan = findViewById(R.id.PAN).toString();
            String exp = findViewById(R.id.exp_year).toString() + findViewById(R.id.exp_month).toString();
            //TODO: Put these into transaction.
            Intent myIntent = new Intent(getBaseContext(),   EmailConfirmation.class);
            //TODO: Add extras
            startActivity(myIntent);
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }
}
