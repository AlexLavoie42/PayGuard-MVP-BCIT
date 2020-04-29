package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import ca.payguard.R;

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
                    //create new table
                } else {
                    Button b = EMToolbar.getSelected();
                    int dim = EMToolbar.getSize();
                    b.setWidth(dim);
                    b.setHeight(dim);
                    b.setMinimumWidth(dim);
                    b.setMinimumHeight(dim);
                    b.setBackgroundResource(0);
                    b.setBackgroundColor(getResources().getColor(R.color.brightGreen));
                }
            }
        });

        circleTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EMToolbar.getSelected() == null){
                    //create new table
                } else {
                    Button b = EMToolbar.getSelected();
                    int dim = EMToolbar.getSize();
                    b.setWidth(dim);
                    b.setHeight(dim);
                    b.setMinimumWidth(dim);
                    b.setMinimumHeight(dim);
                    b.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
                }
            }
        });

        rectangleTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EMToolbar.getSelected() == null){
                    //create new table
                } else {
                    Button b = EMToolbar.getSelected();
                    int dim = EMToolbar.getSize();
                    b.setWidth(dim * 2);
                    b.setHeight(dim);
                    b.setMinimumWidth(dim * 2);
                    b.setMinimumHeight(dim);
                    b.setBackgroundResource(0);
                    b.setBackgroundColor(getResources().getColor(R.color.brightGreen));
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
}
