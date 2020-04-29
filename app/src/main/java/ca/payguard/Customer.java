package ca.payguard;

public class Customer {

    //Customer data
    private int id;
    private float billTotal;
    private float preAuthTotal;
    private float tipAmount;
    private Table table;
    private int phoneNum;

    public Customer(int id, Table table, int phoneNum) {
        this.id = id;
        this.billTotal = 0;
        this.tipAmount = 0;
        this.table = table;
        this.phoneNum = phoneNum;
    }

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
}
