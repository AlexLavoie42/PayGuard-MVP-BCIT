package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import ca.payguard.dbUtil.DatabaseController;
import ca.payguard.editMode.EditMode;
import ca.payguard.paymentUtil.Transaction;
import ca.payguard.paymentUtil.TransactionService;
import ca.payguard.paymentUtil.TransactionViewModel;

import java.util.ArrayList;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private TableSet tableGui;
    public static ArrayList<Button> tblBtns = new ArrayList<>();
    private Fragment popup;
    private Fragment billPopup;

    public DatabaseController getDb() {
        return db;
    }

    private DatabaseController db;
    private ProgressBar loading;
    private Customer curCust;
    private Table curTable;

    //TODO: Change these to match any updates to architecture
    private TransactionViewModel mViewModel;
    private TransactionService mService;

    public static ConstraintLayout tableLayout;
    public static ImageButton settingsBtn;

    static EditMode editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: SERVICE SHIT
        mViewModel = new TransactionViewModel();
        mViewModel.getBinder().observe(this, new Observer<TransactionService.MyBinder>() {
            @Override
            public void onChanged(TransactionService.MyBinder myBinder) {
                if(myBinder != null){
                    Log.d("Service:", "OnChanged: connected to service");
                    mService = myBinder.getService();
                }else{
                    Log.d("Service:", "OnChanged: unbound from service");
                    mService = null;
                }
            }
        });

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

        loading = findViewById(R.id.progressBar);
        loading.setVisibility(View.VISIBLE);

        db.getTableSet(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() == null ||
                        documentSnapshot.getData().get("tableset") == null){
                    tableGui = new TableSet();
                    tableGui.load();
                    enableEditMode();
                    editMode.applyStdArrangement();
                    disableEditMode();
                } else {
                    tableGui = new TableSet((ArrayList) documentSnapshot.getData().get("tableset"));
                    editMode.renderTableSet(tableGui);
                }
                loading.setVisibility(View.GONE);

                if(getIntent().getParcelableExtra("customer") != null
                        && getIntent().getStringExtra("tableNum") != null){
                    tableGui.addCustomer(
                            (Customer) getIntent().getParcelableExtra("customer"),
                            getIntent().getStringExtra("tableNum"));
                    db.updateTableSet(tableGui);
                    editMode.renderTableSet(tableGui);
                }
            }
        });

        editMode.setRatios(getWidthRatio(), getHeightRatio());
        editMode.enableExternalTools(constraintLayout, this);
        disableEditMode();
    }

    //TODO: Service might not need this
    private void bindService(){
        Intent serviceIntent = new Intent(this, TransactionService.class);
        bindService(serviceIntent, mViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

//    /* Crashes app
    @Override
    protected void onResume(){
        super.onResume();

//        Intent i = getIntent();
//
//        if(i.getStringExtra("edit_mode").equals("e"))
//            enableEditMode();
//        else
//            disableEditMode();

        Intent serviceIntent = new Intent(this, TransactionService.class);
        startService(serviceIntent);
        //TODO: Service might not need this.
        bindService();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //TODO: Service might not need this
        if(mViewModel.getBinder() != null){
            unbindService(mViewModel.getServiceConnection());
        }
    }

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
        if(billPopup != null && !billPopup.isDetached())
            closeBillPopup();
        else closePopup();
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

    public void resetTablePopup(){
        ((TableFragment)popup).displayCustomers(findViewById(R.id.tableFragment));
    }

    public void pinPopup(Class<? extends AppCompatActivity> activity, Table table){
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Add TableFragment to layout
        billPopup = EmployeePinFragment.newInstance(activity, table);
        ft.replace(R.id.billPopupLayout, billPopup);
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.billBlur);
        blur.setBlurRadius(6);
        blur.setAlpha(0.8f);
        blur.setOverlayColor(1);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBillPopup();
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

    public void pinBillPopup(){
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Add TableFragment to layout
        billPopup = EmployeePinFragment.newBillInstance();
        ft.replace(R.id.billPopupLayout, billPopup);
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.billBlur);
        blur.setBlurRadius(6);
        blur.setAlpha(0.8f);
        blur.setOverlayColor(1);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBillPopup();
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

    public void billPopup(Customer customer, Table table){
        curCust = customer;
        curTable = table;
        pinBillPopup();
    }

    /** I hate this I'm sorry */
    public void billOnSuccess() {
        tablePopup(curTable);
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Add TableFragment to layout
        billPopup = BillAmountFragment.newInstance(curCust, curTable.getLabel());
        ft.replace(R.id.billPopupLayout, billPopup);
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.billBlur);
        blur.setBlurRadius(6);
        blur.setAlpha(0.8f);
        blur.setOverlayColor(1);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBillPopup();
            }
        });
    }

    public void closeBillPopup(){
        if(billPopup != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.detach(billPopup);
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.billBlur);
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

    public void addCustomer(Customer customer, String tableNum){
        tableGui.addCustomer(customer, tableNum);
        db.updateTableSet(tableGui);
    }

    public void updateCustomer(Customer customer, String tableNum){
        tableGui.updateCustomer(customer, tableNum);
        db.updateTableSet(tableGui);
    }

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
