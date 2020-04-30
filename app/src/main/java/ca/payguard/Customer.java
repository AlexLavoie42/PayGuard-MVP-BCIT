package ca.payguard;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

    //Customer data
    private int id;
    private float billTotal;
    private float preAuthTotal;
    private float tipAmount;
    private Table table;
    private int phoneNum;

    public Customer(int id, Table table) {
        this.id = id;
        this.billTotal = 0;
        this.tipAmount = 0;
        this.table = table;
        this.phoneNum = 0;
    }

    protected Customer(Parcel in) {
        id = in.readInt();
        billTotal = in.readFloat();
        preAuthTotal = in.readFloat();
        tipAmount = in.readFloat();
        phoneNum = in.readInt();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public int getId() {
        return id;
    }

    public float getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(float billTotal) {
        this.billTotal = billTotal;
    }


    public Table getTable() {
        return table;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public float getPreAuthTotal() {
        return preAuthTotal;
    }

    public void setPreAuthTotal(float preAuthTotal) {
        this.preAuthTotal = preAuthTotal;
    }

    public float getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(float tipAmount) {
        this.tipAmount = tipAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeFloat(billTotal);
        dest.writeFloat(preAuthTotal);
        dest.writeFloat(tipAmount);
        dest.writeInt(phoneNum);
    }
}
