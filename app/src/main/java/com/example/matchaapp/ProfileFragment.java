package com.example.matchaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import modelos.User;

public class ProfileFragment extends Fragment {

    private TextView userName;
    private TextView userID;
    private TextView phone;
    private TextView password;
    private TextView country;

    private Button btnFinishSession;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnFinishSession = view.findViewById(R.id.btnLogout);

        userName = view.findViewById(R.id.tvUsername);
        phone = view.findViewById(R.id.tvPhone);
        password = view.findViewById(R.id.tvPassword);
        country = view.findViewById(R.id.tvCountry);
        userID = view.findViewById(R.id.tvUserID);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String usernameValue = bundle.getString("username", "No definido");
            String phoneValue = bundle.getString("phone", "No definido");
            String passwordValue = bundle.getString("password", "No definido");
            String countryValue = bundle.getString("country", "No definido");
            int userIDValue = bundle.getInt("userId", -1);

            userName.setText("Usuario: " + usernameValue);
            phone.setText("Teléfono: " + phoneValue);
            password.setText("Contraseña: " + passwordValue);
            country.setText("País: " + countryValue);
            userID.setText("ID: " + userIDValue);

            handleFinishSession();
        }

        return view;
    }

    public void handleFinishSession() {
        btnFinishSession.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Quieres salir de sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        User userFinish = new User();
                        userFinish.finishSession();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
