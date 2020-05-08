package ca.payguard.editMode;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class LabelInput extends androidx.appcompat.widget.AppCompatEditText {
    public LabelInput(Context c){
        super(c);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
