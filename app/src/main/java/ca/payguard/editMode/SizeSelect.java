package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.payguard.Table;

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

    /** Loads the size when a table is selected. */
    public void selectTable(Table t){
        switch(t.getSizeMod()){
            case 1:
                size = "S";
                break;
            case 2:
                size = "M";
                break;
            default:
                size = "L";
                break;
        }

        if(size.equals("S")){
            subSize.setEnabled(false);
            addSize.setEnabled(true);
        } else if(size.equals("M")){
            subSize.setEnabled(true);
            addSize.setEnabled(true);
        } else{
            subSize.setEnabled(true);
            addSize.setEnabled(false);
        }

        sizeDisplay.setText(size);
    }

    public void addSize(){
        //if(EMToolbar.getSelected() != null), then EMToolbar.getSelectedTbl() also != null
        if(EMToolbar.getSelected() != null){
            if(size.equals("S")){
                EMToolbar.getSelectedTbl().setSizeMod(2);
                size = "M";
                sizeDisplay.setText(size);
                subSize.setEnabled(true);
            } else if(size.equals("M")){
                EMToolbar.getSelectedTbl().setSizeMod(3);
                size = "L";
                sizeDisplay.setText(size);
                addSize.setEnabled(false);
            }

            EMToolbar.shapeSelect.transform();
            EMToolbar.select(EMToolbar.getSelected());
        }
    }

    public void subSize(){
        if(EMToolbar.getSelected() != null){
            if(size.equals("L")){
                EMToolbar.getSelectedTbl().setSizeMod(2);
                size = "M";
                sizeDisplay.setText(size);
                addSize.setEnabled(true);
            } else if(size.equals("M")){
                EMToolbar.getSelectedTbl().setSizeMod(1);
                size = "S";
                sizeDisplay.setText(size);
                subSize.setEnabled(false);
            }

            EMToolbar.shapeSelect.transform();
            EMToolbar.select(EMToolbar.getSelected());
        }
    }
}
