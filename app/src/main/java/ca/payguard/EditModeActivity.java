package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import ca.payguard.dbUtil.DatabaseController;
import ca.payguard.editMode.*;
import java.util.ArrayList;

public class EditModeActivity extends AppCompatActivity {
    LabelInput label;
    public static RotateTool rotateTool;
    public ShapeSelect shapeSelect;
    SizeSelect sizeSelect;
    ImageButton garbage;

    public static TableSet tables;
    public static Table selectedTbl;
    static Button selected;
    private static int tblSize;
    private DatabaseController db;

    public static ConstraintLayout tableLayout;
    public static ArrayList<Button> tblBtns;

    public static float wRatio, hRatio;

    //used for moving btns
    float btnX, btnY;
    final int MOVE_TIME = 5;
    int moveCount = MOVE_TIME;

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
        garbage.setX(getScreenWidth() - 300);
        garbage.setY(getScreenHeight() - 275);

        tableLayout = findViewById(R.id.tableLayout);
    }

    @Override
    protected void onStart(){
        super.onStart();

        wRatio = getWidthRatio();
        hRatio = getHeightRatio();

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

        postTableSet(MainActivity.getTables());
    }

    public void postTableSet(TableSet tables){
        tblBtns = renderTableSet(getBaseContext(), tables);
        EditModeActivity.tables = tables;
        tableLayout.removeAllViews();
        for(Button b : tblBtns)
            tableLayout.addView(b);
    }

    public void postTableSet(){
        tblBtns = renderTableSet(getBaseContext(), tables);
        tableLayout.removeAllViews();
        for(Button b : tblBtns)
            tableLayout.addView(b);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        this.onStart();
    }

    @Override
    public void onBackPressed() {
        exit(null);
    }

    /** Translates a table set to their buttons. */
    public ArrayList<Button> renderTableSet(final Context c, TableSet tables){
        ArrayList<Button> btns = new ArrayList<>();
        tblSize = (int) Math.max((float) TableSet.STD_WIDTH * wRatio,
                (float) TableSet.STD_HEIGHT * hRatio) / 20;

        for(Table t : tables)
            btns.add(renderTblBtn(c, t));

        deselect();

        return btns;
    }

    private Button renderTblBtn(Context c, final Table t){
        final Button b = new Button(c);
        b.setText(t.getLabel());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(b);
            }
        });

        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(getSelected() != null){
                            btnX = event.getRawX();
                            btnY = event.getRawY();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(moveCount == MOVE_TIME){
                            if(getSelected() != null){
                                v.setX(event.getRawX() - b.getWidth() / 2);
                                v.setY(event.getRawY() - b.getHeight() / 2);
                            }

                            moveCount = 0;
                        }

                        moveCount++;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(getSelected() != null){
                            float x = event.getRawX() - b.getWidth() / 2;
                            float y = event.getRawY() - b.getHeight() / 2;

                            v.setX(x);
                            v.setY(y);

                            getSelectedTbl().setCoords((int)(x / wRatio),
                                    (int)(y / hRatio));
                        }

                        moveCount = MOVE_TIME;
                        break;
                }

                return false;
            }
        });

        shapeSelect.transform(b, t);
        RotateTool.verifyRotation(t, b);

        /*int width = getSize() * t.getSizeMod(), height = width;
        if(t.getShape() == Table.Shape.R){
            if(t.getRotated())
                height *= 2;
            else
                width *= 2;
        }*/
        //+ (float) width / 2
        b.setX(t.getX() * wRatio);
        //+ (float) height / 2
        b.setY(t.getY() * hRatio);

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

            rotateTool.display(selectedTbl);

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
        rotateTool.hide();
    }

    public void dispose(View v){
        tables.remove(getSelectedTbl());
        deselect();

        postTableSet();
    }

    public void addTable(char shape) throws IllegalArgumentException {
        Table t = new Table();
        t.setShape(shape);
        t.setSizeMod(1);
        t.setCoords((int)(getScreenWidth() / wRatio / 2), (int)(getScreenHeight() / hRatio / 2));
        t.setLabel("" + (tables.size() + 1));
        tables.add(t);

        postTableSet();

        select(tblBtns.get(tblBtns.size() - 1));
    }

    public void exit(View view){
        db.updateTableSet(tables);
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        //TODO change to finish(), update mainactivity in its onRestart method
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

    public void activityClicked(View v){
        if(!(v instanceof Button))
            deselect();
    }
}
