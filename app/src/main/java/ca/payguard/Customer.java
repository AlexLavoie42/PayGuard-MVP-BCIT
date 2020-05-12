package ca.payguard;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class Customer implements Parcelable {

    public void setId(long id) {
        this.id = id;
    }

    //Customer data
    private long id;
    private double billTotal;
    private double preAuthTotal;
    private double tipAmount;
    private String orderID;

    public void setPhoneNum(long phoneNum) {
        this.phoneNum = phoneNum;
    }

    private long phoneNum;

    public Customer() {
        //TODO: Auto generate ID
        this.id = 0;
        this.billTotal = 0;
        this.tipAmount = 0;
        this.phoneNum = 0;
        this.orderID = generateNewID();
    }

    public Customer(Map m){
        id = (long)m.get("id");
        billTotal = (double)m.get("billTotal");
        preAuthTotal = (double)m.get("preAuthTotal");
        tipAmount = (double)m.get("tipAmount");
        phoneNum = (long)m.get("phoneNum");
        orderID = (String)m.get("orderID");
    }

    protected Customer(Parcel in) {
        id = in.readLong();
        billTotal = in.readDouble();
        preAuthTotal = in.readDouble();
        tipAmount = in.readDouble();
        phoneNum = in.readLong();
        orderID = in.readString();
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
        dest.writeString(orderID);
    }

    public String getOrderID() {
        return orderID;
    }

    private String generateNewID(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        SimpleDateFormat df = new SimpleDateFormat("yyyymmdd");
        return "PG-" + df.format(new Date()) + generatedString;
    }
}
