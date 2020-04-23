package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private TableSet tableGui;
    private ArrayList<Button> tblBtns = new ArrayList<>();

    public static boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //put settings in top right

        //loads the table objects
        tableGui = new TableSet();
        tableGui.load();

        displayTables();
    }

    //TODO doesn't seem to activate? supposed to function on orientation change
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        displayTables();

        /* Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }*/
    }

    public void launchSettings(View v){
        Intent myIntent = new Intent(getBaseContext(), SettingsActivity.class);
        startActivity(myIntent);
    }

    /** Creates new TableFragment as overlay */
    private void tablePopup(String label){
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Add TableFragment to layout
        TableFragment popup = TableFragment.newInstance(label);
        ft.replace(R.id.popupLayout, popup);
        //Complete changes
        ft.commit();
    }

    /** Adjusts the buttons onto the screen. */
    private void displayTables(){
        //gets the layout tag from xml
        ConstraintLayout layout = findViewById(R.id.buttonLayout);

        /*
        Create a button for each table and use it's coordinates
        to adjust it to the tablet's screen size.
         */
        float wRatio = (float) getScreenWidth() / (float) TableSet.STD_WIDTH;
        float hRatio = (float) getScreenHeight() / (float) TableSet.STD_HEIGHT;
        for(int i = 0; i < tableGui.size(); i++){
            final Table t = tableGui.get(i);

            Button b = new Button(this);
            b.setText("W:" + t.getWidth() + "\nNW:" + (int)((float) t.getWidth() * wRatio));
            //adjust the button's dimensions to screen size
            b.setWidth((int)((float) t.getWidth() * wRatio));
            b.setHeight((int)((float) t.getHeight() * hRatio));
            b.setId(i+1);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tablePopup(t.getLabel());
                }
            });

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
        ConstraintLayout layout = findViewById(R.id.buttonLayout);

        for(final Button layoutBtn : tblBtns){
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
