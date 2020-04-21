package com.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import java.util.ArrayList;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private TableSet tableGui;
    private ArrayList<Button> tblBtns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableGui = new TableSet(getScreenWidth() / 2, getScreenHeight() / 2);
        tableGui.load();

        displayTables();
    }

    /** Loads the table set onto the screen. */
    private void displayTables(){
        //gets the layout tag from xml
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.mainLayout);

        //adds a button for each table from the table set
        for(Table t : tableGui){
            Button b = new Button(this);
            b.setText(t.getLabel());
            b.setX(t.getX());
            b.setY(t.getY());

            tblBtns.add(b);
            layout.addView(b);
        }
    }

    private int getScreenWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private int getScreenHeight(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
