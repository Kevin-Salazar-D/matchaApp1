package com.example.matchaapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import modelos.Order;

public class OrderProduct extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private ShapeableImageView imgProductoOrden;
    private TextView tvNombreProducto, tvPrecioProducto, tvUbicacion;

    private AutoCompleteTextView autoCompleteEstado, autoCompleteTamano, autoCompleteLeche;

    private MaterialButton btnFecha, btnHora, btnUbicacion, btnConfirmarOrden;

    private FusedLocationProviderClient fusedLocationClient;

    private String nombreProducto;
    private int precioProducto;
    private String nombreImagen;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);

        initializeViews();
        setupLocation();
        populateProductData();
        setupListeners();
        setupDropdownMenus();
    }

    private void initializeViews() {
        imgProductoOrden = findViewById(R.id.imgProductoOrden);
        tvNombreProducto = findViewById(R.id.tvNombreProducto);
        tvPrecioProducto = findViewById(R.id.tvPrecioProducto);
        tvUbicacion = findViewById(R.id.tvUbicacion);

        autoCompleteEstado = findViewById(R.id.autoCompleteEstado);
        autoCompleteTamano = findViewById(R.id.autoCompleteTamano);
        autoCompleteLeche = findViewById(R.id.autoCompleteLeche);

        btnUbicacion = findViewById(R.id.btnUbicacion);
        btnFecha = findViewById(R.id.btnFecha);
        btnHora = findViewById(R.id.btnHora);
        btnConfirmarOrden = findViewById(R.id.btnConfirmarOrden);
    }

    private void populateProductData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nombre") && intent.hasExtra("precio") && intent.hasExtra("imagen")) {
            nombreProducto = intent.getStringExtra("nombre");
            precioProducto = intent.getIntExtra("precio", 0);
            nombreImagen = intent.getStringExtra("imagen");
            userID = intent.getIntExtra("user_id", -1);

            // Mostrar Toast con el tipo y valor del userID
            Toast.makeText(this, "Tipo: " + ((Object) userID).getClass().getSimpleName() + " | Valor: " + userID, Toast.LENGTH_LONG).show();

            tvNombreProducto.setText(nombreProducto);
            tvPrecioProducto.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(precioProducto));

            if (nombreImagen != null && !nombreImagen.isEmpty()) {
                if (nombreImagen.contains(".")) {
                    nombreImagen = nombreImagen.substring(0, nombreImagen.lastIndexOf('.'));
                }
                int imageResId = getResources().getIdentifier(nombreImagen.toLowerCase(), "drawable", getPackageName());
                imgProductoOrden.setImageResource(imageResId != 0 ? imageResId : R.drawable.matchaapplogo2);
            }
        } else {
            Toast.makeText(this, "Datos del producto no disponibles.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupListeners() {
        btnFecha.setOnClickListener(v -> showDatePicker());
        btnHora.setOnClickListener(v -> showTimePicker());
        btnUbicacion.setOnClickListener(v -> requestLocation());
        btnConfirmarOrden.setOnClickListener(v -> mostrarDialogoConfirmacion());
    }

    private void setupDropdownMenus() {
        autoCompleteEstado.setAdapter(ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_dropdown_item_1line));
        autoCompleteTamano.setAdapter(ArrayAdapter.createFromResource(this, R.array.sizes_array, android.R.layout.simple_dropdown_item_1line));
        autoCompleteLeche.setAdapter(ArrayAdapter.createFromResource(this, R.array.milk_types_array, android.R.layout.simple_dropdown_item_1line));
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle("¿Confirmar orden?")
                .setMessage("¿Estás seguro de que deseas confirmar esta orden?")
                .setPositiveButton("Confirmar", (dialog, which) -> crearOrden())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void crearOrden() {
        String estado = autoCompleteEstado.getText().toString();
        String tamano = autoCompleteTamano.getText().toString();
        String leche = autoCompleteLeche.getText().toString();
        String fecha = btnFecha.getText().toString();
        String hora = btnHora.getText().toString();
        String ubicacion = tvUbicacion.getText().toString();

        if (estado.isEmpty() || tamano.isEmpty() || leche.isEmpty() ||
                fecha.equals("Fecha") || hora.equals("Hora") || ubicacion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Order orden = new Order();
        orden.setId_user(userID);
        orden.setName_product(nombreProducto);
        orden.setPrice_product(precioProducto);
        orden.setImage_product(nombreImagen);
        orden.setLocation(ubicacion);
        orden.setStatus(estado);
        orden.setSize(tamano);
        orden.setDelivery_time(fecha + " " + hora);
        orden.setMilk_type(leche);
        orden.createOrder(this);
        clean();
    }

    private void setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    getAddressFromLocation(location);
                } else {
                    tvUbicacion.setText("No se pudo obtener la ubicación.");
                    Toast.makeText(this, "Activa la ubicación en tu dispositivo.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                tvUbicacion.setText(address.getAddressLine(0));
            } else {
                tvUbicacion.setText("Dirección no encontrada.");
            }
        } catch (IOException e) {
            tvUbicacion.setText("Error al obtener dirección.");
            e.printStackTrace();
        }
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    btnFecha.setText(selectedDate);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    btnHora.setText(selectedTime);
                },
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void clean() {
        autoCompleteEstado.setText("");
        autoCompleteTamano.setText("");
        autoCompleteLeche.setText("");

        btnFecha.setText("Fecha");
        btnHora.setText("Hora");
        tvUbicacion.setText("Ubicación");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
        }
    }
}
