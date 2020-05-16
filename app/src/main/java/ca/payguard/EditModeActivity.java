package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import ca.payguard.editMode.*;
import java.util.ArrayList;

public class EditModeActivity extends AppCompatActivity {
    LabelInput label;
    RotateTool rotateTool;
    public ShapeSelect shapeSelect;
    SizeSelect sizeSelect;
    Button garbage;

    public TableSet tables;
    Table selectedTbl;
    Button selected;
    private int tblSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);

        /*label = new LabelInput(this);
        rotateTool = new RotateTool(this);
        shapeSelect = new ShapeSelect(this);
        sizeSelect = new SizeSelect(this);*/

        garbage = findViewById(R.id.garbage);
        garbage.setX(getScreenWidth() - 50);
        garbage.setY(getScreenHeight() - 50);
    }

    @Override
    protected void onStart(){
        super.onStart();

        ArrayList<Table> tables = (ArrayList<Table>) getIntent().getSerializableExtra("tables");
        //tblBtns = renderTableSet(getBaseContext(), tables);
    }

    /** Translates a table set to their buttons. */
    private ArrayList<Button> renderTableSet(final Context c, ArrayList<Table> tables){
        ArrayList<Button> btns = new ArrayList<>();
        tblSize = (int) Math.max((float) TableSet.STD_WIDTH * getWidthRatio(),
                (float) TableSet.STD_HEIGHT * getHeightRatio()) / 20;

        for(Table t : tables)
            btns.add(renderTblBtn(c, t));

        return btns;
    }

    private Button renderTblBtn(Context c, final Table t){
        final Button b = new Button(c);
        b.setText(t.getLabel());

        //reshape
        if(t.getShape() == Table.Shape.C)
            b.setBackground(getResources().getDrawable(R.drawable.table_round));
        else
            b.setBackground(getResources().getDrawable(R.drawable.table));

        int width, height;

        //resize and rotate if necessary
        if(t.getShape() != Table.Shape.R){
            width = tblSize * t.getSizeMod();
            height = tblSize * t.getSizeMod();
        } else {
            if(t.getRotated()){
                width = tblSize * t.getSizeMod();
                height = tblSize * t.getSizeMod() * 2;
            } else {
                width = tblSize * t.getSizeMod() * 2;
                height = tblSize * t.getSizeMod();
            }
        }

        b.setMinimumWidth(width);
        b.setMinimumHeight(height);
        b.setWidth(width);
        b.setHeight(height);

        b.setX((float) t.getX() * getWidthRatio() + width / 2);
        b.setY((float) t.getY() * getHeightRatio() - height / 3);

        //assign listeners
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select(b);
            }
        });

        return b;
    }

    /*public void select(final Button b){
        Table selectedTbl = null;

        if(this.selected != null) {
            if(this.selectedTbl.getShape() != Table.Shape.C)
                this.selected.setBackground(getResources().getDrawable(R.drawable.table));
            else
                this.selected.setBackground(getResources().getDrawable(R.drawable.table_round));
        }

        for(Table t : tables){
            if(t.getLabel().equals(b.getText())){
                selectedTbl = t;
                break;
            }
        }

        if(selectedTbl != null){
            if(selectedTbl.getShape() != Table.Shape.C)
                b.setBackground(getResources().getDrawable(R.drawable.table_selector));
            else
                b.setBackground(getResources().getDrawable(R.drawable.table_round_selected));

            label.setText(selectedTbl.getLabel());
            sizeSelect.selectTable(selectedTbl);

            rotateTool.rotate.setEnabled(true);
            label.enable();

            garbage.setEnabled(true);
            this.selectedTbl = selectedTbl;
            this.selected = b;
        }
    }

    private void deselect(){
        Button b = getSelected();
        if(b != null){
            Table t = getSelectedTbl();
            if(t.getShape() == Table.Shape.C)
                b.setBackground(getResources().getDrawable(R.drawable.table_round));
            else
                b.setBackground(getResources().getDrawable(R.drawable.table));

            selected = null;
            selectedTbl = null;
            label.clear();

            rotateTool.rotate.setEnabled(false);
            sizeSelect.disable();
            label.disable();
        }

        garbage.setEnabled(false);
    }*/

    public void dispose(View v){

    }

    /** TODO add table to edit mode activity. */
    public void addTable(char shape){

    }

    public int getSize(){ return tblSize; }

    public Button getSelected(){
        return selected;
    }

    public Table getSelectedTbl(){
        return selectedTbl;
    }

    private int getScreenWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getScreenHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public float getWidthRatio(){
        return (float) getScreenWidth() / (float) TableSet.STD_WIDTH;
    }

    public float getHeightRatio(){
        return (float) getScreenHeight() / (float) TableSet.STD_HEIGHT;
    }
}
