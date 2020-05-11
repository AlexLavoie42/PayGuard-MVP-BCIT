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

    public void disable(){
        rotLeft.setEnabled(false);
        rotRight.setEnabled(false);
    }

    public void enable(){
        rotLeft.setEnabled(true);
        rotRight.setEnabled(true);
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
