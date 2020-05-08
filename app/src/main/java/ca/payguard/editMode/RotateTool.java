package ca.payguard.editMode;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import ca.payguard.Table;
import ca.payguard.TableSet;

public class RotateTool {
    public Button rotLeft, rotRight;
    EditMode EMToolbar;

    public RotateTool(Context c, EditMode e){
        rotLeft = new Button(c);
        rotRight = new Button(c);

        EMToolbar = e;

        rotLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotLeft(EMToolbar.getSelectedTbl(), EMToolbar.getSelected());
            }
        });

        rotRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotRight(EMToolbar.getSelectedTbl(), EMToolbar.getSelected());
            }
        });
    }

    /** Locates the rotation tools by the button (excluding circles). */
    public void locateTools(Table t, Button b){
        if(t.getShape() != Table.Shape.C){
            rotLeft.setVisibility(View.VISIBLE);
            rotRight.setVisibility(View.VISIBLE);

            float halfwayX = TableSet.STD_WIDTH * EMToolbar.wRatio / 2;
            float halfwayY = EMToolbar.getHeight();

            rotLeft.setX(halfwayX / 2);
            rotLeft.setY(halfwayY / 8 * 1);
            rotRight.setX(halfwayX / 2);
            rotRight.setY(halfwayY / 8 * 5);
        } else
            hide();
    }

    public void hide(){
        rotLeft.setVisibility(View.GONE);
        rotRight.setVisibility(View.GONE);
    }

    /**
     *
     * @param t - getSelectedTbl()
     * @param b - getSelected()
     * @throws UnsupportedOperationException
     */
    public void rotLeft(Table t, Button b) throws UnsupportedOperationException {
        if(t == null)
            throw new UnsupportedOperationException("Error: a shape must be selected to " +
                    "apply rotation.");

        int nRotation = Table.verifyAngle(t.getAngle() - 30);
        t.setAngle(nRotation);
        b.setRotation(nRotation);
    }

    public void rotRight(Table t, Button b) throws UnsupportedOperationException {
        if(t == null)
            throw new UnsupportedOperationException("Error: a shape must be selected to " +
                    "apply rotation.");

        int nRotation = Table.verifyAngle(t.getAngle() + 30);
        t.setAngle(nRotation);
        b.setRotation(nRotation);
    }
}
