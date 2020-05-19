package ca.payguard.editMode;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ca.payguard.EditModeActivity;
import ca.payguard.R;
import ca.payguard.Table;

public class RotateTool extends Tool {
    public RotateTool(Context c, View ... views){
        super(c, views);
    }

    @Override
    public void applyTransformation(int btnNo){
        Table t = EditModeActivity.getSelectedTbl();
        Button b = EditModeActivity.getSelected();

        /*if(t == null)
            throw new UnsupportedOperationException("Error: a shape must be selected to " +
                    "apply rotation.");*/

        t.setRotated(!t.getRotated());
        verifyRotation(t, b);
    }

    public static void verifyRotation(Table t, Button b){
        if(t.getShape() == Table.Shape.R){
            if(t.getRotated()){
                b.setMinimumWidth(EditModeActivity.getSize() * t.getSizeMod());
                b.setMinimumHeight(EditModeActivity.getSize() * t.getSizeMod() * 2);
                b.setWidth(EditModeActivity.getSize() * t.getSizeMod());
                b.setHeight(EditModeActivity.getSize() * t.getSizeMod() * 2);
            } else {
                b.setMinimumWidth(EditModeActivity.getSize() * t.getSizeMod() * 2);// * 2
                b.setMinimumHeight(EditModeActivity.getSize() * t.getSizeMod());
                b.setWidth(EditModeActivity.getSize() * t.getSizeMod() * 2);// * 2
                b.setHeight(EditModeActivity.getSize() * t.getSizeMod());
            }
        }
    }

    private void setCoords(float x, float y){
        views[0].setEnabled(true);
        views[0].setVisibility(View.VISIBLE);

        views[0].setX(x);
        views[0].setY(y - 100);
    }

    /** Displays the rotate tool above the selected table. */
    public void display(Table t){
        if(t.getShape() == Table.Shape.R)
            setCoords(t.getX() * EditModeActivity.wRatio, t.getY() * EditModeActivity.hRatio);
    }

    public void hide(){
        views[0].setEnabled(false);
        views[0].setVisibility(View.GONE);
    }
}
