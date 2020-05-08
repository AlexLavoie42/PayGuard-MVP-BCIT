package ca.payguard;

import java.util.ArrayList;
import java.util.Map;

/**
 * TableSet contains the tables used within the GUI.
 * It uses the screen size to arrange the tables.
 */
public class TableSet extends ArrayList<Table> {
    public static final int STD_WIDTH = 1440, STD_HEIGHT = 2560;//standard android screen size
    private boolean stdFormation;

    public TableSet(ArrayList<Map> list){
        for(Map m : list){
            add(new Table(m));
        }
    }

    public TableSet(){

    }

    /**
     * Loads the table set setting from the app. If the user
     * doesn't have settings stored, the tables are arranged
     * to the standard layout.
     */
    public void load(){
        //TODO load table settings from back-end => stdFormation = false
        stdFormation = true;
    }

    public boolean isStdFormation(){
        return stdFormation;
    }

    /** Returns false if label is unique. */
    public boolean containsLabel(String label){
        for(Table t : this){
            if(t.getLabel().equals(label))
                return true;
        }

        return false;
    }
}