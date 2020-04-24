package ca.payguard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private TableSet tableGui;
    private ArrayList<Button> tblBtns = new ArrayList<>();
    private Fragment popup;

    //edit mode info
    GridLayout EMToolbar;
    Button garbage;
    boolean editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loads the table objects
        tableGui = new TableSet();
        tableGui.load();

        //Set the main layout on click to close any open popups.
        View layout = findViewById(R.id.mainLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup();
            }
        });

        displayTables();

        //enableEditMode();//TODO delete
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

    @Override
    public void onBackPressed() {
        closePopup();
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
        popup = TableFragment.newInstance(label);
        ft.replace(R.id.popupLayout, popup);
        //Complete changes
        ft.commit();
    }

    /** Closes any open popups */
    private void closePopup(){
        //Detach current popup if it exists
        if(popup != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.detach(popup);
            ft.commit();
        }
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
            b.setText(t.getLabel());
            //adjust the button's dimensions to screen size
            b.setWidth((int)((float)t.getWidth() * wRatio));
            b.setHeight((int)((float)t.getHeight() * hRatio));
            b.setId(i+1);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tablePopup(t.getLabel());
                }
            });

            /* t.getWidth()/2 & t.getHeight()/2 centers the table at it's coordinate
            rather than starting at the specified coordinate. */
            b.setX((float) t.getX() * wRatio
                - (int)((float)t.getWidth() * wRatio) / 2);//b.getWidth() returns 0?
            b.setY((float) t.getY() * hRatio
                - (int)((float)t.getHeight() * hRatio) / 2);//b.getHeight returns 0?

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

    public void enableEditMode(){
        if(editMode)
            return;

        getSupportActionBar().hide();

        EMToolbar = new GridLayout(this);
        EMToolbar.setBackgroundColor(getResources().getColor(R.color.brightGreen));
        EMToolbar.setRowCount(1);
        EMToolbar.setColumnCount(4);

        Button addBtn = new Button(this);
        EditText nameInput = new EditText(this);
        Spinner shapeSelect = new Spinner(this);
        Button exitBtn = new Button(this);

        EMToolbar.addView(addBtn);
        EMToolbar.addView(nameInput);
        EMToolbar.addView(shapeSelect);
        EMToolbar.addView(exitBtn);

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.mainLayout);
        layout.addView(EMToolbar);
        editMode = true;
    }

    public void disableEditMode(){
        if(!editMode)
            return;

        getSupportActionBar().show();

        editMode = false;
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
