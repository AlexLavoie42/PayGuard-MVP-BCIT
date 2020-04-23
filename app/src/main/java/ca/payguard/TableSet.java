package ca.payguard;

import java.util.ArrayList;

/**
 * TableSet contains the tables used within the GUI.
 * It uses the screen size to arrange the tables.
 */
class TableSet extends ArrayList<Table> {
    int[] sizes = new int[3];//S M L
    static final int STD_WIDTH = 1440, STD_HEIGHT = 2560;//standard android screen size

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
     * Loads the set of tables to the standard arrangement using
     * the standard android screen size. No matter the screen size,
     * the tables' coordinates remain the same.
     */
    private void loadStandard(){
        int segmentX = STD_WIDTH / 20, segmentY = STD_HEIGHT / 20;
        //standard table size is equal to the greater segment
        int size = Math.max(segmentX, segmentY);
        sizes[0] = size;
        sizes[1] = size * 4;
        sizes[2] = size * 6;

        for(int i = 0; i < 9; i++){
            Table t = new Table();
            t.setLabel("" + (i+1));
            t.setDimensions(sizes[0], sizes[0]);
            add(t);
        }

        Table t = get(0);
        t.setCoords(2 * segmentX, 6 * segmentY);
        t = get(1);
        t.setCoords(7 * segmentX, 6 * segmentY);
        t = get(2);
        t.setCoords(11 * segmentX, 8 * segmentY);
        t = get(3);
        t.setCoords(2 * segmentX, 10 * segmentY);
        t = get(4);
        t.setCoords(7 * segmentX, 10 * segmentY);
        t = get(5);
        t.setCoords(11 * segmentX, 12 * segmentY);
        t = get(6);
        t.setCoords(2 * segmentX, 14 * segmentY);
        t = get(7);
        t.setCoords(7 * segmentX, 14 * segmentY);
        t = get(8);
        t.setCoords(16 * segmentX, 10 * segmentY);
        t.setDimensions(t.getWidth(), sizes[2]);
    }
}