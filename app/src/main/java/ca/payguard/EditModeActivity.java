package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

    public static TableSet tables;
    static Table selectedTbl;
    static Button selected;
    private int tblSize;

    public static ConstraintLayout tableLayout;
    public static ArrayList<Button> tblBtns;

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

        tableLayout = findViewById(R.id.tableLayout);
    }

    @Override
    protected void onStart(){
        super.onStart();

        ArrayList<Table> tables = (ArrayList<Table>) getIntent().getSerializableExtra("tables");
        tblBtns = renderTableSet(getBaseContext(), tables);
        tableLayout.removeAllViews();
        for(Button b : tblBtns)
            tableLayout.addView(b);
    }

    /** Translates a table set to their buttons. */
    private ArrayList<Button> renderTableSet(final Context c, ArrayList<Table> tables){
        ArrayList<Button> btns = new ArrayList<>();

        return btns;
    }

    private Button renderTblBtn(Context c, final Table t){
        final Button b = new Button(c);

        return b;
    }

    /*public static void select(final Button b){
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

    private static void deselect(){
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

    public static Button getSelected(){
        return selected;
    }

    public static Table getSelectedTbl(){
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
