package ca.payguard.editMode;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

public class LabelInput extends LinearLayout {
    AppCompatEditText label;
    Button[] btns;

    LinearLayout r1, r3;//layout rows - r2 only has 1 element and doesn't need layout

    public LabelInput(Context c){
        super(c);

        btns = new Button[4];
        for(int i = 0; i < 4; i++) {
            btns[i] = new Button(c);
            btns[i].setText("^");
        }

        label = new AppCompatEditText(c);
        btns[2].setRotation(180);
        btns[3].setRotation(180);

        r1 = new LinearLayout(c);
        r3 = new LinearLayout(c);
        r1.setOrientation(LinearLayout.HORIZONTAL);
        r3.setOrientation(LinearLayout.HORIZONTAL);

        r1.addView(btns[0]);
        r1.addView(btns[1]);
        r3.addView(btns[3]);
        r3.addView(btns[2]);

        label.setOnTouchListener(new OnTouchListener() {
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

        setOrientation(LinearLayout.VERTICAL);
        addView(r1);
        addView(label);
        addView(r3);
    }

    public void setText(String s){
        label.setText(s);
    }

    public void setSingleLine(boolean val){
        label.setSingleLine(val);
    }

    public void enable(){
        for(int i = 0; i < 4; i++)
            btns[i].setEnabled(true);

        label.setEnabled(true);
    }

    public void disable(){
        for(int i = 0; i < 4; i++)
            btns[i].setEnabled(false);

        label.setEnabled(false);
    }
}
