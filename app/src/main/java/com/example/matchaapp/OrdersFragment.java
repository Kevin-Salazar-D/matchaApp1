package com.example.matchaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import modelos.Order;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    private int idUser;
    private RecyclerView recyclerView;
    private OrdersAdapater ordersAdapter;
    private ArrayList<Order> ordersList;
    private ArrayList<Order> filteredOrdersList;

    private RadioGroup rgFilterStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            idUser = bundle.getInt("userId", -1);
        }

        Toast.makeText(getContext(), "User ID recibido: " + idUser, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ordes, container, false);

        recyclerView = view.findViewById(R.id.rvOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rgFilterStatus = view.findViewById(R.id.rgFilterStatus);

        // Obtén todas las órdenes del usuario
        ordersList = new Order().getAllOrders(getContext(), idUser);

        filteredOrdersList = new ArrayList<>(ordersList);

        ordersAdapter = new OrdersAdapater(filteredOrdersList);
        recyclerView.setAdapter(ordersAdapter);

        // Listener para cambiar el filtro según RadioButton seleccionado
        rgFilterStatus.setOnCheckedChangeListener((group, checkedId) -> {
            filterOrders(checkedId);
        });

        return view;
    }

    private void filterOrders(int checkedId) {
        filteredOrdersList.clear();

        if (checkedId == R.id.rbAll) {
            // Mostrar todas las órdenes sin filtrar
            filteredOrdersList.addAll(ordersList);
        } else if (checkedId == R.id.rbApartado) {
            // Filtrar órdenes con status "Apartado"
            for (Order order : ordersList) {
                if ("Apartado".equalsIgnoreCase(order.getStatus())) {
                    filteredOrdersList.add(order);
                }
            }
        } else if (checkedId == R.id.rbComprado) {
            // Filtrar órdenes con status "Comprado"
            for (Order order : ordersList) {
                if ("Comprado".equalsIgnoreCase(order.getStatus())) {
                    filteredOrdersList.add(order);
                }
            }
        }

        ordersAdapter.notifyDataSetChanged();
    }
}
