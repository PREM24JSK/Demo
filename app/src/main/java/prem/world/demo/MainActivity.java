package prem.world.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    AppCompatButton Btn_SignIn, loginActivity_btnSignUp;
    TextInputEditText Email, Password;
    PrefManger prefManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManger = new PrefManger(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CheckLogin();
        Btn_SignIn = findViewById(R.id.loginTab_Btn_SignIn);
        Email = findViewById(R.id.loginTab_TextEdit_Email);
        Password = findViewById(R.id.loginTab_TextEdit_Password);
        loginActivity_btnSignUp = findViewById(R.id.loginActivity_btnSignUp);
        Btn_SignIn.setOnClickListener(v -> {
            String tmpEmail = Objects.requireNonNull(Email.getText()).toString();
            String tmpPassword = Objects.requireNonNull(Password.getText()).toString();
            if (TextUtils.isEmpty(tmpEmail) || TextUtils.isEmpty(tmpPassword)) {
                Toast.makeText(MainActivity.this, "Enter Correct Data", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(tmpEmail, tmpPassword).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        if (task.isSuccessful()) {
                            prefManger.putString(PrefManger.Email, tmpEmail);
                            prefManger.putString(PrefManger.Password, tmpPassword);
                            prefManger.putBoolean(PrefManger.IsLogin, true);
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                String email = user.getEmail();
                                prefManger.putString(PrefManger.Email, email);
                                prefManger.putString(PrefManger.Uid,uid);
                                
                            }
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(this, "Error" + Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        loginActivity_btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });


    }

    private void CheckLogin() {
        PrefManger prefManger = new PrefManger(this);
        boolean isLogin;
        isLogin = prefManger.getBoolean(PrefManger.IsLogin);
        if (isLogin) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}