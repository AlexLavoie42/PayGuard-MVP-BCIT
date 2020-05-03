package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import ca.payguard.R;
import ca.payguard.TableSet;

public class NumberList extends LinearLayout {
    EditMode EMToolbar;
    TextView label;
    NumberPicker p1, p2;
    Button cancelBtn, doneBtn;

    public NumberList(Context c, final EditMode EMToolbar){
        super(c);
        this.EMToolbar = EMToolbar;
        setOrientation(LinearLayout.VERTICAL);

        //row with label
        label = new TextView(c);
        label.setText("Select a table number");

        LinearLayout r1 = new LinearLayout(c);
        r1.addView(label);

        //row with number pickers
        p1 = new NumberPicker(c);
        p1.setGravity(Gravity.CENTER);
        p1.setMinValue(0);
        p1.setMaxValue(9);

        p2 = new NumberPicker(c);
        p2.setGravity(Gravity.CENTER);
        p2.setMinValue(0);
        p2.setMaxValue(9);

        LinearLayout r2 = new LinearLayout(c);
        r2.setOrientation(LinearLayout.HORIZONTAL);
        r2.addView(p1);
        r2.addView(p2);

        //row with buttons
        cancelBtn = new Button(c);
        cancelBtn.setText("Cancel");
        cancelBtn.setBackgroundColor(getResources().getColor(R.color.brightGreen));
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.GONE);
                p1.setValue(0);
                p2.setValue(0);
            }
        });

        doneBtn = new Button(c);
        doneBtn.setText("Done");
        doneBtn.setBackgroundColor(getResources().getColor(R.color.brightGreen));
        doneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.GONE);
                String nLabel = (p1.getValue() == 0) ? "" + p2.getValue() :
                        "" + p1.getValue() + p2.getValue();
                EMToolbar.getSelectedTbl().setLabel(nLabel);
                EMToolbar.getSelected().setText(nLabel);
            }
        });

        LinearLayout r3 = new LinearLayout(c);
        r3.setOrientation(LinearLayout.HORIZONTAL);
        r3.addView(cancelBtn);
        r3.addView(doneBtn);

        setBackgroundColor(getResources().getColor(R.color.brightGreen));
        addView(r1);
        addView(r2);
        addView(r3);
    }

    /** Aligns query to screen. */
    public void enableExternalTools(float wRatio, float hRatio){
        setMinimumWidth((int)((float)TableSet.STD_WIDTH * wRatio));
        setMinimumHeight((int)((float) TableSet.STD_HEIGHT / 3 * hRatio));
        setX(0);
        setY((float) TableSet.STD_HEIGHT / 3 * 2 * hRatio);

        //center coords of each component
        //TODO subtract half of component width to correctly center
        label.setX((float) TableSet.STD_WIDTH / 2 * wRatio);
        p1.setX((float) TableSet.STD_WIDTH / 2 * wRatio);
        p2.setX((float) TableSet.STD_WIDTH / 2 * wRatio);
        cancelBtn.setX((float) TableSet.STD_WIDTH / 2 * wRatio);
        doneBtn.setX((float) TableSet.STD_WIDTH / 2 * wRatio);
    }

    /** Updates the number pickers to display the parameter. */
    public void selectLabel(String num){
        char[] nums = num.toCharArray();

        if(nums.length == 1)
            p2.setValue(Character.getNumericValue(nums[0]));
        else{
            p1.setValue(Character.getNumericValue(nums[0]));
            p2.setValue(Character.getNumericValue(nums[1]));
        }
    }
}
