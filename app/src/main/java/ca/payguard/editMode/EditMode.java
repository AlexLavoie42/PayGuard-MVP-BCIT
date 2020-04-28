package ca.payguard.editMode;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import ca.payguard.R;

public class EditMode extends GridLayout {
    Button garbage;
    boolean active;

    //access bar tools
    EditText nameInput;
    ShapeSelect shapeSelect;
    SizeSelect sizeSelect;
    Button exitBtn;

    public EditMode(Context context) {
        super(context);

        setBackgroundColor(getResources().getColor(R.color.brightGreen));
        setRowCount(1);
        setColumnCount(4);

        nameInput = new EditText(context);
        shapeSelect = new ShapeSelect(context);
        sizeSelect = new SizeSelect(context);
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

        /*ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.mainLayout);
        layout.addView(this);
        setVisibility(View.GONE);*/
    }

    public void enable(float width, int height){
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
    }

    public void disable(){
        if(!active)
            return;

        //getSupportActionBar().show();//used if action bar is active on home screen

        setVisibility(View.GONE);
        active = false;
    }
}
