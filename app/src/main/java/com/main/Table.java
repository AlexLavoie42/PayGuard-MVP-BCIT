package com.main;

/**
 * The Table class stores all preauth/payment
 * data for one table.
 */
public class Table {
    //data pertaining to bill/preauth info
    private double preauthAmt;

    //customizeable table gui info
    private String label;
    static final int STD_DIMENSION = 100;
    private int width = STD_DIMENSION, height = STD_DIMENSION;
    private int x, y;

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
}