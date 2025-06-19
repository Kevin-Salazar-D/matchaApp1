package modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import baseDatos.Conexion;

public class User {

    private int id;
    private String username;
    private String password;
    private String phone;
    private String country;

    public User(String username, String password, String phone, String country) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.country = country;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long insertarUsuario(Context context) {
        Conexion conexion = new Conexion(context);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", this.username);
        values.put("password", this.password);
        values.put("telefono", this.phone);
        values.put("pais", this.country);

        long result = db.insert("usuarios", null, values);
        db.close();
        return result;
    }
    public User startSession(Context context) {
        Conexion conexion = new Conexion(context);
        SQLiteDatabase db = conexion.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        String[] args = { username, password };

        Cursor cursor = db.rawQuery(query, args);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("telefono")));
            user.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("pais")));
        }

        cursor.close();
        db.close();
        return user;
    }
    public void finishSession() {
       setCountry("");
       setPassword("");
       setPhone("");
       setId(0);
       setUsername("");
    }
}
