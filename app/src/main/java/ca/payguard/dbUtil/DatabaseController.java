package ca.payguard.dbUtil;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import ca.payguard.TableSet;

public class DatabaseController {
    final String DATA = "data";
    final String USERS = "users";
    final String TABLES = "tables";
    final String TABLE_SET = "tableset";

    FirebaseFirestore db;
    FirebaseAuth auth;
    CollectionReference userDB;
    CollectionReference users;
    CollectionReference tables;

    public DatabaseController() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if(auth.getCurrentUser() != null){
            String email = auth.getCurrentUser().getEmail();
            userDB = db.collection(email);
            users = userDB.document(DATA).collection(USERS);
            tables = userDB.document(DATA).collection(TABLES);
        } else throw new AuthNotFoundError();
    }

    public void addTableSet(TableSet tableset){
        tables.document(TABLE_SET).set(tableset);
    }

    public void getTableSet(OnSuccessListener<DocumentSnapshot> onSuccess){
        tables.document(TABLE_SET)
                .get()
                .addOnSuccessListener(onSuccess);
    }

    public void checkPin(String pin, final Runnable onComplete){
        users.whereEqualTo("id", pin)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.getResult().isEmpty()){
                            onComplete.run();
                        }
                    }
                });
    }

    public void checkPin(EditText pin, final Runnable onComplete){
        users.whereEqualTo("id", pin.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.getResult().isEmpty()){
                            onComplete.run();
                        }
                    }
                });
    }

    private static class AuthNotFoundError extends RuntimeException {
        public AuthNotFoundError(){
            super("Attempted to access database without authentication.");
        }
    }
}