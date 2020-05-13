package ca.payguard.editMode;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import ca.payguard.LoginActivity;
import ca.payguard.MainActivity;
import ca.payguard.R;
import ca.payguard.Table;
import ca.payguard.TableSet;
import ca.payguard.dbUtil.DatabaseController;

public class EditMode extends LinearLayout {
    private boolean active;
    private DatabaseController db;
    final String ILLEGAL_ARGE = "Error: table shape is not allowed.";

    //access bar tools
    LabelInput labelInput;
    public ShapeSelect shapeSelect;
    SizeSelect sizeSelect;
    Button exitBtn;

    TableSet tables;
    private Button selected;
    private Table selectedTbl;
    private int size;

    //used for buttonTouched()
    float wRatio, hRatio;
    float btnX, btnY;

    //external tools
    ConstraintLayout mainLayout;
    RotateTool rotateTool;
    public static Button garbage;

    LayoutInflater inflater;

    public EditMode(Context context) {
        super(context);

        try{
            db = new DatabaseController();
        } catch (DatabaseController.AuthNotFoundError e) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(getResources().getColor(R.color.brightGreen));

        labelInput = new LabelInput(context, this);
        shapeSelect = new ShapeSelect(context, this);
        sizeSelect = new SizeSelect(context, this);
        sizeSelect.disable();
        exitBtn = new Button(context);

        labelInput.setText("Table Name");
        labelInput.setSingleLine(true);
        labelInput.disable();
        exitBtn.setText("X");
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselect();
                disable();
                garbage.setVisibility(View.GONE);
            }
        });

        rotateTool = new RotateTool(context, this);
        rotateTool.disable();
        rotateTool.rotLeft.setText("rotL");//TODO replace with drawable
        rotateTool.rotRight.setText("rotR");//TODO replace with drawable

        addView(labelInput);
        addView(rotateTool.rotLeft);
        addView(rotateTool.rotRight);
        addView(shapeSelect);
        addView(sizeSelect);
        addView(exitBtn);

        //load xml layout/////////////////////////////////
        /*inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_edit_mode, this);*/
    }

    public void enable(TableSet tables){
        if(active)
            return;

        //getSupportActionBar().hide();//used if action bar is active on home screen

        active = true;
        this.tables = tables;
        MainActivity.settingsBtn.setVisibility(View.GONE);
    }

    public void disable(){
        if(!active)
            return;

        //getSupportActionBar().show();//used if action bar is active on home screen

        db.updateTableSet(tables);
        setVisibility(View.GONE);
        active = false;
        MainActivity.settingsBtn.setVisibility(View.VISIBLE);
    }

    public void select(final Button b){
        Table selected = null;

        if(this.selected != null) {
            if(this.selectedTbl.getShape() != Table.Shape.C)
                this.selected.setBackgroundColor(getResources().getColor(R.color.brightGreen));
            else
                this.selected.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
        }

        for(Table t : tables){
            if(t.getLabel().equals(b.getText())){
                selected = t;
                break;
            }
        }

        if(selected.getShape() != Table.Shape.C)
            b.setBackground(getResources().getDrawable(R.drawable.table_selector));
        else
            b.setBackground(getResources().getDrawable(R.drawable.btn_rounded_selected));

        if(selected != null){
            labelInput.setText(selected.getLabel());
            sizeSelect.selectTable(selected);

            rotateTool.enable();
            labelInput.enable();

            garbage.setEnabled(true);
            selectedTbl = selected;
            this.selected = b;
        }
    }

    private void deselect(){
        Button b = getSelected();
        if(b != null){
            Table t = getSelectedTbl();
            if(t.getShape() == Table.Shape.C)
                b.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
            else
                b.setBackgroundColor(getResources().getColor(R.color.brightGreen));

            selected = null;
            selectedTbl = null;
            labelInput.clear();

            rotateTool.disable();
            sizeSelect.disable();
            labelInput.disable();
        }

        garbage.setEnabled(false);
    }

    /** This method moves the button if the user attempts to drag
     * a button once selected. */
    public void buttonTouched(Button b, MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(getSelected() == b){
                    btnX = event.getRawX() - getSize();
                    btnY = event.getRawY() - getSize();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(getSelected() == b){
                    float x = event.getRawX() - getSize(), y = event.getRawY() - getSize();
                    float distanceX = (btnX - x) * -1, distanceY = (btnY - y) * -1;

                    float nx = btnX + distanceX, ny = btnY + distanceY;
                    b.setX(nx);
                    b.setY(ny);
                    btnX = nx;
                    btnY = ny;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(getSelected() == b){
                    float x = event.getRawX() - getSize(), y = event.getRawY() - getSize();
                    float distanceX = (btnX - x) * -1, distanceY = (btnY - y) * -1;

                    float nx = btnX + distanceX, ny = btnY + distanceY;
                    b.setX(nx);
                    b.setY(ny);
                    btnX = nx;
                    btnY = ny;
                    getSelectedTbl().setCoords((int)(btnX / wRatio), (int)(btnY / hRatio));
                }
        }
    }

    /**
     * This method enables tools that are outside of Edit Mode's
     * access bar such as the rotation and garbage tools.
     */
    public void enableExternalTools(ConstraintLayout mainLayout, Context c){
        this.mainLayout = mainLayout;

        mainLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //deselect if user taps away from a button
                if(!(v instanceof Button))
                    deselect();

                //deselect the number picker if tapped away
                //TODO
            }
        });

        garbage = new Button(c);
        garbage.setText("Garb");//TODO replace with drawable
        garbage.setEnabled(false);//disabled until table is selected
        garbage.setX((TableSet.STD_WIDTH - 150) * wRatio);
        garbage.setY((TableSet.STD_HEIGHT - 300) * hRatio);
        garbage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTable();
            }
        });

        setMinimumWidth((int) (TableSet.STD_WIDTH * wRatio));
        setMinimumHeight(200);
        float segment = (TableSet.STD_WIDTH * wRatio) / 25;
        labelInput.setX(segment * 1);
        rotateTool.rotLeft.setX(segment * (float) 3.5);
        rotateTool.rotRight.setX(segment * (float) 4.5);
        rotateTool.rotLeft.setY(100);
        rotateTool.rotRight.setY(100);
        shapeSelect.setX(segment * 6);
        sizeSelect.setX(segment * 8);
        exitBtn.setX(segment * (float) 8.5);

        mainLayout.addView(garbage);
    }

    /** Allocates a table at the specified point. */
    private void allocateTable(char shape, int pointX, int pointY)
        throws IllegalArgumentException{
        if(shape != 'S' && shape != 'C' && shape != 'R')
            throw new IllegalArgumentException("Error: table shape is not allowed.");

        int segmentX = TableSet.STD_WIDTH / 20, segmentY = TableSet.STD_HEIGHT / 20;
        addTable(shape);
        selectedTbl.setCoords(segmentX * pointX, segmentY * pointY);
        selected.setX(selectedTbl.getX() * wRatio);
        selected.setY(selectedTbl.getY() * hRatio);
    }

    /** Transforms the last table to a bar table if standard
     * table arrangement is used. */
    public void applyStdArrangement(){
        if(tables.isStdFormation()){
            int segmentX = TableSet.STD_WIDTH / 20, segmentY = TableSet.STD_HEIGHT / 20;

            allocateTable('C', 2, 6);
            allocateTable('C', 7, 6);
            allocateTable('C', 11, 8);
            allocateTable('C', 2, 10);
            allocateTable('C', 7, 10);
            allocateTable('C', 11, 12);
            allocateTable('C', 2, 14);
            allocateTable('C', 7, 14);
            allocateTable('C', 15, 9);

            sizeSelect.addSize();
            for(int i = 0; i < 3; i++)
                rotateTool.rotRight(getSelectedTbl(), getSelected());

            deselect();
        }
    }

    public void renderTableSet(ArrayList<Table> tables){
        MainActivity.tableLayout.removeAllViews();
        this.tables = (TableSet) tables;

        for(final Table t : tables){
            final Button b = new Button(getContext());
            MainActivity.tblBtns.add(b);
            MainActivity.tableLayout.addView(b);

            selected = b;
            selectedTbl = t;

            b.setText(t.getLabel());
            b.setX(t.getX() * wRatio);
            b.setY(t.getY() * hRatio);

            shapeSelect.transform();

            int angle = t.getAngle();
            if(angle != 0)
                b.setRotation(angle);

            b.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!getActive())
                        ((MainActivity) getContext()).tablePopup(t);
                    else
                        select(b);
                }
            });

            b.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    buttonTouched(b, event);
                    return false;
                }
            });
        }

        deselect();
    }

    /** Adds a table to tableset and button to the screen. */
    public void addTable(char shape) throws IllegalArgumentException {
        if(shape != 'S' && shape != 'C' && shape != 'R')
            throw new IllegalArgumentException(ILLEGAL_ARGE);

        final Table t = new Table();
        t.setShape(shape);

        tables.add(t);
        t.setLabel("" + tables.size());

        //add button to screen
        final Button b = new Button(getContext());
        b.setText("" + tables.size());
        b.setX((float) TableSet.STD_WIDTH / 2 * wRatio - (float) size / 2);
        b.setY((float) TableSet.STD_HEIGHT / 2 * hRatio - (float) size / 2);

        t.setCoords((int)(b.getX() / wRatio), (int)(b.getY() / hRatio));

        MainActivity.tblBtns.add(b);
        MainActivity.tableLayout.addView(b);

        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getActive())
                    ((MainActivity) getContext()).tablePopup(t);
                /*TODO else if(numList.getActive()){
                    //tables are non selectable while the number list is selected
                }*/
                else
                    select(b);
            }
        });

        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                buttonTouched(b, event);
                return false;
            }
        });

        //transform to specified shape
        final Table.Shape ts = t.getShape();
        if(ts == Table.Shape.S)
            shapeSelect.applySquare(b);
        else if(ts == Table.Shape.C)
            shapeSelect.applyCircle(b);
        else
            shapeSelect.applyRectangle(b);

        select(b);
    }

    /** Deletes table from table set and deletes button from the screen. */
    public void deleteTable(){
        Button b = getSelected();
        if(b == null)
            return;

        MainActivity.tblBtns.remove(b);
        tables.remove(getSelectedTbl());
        /*((ConstraintLayout) findViewById(R.id.tableLayout)).removeView(b) crashes app.
        Since there seems to be no other way to remove button from layout, only remove button
        from access lists and move off screen. */
        b.setVisibility(View.GONE);

        deselect();
    }

    public void setSize(int size){
        this.size = size;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setRatios(float wRatio, float hRatio){
        this.wRatio = wRatio;
        this.hRatio = hRatio;
    }

    public int getSize(){
        return size;
    }

    public Button getSelected(){
        return selected;
    }

    public Table getSelectedTbl(){
        return selectedTbl;
    }

    public boolean getActive(){
        return active;
    }
}
