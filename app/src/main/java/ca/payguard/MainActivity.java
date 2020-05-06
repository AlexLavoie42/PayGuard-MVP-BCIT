package ca.payguard;

import androidx.annotation.Nullable;
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
    public static ArrayList<Button> tblBtns = new ArrayList<>();
    private Fragment popup;

    public static ConstraintLayout tableLayout;

    EditMode editMode;
    public static Button settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loads the table objects
        tableGui = new TableSet();
        tableGui.load();

        //Set the main layout on click to close any open popups.
        View layout = findViewById(R.id.mainLayout);
        tableLayout = findViewById(R.id.tableLayout);
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

        editMode.setSize(Math.max(TableSet.STD_WIDTH / 20, TableSet.STD_HEIGHT / 20));
        settingsBtn = findViewById(R.id.settings_btn);

        enableEditMode();
        editMode.setRatios(getWidthRatio(), getHeightRatio());
        editMode.enableExternalTools(constraintLayout, this);
        editMode.applyStdArrangement();//creates std set of tables
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Intent i = getIntent();

        if(i.getStringExtra("edit_mode").equals("e"))
            enableEditMode();
        super.onActivityResult(requestCode, resultCode, data);
    }

    //TODO doesn't seem to activate? supposed to function on orientation change
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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
    public void tablePopup(String label){
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

    static void setTblListeners(Button b){

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
