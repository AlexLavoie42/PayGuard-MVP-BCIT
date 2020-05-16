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
        addListeners();
    }

    @Override
    public void addListeners(){
        ((Button) views[0]).setOnClickListener(new View.OnClickListener() {
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
        Table t = EditModeActivity.getSelectedTbl();
        Button b = EditModeActivity.getSelected();

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
