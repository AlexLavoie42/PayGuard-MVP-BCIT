package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import ca.payguard.editMode.EditMode;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private TableSet tableGui;
    private ArrayList<Button> tblBtns = new ArrayList<>();
    private Fragment popup;

    EditMode editMode;

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

        //init edit mode toolbar and add it to view
        editMode = new EditMode(this);
        editMode.setVisibility(View.GONE);
        ConstraintLayout constraintLayout = (ConstraintLayout) layout;
        constraintLayout.addView(editMode);

        displayTables();

        enableEditMode();
        editMode.setRatios(getWidthRatio(), getHeightRatio());
    }

    /* Crashes app
    @Override
    protected void onResume(){
        super.onStart();

        Intent i = getIntent();

        if(i.getStringExtra("edit_mode").equals("e"))
            enableEditMode();
        else
            disableEditMode();
    } */

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
        /*
        Create a button for each table and use it's coordinates
        to adjust it to the tablet's screen size.
         */
        float wRatio = getWidthRatio();
        float hRatio = getHeightRatio();
        editMode.setSize(Math.max(TableSet.STD_WIDTH / 20, TableSet.STD_HEIGHT / 20));
        for(int i = 0; i < tableGui.size(); i++){
            final Table t = tableGui.get(i);

            final Button b = new Button(this);
            b.setText(t.getLabel());
            b.setBackgroundColor(getResources().getColor(R.color.brightGreen));
            b.setId(i+1);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!editMode.isActive())
                        tablePopup(t.getLabel());
                    else
                        editMode.select(b);
                }
            });

            b.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    editMode.buttonTouched(b, event);
                    return false;
                }
            });

            b.setX((float) t.getX() * wRatio);
            b.setY((float) t.getY() * hRatio);

            editMode.shapeSelect.applyRectangle(b);
            if(i == tableGui.size() - 1){
                //resize and rotate bar table
            }

            addButton(b);
        }
    }

    /** Verifies if the button is already in layout view or not. */
    private void addButton(Button b){
        //gets the layout tag from xml
        ConstraintLayout layout = findViewById(R.id.tableLayout);

        for(final Button layoutBtn : tblBtns){
            //if button is already in layout, update said button
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
        editMode.enable((float) getScreenWidth(), 250, tableGui);
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

    public float getWidthRatio(){
        return (float) getScreenWidth() / (float) TableSet.STD_WIDTH;
    }

    public float getHeightRatio(){
        return (float) getScreenHeight() / (float) TableSet.STD_HEIGHT;
    }
}
