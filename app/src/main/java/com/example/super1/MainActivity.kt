package com.example.super1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val usernameEditText = findViewById<EditText>(R.id.editUserName)
        val emailEditText = findViewById<EditText>(R.id.editNewEmail)
        val passwordEditText = findViewById<EditText>(R.id.editNewPassword)
        val createAccountButton = findViewById<Button>(R.id.button)

        createAccountButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Verificação dos campos
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Verificação do formato do email
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Lógica para criar a conta (salvar no banco, etc.)
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()

                    // Navegar para a tela de mapa
                    val intent = Intent(this, MapaActivity2::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Por favor, insira um email válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
