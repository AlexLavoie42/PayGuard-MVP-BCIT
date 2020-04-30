package ca.payguard;

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

    private final int[] sizeMods = {1, 2, 3};//float? would allow for 1.5x modifier, etc.
    private int sizeMod = sizeMods[0];

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

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}