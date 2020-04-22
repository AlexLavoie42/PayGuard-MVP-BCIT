package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.payguard.R;

public class TapCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_card);
    }

    //TODO: Add tap feature
    private void card_tapped(){};

    //TODO: Add insert/swipe feature
    private void other_pay(){};
}
