package ca.payguard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ca.payguard.dbUtil.DatabaseController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeePinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeePinFragment extends Fragment {

    private Class<? extends AppCompatActivity> activity;
    private Table table;
    private boolean bill;

    public EmployeePinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment and send a table in the intent.
     *
     * TODO: Make parameters to accept any kind and number of parcelables.
     *
     * @param activity Activity Class that will be opened after pin.
     * @param table Table object to be sent to activity.
     * @return A new instance of fragment EmployeePinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeePinFragment newInstance(Class<? extends AppCompatActivity> activity,
                                                  Parcelable table) {
        EmployeePinFragment fragment = new EmployeePinFragment();
        Bundle args = new Bundle();
        args.putParcelable("table", table);
        args.putString("activity", activity.getCanonicalName());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment with a target activity.
     *
     * @param activity Activity Class that will be opened after pin.
     * @return A new instance of fragment EmployeePinFragment.
     */
    public static EmployeePinFragment newInstance(Class<? extends AppCompatActivity> activity) {
        EmployeePinFragment fragment = new EmployeePinFragment();
        Bundle args = new Bundle();
        args.putString("activity", activity.getCanonicalName());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment with a target activity.
     *
     * @return A new instance of fragment EmployeePinFragment.
     */
    public static EmployeePinFragment newBillInstance() {
        EmployeePinFragment fragment = new EmployeePinFragment();
        Bundle args = new Bundle();
        args.putBoolean("fragment", true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().getString("activity") != null){
            try {
                activity = (Class<? extends AppCompatActivity>)
                        Class.forName(getArguments().getString("activity"));
                table = getArguments().getParcelable("table");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else
            bill = getArguments().getBoolean("fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_pin, container, false);
        final View pin = view.findViewById(R.id.et_pin);
        view.findViewById(R.id.btn_pinSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(table != null){
                    new DatabaseController().checkPin((EditText) pin, new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), activity);
                            intent.putExtra("tableNum", table.getLabel());
                            startActivity(intent);
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Invalid Pin", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if(activity != null){
                    new DatabaseController().checkPin((EditText) pin, new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getContext(), activity));
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Invalid Pin", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if(bill){
                    ((MainActivity)getActivity()).getDb().checkPin((EditText) pin, new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)getActivity()).billOnSuccess();
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Invalid Pin", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

}
