package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import ca.payguard.EditModeActivity;
import ca.payguard.MainActivity;
import ca.payguard.R;
import ca.payguard.Table;

public class ShapeSelect extends Tool {
    Button[] btns = new Button[3];

    public ShapeSelect(Context context, View ... views) {
        super(context, views);

        for(int i = 0; i < views.length; i++)
            btns[i] = (Button) views[i];
    }

    @Override
    public void addListeners(){
        for(int i = 0; i < btns.length; i++) {
            final int num = i;
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applyTransformation(num);
                }
            });
        }
    }

    @Override
    public void applyTransformation(int btnNo){
        switch(btnNo){
            case 0:
                if(EditModeActivity.getSelected() == null){
                    ((EditModeActivity) context).addTable('S');
                } else {
                    applySquare(EditModeActivity.getSelected());
                }
                break;
            case 1:
                if(EditModeActivity.getSelected() == null){
                    ((EditModeActivity) context).addTable('C');
                } else {
                    applyCircle(EditModeActivity.getSelected());
                }
                break;
            case 3:
                if(EditModeActivity.getSelected() == null){
                    ((EditModeActivity) context).addTable('R');
                } else {
                    applyRectangle(EditModeActivity.getSelected());
                }
                break;
        }
    }

    public void applySquare(Button b){
        int dim = EditModeActivity.getSize();

        //get the size modification from size selection
        int mod;
        if(EditModeActivity.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = EditModeActivity.getSelectedTbl().getSizeMod();
            EditModeActivity.getSelectedTbl().setShape('S');
        }

        b.setWidth(dim * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * mod);
        b.setMinimumHeight(dim * mod);
        //TODO verify whether shape is selected
        b.setBackground(context.getResources().getDrawable(R.drawable.table));
    }

    public void applyCircle(Button b){
        int dim = EditModeActivity.getSize();

        //get the size modification from size selection
        int mod;
        if(EditModeActivity.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = EditModeActivity.getSelectedTbl().getSizeMod();
            EditModeActivity.getSelectedTbl().setShape('C');
        }

        b.setWidth(dim * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * mod);
        b.setMinimumHeight(dim * mod);
        //TODO verify whether shape is selected
        b.setBackground(context.getResources()
                .getDrawable(R.drawable.table_round));
    }

    public void applyRectangle(Button b){
        int dim = EditModeActivity.getSize();

        //get the size modification from size selection
        int mod;
        if(EditModeActivity.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = EditModeActivity.getSelectedTbl().getSizeMod();
            EditModeActivity.getSelectedTbl().setShape('R');
        }

        b.setWidth(dim * 2 * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * 2 * mod);
        b.setMinimumHeight(dim * mod);
        //TODO verify whether shape is selected
        b.setBackground(context.getResources().getDrawable(R.drawable.table));
    }

    /* Finds the table's set shape and calls the corresponding function. */
    public void transform(){
        Table.Shape shape = EditModeActivity.getSelectedTbl().getShape();
        if(shape == Table.Shape.S)
            applySquare(EditModeActivity.getSelected());
        else if(shape == Table.Shape.C)
            applyCircle(EditModeActivity.getSelected());
        else
            applyRectangle(EditModeActivity.getSelected());
    }
}
