package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.payguard.R;

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

        //loads the table objects
        tableGui = new TableSet();
        tableGui.load();

        displayTables();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        displayTables();

        /* Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }*/
    }

    /** Adjusts the buttons onto the screen. */
    private void displayTables(){
        //gets the layout tag from xml
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.mainLayout);

        /*
        Create a button for each table and use it's coordinates
        to adjust it to the tablet's screen size.
         */
        float wRatio = (float) getScreenWidth() / (float) TableSet.STD_WIDTH;
        float hRatio = (float) getScreenHeight() / (float) TableSet.STD_HEIGHT;
        for(int i = 0; i < tableGui.size(); i++){
            Table t = tableGui.get(i);

            Button b = new Button(this);
            b.setText(t.getLabel());
            b.setWidth(t.getWidth());
            b.setHeight(t.getHeight());
            b.setId(i+1);

            /* t.getWidth()/2 & t.getHeight()/2 centers the table at it's coordinate
            rather than starting at the specified coordinate. */
            b.setX((float) t.getX() * wRatio - t.getWidth() / 2);
            b.setY((float) t.getY() * hRatio - t.getHeight() / 2);

            addButton(b);
        }
    }

    /** Verifies if the button is already in layout view or not. */
    private void addButton(Button b){
        //gets the layout tag from xml
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.mainLayout);

        for(Button layoutBtn : tblBtns){
            //if button is already in layout, match layout button to specified button
            if(b.getId() == layoutBtn.getId()){
                layoutBtn.setX(b.getX());
                layoutBtn.setY(b.getY());
                layoutBtn.setWidth(b.getWidth());
                layoutBtn.setHeight(b.getHeight());
                layoutBtn.setText(b.getText());
                return;
            }
        }

        //if not found, add the button to layout
        tblBtns.add(b);
        layout.addView(b);
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
