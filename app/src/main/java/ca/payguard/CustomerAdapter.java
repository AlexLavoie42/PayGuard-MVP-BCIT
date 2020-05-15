package ca.payguard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerAdapter extends ArrayAdapter<Customer> {
    private Table table;
    private ArrayList<Customer> customers;
    private MainActivity mainActivity;
    private Context context;

    public CustomerAdapter(ArrayList<Customer> customers, Table table, MainActivity mainActivity, Context context) {
        super(context, -1, customers);
        this.mainActivity = mainActivity;
        this.context = context;
        this.customers = customers;
        this.table = table;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View custView = inflater.inflate(R.layout.layout_customer, parent, false);

            TextView custText = custView.findViewById(R.id.tv_seatLabel);
            custText.setText(context.getResources().getString(R.string.seatInfoText, customers.get(position).getId(),
                    customers.get(position).getPreAuthTotal(), customers.get(position).getBillTotal()));

            Button addBill = custView.findViewById(R.id.btn_addBill);
            final Customer cRef = customers.get(position);
            addBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.changeBillPopup(cRef, table);
                }
            });
            Button closeBill = custView.findViewById(R.id.btn_closeCustomer);
            closeBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.billCustomer(cRef);
                }
            });

            convertView = custView;
        }
        return convertView;
    }
}
