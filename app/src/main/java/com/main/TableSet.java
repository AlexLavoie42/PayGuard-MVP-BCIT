package com.main;

import java.util.ArrayList;

/**
 * TableSet contains the tables used within the GUI.
 * It uses the screen size to arrange the tables.
 */
public class TableSet extends ArrayList<Table> {
    private int centerX, centerY;//center coordinates of the screen

    public TableSet(int centerX, int centerY){
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * Loads the table set setting from the app. If the user
     * doesn't have settings stored, the tables are arranged
     * to the standard layout.
     */
    public void load(){
        //TODO load table settings from back-end

        //arrange standard layout if data can't be loaded
        if(size() == 0)
            loadStandard();
    }

    /**
     * Loads the set of tables to the
     * standard arrangement.
     */
    private void loadStandard(){
        Table t = new Table();
        t.setLabel("1");
        t.setCoords(centerX / 3, centerY / 5);
        add(t);

        t = new Table();
        t.setLabel("2");
        t.setCoords(centerX / 3 * 2, centerY / 5);
        add(t);

        t = new Table();
        t.setLabel("3");
        t.setCoords(centerX, centerY / 5 * 3);
        add(t);

        t = new Table();
        t.setLabel("4");
        t.setCoords(centerX / 3, centerY);
        add(t);

        t = new Table();
        t.setLabel("5");
        t.setCoords(centerX / 3 * 2, centerY);
        add(t);

        t = new Table();
        t.setLabel("6");
        t.setCoords(centerX, centerY / 5 * 7);
        add(t);

        t = new Table();
        t.setLabel("7");
        t.setCoords(centerX / 3, centerY / 5 * 9);
        add(t);

        t = new Table();
        t.setLabel("8");
        t.setCoords(centerX / 3 * 2, centerY / 5 * 9);
        add(t);

        t = new Table();
        t.setLabel("9");
        t.setCoords(centerX / 3 * 5, centerY / 5);
        t.setDimensions(Table.STD_DIMENSION, centerY);
        add(t);
    }
}