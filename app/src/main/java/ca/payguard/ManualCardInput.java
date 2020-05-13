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
                TransactionService.instance.executeTransaction(customer.getOrderID(), pan, exp, dollarAmount);
            }else{
                Intent intent = new Intent(this, TransactionService.class);
                startService(intent);
                TransactionService.instance.executeTransaction(customer.getOrderID(), pan, exp, dollarAmount);
            }

            Intent myIntent = new Intent(getBaseContext(),   EmailConfirmation.class);
            myIntent.putExtra("customer", getIntent().getParcelableExtra("customer"));
            myIntent.putExtra("tableNum", getIntent().getStringExtra("tableNum"));
            startActivity(myIntent);
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }
}
