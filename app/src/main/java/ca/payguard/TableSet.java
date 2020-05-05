package ca.payguard;

import java.util.ArrayList;

/**
 * TableSet contains the tables used within the GUI.
 * It uses the screen size to arrange the tables.
 */
public class TableSet extends ArrayList<Table> {
    public static final int STD_WIDTH = 1440, STD_HEIGHT = 2560;//standard android screen size
    private boolean stdFormation;

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
}