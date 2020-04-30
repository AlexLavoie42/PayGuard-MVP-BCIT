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
    private int x, y;

    private final int[] sizeMods = {1, 2, 3};//float? would allow for 1.5x modifier, etc.
    private int sizeMod = sizeMods[0];
    public static final char[] shapes = {'S', 'C', 'R'};
    private char shape = shapes[2];//default shape is rectangle

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
        if(c != shapes[0] && c != shapes[1] && c != shapes[2])
            throw new IllegalArgumentException("Error: shape character must be an allowed shape.");

        shape = c;
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

    public char getShape(){
        return shape;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}