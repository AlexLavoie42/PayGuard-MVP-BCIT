package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import ca.payguard.EditModeActivity;
import ca.payguard.R;
import ca.payguard.Table;

public class SizeSelect extends Tool {
    private TextView sizeDisplay;

    public SizeSelect(Context c, View ... views) {
        super(c, views);

        sizeDisplay = (TextView) views[1];
    }

    @Override
    public void applyTransformation(int btnNo){
        if(btnNo == 0)
            addSize();
        else if(btnNo == 2)
            subSize();
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
            views[2].setEnabled(false);
            views[0].setEnabled(true);
        } else if(size.equals("M")){
            setEnabled(true);
        } else{
            views[2].setEnabled(true);
            views[0].setEnabled(false);
        }

        sizeDisplay.setText(size);
    }

    public void addSize(){
        //if(EMToolbar.getSelected() != null), then EMToolbar.getSelectedTbl() also != null
        if(EditModeActivity.getSelected() != null){
            String size = ((TextView) views[1]).getText().toString();

            if(size.equals("S")){
                EditModeActivity.getSelectedTbl().setSizeMod(2);
                sizeDisplay.setText("M");
                views[2].setEnabled(true);
            } else if(size.equals("M")){
                EditModeActivity.getSelectedTbl().setSizeMod(3);
                sizeDisplay.setText("L");
                views[0].setEnabled(false);
            }

            ((EditModeActivity) context).shapeSelect.transform();
        }
    }

    public void subSize(){
        if(EditModeActivity.getSelected() != null){
            String size = sizeDisplay.getText().toString();

            if(size.equals("L")){
                EditModeActivity.getSelectedTbl().setSizeMod(2);
                sizeDisplay.setText("M");
                views[0].setEnabled(true);
            } else if(size.equals("M")){
                EditModeActivity.getSelectedTbl().setSizeMod(1);
                sizeDisplay.setText("S");
                views[2].setEnabled(false);
            }

            ((EditModeActivity) context).shapeSelect.transform();
        }
    }
}
