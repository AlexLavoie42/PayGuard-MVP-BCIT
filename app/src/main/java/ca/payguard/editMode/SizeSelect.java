package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SizeSelect extends LinearLayout {
    Button addSize, subSize;
    TextView sizeDisplay;

    public SizeSelect(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        addSize = new Button(context);
        addSize.setText("+");
        sizeDisplay = new TextView(context);
        sizeDisplay.setText("M");
        subSize = new Button(context);
        subSize.setText("-");

        addView(addSize);
        addView(sizeDisplay);
        addView(subSize);
    }

    void load(float width, int height){
        setMinimumWidth((int)(width * 0.125));
        setMinimumHeight(height);
        setGravity(Gravity.CENTER);
    }
}
