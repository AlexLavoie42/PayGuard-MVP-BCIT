package ca.payguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillAllConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillAllConfirmFragment extends Fragment {

    private Table table;

    public BillAllConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BillConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillAllConfirmFragment newInstance(Table table) {
        BillAllConfirmFragment billConfirmFragment = new BillAllConfirmFragment();
        Bundle args = new Bundle();
        args.putParcelable("table", table);
        billConfirmFragment.setArguments(args);
        return billConfirmFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        table = getArguments().getParcelable("table");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_bill_confirm, container, false);
        if(getActivity() != null && getActivity().getClass().equals(MainActivity.class)){
            ((TextView)inflate.findViewById(R.id.tv_endBill)).setText(getResources()
                    .getString(R.string.endBillAllConfirm, table.getLabel()));
            inflate.findViewById(R.id.btn_confirmEndBill).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).billAllCustomers(table);
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
