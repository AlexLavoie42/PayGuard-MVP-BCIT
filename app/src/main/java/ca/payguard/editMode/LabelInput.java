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

        label = (AppCompatEditText) views[2];
        setEnabled(false);
    }

    @Override
    public void addListeners(){
        super.addListeners();

        ((AppCompatEditText) views[2]).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void applyTransformation(int btnNo){
        switch(btnNo){
            case 0:
                changeLabel(10);
                break;
            case 1:
                changeLabel(1);
                break;
            case 3:
                changeLabel(-1);
                break;
            case 4:
                changeLabel(-10);
                break;
        }
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
        label.setText("");
        label.setBackgroundColor(context.getResources().getColor(R.color.colorBackground));
    }

    public void setText(String s){
        label.setText(s);
    }
}
