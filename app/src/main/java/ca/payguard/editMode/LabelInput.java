package ca.payguard.editMode;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

import ca.payguard.EditModeActivity;
import ca.payguard.R;

public class LabelInput extends LinearLayout {
    AppCompatEditText label;

    LinearLayout r1, r3;//layout rows - r2 only has 1 element and doesn't need layout
    Button[] btns;

    EditModeActivity em;

    public LabelInput(EditModeActivity c){
        super(c);
        em = c;

        btns = new Button[4];
        btns[0] = findViewById(R.id.btn1);
        btns[1] = findViewById(R.id.btn2);
        btns[2] = findViewById(R.id.btn3);
        btns[3] = findViewById(R.id.btn4);

        label = findViewById(R.id.labelPicker);
        label.setSingleLine(true);
        label.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });//disable keyboard

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
    }

    public void changeLabel(int add){
        int curNo = Integer.parseInt(label.getText().toString()) + add;
        if(curNo < 0)
            return;

        String nLabel = "" + curNo;
        label.setText(nLabel);

        if(em.tables.containsLabel(nLabel) &&
            Integer.parseInt(em.getSelectedTbl().getLabel()) != curNo)
            label.setBackgroundColor(getResources().getColor(R.color.red));
        else {
            em.getSelectedTbl().setLabel(nLabel);
            em.getSelected().setText(nLabel);
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

    public void clear(){
        label.setText("Table Name");
        label.setBackgroundColor(getResources().getColor(R.color.colorBackground));
    }

    public void setText(String s){
        label.setText(s);
    }
}
