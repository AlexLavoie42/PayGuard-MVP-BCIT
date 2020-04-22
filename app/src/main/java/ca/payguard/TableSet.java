package ca.payguard;

import java.util.ArrayList;

/**
 * TableSet contains the tables used within the GUI.
 * It uses the screen size to arrange the tables.
 */
class TableSet extends ArrayList<Table> {
    int[] sizes = new int[3];//S M L

    /**
     * Loads the table set setting from the app. If the user
     * doesn't have settings stored, the tables are arranged
     * to the standard layout.
     */
    public void load(int width, int height){
        //TODO load table settings from back-end

        //arrange standard layout if data can't be loaded
        if(size() == 0)
            loadStandard(width, height);
    }

    /**
     * Loads the set of tables to the
     * standard arrangement.
     */
    private void loadStandard(int width, int height){
        int segmentX = width / 20, segmentY = height / 20;
        int size = Math.max(segmentX, segmentY);
        sizes[0] = size;
        sizes[1] = size * 2;
        sizes[2] = size * 3;

        for(int i = 0; i < 9; i++){
            Table t = new Table();
            t.setDimensions(sizes[0], sizes[0]);
            add(t);
        }

        Table t = get(0);
        t.setCoords(13 * segmentX, 2 * segmentY);
        t = get(1);
        t.setCoords(7 * segmentX, 2 * segmentY);
        t = get(2);
        t.setCoords(10 * segmentX, 6 * segmentY);
        t = get(3);
        t.setCoords(3 * segmentX, 10 * segmentY);
        t = get(4);
        t.setCoords(7 * segmentX, 10 * segmentY);
        t = get(5);
        t.setCoords(8 * segmentX, 14 * segmentY);
        t = get(6);
        t.setCoords(3 * segmentX, 18 * segmentY);
        t = get(7);
        t.setCoords(5 * segmentX, 18 * segmentY);
        t = get(8);
        t.setCoords(13 * segmentX, 2 * segmentY);
    }
}