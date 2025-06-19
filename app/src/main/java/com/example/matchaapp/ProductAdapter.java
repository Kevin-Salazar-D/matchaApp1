package com.example.matchaapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import modelos.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductoViewHolder> {

    private final List<Product> productos;
    private final Context context;
    private final int userId; // ← Añadido para pasar el ID del usuario

    public ProductAdapter(Context context, List<Product> productos, int userId) {
        this.context = context;
        this.productos = productos;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Product p = productos.get(position);
        holder.tvNombre.setText(p.getName());
        holder.tvPrecio.setText("Price: $" + p.getPrice());
        holder.tvTipo.setText("Type: " + p.getType());

        String imageName = p.getImage();

        if (imageName.contains(".")) {
            imageName = imageName.substring(0, imageName.lastIndexOf('.'));
        }

        int imageResId = context.getResources().getIdentifier(
                imageName.toLowerCase(),
                "drawable",
                context.getPackageName()
        );

        if (imageResId != 0) {
            holder.imgProducto.setImageResource(imageResId);
        } else {
            holder.imgProducto.setImageResource(R.drawable.matchaapplogo2);
        }

        holder.btnComprar.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderProduct.class);
            intent.putExtra("nombre", p.getName());
            intent.putExtra("precio", p.getPrice());
            intent.putExtra("imagen", p.getImage());
            intent.putExtra("user_id", userId); // ← CORRECTO: este es el ID del usuario

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvTipo;
        ImageView imgProducto;
        Button btnComprar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.product_name);
            tvPrecio = itemView.findViewById(R.id.product_price);
            tvTipo = itemView.findViewById(R.id.product_category);
            imgProducto = itemView.findViewById(R.id.product_image);
            btnComprar = itemView.findViewById(R.id.button_primary_action);
        }
    }
}
