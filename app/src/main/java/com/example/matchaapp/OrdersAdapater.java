package com.example.matchaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matchaapp.R;
import modelos.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;

public class OrdersAdapater extends RecyclerView.Adapter<OrdersAdapater.OrderViewHolder> {

    private List<Order> ordersList;

    public OrdersAdapater(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ordes, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    // Nuevo método para actualizar la lista y refrescar el RecyclerView
    public void updateList(List<Order> newList) {
        ordersList.clear();
        ordersList.addAll(newList);
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgOrderProduct;
        TextView tvOrderName, tvOrderStatus, tvOrderPrice, tvOrderMilkSize, tvOrderDeliveryTime, tvOrderLocation;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrderProduct = itemView.findViewById(R.id.imgOrderProduct);
            tvOrderName = itemView.findViewById(R.id.tvOrderName);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvOrderMilkSize = itemView.findViewById(R.id.tvOrderMilkSize);
            tvOrderDeliveryTime = itemView.findViewById(R.id.tvOrderDeliveryTime);
            tvOrderLocation = itemView.findViewById(R.id.tvOrderLocation);
        }

        public void bind(Order order) {
            tvOrderName.setText(order.getName_product());
            tvOrderStatus.setText(order.getStatus());
            tvOrderPrice.setText(NumberFormat.getCurrencyInstance().format(order.getPrice_product()));

            String milkSize = "Leche: " + order.getMilk_type() + " | Tamaño: " + order.getSize();
            tvOrderMilkSize.setText(milkSize);

            tvOrderDeliveryTime.setText("Entrega: " + order.getDelivery_time());
            tvOrderLocation.setText("Ubicación: " + order.getLocation());

            String imageName = order.getImage_product();
            if (imageName != null && !imageName.isEmpty()) {
                if (imageName.contains(".")) {
                    imageName = imageName.substring(0, imageName.lastIndexOf('.'));
                }
                int resId = itemView.getContext().getResources()
                        .getIdentifier(imageName.toLowerCase(), "drawable", itemView.getContext().getPackageName());
                if (resId != 0) {
                    imgOrderProduct.setImageResource(resId);
                } else {
                    imgOrderProduct.setImageResource(R.drawable.matchaapplogo2);
                }
            } else {
                imgOrderProduct.setImageResource(R.drawable.matchaapplogo2);
            }
        }
    }
}
