package ca.payguard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.payguard.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillAmountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillAmountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_customer = "customer";

    // TODO: Rename and change types of parameters
    private Customer cust;

    public BillAmountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param customer Customer object.
     * @return A new instance of fragment BillAmountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillAmountFragment newInstance(Customer customer) {
        BillAmountFragment fragment = new BillAmountFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_customer, customer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cust = getArguments().getParcelable(ARG_customer);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_amount, container, false);
    }
}
