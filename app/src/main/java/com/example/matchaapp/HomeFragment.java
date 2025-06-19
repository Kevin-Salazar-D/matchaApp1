package com.example.matchaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelos.Product;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private Spinner spinnerFiltro;
    private ProductAdapter adapter;
    private List<Product> listaProductos;
    private int userId = -1;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerProductos);
        spinnerFiltro = view.findViewById(R.id.spinnerFiltro);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("userId", -1);
        }

        Product temp = new Product(0, "", 0, "", "", "", "");
        listaProductos = temp.getAllProducts(getContext());
        Collections.shuffle(listaProductos); // Mezcla aleatoria

        Set<String> tiposSet = new HashSet<>();
        for (Product p : listaProductos) {
            tiposSet.add(p.getType());
        }
        List<String> tipos = new ArrayList<>(tiposSet);
        Collections.sort(tipos);
        tipos.add(0, "Todos"); // Opción para mostrar todos

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                tipos
        );
        spinnerFiltro.setAdapter(spinnerAdapter);

        adapter = new ProductAdapter(getContext(), listaProductos, userId);
        recyclerView.setAdapter(adapter);

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String tipoSeleccionado = parent.getItemAtPosition(pos).toString();

                List<Product> filtrados = new ArrayList<>();
                for (Product p : listaProductos) {
                    if (tipoSeleccionado.equals("Todos") || p.getType().equalsIgnoreCase(tipoSeleccionado)) {
                        filtrados.add(p);
                    }
                }
                Collections.shuffle(filtrados); // También mezclar los filtrados
                adapter = new ProductAdapter(getContext(), filtrados, userId);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }
}
