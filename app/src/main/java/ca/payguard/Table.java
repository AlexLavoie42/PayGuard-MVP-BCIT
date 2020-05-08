package ca.payguard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Table class stores all preauth/payment
 * data for one table.
 */
public class Table implements Parcelable {
    //data pertaining to bill/preauth info
    private double preauthAmt;

    //customizeable table gui info
    private String label;
    private int x, y;

    private final int[] sizeMods = {1, 2, 3};//float? would allow for 1.5x modifier, etc.
    private int sizeMod = sizeMods[0];
    public enum Shape { S, C, R }
    private Shape shape = Shape.R;//default shape is rectangle
    private int angle = 0;

    //Array of Customers at Table.
    private Customer[] customers;

    public Table(){

    }

    protected Table(Parcel in) {
        preauthAmt = in.readDouble();
        label = in.readString();
        x = in.readInt();
        y = in.readInt();
        customers = in.createTypedArray(Customer.CREATOR);
        //TODO read size, shape, and angle
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

    public void setPreauthAmt(double preauthAmt){
        this.preauthAmt = preauthAmt;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setSizeMod(char c) throws IllegalArgumentException {
        if(c != 'S' && c != 'M' && c != 'L')
            throw new IllegalArgumentException("Error: size mod must be S, M, or L.");

        if(c == 'S')
            sizeMod = sizeMods[0];
        else if(c == 'M')
            sizeMod = sizeMods[1];
        else
            sizeMod = sizeMods[2];
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

    /** Ensures that the angle is between 0-359. */
    public static int verifyAngle(int angle){
        int vAngle = angle;

        while(vAngle < 0)
            vAngle += 360;
        while(vAngle > 359)
            vAngle -= 360;

        return vAngle;
    }

    public void setAngle(int angle) throws IllegalArgumentException {
        if(angle < 0 || angle > 359)
            throw new IllegalArgumentException("Error: angle must be 0-359");

        this.angle = angle;
    }

    public char getSizeChar(){
        if(sizeMod == sizeMods[0])
            return 'S';
        else if(sizeMod == sizeMods[1])
            return 'M';
        else
            return 'L';
    }

    public double getPreauthAmt(){
        return preauthAmt;
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

    public int getAngle(){
        return angle;
    }

    public Customer[] getAllCustomers(){
        if(customers != null)
            return customers;
        else return new Customer[0];
    }

    public void addCustomer(Customer customer){
        if(customers != null) {
            Customer[] newArr = new Customer[customers.length + 1];
            for (int i = 0; i < customers.length; i++) {
                newArr[i] = customers[i];
            }
            newArr[customers.length] = customer;
            customers = newArr;
        } else {
            customers = new Customer[1];
            customers[0] = customer;
        }
    }

    //TODO: Add error handling.
    public Customer getCustomerByID(int id){
        return customers[id];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(preauthAmt);
        dest.writeString(label);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeTypedArray(customers, flags);
        //dest.write(shape);
        //TODO write size, shape, and angle
    }
}