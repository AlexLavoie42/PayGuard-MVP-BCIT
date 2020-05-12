package ca.payguard.editMode;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

import ca.payguard.R;

public class LabelInput extends LinearLayout {
    AppCompatEditText label;
    Button[] btns;

    LinearLayout r1, r3;//layout rows - r2 only has 1 element and doesn't need layout

    EditMode EMToolbar;

    public LabelInput(Context c, EditMode em){
        super(c);
        this.EMToolbar = em;

        btns = new Button[4];
        for(int i = 0; i < 4; i++) {
            btns[i] = new Button(c);
            btns[i].setText("^");
        }

        label = new AppCompatEditText(c);
        label.setBackgroundColor(getResources().getColor(R.color.colorBackground));
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

        btns[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(10);
            }
        });

        btns[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(1);
            }
        });

        btns[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(-1);
            }
        });

        btns[3].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(-10);
            }
        });

        setOrientation(LinearLayout.VERTICAL);
        addView(r1);
        addView(label);
        addView(r3);
    }

    public void changeLabel(int add){
        int curNo = Integer.parseInt(label.getText().toString()) + add;
        if(curNo < 0)
            return;

        String nLabel = "" + curNo;
        label.setText(nLabel);

        if(EMToolbar.tables.containsLabel(nLabel) &&
            Integer.parseInt(EMToolbar.getSelectedTbl().getLabel()) != curNo)
            label.setBackgroundColor(getResources().getColor(R.color.red));
        else {
            EMToolbar.getSelectedTbl().setLabel(nLabel);
            EMToolbar.getSelected().setText(nLabel);
            label.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        }
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

    public void setText(String s){
        label.setText(s);
    }

    public void setSingleLine(boolean val){
        label.setSingleLine(val);
    }
}
