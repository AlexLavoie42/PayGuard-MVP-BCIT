package ca.payguard.editMode;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ca.payguard.EditModeActivity;
import ca.payguard.R;
import ca.payguard.Table;

public class RotateTool extends LinearLayout {
    public Button rotate;
    EditModeActivity em;

    public RotateTool(EditModeActivity c){
        super(c);
        em = c;

        rotate = findViewById(R.id.rotate);
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate();
            }
        });
    }

    /**
     *
     * @param t - getSelectedTbl()
     * @param b - getSelected()
     * @throws UnsupportedOperationException
     */
    public void rotate() throws UnsupportedOperationException {
        Table t = em.getSelectedTbl();
        Button b = em.getSelected();

        if(t == null)
            throw new UnsupportedOperationException("Error: a shape must be selected to " +
                    "apply rotation.");

        t.setRotated(!t.getRotated());
        //swap dimensions
        int width = b.getHeight(), height = b.getWidth();
        b.setWidth(width);
        b.setHeight(height);
    }
}
