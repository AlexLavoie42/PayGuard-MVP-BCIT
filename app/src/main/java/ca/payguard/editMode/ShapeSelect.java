package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import ca.payguard.R;
import ca.payguard.Table;

public class ShapeSelect extends LinearLayout {
    Button squareTool, circleTool, rectangleTool;

    EditMode EMToolbar;

    public ShapeSelect(Context context, EditMode em) {
        super(context);
        EMToolbar = em;
        setOrientation(LinearLayout.HORIZONTAL);

        squareTool = new Button(context);
        circleTool = new Button(context);
        rectangleTool = new Button(context);

        squareTool.setCompoundDrawablesWithIntrinsicBounds(R.drawable.common_full_open_on_phone,
                0, 0, 0);
        circleTool.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher_background,
                0, 0, 0);
        rectangleTool.setCompoundDrawablesWithIntrinsicBounds(R.drawable.common_full_open_on_phone,
                0, 0, 0);

        squareTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EMToolbar.getSelected() == null){
                    EMToolbar.addTable('S');
                } else {
                    applySquare(EMToolbar.getSelected());
                    EMToolbar.select(EMToolbar.getSelected());
                }
            }
        });

        circleTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EMToolbar.getSelected() == null){
                    EMToolbar.addTable('C');
                } else {
                    applyCircle(EMToolbar.getSelected());
                    EMToolbar.select(EMToolbar.getSelected());
                }
            }
        });

        rectangleTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EMToolbar.getSelected() == null){
                    EMToolbar.addTable('R');
                } else {
                    applyRectangle(EMToolbar.getSelected());
                    EMToolbar.select(EMToolbar.getSelected());
                }
            }
        });

        addView(squareTool);
        addView(circleTool);
        addView(rectangleTool);
    }

    void load(float width, int height){
        setMinimumWidth((int)(width * 0.35));
        setMinimumHeight(height);
        setGravity(Gravity.CENTER);
    }

    public void applySquare(Button b){
        int dim = EMToolbar.getSize();

        //get the size modification from size selection
        int mod;
        if(EMToolbar.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = EMToolbar.getSelectedTbl().getSizeMod();
            EMToolbar.getSelectedTbl().setShape('S');
        }

        b.setWidth(dim * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * mod);
        b.setMinimumHeight(dim * mod);
        b.setBackgroundResource(0);
        b.setBackgroundColor(getResources().getColor(R.color.brightGreen));
    }

    public void applyCircle(Button b){
        int dim = EMToolbar.getSize();

        //get the size modification from size selection
        int mod;
        if(EMToolbar.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = EMToolbar.getSelectedTbl().getSizeMod();
            EMToolbar.getSelectedTbl().setShape('C');
        }

        b.setWidth(dim * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * mod);
        b.setMinimumHeight(dim * mod);
        b.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
    }

    public void applyRectangle(Button b){
        int dim = EMToolbar.getSize();

        //get the size modification from size selection
        int mod;
        if(EMToolbar.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = EMToolbar.getSelectedTbl().getSizeMod();
            EMToolbar.getSelectedTbl().setShape('R');
        }

        b.setWidth(dim * 2 * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * 2 * mod);
        b.setMinimumHeight(dim * mod);
        b.setBackgroundResource(0);
        b.setBackgroundColor(getResources().getColor(R.color.brightGreen));
    }

    /* Finds the table's set shape and calls the corresponding function. */
    public void transform(){
        char shape = EMToolbar.getSelectedTbl().getShape();
        if(shape == Table.shapes[0])
            applySquare(EMToolbar.getSelected());
        else if(shape == Table.shapes[1])
            applyCircle(EMToolbar.getSelected());
        else
            applyRectangle(EMToolbar.getSelected());
    }
}
