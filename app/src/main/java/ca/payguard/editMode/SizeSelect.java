package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.payguard.EditModeActivity;
import ca.payguard.R;
import ca.payguard.Table;

public class SizeSelect extends LinearLayout {
    Button addSize, subSize;
    TextView sizeDisplay;

    EditModeActivity em;

    public SizeSelect(EditModeActivity c) {
        super(c);
        em = c;

        addSize = findViewById(R.id.add_size);
        sizeDisplay = findViewById(R.id.size_display);
        subSize = findViewById(R.id.sub_size);

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
    }

    /** Loads the size when a table is selected. */
    public void selectTable(Table t){
        String size;

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
        if(em.getSelected() != null){
            String size = sizeDisplay.getText().toString();

            if(size.equals("S")){
                em.getSelectedTbl().setSizeMod(2);
                sizeDisplay.setText("M");
                subSize.setEnabled(true);
            } else if(size.equals("M")){
                em.getSelectedTbl().setSizeMod(3);
                sizeDisplay.setText("L");
                addSize.setEnabled(false);
            }

            em.shapeSelect.transform();
        }
    }

    public void subSize(){
        if(em.getSelected() != null){
            String size = sizeDisplay.getText().toString();

            if(size.equals("L")){
                em.getSelectedTbl().setSizeMod(2);
                sizeDisplay.setText("M");
                addSize.setEnabled(true);
            } else if(size.equals("M")){
                em.getSelectedTbl().setSizeMod(1);
                sizeDisplay.setText("S");
                subSize.setEnabled(false);
            }

            em.shapeSelect.transform();
        }
    }

    public void disable(){
        subSize.setEnabled(false);
        addSize.setEnabled(false);
    }
}
