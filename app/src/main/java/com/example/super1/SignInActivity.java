package com.example.super1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // botao entrar
        findViewById(R.id.buttonSignIn).setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, MapActivity.class);
            startActivity(intent);
            onPause();
            Toast.makeText(SignInActivity.this, "Bem-vindo(a)", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    public void entrarClicado(View view) {
//        Intent intent = new Intent(this, MapActivity.class);
//        startActivity(intent);
//        onPause();
//        Toast.makeText(this, "Bem-vindo(a)", Toast.LENGTH_SHORT).show();
//        EditText editTextEmail = findViewById(R.id.editTextEmail);
//        EditText editTextPassword = findViewById(R.id.editTextPassword);
//
//        String email = editTextEmail.getText().toString();
//        String password = editTextPassword.getText().toString();
//
//        if (email.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        boolean userExists = false;
//        try {
//            Database db = new Database(this);
//            userExists = db.getUser(email, password);
//            db.close();
//        } catch (Exception e) {
//            // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//        if (userExists) {
//            // ir para a tela principal (mapa)
//            Toast.makeText(this, "Usuario logado", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void signInWithGoogleClicked(View view) {
//        // firebase
//        Toast.makeText(this, "Entrar com Google clicado", Toast.LENGTH_SHORT).show();
//    }
//
//    public void createAccountClicked(View view) {
////        Toast.makeText(this, "Criar conta clicado", Toast.LENGTH_SHORT).show();
//        // go to create account activity
//        Intent intent = new Intent(this, CreateAccountActivity.class);
//        startActivity(intent);
////        onPause();
//    }
}
