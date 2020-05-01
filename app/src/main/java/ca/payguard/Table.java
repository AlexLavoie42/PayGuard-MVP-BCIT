package ca.payguard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Table class stores all preauth/payment
 * data for one table.
 */
class Table implements Parcelable {
    //data pertaining to bill/preauth info
    private double preauthAmt;

    //customizeable table gui info
    private String label;
    static final int STD_DIMENSION = 100;
    private int width = STD_DIMENSION, height = STD_DIMENSION;
    private int x, y;

    //Array of Customers at Table.
    private Customer[] customers;

    /*
    Table shape:
    0 - Circle/Oval
    1 - Square/Rectangle
    2 - Diamond
    3 - Triangle
    4 - Polygon (maybe?)
     */
    private int shape;
    private final int[] shapes = {0, 1, 2, 3, 4};

    public Table(){

    }

    protected Table(Parcel in) {
        preauthAmt = in.readDouble();
        label = in.readString();
        width = in.readInt();
        height = in.readInt();
        x = in.readInt();
        y = in.readInt();
        customers = in.createTypedArray(Customer.CREATOR);
        shape = in.readInt();
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

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setShape(int shapeNo) throws IllegalArgumentException {
        if(shapeNo < shapes[0] || shapeNo > shapes[shapes.length - 1])
            throw new IllegalArgumentException("Error: shapeNo is not in range of table shapes");

        this.shape = shapeNo;
    }

    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }

    public double getPreauthAmt(){
        return preauthAmt;
    }

    public String getLabel(){
        return label;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getShape(){
        return shape;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
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
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeTypedArray(customers, flags);
        dest.writeInt(shape);
    }
}