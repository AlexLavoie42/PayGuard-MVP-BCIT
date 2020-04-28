package ca.payguard;

public class Customer {

    //Customer data
    private int id;
    private float billTotal;
    private Table table;
    private int phoneNum;

    //TODO: Add pre-auth token

    public Customer(int id, Table table, int phoneNum) {
        this.id = id;
        this.billTotal = 0;
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
}
