package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;

import com.payguard.R;

import java.util.ArrayList;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private TableSet tableGui;
    private ArrayList<Button> tblBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayTables();
    }

    /** Loads the table set onto the screen. */
    private void displayTables(){
        //loads the table objects
        tableGui = new TableSet();
        tableGui.load(getScreenWidth(), getScreenHeight());

        //sets a list for the table xml buttons
        tblBtns = new ArrayList<>();

        //gets the layout tag from xml
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.mainLayout);

        //adds a button for each table from the table set
        for(Table t : tableGui){
            Button b = new Button(this);
            b.setText(t.getLabel());
            b.setX(t.getX() - t.getWidth() / 2);
            b.setY(t.getY() - t.getHeight() / 2);
            b.setWidth(t.getWidth());
            b.setHeight(t.getHeight());

            tblBtns.add(b);
            layout.addView(b);
        }
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
}
