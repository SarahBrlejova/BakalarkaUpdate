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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameTextView, passwordTextView, ageTextView, nickNameTextView;
    private Button registerButton;
    FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userNameTextView = findViewById(R.id.etUsernameRegister);
        passwordTextView = findViewById(R.id.etPasswordRegister);
        ageTextView = findViewById(R.id.etAgeRegister);
        nickNameTextView = findViewById(R.id.etNickNameRegister);

        registerButton= findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(evt-> register());
    }

    private void register(){
        String userName = userNameTextView.getText().toString().trim();
        String password = passwordTextView.getText().toString();
        String ageStr = ageTextView.getText().toString().trim();
        String nickNameStr = nickNameTextView.getText().toString().trim();
        int age;

        if (userName.isEmpty() || password.isEmpty() || ageStr.isEmpty() || nickNameStr.isEmpty()) {
            Toast.makeText(this, "Vyplň údaje!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Zadaj správny vek.", Toast.LENGTH_LONG).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userName, password)
                .addOnSuccessListener(authResult -> {
                    String userId = authResult.getUser().getUid();
                    Toast.makeText(this, "autentifikácia done!", Toast.LENGTH_LONG).show();
                    createDocument(userId, userName, age, nickNameStr);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Registrácia zlyhala: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void createDocument(String userId, String email, int age, String nickNameStr){
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("nick", nickNameStr);
        user.put("age", age);
        user.put("totalMeters", 0);
        user.put("availableMeters", 0);
        user.put("averageDifficulty", "N/A");

        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Registrácia úspešná!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Chyba pri ukladaní údajov: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}