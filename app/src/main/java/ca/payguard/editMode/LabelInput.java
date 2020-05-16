package ca.payguard.editMode;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

import ca.payguard.EditModeActivity;
import ca.payguard.R;

public class LabelInput extends Tool {
    AppCompatEditText label;

    public LabelInput(Context c, View ... views){
        super(c, views);
        addListeners();
    }

    @Override
    public void addListeners(){
        ((Button) views[0]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((Button) views[1]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((AppCompatEditText) views[2]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((Button) views[3]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((Button) views[4]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void changeLabel(int add){
        int curNo = Integer.parseInt(label.getText().toString()) + add;
        if(curNo < 0)
            return;

        String nLabel = "" + curNo;
        label.setText(nLabel);

        if(EditModeActivity.tables.containsLabel(nLabel) &&
            Integer.parseInt(EditModeActivity.getSelectedTbl().getLabel()) != curNo)
            label.setBackgroundColor(context.getResources().getColor(R.color.red));
        else {
            EditModeActivity.getSelectedTbl().setLabel(nLabel);
            EditModeActivity.getSelected().setText(nLabel);
            label.setBackgroundColor(context.getResources().getColor(R.color.colorBackground));
        }
    }

    public void clear(){
        label.setText("Table Name");
        label.setBackgroundColor(context.getResources().getColor(R.color.colorBackground));
    }

    public void setText(String s){
        label.setText(s);
    }
}
