package ca.payguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ca.payguard.editMode.*;
import java.util.ArrayList;

public class EditModeActivity extends AppCompatActivity {
    LabelInput label;
    RotateTool rotateTool;
    public ShapeSelect shapeSelect;
    SizeSelect sizeSelect;
    Button garbage;

    public TableSet tables;
    Table selectedTbl;
    Button selected;

    int size = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);

        label = new LabelInput(this);
        rotateTool = new RotateTool(this);
        shapeSelect = new ShapeSelect(this);
        sizeSelect = new SizeSelect(this);
    }

    /** Translates a table set to their buttons. NOTE: Does not assign
     * listeners to buttons as they need to be set in their own activity. */
    public static ArrayList<Button> renderTableSet(final Context c, TableSet tables,
                                                   float wRatio, float hRatio){
        ArrayList<Button> btns = new ArrayList<>();

        for(Table t : tables)
            btns.add(renderTblBtn(c, t, wRatio, hRatio));

        return btns;
    }

    private static Button renderTblBtn(Context c, Table t, float wRatio, float hRatio){
        final Button b = new Button(c);
        b.setText(t.getLabel());
        b.setX((float) t.getX() * wRatio);
        b.setY((float) t.getY() * hRatio);

        //reshape
        //resize
        //rotate

        return b;
    }

    public void select(final Button b){
        Table selectedTbl = null;

        if(this.selected != null) {
            if(this.selectedTbl.getShape() != Table.Shape.C)
                this.selected.setBackground(getResources().getDrawable(R.drawable.table));
            else
                this.selected.setBackground(getResources().getDrawable(R.drawable.table_round));
        }

        for(Table t : tables){
            if(t.getLabel().equals(b.getText())){
                selectedTbl = t;
                break;
            }
        }

        if(selectedTbl != null){
            if(selectedTbl.getShape() != Table.Shape.C)
                b.setBackground(getResources().getDrawable(R.drawable.table_selector));
            else
                b.setBackground(getResources().getDrawable(R.drawable.table_round_selected));

            label.setText(selectedTbl.getLabel());
            sizeSelect.selectTable(selectedTbl);

            rotateTool.rotate.setEnabled(true);
            label.enable();

            garbage.setEnabled(true);
            this.selectedTbl = selectedTbl;
            this.selected = b;
        }
    }

    private void deselect(){
        Button b = getSelected();
        if(b != null){
            Table t = getSelectedTbl();
            if(t.getShape() == Table.Shape.C)
                b.setBackground(getResources().getDrawable(R.drawable.table_round));
            else
                b.setBackground(getResources().getDrawable(R.drawable.table));

            selected = null;
            selectedTbl = null;
            label.clear();

            rotateTool.rotate.setEnabled(false);
            sizeSelect.disable();
            label.disable();
        }

        garbage.setEnabled(false);
    }

    public void dispose(View v){

    }

    /** TODO add table to edit mode activity. */
    public void addTable(char shape){

    }

    public void setSize(int size){
        this.size = size;
    }

    public int getSize(){
        return size;
    }

    public Button getSelected(){
        return selected;
    }

    public Table getSelectedTbl(){
        return selectedTbl;
    }
}
