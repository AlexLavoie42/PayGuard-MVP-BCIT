package ca.payguard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillAmountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillAmountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_customer = "customer";
    private static final String ARG_table = "tableNum";

    // TODO: Rename and change types of parameters
    private Customer cust;
    private String tableNum;

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
    public static BillAmountFragment newInstance(Customer customer, String tableNum) {
        BillAmountFragment fragment = new BillAmountFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_customer, customer);
        args.putString(ARG_table, tableNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cust = getArguments().getParcelable(ARG_customer);
            tableNum = getArguments().getString(ARG_table);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bill_amount, container, false);
        TextView seatNum = view.findViewById(R.id.tv_billTableNum);
        TextView limit = view.findViewById(R.id.tv_billPreAuthLimit);

        seatNum.setText(getResources().getString(R.string.bill_limit, cust.getPreAuthTotal()));
        limit.setText(getResources().getString(R.string.bill_table_num, tableNum));

        view.findViewById(R.id.btn_updateBill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText total = view.findViewById(R.id.et_billAmount);
                if(total.getText().toString().matches("\\b[0-9]+\\b$")) {
                    cust.setBillTotal(Float.parseFloat(total.getText().toString()));
                    ((MainActivity) getActivity()).updateCustomer(cust, tableNum);
                    ((MainActivity) getActivity()).resetTablePopup();
                    ((MainActivity) getActivity()).closeBillPopup();
                } else {
                    Toast.makeText(getContext(), "Enter new bill amount.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return view;
    }
}
