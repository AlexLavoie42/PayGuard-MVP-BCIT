package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SizeSelect extends LinearLayout {
    Button addSize, subSize;
    TextView sizeDisplay;

    EditMode EMToolbar;

    String size = "S";

    public SizeSelect(Context context, EditMode em) {
        super(context);
        EMToolbar = em;
        setOrientation(LinearLayout.VERTICAL);

        addSize = new Button(context);
        addSize.setText("+");
        sizeDisplay = new TextView(context);
        sizeDisplay.setText(size);
        subSize = new Button(context);
        subSize.setText("-");

        addSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addSize();
            }
        });

        subSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                subSize();
            }
        });

        addView(addSize);
        addView(sizeDisplay);
        addView(subSize);
    }

    void load(float width, int height){
        setMinimumWidth((int)(width * 0.125));
        setMinimumHeight(height);
        setGravity(Gravity.CENTER);
    }

    private void addSize(){
        if(EMToolbar.getSelected() != null){
            if(size.equals("S")){
                size = "M";
                sizeDisplay.setText(size);
                subSize.setEnabled(true);
            } else if(size.equals("M")){
                size = "L";
                sizeDisplay.setText(size);
                addSize.setEnabled(false);
            }
        }
    }

    private void subSize(){
        if(EMToolbar.getSelected() != null){
            if(size.equals("L")){
                size = "M";
                sizeDisplay.setText(size);
                addSize.setEnabled(true);
            } else if(size.equals("M")){
                size = "S";
                sizeDisplay.setText(size);
                subSize.setEnabled(false);
            }
        }
    }
}
