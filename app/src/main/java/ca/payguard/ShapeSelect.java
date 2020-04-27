package ca.payguard;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

public class ShapeSelect extends LinearLayout {
    public ShapeSelect(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    void load(float width, int height, Context c){
        setMinimumWidth((int)(width * 0.35));
        setMinimumHeight(height);
        setGravity(Gravity.CENTER);

        Button squareTool = new Button(c);
        Button circleTool = new Button(c);
        Button rectangleTool = new Button(c);
        addView(squareTool);
        addView(circleTool);
        addView(rectangleTool);
    }
}
