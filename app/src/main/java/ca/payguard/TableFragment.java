package ca.payguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ca.payguard.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableFragment extends Fragment {
    private static final String ARG_TABLE = "table";

    private Table table;

    public TableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param table Table Object.
     * @return A new instance of fragment TableFragment.
     */
    public static TableFragment newInstance(Table table) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TABLE, table);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            table = getArguments().getParcelable(ARG_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_table, container, false);
        TextView header = root.findViewById(R.id.tv_tableHeaderText);
        header.setText(root.getResources().getString(R.string.tableHeader,
                table.getLabel()));
        displayCustomers(root);
        return root;
    }

    public void displayCustomers(View root){
        ListView listView = root.findViewById(R.id.layout_seats);
        CustomerAdapter adapter = new CustomerAdapter(
                table.getAllCustomers(), table, (MainActivity) getActivity(), getContext());
        listView.setAdapter(adapter);
    }

    /*public void displayCustomers(View root) {
        View layout = root.findViewById(R.id.layout_seats);
        ((ViewGroup) layout).removeAllViews();
        if(table.getAllCustomers() != null) {
            Button processTable = root.findViewById(R.id.closeTableButton);
            processTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    billAll();
                }
            });
            for (Customer c : table.getAllCustomers()) {
                View custView = LayoutInflater.from(root.getContext()).inflate(R.layout.layout_customer,
                        (ViewGroup) layout, false);

                TextView custText = custView.findViewById(R.id.tv_seatLabel);
                custText.setText(getResources().getString(R.string.seatInfoText, c.getId(),
                        c.getPreAuthTotal(), c.getBillTotal()));

                Button addBill = custView.findViewById(R.id.btn_addBill);
                final Customer cRef = c;
                addBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).billPopup(cRef, table);
                    }
                });
                Button closeBill = custView.findViewById(R.id.btn_closeCustomer);
                closeBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).billCustomer(cRef);
                    }
                });

                ((ViewGroup) layout).addView(custView);
            }
        }
    }*/

    public void billAll(){
        if(table.getAllCustomers() != null) {
            for (Customer c : table.getAllCustomers()) {
                TransactionService.instance.completeTransaction(c.getOrderID(), "" + c.getBillTotal());
            }
        }
    }
}
