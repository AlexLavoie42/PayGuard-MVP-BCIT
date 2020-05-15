package ca.payguard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

    public interface onConfirmListener {
        public void onSuccess();
    }

    private onConfirmListener onConfirm;

    public void setConConfirm(onConfirmListener listener){
        onConfirm = listener;
    }

    public EmployeePinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment and send a table in the intent.
     *
     * @return A new instance of fragment EmployeePinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeePinFragment newInstance() {
        EmployeePinFragment fragment = new EmployeePinFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_pin, container, false);
        final View pin = view.findViewById(R.id.et_pin);
        pin.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        view.findViewById(R.id.btn_pinSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onConfirm != null){
                    new DatabaseController().checkPin((EditText) pin, new Runnable() {
                        @Override
                        public void run() {
                            onConfirm.onSuccess();
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Invalid Pin", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            }
        });
        return view;
    }

    /*@Override
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
    }*/
}
