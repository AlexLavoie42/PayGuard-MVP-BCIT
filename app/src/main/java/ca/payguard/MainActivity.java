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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import ca.payguard.dbUtil.DatabaseController;
import ca.payguard.miscUtil.KeyboardCheck;
import java.util.ArrayList;

/**
 * MainActivity is the main controller class
 * for PayGuard MVP.
 */
public class MainActivity extends AppCompatActivity {
    private final boolean DEBUG_NO_PIN = true;

    private TableSet tableGui;
    public static ArrayList<Button> tblBtns = new ArrayList<>();
    private int tblSize;
    private Fragment popup;
    private Fragment upperPopup;
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

    static boolean queryEditMode;

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
        settingsBtn = findViewById(R.id.settings_btn);

        loading = findViewById(R.id.progressBar);
        loading.setVisibility(View.VISIBLE);

        db.getTableSet(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() == null ||
                        documentSnapshot.getData().get("tableset") == null){
                    tableGui = new TableSet();
                    tableGui.renderStdTableSet();

                    tblBtns = renderTableSet(getBaseContext(), tableGui);
                    tableLayout.removeAllViews();
                    for(Button b : tblBtns)
                        tableLayout.addView(b);
                } else {
                    tableGui = new TableSet((ArrayList) documentSnapshot.getData().get("tableset"));
                    /*tableGui = new TableSet();
                    tableGui.renderStdTableSet();//TODO change back*/

                    tblBtns = renderTableSet(getBaseContext(), tableGui);
                    tableLayout.removeAllViews();
                    for(Button b : tblBtns)
                        tableLayout.addView(b);
                }
                loading.setVisibility(View.GONE);

                if(getIntent().getParcelableExtra("customer") != null
                        && getIntent().getStringExtra("tableNum") != null){
                    tableGui.addCustomer(
                            (Customer) getIntent().getParcelableExtra("customer"),
                            getIntent().getStringExtra("tableNum"));
                    db.updateTableSet(tableGui);

                    tblBtns = renderTableSet(getBaseContext(), tableGui);
                    tableLayout.removeAllViews();
                    for(Button b : tblBtns)
                        tableLayout.addView(b);
                }
            }
        });

        if( !TransactionService.isRunning ){
            // Start service
            Intent intent = new Intent(this, TransactionService.class);
            startService(intent);
        }
        keyboardCheck = new KeyboardCheck((ConstraintLayout) findViewById(R.id.mainLayout));
    }

    @Override
    public void onRestart(){
        super.onRestart();

        if(queryEditMode){
            Intent i = new Intent(getBaseContext(), EditModeActivity.class);
            i.putExtra("tables", tableGui);
            startActivity(i);
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
    public void onBackPressed() {
        if(upperPopup != null && !upperPopup.isDetached())
            closeUpperPopup();
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
        blur.setVisibility(View.VISIBLE);
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
                        pinTopPopup(Payment.class, table);
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
            blur.setVisibility(View.VISIBLE);
            blur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeUpperPopup();
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
            blur.setVisibility(View.VISIBLE);
            blur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closePopup();
                }
            });
        }
    }

    public void pinTopPopup(final Class<? extends AppCompatActivity> activity, final Table table){
        if(DEBUG_NO_PIN){
            Intent intent = new Intent(getBaseContext(), activity);
            intent.putExtra("table", table);
            startActivity(intent);
        } else {
            //Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //Add TableFragment to layout
            upperPopup = EmployeePinFragment.newInstance();
            ((EmployeePinFragment) upperPopup).setConConfirm(new EmployeePinFragment.onConfirmListener() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(getBaseContext(), activity);
                    intent.putExtra("tableNum", table.getLabel());
                    startActivity(intent);
                }
            });
            ft.replace(R.id.upperPopupLayout, upperPopup);
            //Complete changes
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.upperBlur);
            blur.setVisibility(View.VISIBLE);
            blur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeUpperPopup();
                }
            });
        }
    }

    public void pinTopPopup(EmployeePinFragment.onConfirmListener onConfirm){
        if(DEBUG_NO_PIN)
            onConfirm.onSuccess();
        else {
            //Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //Add TableFragment to layout
            upperPopup = EmployeePinFragment.newInstance();
            ((EmployeePinFragment) upperPopup).setConConfirm(onConfirm);
            ft.replace(R.id.upperPopupLayout, upperPopup);
            //Complete changes
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.upperBlur);
            blur.setVisibility(View.VISIBLE);
            blur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeUpperPopup();
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
            blur.setVisibility(View.GONE);
        } else if(keyboardCheck.isKeyboardShowing()){
            hideKeyboard(this);
        }
    }

    public void changeBillPopup(Customer customer, Table table){
        curCust = customer;
        curTable = table;
        pinTopPopup(new EmployeePinFragment.onConfirmListener() {
            @Override
            public void onSuccess() {
                tablePopup(curTable);
                //Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //Add TableFragment to layout
                upperPopup = BillAmountFragment.newInstance(curCust, curTable.getLabel());
                ft.replace(R.id.upperPopupLayout, upperPopup);
                //Complete changes
                ft.commit();
                com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.upperBlur);
                blur.setVisibility(View.VISIBLE);
                blur.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeUpperPopup();
                    }
                });
            }
        });
    }

    public void billCustomerPopup(Customer customer, final Table table){
        curCust = customer;
        pinTopPopup(new EmployeePinFragment.onConfirmListener() {
            @Override
            public void onSuccess() {
                //Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //Add TableFragment to layout
                upperPopup = BillConfirmFragment.newInstance(curCust, table);
                ft.replace(R.id.upperPopupLayout, upperPopup);
                //Complete changes
                ft.commit();
                com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.upperBlur);
                blur.setVisibility(View.VISIBLE);
                blur.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeUpperPopup();
                    }
                });
            }
        });
    }

    public void billAllCustomersPopup(final Table table){
        pinTopPopup(new EmployeePinFragment.onConfirmListener() {
            @Override
            public void onSuccess() {
                //Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                //Add TableFragment to layout
                upperPopup = BillAllConfirmFragment.newInstance(table);
                ft.replace(R.id.upperPopupLayout, upperPopup);
                //Complete changes
                ft.commit();
                com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.upperBlur);
                blur.setVisibility(View.VISIBLE);
                blur.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeUpperPopup();
                    }
                });
            }
        });
    }

    public void closeUpperPopup(){
        if(upperPopup != null && !keyboardCheck.isKeyboardShowing()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.detach(upperPopup);
            ft.commit();
            com.github.mmin18.widget.RealtimeBlurView blur = findViewById(R.id.upperBlur);
            blur.setVisibility(View.GONE);
        } else if(keyboardCheck.isKeyboardShowing()){
            hideKeyboard(this);
        }
    }

    public void billCustomer(Customer customer, Table table){
        if(TransactionService.isRunning ){
            TransactionService.instance.completeTransaction(customer.getOrderID(), "" + customer.getBillTotal());
        }else{
            Intent intent = new Intent(this, TransactionService.class);
            startService(intent);
            TransactionService.instance.completeTransaction(customer.getOrderID(), "" + customer.getBillTotal());
        }
        table.removeCustomer(customer);
        resetTablePopup();
    }

    public void billAllCustomers(Table table) {
        for(Customer customer : table.getAllCustomers()) {
            if (TransactionService.isRunning) {
                TransactionService.instance.completeTransaction(customer.getOrderID(), "" + customer.getBillTotal());
            } else {
                Intent intent = new Intent(this, TransactionService.class);
                startService(intent);
                TransactionService.instance.completeTransaction(customer.getOrderID(), "" + customer.getBillTotal());
            }
        }
        table.removeAllCustomers();
        closeUpperPopup();
        resetTablePopup();
    }

    /** Translates a table set to their buttons. */
    private ArrayList<Button> renderTableSet(final Context c, TableSet tables){
        ArrayList<Button> btns = new ArrayList<>();
        tblSize = (int) Math.max((float) TableSet.STD_WIDTH * getWidthRatio(),
                (float) TableSet.STD_HEIGHT * getHeightRatio()) / 20;

        for(Table t : tables)
            btns.add(renderTblBtn(c, t));

        return btns;
    }

    private Button renderTblBtn(Context c, final Table t){
        final Button b = new Button(c);
        b.setText(t.getLabel());

        //reshape
        if(t.getShape() == Table.Shape.C)
            b.setBackground(getResources().getDrawable(R.drawable.table_round));
        else
            b.setBackground(getResources().getDrawable(R.drawable.table));

        int width, height;

        //resize and rotate if necessary
        if(t.getShape() != Table.Shape.R){
            width = tblSize * t.getSizeMod();
            height = tblSize * t.getSizeMod();
        } else {
            if(t.getRotated()){
                width = tblSize * t.getSizeMod();
                height = tblSize * t.getSizeMod() * 2;
            } else {
                width = tblSize * t.getSizeMod() * 2;
                height = tblSize * t.getSizeMod();
            }
        }

        b.setMinimumWidth(width);
        b.setMinimumHeight(height);
        b.setWidth(width);
        b.setHeight(height);

        b.setX((float) t.getX() * getWidthRatio() + width / 2);
        b.setY((float) t.getY() * getHeightRatio() - height / 3);

        //assign listeners
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tablePopup(t);
            }
        });

        return b;
    }

    public void addCustomer(Customer customer, String tableNum){
        tableGui.addCustomer(customer, tableNum);
        db.updateTableSet(tableGui);
    }

    public void updateCustomer(Customer customer, String tableNum){
        tableGui.updateCustomer(customer, tableNum);
        db.updateTableSet(tableGui);
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
