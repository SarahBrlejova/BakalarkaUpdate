package com.example.bakalarkaupdate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText userMailextView, passwordTextView;
    FirebaseAuth auth;
    private Button buttonRegister, buttonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        FirestoreHelper firestoreHelper = new FirestoreHelper();
//        firestoreHelper.addTestData();

        buttonRegister = findViewById(R.id.BMainRegister);
        buttonResetPassword = findViewById(R.id.BMainReset);
        Button buttonlogin = findViewById(R.id.BMainLogin);

        buttonRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class))
        );

        buttonResetPassword.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class))
        );

        buttonlogin.setOnClickListener(evt-> login());
    }

    private void login(){
        userMailextView = findViewById(R.id.ETMainEmail);
        passwordTextView = findViewById(R.id.ETMainPassword);
        String userMail = userMailextView.getText().toString().trim();
        String password = passwordTextView.getText().toString();

        if (userMail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vyplň údaje!", Toast.LENGTH_LONG).show();
            return;
        }

        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(userMail, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(MainActivity.this, "Login successful!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, AppActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}