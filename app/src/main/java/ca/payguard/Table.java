package ca.payguard;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * The Table class stores all preauth/payment
 * data for one table.
 *
 * TODO: Fix writing customers to parcel. DO NOT PASS TABLES THROUGH INTENTS.
 */
public class Table implements Parcelable {

    //customizeable table gui info
    private String label;
    private int x, y;
    private final int[] sizeMods = {1, 2, 3};//float? would allow for 1.5x modifier, etc.
    private int sizeMod = sizeMods[0];

    public void removeAllCustomers() {
        customers.clear();
    }

    public enum Shape { S, C, R }
    private Shape shape = Shape.R;//default shape is rectangle
    private boolean rotated;

    //Array of Customers at Table.
    private ArrayList<Customer> customers;

    public Table(){
        customers = new ArrayList<>();
    }

    public Table(Map m){
        setShape(((String)m.get("shape")).charAt(0));
        y = ((Long)m.get("y")).intValue();
        x = ((Long)m.get("x")).intValue();
        label = (String)m.get("label");
        sizeMod = ((Long)m.get("sizeMod")).intValue();
        rotated = (Boolean) m.get("rotated");
        customers = new ArrayList<>();
        for(Map map : (ArrayList<Map>)m.get("allCustomers")){
            customers.add(new Customer(map));
        }
    }

    protected Table(Parcel in) {
        /*
        dest.writeString(label);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeSerializable(shape);
        dest.writeInt(sizeMod);
        dest.writeInt((rotated) ? 1 : 0);
         */

        label = in.readString();
        x = in.readInt();
        y = in.readInt();
        shape = (Table.Shape) in.readSerializable();
        sizeMod = in.readInt();
        rotated = (in.readInt() == 1);
        customers = new ArrayList<Customer>();
    }

    public static final Creator<Table> CREATOR = new Creator<Table>() {
        @Override
        public Table createFromParcel(Parcel in) {
            return new Table(in);
        }

        @Override
        public Table[] newArray(int size) {
            return new Table[size];
        }
    };

    public void setLabel(String label){
        this.label = label;
    }

    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setSizeMod(int m) throws IllegalArgumentException {
        if(m != 1 && m != 2 && m != 3)
            throw new IllegalArgumentException("Error: size mod is not allowed.");

        sizeMod = m;
    }

    public void setShape(char c) throws IllegalArgumentException {
        switch (c){
            case 'S':
                shape = Shape.S;
                break;
            case 'C':
                shape = Shape.C;
                break;
            case 'R':
                shape = Shape.R;
                break;
            default:
                throw new IllegalArgumentException("Error: argument shape isn't allowed.");
        }
    }

    public void setRotated(boolean rotated) {
        this.rotated = rotated;
    }

    public String getLabel(){
        return label;
    }

    public int getSizeMod(){
        return sizeMod;
    }

    public Shape getShape(){
        return shape;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean getRotated(){
        return rotated;
    }

    public ArrayList<Customer> getAllCustomers(){
        return customers;
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
    }

    public void updateCustomer(Customer customer){
        Customer cust = null;
        for(Customer c : customers){
            if(c.getId() == customer.getId()){
                cust = c;
            }
        }
        if(cust != null) {
            customers.remove(cust);
            customers.add((int) (customer.getId() - 1), customer);
        }
    }

    public void removeCustomer(Customer customer){
        Customer cust = null;
        for(Customer c : customers){
            if(c.getId() == customer.getId()){
                cust = c;
            }
        }
        if(cust != null) {
            customers.remove(cust);
        }
    }

    //TODO: Add error handling.
    public Customer getCustomerByID(int id){
        return customers.get(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeSerializable(shape);
        dest.writeInt(sizeMod);
        dest.writeInt((rotated) ? 1 : 0);
    }
}