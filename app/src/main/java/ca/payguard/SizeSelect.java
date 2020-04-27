package ca.payguard;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SizeSelect extends LinearLayout {
    public SizeSelect(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    void load(float width, int height, Context c){
        setMinimumWidth((int)(width * 0.125));
        setMinimumHeight(height);
        setGravity(Gravity.CENTER);

        Button addSize = new Button(c);
        addSize.setText("+");
        TextView sizeDisplay = new TextView(c);
        sizeDisplay.setText("M");
        Button subSize = new Button(c);
        subSize.setText("-");
        addView(addSize);
        addView(sizeDisplay);
        addView(subSize);
    }
}
