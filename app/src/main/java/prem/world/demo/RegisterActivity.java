package prem.world.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText Email, Password, Mobile;
    TextInputLayout txtEmail, txtPassword, txtMobile;
    AppCompatButton BtnSignup;
    String tmpEmail, tmpPassword, tmpMobile;
    PrefManger prefManger;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth= FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        prefManger = new PrefManger(this);
        Check();//Check AlReady Login or Not
        //Initialize Control
        Email = findViewById(R.id.register_Edit_Email);
        Password = findViewById(R.id.register_Edit_Password);
        Mobile = findViewById(R.id.register_Edit_mobile);
        txtEmail = findViewById(R.id.register_Text_Email);
        txtMobile = findViewById(R.id.register_Text_Mobile);
        txtPassword = findViewById(R.id.register_Text_Password);
        BtnSignup = findViewById(R.id.register_btn_Signup);
        BtnSignup.setOnClickListener(v -> {
            boolean go=true;
            if (!isValidEmail())
                go=false;
            if (!isValidNumber())
                go=false;
            if (!isValidPassword())
                go=false;
            if (go) {
                String finalTmpEmail = tmpEmail;
                mAuth.createUserWithEmailAndPassword(tmpEmail, tmpPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User account created successfully
                        //singin not requiewd
                        ///signIn(tmpEmail,tmpPassword);
                        prefManger.putString(PrefManger.Email, finalTmpEmail);
                        prefManger.putString(PrefManger.Password, tmpPassword);
                        prefManger.putBoolean(PrefManger.IsLogin, true);
                        Map<String, Object> data = new HashMap<>();
                        data.put("Email", finalTmpEmail);
                        data.put("Password", tmpPassword);
                        data.put("Mobile", tmpMobile);
                        db.collection("demo").document("user").set(data).addOnCompleteListener(task1 -> {
                            if (task1.isComplete() && task1.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Data Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                                FirebaseUser user = mAuth.getCurrentUser();
                                prefManger.putString(PrefManger.Email, finalTmpEmail);
                                startActivity(intent);
                            }
                        });
                    } else {
                        // User account creation failed
                        Toast.makeText(RegisterActivity.this, "Something Wrong" + Objects.requireNonNull(task.getException()), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPassword() {
        if (Password.getText() == null || TextUtils.isEmpty(Password.getText().toString())) {
            txtPassword.setError("Password is Required");
            txtPassword.setErrorEnabled(true);
            return false;
        } else {
            tmpPassword = Password.getText().toString().trim();
            if (tmpPassword.length() < 8) {
                txtPassword.setError("Enter 8 Digit Password");
                txtPassword.setErrorEnabled(true);
                return false;
            }
            else {
                txtPassword.setError(null);
                txtPassword.setErrorEnabled(false);
                return true;
            }
        }
    }

    private void signIn(String tmpEmail, String tmpPassword) {
           mAuth.signInWithEmailAndPassword(tmpEmail,tmpPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                    //
               }
           });
    }

    private boolean isValidEmail() {
       // String tmpEmail;
        if (Email.getText() == null || TextUtils.isEmpty(Email.getText())) {
            txtEmail.setError("Email Id Is Required");
            txtEmail.setErrorEnabled(true);
            return false;
        } else {
            tmpEmail = Email.getText().toString().trim();
            String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z0-9._-]+";
            if (!tmpEmail.matches(checkEmail)) {
                txtEmail.setError("Please Check Your Email");
                txtEmail.setErrorEnabled(true);
                return false;
            } else {
                txtEmail.setError(null);
                txtEmail.setErrorEnabled(false);
                return true;
            }
        }
    }
    private void Check() {
        prefManger = new PrefManger(this);
        boolean isLogin;
        isLogin = prefManger.getBoolean(PrefManger.IsLogin);
        if (isLogin) {
            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @NonNull
    private Boolean isValidNumber() {
        if (Mobile.getText() == null || TextUtils.isEmpty(Mobile.getText().toString())) {
            txtMobile.setError("Mobile No is Required");
            txtMobile.setErrorEnabled(true);
            return false;
        } else {
            tmpMobile = Mobile.getText().toString().trim();
            if (tmpMobile.length() < 10) {
                txtMobile.setError("Enter 10 Digit Mobile No");
                txtMobile.setErrorEnabled(true);
                return false;
            }
            else {
                txtMobile.setError(null);
                txtMobile.setErrorEnabled(false);
                return true;
            }
        }
    }
}