package com.example.bakalarkaupdate;

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

public class ResetPasswordActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private Button buttonResetPassword, buttonResetBack;
    private EditText userMailextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        buttonResetPassword = findViewById(R.id.BReset);
        buttonResetBack = findViewById(R.id.BResetBack);
        userMailextView = findViewById(R.id.ETResetMail);

        buttonResetBack.setOnClickListener(v ->
                finish()
        );
        buttonResetPassword.setOnClickListener(evt -> resetPassword());
    }

    private void resetPassword() {
        String userMail = userMailextView.getText().toString().trim();
        if (userMail.isEmpty()) {
            Toast.makeText(this, "Vyplň údaje!", Toast.LENGTH_LONG).show();
            return;
        }

        auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(userMail)
                .addOnSuccessListener(reset -> {
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Úspešne odoslané", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ResetPasswordActivity.this, "Chyba: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}