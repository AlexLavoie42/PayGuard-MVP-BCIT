package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import ca.payguard.dbUtil.DatabaseController;
import ca.payguard.editMode.EditMode;
import ca.payguard.miscUtil.KeyboardCheck;

import java.util.ArrayList;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private final boolean DEBUG_NO_PIN = false;

    private TableSet tableGui;
    public static ArrayList<Button> tblBtns = new ArrayList<>();
    private Fragment popup;
    private Fragment billPopup;
    private KeyboardCheck keyboardCheck;

    public DatabaseController getDb() {
        return db;
    }

    private DatabaseController db;
    private ProgressBar loading;
    private Customer curCust;
    private Table curTable;
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
        editMode.setRatios(getWidthRatio(), getHeightRatio());
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

        editMode.enableExternalTools(constraintLayout, this);
        if( !TransactionService.isRunning ){
            // Start service
            Intent intent = new Intent(this, TransactionService.class);
            startService(intent);
        }
        keyboardCheck = new KeyboardCheck(constraintLayout);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(editMode.getActive()) {
            enableEditMode();
        } else {
            disableEditMode();
        }
    }

    @Override
    public void onBackPressed() {
        if(billPopup != null && !billPopup.isDetached())
            closeBillPopup();
        else closePopup();
    }

    public void launchSettings(View v){
        pinPopup(new EmployeePinFragment.onConfirmListener() {
            @Override
            public void onSuccess() {
                closePopup();
                Intent myIntent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(myIntent);
            }
        });
    }

    /** Creates new TableFragment as overlay */
    public void tablePopup(final Table table){
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Add TableFragment to layout
        boolean right = table.getX()*getWidthRatio() < getScreenWidth()/2;
        popup = TableFragment.newInstance(table, right);
        ft.replace(R.id.popupLayout, popup);
        //Complete changes
        ft.commit();
        com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
        setBlur(blur);
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
                        pinBillPopup(Payment.class, table);
                    }
                });
            }
        });
    }

    public void resetTablePopup(){
        ((TableFragment)popup).displayCustomers(findViewById(R.id.tableFragment));
    }

    public void pinPopup(final Class<? extends AppCompatActivity> activity, final Table table){
        if(DEBUG_NO_PIN){
            Intent intent = new Intent(getBaseContext(), activity);
            intent.putExtra("table", table);
            startActivity(intent);
        } else {

            //Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //Add TableFragment to layout
            popup = EmployeePinFragment.newInstance();
            ((EmployeePinFragment) popup).setConConfirm(new EmployeePinFragment.onConfirmListener() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(getBaseContext(), activity);
                    intent.putExtra("table", table);
                    startActivity(intent);
                }
            });
            ft.replace(R.id.popupLayout, popup);
            //Complete changes
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
            setBlur(blur);
            blur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeBillPopup();
                }
            });
        }
    }

    public void pinPopup(EmployeePinFragment.onConfirmListener onConfirm){
        if(DEBUG_NO_PIN)
            onConfirm.onSuccess();
        else {
            //Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //Add TableFragment to layout
            popup = EmployeePinFragment.newInstance();
            ((EmployeePinFragment) popup).setConConfirm(onConfirm);
            ft.replace(R.id.popupLayout, popup);
            //Complete changes
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
            setBlur(blur);
            blur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closePopup();
                }
            });
        }
    }

    public void pinBillPopup(final Class<? extends AppCompatActivity> activity, final Table table){
        if(DEBUG_NO_PIN){
            Intent intent = new Intent(getBaseContext(), activity);
            intent.putExtra("table", table);
            startActivity(intent);
        } else {
            //Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //Add TableFragment to layout
            billPopup = EmployeePinFragment.newInstance();
            ((EmployeePinFragment) billPopup).setConConfirm(new EmployeePinFragment.onConfirmListener() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(getBaseContext(), activity);
                    intent.putExtra("table", table);
                    startActivity(intent);
                }
            });
            ft.replace(R.id.billPopupLayout, billPopup);
            //Complete changes
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.billBlur);
            setBlur(blur);
            blur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeBillPopup();
                }
            });
        }
    }

    public void pinBillPopup(EmployeePinFragment.onConfirmListener onConfirm){
        if(DEBUG_NO_PIN)
            onConfirm.onSuccess();
        else {
            //Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //Add TableFragment to layout
            billPopup = EmployeePinFragment.newInstance();
            ((EmployeePinFragment) billPopup).setConConfirm(onConfirm);
            ft.replace(R.id.billPopupLayout, billPopup);
            //Complete changes
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.billBlur);
            setBlur(blur);
            blur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeBillPopup();
                }
            });
        }
    }

    /** Closes any open popups */
    private void closePopup(){
        //Detach current popup if it exists
        if(popup != null && !keyboardCheck.isKeyboardShowing()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.detach(popup);
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.blur);
            blur.setBlurRadius(0);
            blur.setAlpha(0);
            blur.setClickable(false);
        } else if(keyboardCheck.isKeyboardShowing()){
            hideKeyboard(this);
        }
    }

    public void billPopup(Customer customer, Table table){
        curCust = customer;
        curTable = table;
        pinBillPopup(new EmployeePinFragment.onConfirmListener() {
            @Override
            public void onSuccess() {
                tablePopup(curTable);
                //Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //Add TableFragment to layout
                billPopup = BillAmountFragment.newInstance(curCust, curTable.getLabel());
                ft.replace(R.id.billPopupLayout, billPopup);
                //Complete changes
                ft.commit();
                com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.billBlur);
                setBlur(blur);
                blur.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeBillPopup();
                    }
                });
            }
        });
    }

    public void closeBillPopup(){
        if(billPopup != null && !keyboardCheck.isKeyboardShowing()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.detach(billPopup);
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.billBlur);
            blur.setBlurRadius(0);
            blur.setAlpha(0);
            blur.setClickable(false);
        } else if(keyboardCheck.isKeyboardShowing()){
            hideKeyboard(this);
        }
    }

    public void setBlur(RealtimeBlurView blur){
        blur.setBlurRadius(0.1f);
        blur.setAlpha(0.45f);
        blur.setOverlayColor(R.color.blurColor);
    }

    public void billCustomer(Customer customer){
        if(TransactionService.isRunning ){
            TransactionService.instance.completeTransaction(customer.getOrderID(), "" + customer.getBillTotal());
        }else{
            Intent intent = new Intent(this, TransactionService.class);
            startService(intent);
            TransactionService.instance.completeTransaction(customer.getOrderID(), "" + customer.getBillTotal());
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
        editMode.enable(tableGui);
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
