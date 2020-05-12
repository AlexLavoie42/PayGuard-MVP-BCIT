package ca.payguard;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Customer implements Parcelable {

    //Customer data
    private long id;
    private double billTotal;
    private double preAuthTotal;
    private double tipAmount;
    private long phoneNum;

    public Customer() {
        //TODO: Auto generate ID
        this.id = 0;
        this.billTotal = 0;
        this.tipAmount = 0;
        this.phoneNum = 0;
    }

    public Customer(Map m){
        id = (long)m.get("id");
        billTotal = (double)m.get("billTotal");
        preAuthTotal = (double)m.get("preAuthTotal");
        tipAmount = (double)m.get("tipAmount");
        phoneNum = (long)m.get("phoneNum");
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

    public long getId() {
        return id;
    }

    public double getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(float billTotal) {
        this.billTotal = billTotal;
    }


    public long getPhoneNum() {
        return phoneNum;
    }

    public double getPreAuthTotal() {
        return preAuthTotal;
    }

    public void setPreAuthTotal(float preAuthTotal) {
        this.preAuthTotal = preAuthTotal;
    }

    public double getTipAmount() {
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
        dest.writeLong(id);
        dest.writeDouble(billTotal);
        dest.writeDouble(preAuthTotal);
        dest.writeDouble(tipAmount);
        dest.writeLong(phoneNum);
    }
}
