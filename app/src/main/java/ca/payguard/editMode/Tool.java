package ca.payguard.editMode;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import ca.payguard.EditModeActivity;

public abstract class Tool {
    protected View[] views;
    Context context;

    /** Stores the tool's buttons */
    public Tool(Context context, View ... views){
        this.context = context;
        this.views = views;

        addListeners();
    }

    public void addListeners(){
        for(int i = 0; i < views.length; i++) {
            final int num = i;
            views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(EditModeActivity.selectedTbl != null)
                        applyTransformation(num);
                }
            });
        }
    }

    public abstract void applyTransformation(int btnNo);

    public void setEnabled(boolean val){
        for(View v : views)
            v.setEnabled(val);
    }
}
