package ca.payguard.editMode;

import android.content.Context;
import android.view.View;
import android.widget.Button;

public abstract class Tool {
    protected View[] views;
    Context context;

    /** Stores the tool's buttons */
    public Tool(Context context, View ... views){
        this.context = context;
        this.views = views;

        addListeners();
    }

    public abstract void addListeners();

    public void setEnabled(boolean val){
        for(View v : views)
            v.setEnabled(val);
    }
}
