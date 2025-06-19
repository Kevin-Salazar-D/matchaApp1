package modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import baseDatos.Conexion;

public class Product {
    private int id;
    private String name;
    private int price;
    private String image;
    private String type;
    private String flavor;
    private String brand;

    public Product(int id, String name, int price, String image, String type, String flavor, String brand) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.type = type;
        this.flavor = flavor;
        this.brand = brand;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getFlavor() {
        return flavor;
    }

    public String getBrand() {
        return brand;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", flavor='" + flavor + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }

    public long insertProduct(Context context) {
        Conexion conexion = new Conexion(context);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", this.name);
        values.put("precio", this.price);
        values.put("imagen", this.image);
        values.put("tipo", this.type);
        values.put("sabor", this.flavor);
        values.put("marca", this.brand);

        long result = db.insert("productos", null, values);
        db.close();
        return result;
    }

    public ArrayList<Product> getAllProducts(Context context) {
        ArrayList<Product> productList = new ArrayList<>();
        Conexion conexion = new Conexion(context);
        SQLiteDatabase db = conexion.getReadableDatabase();

        String query = "SELECT * FROM productos";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("precio"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("imagen"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                String flavor = cursor.getString(cursor.getColumnIndexOrThrow("sabor"));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow("marca"));

                Product product = new Product(id, name, price, image, type, flavor, brand);
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return productList;
    }
}
