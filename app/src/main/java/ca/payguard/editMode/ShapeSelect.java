package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import ca.payguard.R;

public class ShapeSelect extends LinearLayout {
    Button squareTool, circleTool, rectangleTool;

    public ShapeSelect(Context context) {
        super(context);
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
