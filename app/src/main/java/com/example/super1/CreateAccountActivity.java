package com.example.super1;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void createNewAccountClicked(View view) {
        EditText editTextUserName = findViewById(R.id.editTextUserName);
        EditText editTextEmail = findViewById(R.id.editTextNewEmail);
        EditText editTextPassword = findViewById(R.id.editTextNewPassword);

        String userName = editTextUserName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
             Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean userExists = false;
        try {
            Database db = new Database(this);
            userExists = db.getUser(email, password);
            db.close();
        } catch (Exception e) {
            // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (userExists) {
            // ir para a tela principal (mapa)
            Toast.makeText(this, "Usuário já existe", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "E-mail inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Database db = new Database(this);
            db.insertUser(userName, email, password);
            db.close();
            Toast.makeText(this, "Usuário criado com sucesso", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}