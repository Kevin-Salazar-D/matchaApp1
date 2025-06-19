package baseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class Conexion extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "matchaapp.db";
    private static final int DATABASE_VERSION = 4;

    public Conexion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "password TEXT," +
                "telefono TEXT," +
                "pais TEXT" +
                ")";
        db.execSQL(createUserTable);

        String createProductTable = "CREATE TABLE productos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "precio INTEGER," +
                "imagen TEXT," +
                "tipo TEXT," +
                "sabor TEXT," +
                "marca TEXT" +
                ")";
        db.execSQL(createProductTable);

        // NUEVA TABLA DE ÓRDENES
        String createOrderTable = "CREATE TABLE ordenes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_user INTEGER," +
                "name_product TEXT," +
                "price_product INTEGER," +
                "image_product TEXT," +
                "location TEXT," +
                "status TEXT," +
                "size TEXT," +
                "delivery_time TEXT," +
                "milk_type TEXT," +
                "FOREIGN KEY (id_user) REFERENCES usuarios(id)" +
                ")";
        db.execSQL(createOrderTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS ordenes");
        onCreate(db);
    }
}
