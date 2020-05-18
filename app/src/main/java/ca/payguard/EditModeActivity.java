package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ca.payguard.dbUtil.DatabaseController;
import ca.payguard.editMode.*;
import java.util.ArrayList;

public class EditModeActivity extends AppCompatActivity {
    LabelInput label;
    RotateTool rotateTool;
    public ShapeSelect shapeSelect;
    SizeSelect sizeSelect;
    Button garbage;

    public static TableSet tables;
    public static Table selectedTbl;
    static Button selected;
    private static int tblSize;
    private DatabaseController db;

    public static ConstraintLayout tableLayout;
    public static ArrayList<Button> tblBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);

        try{
            db = new DatabaseController();
        } catch (DatabaseController.AuthNotFoundError e) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }

        garbage = findViewById(R.id.garbage);
        garbage.setX(getScreenWidth() - 50);
        garbage.setY(getScreenHeight() - 50);

        tableLayout = findViewById(R.id.tableLayout);
    }

    @Override
    protected void onStart(){
        super.onStart();

        label = new LabelInput(
                this,
                findViewById(R.id.label_btn1),
                findViewById(R.id.label_btn2),
                findViewById(R.id.label_picker),
                findViewById(R.id.label_btn3),
                findViewById(R.id.label_btn4)
        );

        rotateTool = new RotateTool(
                this,
                findViewById(R.id.rotate_btn)
        );

        shapeSelect = new ShapeSelect(
                this,
                findViewById(R.id.square_tool),
                findViewById(R.id.circle_tool),
                findViewById(R.id.rectangle_tool)
        );

        sizeSelect = new SizeSelect(
                this,
                findViewById(R.id.add_size),
                findViewById(R.id.size_display),
                findViewById(R.id.sub_size)
        );

        /* TableSet tables = (TableSet) getIntent().getExtras().getSerializable("tables");*/
        TableSet tables = MainActivity.getTables();
        tblBtns = renderTableSet(getBaseContext(), tables);
        EditModeActivity.tables = tables;
        tableLayout.removeAllViews();
        for(Button b : tblBtns)
            tableLayout.addView(b);
    }

    /** Translates a table set to their buttons. */
    private ArrayList<Button> renderTableSet(final Context c, TableSet tables){
        ArrayList<Button> btns = new ArrayList<>();
        tblSize = (int) Math.max((float) TableSet.STD_WIDTH * getWidthRatio(),
                (float) TableSet.STD_HEIGHT * getHeightRatio()) / 20;

        for(Table t : tables)
            btns.add(renderTblBtn(c, t));

        deselect();

        return btns;
    }

    private Button renderTblBtn(Context c, final Table t){
        final Button b = new Button(c);
        b.setX(t.getX() * getWidthRatio());
        b.setY(t.getY() * getHeightRatio());
        b.setText(t.getLabel());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(b);
            }
        });

        shapeSelect.transform(b, t);

        return b;
    }

    public void select(final Button b){
        if(selected  != null) {
            if(selectedTbl.getShape() != Table.Shape.C)
                selected.setBackground(getResources().getDrawable(R.drawable.table));
            else
                selected.setBackground(getResources().getDrawable(R.drawable.table_round));
        }

        Table selectedTbl = null;

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

            rotateTool.setEnabled(true);
            label.setEnabled(true);

            garbage.setEnabled(true);
            EditModeActivity.selectedTbl = selectedTbl;
            selected = b;
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

            rotateTool.setEnabled(false);
            sizeSelect.setEnabled(false);
            label.setEnabled(false);
        }

        garbage.setEnabled(false);
    }

    public void dispose(View v){

    }

    /** TODO add table to edit mode activity. */
    public void addTable(char shape){

    }

    public void exit(View view){
        db.updateTableSet(tables);
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    public static int getSize(){ return tblSize; }

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
