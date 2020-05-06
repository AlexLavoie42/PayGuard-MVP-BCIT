package ca.payguard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreAuthAmountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreAuthAmountFragment extends Fragment {

    public PreAuthAmountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreAuthAmountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreAuthAmountFragment newInstance() {
        PreAuthAmountFragment fragment = new PreAuthAmountFragment();
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
        View view = inflater.inflate(R.layout.fragment_pre_auth_amount, container, false);
        view.findViewById(R.id.btn_submitCustom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Payment paymentView = (Payment)getActivity();
                paymentView.forwardAmount(((EditText)paymentView.findViewById(R.id.et_customAmount)).getText().toString());
            }
        });
        return view;
    }
}
