package ca.payguard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableFragment extends Fragment {
    private static final String ARG_TABLE = "table";

    private Table table;
    private boolean isRight;

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
    public static TableFragment newInstance(Table table, boolean right) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TABLE, table);
        args.putBoolean("right", right);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            table = getArguments().getParcelable(ARG_TABLE);
            isRight = getArguments().getBoolean("right");
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
        if(isRight)
            ((LinearLayout)container).setGravity(Gravity.END);
        else
            ((LinearLayout)container).setGravity(Gravity.START);
        displayCustomers(root);
        return root;
    }

    public void displayCustomers(View root){
        ListView listView = root.findViewById(R.id.layout_seats);
        CustomerAdapter adapter = new CustomerAdapter(
                table.getAllCustomers(), table, (MainActivity) getActivity(), getContext());
        listView.setAdapter(adapter);
    }

    public void billAll(){
        if(table.getAllCustomers() != null) {
            for (Customer c : table.getAllCustomers()) {
                TransactionService.instance.completeTransaction(c.getOrderID(), "" + c.getBillTotal());
            }
        }
    }
}
