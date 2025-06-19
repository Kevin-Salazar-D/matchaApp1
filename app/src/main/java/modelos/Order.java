package modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import baseDatos.Conexion;

public class Order {

    private int id;
    private int id_user;
    private String name_product;
    private int price_product;
    private String image_product;
    private String location;
    private String status;
    private String size;
    private String delivery_time;
    private String milk_type;

    public Order() {}

    public Order(int id, int id_user, String name_product, int price_product,
                 String image_product, String location, String status,
                 String size, String delivery_time, String milk_type) {
        this.id = id;
        this.id_user = id_user;
        this.name_product = name_product;
        this.price_product = price_product;
        this.image_product = image_product;
        this.location = location;
        this.status = status;
        this.size = size;
        this.delivery_time = delivery_time;
        this.milk_type = milk_type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getId_user() { return id_user; }
    public void setId_user(int id_user) { this.id_user = id_user; }

    public String getName_product() { return name_product; }
    public void setName_product(String name_product) { this.name_product = name_product; }

    public int getPrice_product() { return price_product; }
    public void setPrice_product(int price_product) { this.price_product = price_product; }

    public String getImage_product() { return image_product; }
    public void setImage_product(String image_product) { this.image_product = image_product; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getDelivery_time() { return delivery_time; }
    public void setDelivery_time(String delivery_time) { this.delivery_time = delivery_time; }

    public String getMilk_type() { return milk_type; }
    public void setMilk_type(String milk_type) { this.milk_type = milk_type; }

    public void createOrder(Context context) {
        Conexion conexion = new Conexion(context);
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_user", this.id_user);
        values.put("name_product", this.name_product);
        values.put("price_product", this.price_product);
        values.put("image_product", this.image_product);
        values.put("location", this.location);
        values.put("status", this.status);
        values.put("size", this.size);
        values.put("delivery_time", this.delivery_time);
        values.put("milk_type", this.milk_type);

        long resultado = db.insert("ordenes", null, values);
        db.close();

        if (resultado != -1) {
            Toast.makeText(context, "Orden creada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error al crear la orden", Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<Order> getAllOrders(Context context, int userID) {
        ArrayList<Order> orderList = new ArrayList<>();
        Conexion conexion = new Conexion(context);
        SQLiteDatabase db = conexion.getReadableDatabase();

        String query = "SELECT * FROM ordenes WHERE id_user = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                order.setId_user(cursor.getInt(cursor.getColumnIndexOrThrow("id_user")));
                order.setName_product(cursor.getString(cursor.getColumnIndexOrThrow("name_product")));
                order.setPrice_product(cursor.getInt(cursor.getColumnIndexOrThrow("price_product")));
                order.setImage_product(cursor.getString(cursor.getColumnIndexOrThrow("image_product")));
                order.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                order.setSize(cursor.getString(cursor.getColumnIndexOrThrow("size")));
                order.setDelivery_time(cursor.getString(cursor.getColumnIndexOrThrow("delivery_time")));
                order.setMilk_type(cursor.getString(cursor.getColumnIndexOrThrow("milk_type")));

                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return orderList;
    }


}
