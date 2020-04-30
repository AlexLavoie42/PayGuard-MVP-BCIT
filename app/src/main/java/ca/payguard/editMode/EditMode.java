package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import ca.payguard.R;
import ca.payguard.Table;

public class EditMode extends GridLayout {
    Button garbage;
    private boolean active;

    //access bar tools
    EditText nameInput;
    public ShapeSelect shapeSelect;
    SizeSelect sizeSelect;
    Button exitBtn;

    ArrayList<Table> tables;
    private Button selected;
    private int size;

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
            sizeSelect.addSize.setEnabled(true);
            sizeSelect.subSize.setEnabled(true);
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

            this.selected = b;
        }
    }

    public void setSize(int size){
        this.size = size;
    }

    public int getSize(){
        return size;
    }

    public Button getSelected(){
        return selected;
    }

    public boolean isActive(){
        return active;
    }
}
