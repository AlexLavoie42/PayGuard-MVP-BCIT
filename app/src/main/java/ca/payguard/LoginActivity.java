package ca.payguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import ca.payguard.R;
import ca.payguard.dbUtil.DatabaseController;

/**
 * LoginActivity ensures user is authenticated before
 * opening MainActivity.
 */
public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Open MainActivity if user is already logged in.
        if (mAuth.getCurrentUser() != null) {
            openMainActivity();
        }
    }

    /** Logs in user through firebase and opens MainActivity on success. */
    public void login(View view){
        String username = ((EditText)findViewById(R.id.et_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.et_password)).getText().toString();

        if(!username.equals("") && !password.equals("")) {
            //Sign in through Firebase Auth
            mAuth.signInWithEmailAndPassword(username.toString(),
                    password.toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                openMainActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // If there are no credentials, display a message to the user.
            Toast.makeText(LoginActivity.this, "Please enter login credentials.",
                    Toast.LENGTH_SHORT).show();

        }
    }

    /** Starts MainActivity */
    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
