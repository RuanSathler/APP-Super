//package com.example.super1
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Patterns
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//
//class SignInActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_sign_in)
//
//        /*
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_in)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }*/
//
//        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
//        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
//        val signInButton = findViewById<Button>(R.id.buttonSignIn)
//        val signInWithGoogleButton = findViewById<Button>(R.id.buttonSignInWithGoogle)
//        val createAccountButton = findViewById<Button>(R.id.buttonCreateAccount)
//
//        signInButton.setOnClickListener {
//            val email = emailEditText.text.toString()
//            val password = passwordEditText.text.toString()
//
//            // Verificação dos campos
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Pegar o email e a senha e verificar se são válidos
//            val db = Database(this)
//            val userExists = db.getUser(email, password)
//            db.close()
//            if (!userExists) {
//                Toast.makeText(this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Navegar para a tela de mapa
//            val intent = Intent(this, MapaActivity2::class.java)
//            startActivity(intent)
//        }
//
//        signInWithGoogleButton.setOnClickListener {
//            Toast.makeText(this, "Sign in with Google", Toast.LENGTH_SHORT).show()
//        }
//
//        createAccountButton.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//
//        // Verificar se o usuário já está logado
//        val sharedPreferences = getSharedPreferences("super1", MODE_PRIVATE)
//        val email = sharedPreferences.getString("email", null)
//        val password = sharedPreferences.getString("password", null)
//        if (email != null && password != null) {
//            val db = Database(this)
//            val userExists = db.getUser(email, password)
//            db.close()
//            if (userExists) {
//                val intent = Intent(this, MapaActivity2::class.java)
//                startActivity(intent)
//            }
//        }
//    }
//}