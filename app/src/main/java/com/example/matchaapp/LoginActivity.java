package com.example.matchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import modelos.User;

public class LoginActivity extends AppCompatActivity {

    private TextView btnAccountCreate;
    private Button btnStartSession;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        btnAccountCreate = findViewById(R.id.tvNoAccount);
        btnStartSession = findViewById(R.id.btnLogin);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);

        setupCreateAccountNavigation();
        setupLoginAction();
    }
    public void cleanInputs(){

        username.setText("");
        password.setText("");
    }
    private void setupCreateAccountNavigation() {
        btnAccountCreate.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            cleanInputs();
        });
    }

    private void setupLoginAction() {
        btnStartSession.setOnClickListener(v -> {
            String userN = username.getText().toString().trim();
            String userP = password.getText().toString().trim();

            if (userN.isEmpty() || userP.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.setUsername(userN);
            user.setPassword(userP);

            User sessionUser = user.startSession(getApplicationContext());

            if (sessionUser != null) {
                Toast.makeText(this, "Ingresaste correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("userId", sessionUser.getId());
                intent.putExtra("username", sessionUser.getUsername());
                intent.putExtra("phone", sessionUser.getPhone());
                intent.putExtra("password", sessionUser.getPassword());
                intent.putExtra("country", sessionUser.getCountry());
                startActivity(intent);
                cleanInputs();

            } else {
                Toast.makeText(this, "No pudiste entrar. Verifica tus credenciales", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
