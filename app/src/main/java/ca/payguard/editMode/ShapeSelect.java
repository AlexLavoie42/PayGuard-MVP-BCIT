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

public class ShapeSelect extends LinearLayout {
    Button squareTool, circleTool, rectangleTool;
    EditModeActivity em;

    public ShapeSelect(EditModeActivity c) {
        super(c);
        em = c;

        squareTool = findViewById(R.id.squareTool);
        circleTool = findViewById(R.id.circleTool);
        rectangleTool = findViewById(R.id.rectangleTool);

        squareTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(em.getSelected() == null){
                    em.addTable('S');
                } else {
                    applySquare(em.getSelected());
                }
            }
        });

        circleTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(em.getSelected() == null){
                    em.addTable('C');
                } else {
                    applyCircle(em.getSelected());
                }
            }
        });

        rectangleTool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(em.getSelected() == null){
                    em.addTable('R');
                } else {
                    applyRectangle(em.getSelected());
                }
            }
        });
    }

    public void applySquare(Button b){
        int dim = em.getSize();

        //get the size modification from size selection
        int mod;
        if(em.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = em.getSelectedTbl().getSizeMod();
            em.getSelectedTbl().setShape('S');
        }

        b.setWidth(dim * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * mod);
        b.setMinimumHeight(dim * mod);
        //TODO verify whether shape is selected
        b.setBackground(getResources().getDrawable(R.drawable.table));
    }

    public void applyCircle(Button b){
        int dim = em.getSize();

        //get the size modification from size selection
        int mod;
        if(em.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = em.getSelectedTbl().getSizeMod();
            em.getSelectedTbl().setShape('C');
        }

        b.setWidth(dim * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * mod);
        b.setMinimumHeight(dim * mod);
        //TODO verify whether shape is selected
        b.setBackground(getResources().getDrawable(R.drawable.table_round));
    }

    public void applyRectangle(Button b){
        int dim = em.getSize();

        //get the size modification from size selection
        int mod;
        if(em.getSelectedTbl() == null)
            mod = 1;
        else {
            mod = em.getSelectedTbl().getSizeMod();
            em.getSelectedTbl().setShape('R');
        }

        b.setWidth(dim * 2 * mod);
        b.setHeight(dim * mod);
        b.setMinimumWidth(dim * 2 * mod);
        b.setMinimumHeight(dim * mod);
        //TODO verify whether shape is selected
        b.setBackground(getResources().getDrawable(R.drawable.table));
    }

    /* Finds the table's set shape and calls the corresponding function. */
    public void transform(){
        Table.Shape shape = em.getSelectedTbl().getShape();
        if(shape == Table.Shape.S)
            applySquare(em.getSelected());
        else if(shape == Table.Shape.C)
            applyCircle(em.getSelected());
        else
            applyRectangle(em.getSelected());
    }
}
