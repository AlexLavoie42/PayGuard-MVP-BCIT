package ca.payguard;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * TableSet contains the tables used within the GUI.
 * It uses the screen size to arrange the tables.
 */
public class TableSet extends ArrayList<Table> implements Serializable {
    public static final int STD_WIDTH = 1440, STD_HEIGHT = 2560;//standard android screen size
    private boolean stdFormation;

    public TableSet(ArrayList<Map> list){
        for(Map m : list){
            add(new Table(m));
        }
    }

    public TableSet(){

    }

    public Table allocateTable(char shape, int pointX, int pointY) throws IllegalArgumentException {
        if(shape != 'S' && shape != 'C' && shape != 'R')
            throw new IllegalArgumentException("Error: Shape is not allowed.");

        Table t = new Table();
        t.setShape(shape);
        t.setCoords(pointX, pointY);
        t.setLabel("" + (size() + 1));
        add(t);

        return t;
    }

    public void renderStdTableSet(){
        int segmentX = STD_WIDTH / 20, segmentY = STD_HEIGHT / 20;

        allocateTable('C', 2 * segmentX, 6 * segmentY);
        allocateTable('C', 7 * segmentX, 6 * segmentY);
        allocateTable('C', 11 * segmentX, 8 * segmentY);
        allocateTable('C', 2 * segmentX, 10 * segmentY);
        allocateTable('C', 7 * segmentX, 10 * segmentY);
        allocateTable('C', 11 * segmentX, 12 * segmentY);
        allocateTable('C', 2 * segmentX, 14 * segmentY);
        allocateTable('S', 7 * segmentX, 14 * segmentY);
        allocateTable('R', 15 * segmentX, 9 * segmentY);

        Table last = get(size() - 1);
        last.setSizeMod(2);
        last.setRotated(true);
    }

    public boolean isStdFormation(){
        return stdFormation;
    }

    public void updateTable(Table table){
        Table clone = null;
        for(Table t : this){
            if(t.getLabel().equals(table.getLabel())) {
                clone = t;
            }
        }
        if(clone != null){
            this.remove(clone);
            this.add(table);
        }
    }

    /** Returns false if label is unique. */
    public boolean containsLabel(String label){
        for(Table t : this){
            if(t.getLabel().equals(label))
                return true;
        }

        return false;
    }

    public void addCustomer(Customer customer, String tableNum) {
        for(Table t : this){
            if(t.getLabel().equals(tableNum)) {
                t.addCustomer(customer);
                customer.setId(t.getAllCustomers().size());
            }
        }
    }

    public void updateCustomer(Customer customer, String tableNum) {
        for(Table t : this){
            if(t.getLabel().equals(tableNum)) {
                t.updateCustomer(customer);
                customer.setId(t.getAllCustomers().size());
            }
        }
    }
}