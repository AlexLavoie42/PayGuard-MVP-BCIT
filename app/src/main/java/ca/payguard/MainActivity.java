package ca.payguard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import ca.payguard.dbUtil.DatabaseController;
import ca.payguard.editMode.EditMode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private TableSet tableGui;
    public static ArrayList<Button> tblBtns = new ArrayList<>();
    private Fragment popup;
    private DatabaseController db;

    public static ConstraintLayout tableLayout;
    public static ImageButton settingsBtn;

    static EditMode editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            db = new DatabaseController();
        } catch (DatabaseController.AuthNotFoundError e) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }

        //Set the main layout on click to close any open popups.
        tableLayout = findViewById(R.id.tableLayout);

        //init edit mode toolbar and add it to view
        editMode = new EditMode(this);
        editMode.setVisibility(View.GONE);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        constraintLayout.addView(editMode);

        editMode.setSize(Math.max(TableSet.STD_WIDTH / 20, TableSet.STD_HEIGHT / 20));
        settingsBtn = findViewById(R.id.settings_btn);

        db.getTableSet(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tableGui = new TableSet((ArrayList)documentSnapshot.getData().get("tableset"));
                editMode.renderTableSet(tableGui);
            }
        });
        editMode.setRatios(getWidthRatio(), getHeightRatio());
        editMode.enableExternalTools(constraintLayout, this);
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

    @Override
    protected void onRestart(){
        super.onRestart();
        if(editMode.getActive()) {
            enableEditMode();
        } else {
            disableEditMode();
        }
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
    public void tablePopup(final Table table){
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Add TableFragment to layout
        popup = TableFragment.newInstance(table);
        ft.replace(R.id.popupLayout, popup);
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
        blur.setBlurRadius(6);
        blur.setAlpha(0.8f);
        blur.setOverlayColor(1);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup();
            }
        });
        ft.runOnCommit(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.btn_addCustomer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pinPopup(Payment.class, table);
                    }
                });
            }
        });
    }

    public void pinPopup(Class<? extends AppCompatActivity> activity, Table table){
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Add TableFragment to layout
        popup = EmployeePinFragment.newInstance(activity, table);
        ft.replace(R.id.popupLayout, popup);
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
        blur.setBlurRadius(6);
        blur.setAlpha(0.8f);
        blur.setOverlayColor(1);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup();
            }
        });
    }

    public void pinPopup(Class<? extends AppCompatActivity> activity){
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Add TableFragment to layout
        popup = EmployeePinFragment.newInstance(activity);
        ft.replace(R.id.popupLayout, popup);
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
        blur.setBlurRadius(6);
        blur.setAlpha(0.8f);
        blur.setOverlayColor(1);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup();
            }
        });
    }

    /** Closes any open popups */
    private void closePopup(){
        //Detach current popup if it exists
        if(popup != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.detach(popup);
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
            blur.setBlurRadius(0);
            blur.setAlpha(0);
            blur.setClickable(false);
        }
    }

    /** Verifies if the button is already in layout view or not. */
    /*private void addButton(Button b){
        //gets the layout tag from xml
        ConstraintLayout layout = findViewById(R.id.buttonLayout);

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
    }*/

    public void enableEditMode(){
        editMode.enable((float) getScreenWidth(), 250, tableGui);
        editMode.setVisibility(View.VISIBLE);
        editMode.garbage.setVisibility(View.VISIBLE);
    }

    public static void disableEditMode(){
        editMode.disable();
        editMode.garbage.setVisibility(View.GONE);
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
