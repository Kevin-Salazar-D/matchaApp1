package com.example.matchaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import baseDatos.Conexion;
import modelos.User;

public class CreateAccountActivity extends AppCompatActivity {

    private Spinner spinnerCountry;
    private EditText nameUser;
    private EditText passwordUser;
    private EditText phone;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_activity);

        spinnerCountry = findViewById(R.id.spinnerCountry);
        nameUser = findViewById(R.id.etUsername);
        passwordUser = findViewById(R.id.etPassword);
        phone = findViewById(R.id.etPhone);
        btnCreateAccount = findViewById(R.id.btnCreateAccount); // Asegúrate de tener este botón en tu XML

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.countrty_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);

        handleButtonCreate(btnCreateAccount);
    }

    public void handleButtonCreate(Button btn){
        btn.setOnClickListener(v -> createUser());
    }
    private void createUser() {
        String username = nameUser.getText().toString().trim();
        String password = passwordUser.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        String selectedCountry = spinnerCountry.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || selectedCountry.equals("Ingresa tu país")) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        User newUser = new User(username, password, phoneNumber, selectedCountry);
        long createUser = newUser.insertarUsuario(getApplicationContext());
        if (createUser > 0) {
            Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }

    }

    private void limpiarCampos() {
        nameUser.setText("");
        passwordUser.setText("");
        phone.setText("");
        spinnerCountry.setSelection(0);
    }
}
