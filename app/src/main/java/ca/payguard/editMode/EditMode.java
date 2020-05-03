package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import ca.payguard.R;
import ca.payguard.Table;
import ca.payguard.TableSet;

public class EditMode extends GridLayout {
    private boolean active;

    //access bar tools
    TextView nameInput;
    static NumberList numList;
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
    Button rotLeft, rotRight;
    Button garbage;

    public EditMode(Context context) {
        super(context);

        setBackgroundColor(getResources().getColor(R.color.brightGreen));
        setRowCount(1);
        setColumnCount(4);

        nameInput = new TextView(context);
        numList = new NumberList(context, this);
        numList.setVisibility(View.GONE);
        shapeSelect = new ShapeSelect(context, this);
        sizeSelect = new SizeSelect(context, this);
        exitBtn = new Button(context);

        nameInput.setText("Table Name");
        nameInput.setSingleLine(true);
        nameInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSelected() != null)
                    numList.setVisibility(View.VISIBLE);
            }
        });
        exitBtn.setText("X");
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disable();
            }
        });

        addView(nameInput);
        addView(shapeSelect);
        addView(sizeSelect);
        addView(exitBtn);
    }

    public void enable(float width, int height, TableSet tables){
        if(active)
            return;

        //getSupportActionBar().hide();//used if action bar is active on home screen

        setMinimumWidth((int) width);
        setMinimumHeight(height);

        /*
        Width distribution:
        nameInput - 40%
        shapeSelect - 35%
        sizeSelect - 12.5%
        exitBtn - 12.5%
         */
        nameInput.setMinimumWidth((int)(width * 0.4));
        nameInput.setMinimumHeight(height);
        //nameInput.setGravity(Gravity.CENTER);
        shapeSelect.load(width, height);
        sizeSelect.load(width, height);
        exitBtn.setMinimumWidth((int)(width * 0.125));
        exitBtn.setMinimumHeight(height);
        exitBtn.setGravity(Gravity.CENTER);

        setVisibility(View.VISIBLE);
        active = true;

        this.tables = tables;
        nameInput.setEnabled(false);
        sizeSelect.addSize.setEnabled(false);
        sizeSelect.subSize.setEnabled(false);
    }

    public void disable(){
        if(!active)
            return;

        //getSupportActionBar().show();//used if action bar is active on home screen

        setVisibility(View.GONE);
        active = false;
    }

    public void select(final Button b){
        Table selected = null;

        if(this.selected != null) {
            if(this.selectedTbl.getShape() != 'C')
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

        if(selected.getShape() != 'C')
            b.setBackground(getResources().getDrawable(R.drawable.table_selector));
        else
            b.setBackground(getResources().getDrawable(R.drawable.btn_rounded_selected));

        if(selected != null){
            nameInput.setText(selected.getLabel());
            nameInput.setEnabled(true);
            sizeSelect.selectTable(selected);

            //locate the rotation tools by the button (exclude circles)
            if(selected.getShape() != 'C'){
                rotLeft.setVisibility(View.VISIBLE);
                rotLeft.setX(b.getX() - 200);
                rotLeft.setY(b.getY() + 25);
                rotRight.setVisibility(View.VISIBLE);
                rotRight.setX(b.getX() + 275);
                rotRight.setY(b.getY() + 25);
            } else {
                rotLeft.setVisibility(View.GONE);
                rotRight.setVisibility(View.GONE);
            }

            selectedTbl = selected;
            this.selected = b;
        }
    }

    private void deselect(){
        Button b = getSelected();
        if(b != null){
            Table t = getSelectedTbl();
            if(t.getShape() == 'C')
                b.setBackground(getResources().getDrawable(R.drawable.btn_rounded));
            else
                b.setBackgroundColor(getResources().getColor(R.color.brightGreen));

            selected = null;
            selectedTbl = null;
            nameInput.setText("Table Name");

            //hide rotation tools
            rotLeft.setVisibility(View.GONE);
            rotRight.setVisibility(View.GONE);
        }
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

        rotLeft = new Button(c);
        rotRight = new Button(c);
        rotLeft.setVisibility(View.GONE);
        rotRight.setVisibility(View.GONE);
        rotLeft.setText("rotL");//TODO replace with drawable
        rotRight.setText("rotR");//TODO replace with drawable

        rotLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSelected() != null)
                    rotLeft();
            }
        });

        rotRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSelected() != null)
                    rotRight();
            }
        });

        garbage = new Button(c);
        garbage.setText("Garb");//TODO replace with drawable
        garbage.setEnabled(false);//disabled until table is selected
        garbage.setX((TableSet.STD_WIDTH - 150) * wRatio);
        garbage.setY((TableSet.STD_HEIGHT - 300) * hRatio);

        mainLayout.addView(rotLeft);
        mainLayout.addView(rotRight);
        mainLayout.addView(garbage);

        //add the number list query to main layout
        numList.enableExternalTools(wRatio, hRatio);
        mainLayout.addView(numList);
    }

    public void rotLeft() throws UnsupportedOperationException {
        if(getSelectedTbl() == null)
            throw new UnsupportedOperationException("Error: a shape must be selected to " +
                    "apply rotation.");

        int nRotation = Table.verifyAngle(getSelectedTbl().getAngle() - 30);
        getSelectedTbl().setAngle(nRotation);
        getSelected().setRotation(nRotation);
    }

    public void rotRight() throws UnsupportedOperationException {
        if(getSelectedTbl() == null)
            throw new UnsupportedOperationException("Error: a shape must be selected to " +
                    "apply rotation.");

        int nRotation = Table.verifyAngle(getSelectedTbl().getAngle() + 30);
        getSelectedTbl().setAngle(nRotation);
        getSelected().setRotation(nRotation);
    }

    /** Transforms the last table to a bar table if standard
     * table arrangement is used. */
    public void applyStdTransformation(ArrayList<Button> tblBtns){
        if(tables.isStdFormation()){
            select(tblBtns.get(tblBtns.size() - 1));
            sizeSelect.addSize();
            for(int i = 0; i < 3; i++)
                rotRight();
            deselect();
        }
    }

    //TODO
    /** Adds a table to tableset and button to the screen. */
    public void addTable(char shape) throws IllegalArgumentException {
        if(shape != 'S' && shape != 'C' && shape != 'R')
            throw new IllegalArgumentException("Error: table shape is not allowed.");

        Table t = new Table();
        t.setShape(shape);
    }

    //TODO
    /** Deletes table from table set and deletes button from the screen. */
    public void deleteTable(){
        if(getSelectedTbl() == null)
            return;
    }

    public void setSize(int size){
        this.size = size;
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

    public boolean isActive(){
        return active;
    }
}
