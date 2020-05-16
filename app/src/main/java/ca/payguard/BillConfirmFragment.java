package ca.payguard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ca.payguard.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillConfirmFragment extends Fragment {

    private Customer customer;
    private Table table;

    public BillConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BillConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillConfirmFragment newInstance(Customer customer, Table table) {
        BillConfirmFragment billConfirmFragment = new BillConfirmFragment();
        Bundle args = new Bundle();
        args.putParcelable("customer", customer);
        args.putParcelable("table", table);
        billConfirmFragment.setArguments(args);
        return billConfirmFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        customer = getArguments().getParcelable("customer");
        table = getArguments().getParcelable("table");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_bill_confirm, container, false);
        if(getActivity() != null && getActivity().getClass().equals(MainActivity.class)){
            ((TextView)inflate.findViewById(R.id.tv_endBill)).setText(getResources()
                    .getString(R.string.endBillConfirm, customer.getId(), customer.getBillTotal()));
            inflate.findViewById(R.id.btn_confirmEndBill).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).billCustomer(customer, table);
                }
            });
            inflate.findViewById(R.id.btn_declineEndBill).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).closeUpperPopup();
                }
            });
        } else throw new RuntimeException("Bill Confirm View used outside of MainActivity");
        return inflate;
    }
}
