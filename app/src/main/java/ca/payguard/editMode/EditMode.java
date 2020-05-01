package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import ca.payguard.R;
import ca.payguard.Table;

public class EditMode extends GridLayout {
    private boolean active;

    //access bar tools
    EditText nameInput;
    public ShapeSelect shapeSelect;
    SizeSelect sizeSelect;
    Button exitBtn;

    ArrayList<Table> tables;
    private Button selected;
    private Table selectedTbl;
    private int size;

    //used for buttonTouched()
    float wRatio, hRatio;
    float btnX, btnY;

    //external tools
    Button rotLeft, rotRight;
    Button garbage;

    public EditMode(Context context) {
        super(context);

        setBackgroundColor(getResources().getColor(R.color.brightGreen));
        setRowCount(1);
        setColumnCount(4);

        nameInput = new EditText(context);
        shapeSelect = new ShapeSelect(context, this);
        sizeSelect = new SizeSelect(context, this);
        exitBtn = new Button(context);

        nameInput.setText("Table Name");
        nameInput.setSingleLine(true);
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

    public void enable(float width, int height, ArrayList<Table> tables){
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

        if(this.selected != null)
            this.selected.setBackgroundColor(getResources().getColor(R.color.brightGreen));
        b.setBackground(getResources().getDrawable(R.drawable.table_selector));

        for(Table t : tables){
            if(t.getLabel().equals(b.getText())){
                selected = t;
                break;
            }
        }

        if(selected != null){
            nameInput.setText(selected.getLabel());
            nameInput.setEnabled(true);
            sizeSelect.selectTable(selected);
            nameInput.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch(keyCode){
                        //backspace if the key is illegal
                        case KeyEvent.KEYCODE_PLUS:
                            nameInput.setText(nameInput.getText().toString().substring(0,
                                    nameInput.getText().length() - 1));
                            break;
                        default:
                            b.setText(nameInput.getText());
                    }

                    return false;
                }
            });

            //locate the rotation tools by the button
            rotLeft.setVisibility(View.VISIBLE);
            rotLeft.setX(b.getX() - 200);
            rotLeft.setY(b.getY() + 25);
            rotRight.setVisibility(View.VISIBLE);
            rotRight.setX(b.getX() + 275);
            rotRight.setY(b.getY() + 25);

            selectedTbl = selected;
            this.selected = b;
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
        rotLeft = new Button(c);
        rotRight = new Button(c);
        rotLeft.setVisibility(View.GONE);
        rotRight.setVisibility(View.GONE);
        rotLeft.setText("rotL");//TODO replace with drawable
        rotRight.setText("rotR");//TODO replace with drawable

        rotLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = getSelected();
                if(b != null){
                    int nRotation = Table.verifyAngle(getSelectedTbl().getAngle() - 30);
                    getSelectedTbl().setAngle(nRotation);
                    b.setRotation(nRotation);
                }
            }
        });

        rotRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = getSelected();
                if(b != null){
                    int nRotation = Table.verifyAngle(getSelectedTbl().getAngle() + 30);
                    getSelectedTbl().setAngle(nRotation);
                    b.setRotation(nRotation);
                }
            }
        });

        garbage = new Button(c);
        garbage.setText("Garb");//TODO replace with drawable
        garbage.setEnabled(false);//disabled until table is selected

        mainLayout.addView(rotLeft);
        mainLayout.addView(rotRight);
        mainLayout.addView(garbage);
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
