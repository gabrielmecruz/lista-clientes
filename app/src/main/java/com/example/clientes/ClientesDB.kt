package com.example.clientes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


class ClientesDB(context: Context) : SQLiteOpenHelper(context, "clientes.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE  clientes (id INTEGER PRIMARY KEY, nome TEXT, email TEXT, fone TEXT, img BLOB);"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS clientes"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun create(cliente: Cliente): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        //convertendo o formato de imagem para byteArray
        val stream = ByteArrayOutputStream()
        cliente.img?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()

        values.put("nome", cliente.nome)
        values.put("email", cliente.email)
        values.put("fone", cliente.fone)
        values.put("img", image)

        val _sucess = db.insert("clientes", null, values)
        return (("$_sucess").toInt() != -1)
    }

    fun read(termoBusca: String): ArrayList<Cliente> {
        val db = readableDatabase
        val listaClientes = ArrayList<Cliente>()
        val query = "SELECT * FROM clientes WHERE lower(nome) like lower('%$termoBusca%') OR lower(fone) like lower('%$termoBusca%') OR lower(email) like lower('%$termoBusca%')"
        val cursor = db.rawQuery(query, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("id"))
                    val nome = cursor.getString(cursor.getColumnIndex("nome"))
                    val email = cursor.getString(cursor.getColumnIndex("email"))
                    val fone = cursor.getString(cursor.getColumnIndex("fone"))

                    //converter novamente a img byteArray para Bitmap
                    val img = cursor.getBlob(cursor.getColumnIndex("img"))
                    val options = BitmapFactory.Options()
                    val bitmap = BitmapFactory.decodeByteArray(img, 0, img.size, options)

                    val cliente = Cliente(id, nome, email, fone, bitmap)
                    listaClientes.add(cliente)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return listaClientes
    }

    fun read(id: Int): Cliente {
        val db = readableDatabase
        val query = "SELECT * FROM clientes WHERE id = $id"
        val cursor = db.rawQuery(query, null)
        var clienteExist = Cliente(id, "", "", "")

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                val nome = cursor.getString(cursor.getColumnIndex("nome"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                val fone = cursor.getString(cursor.getColumnIndex("fone"))

                //converter novamente a img byteArray para Bitmap
                val img = cursor.getBlob(cursor.getColumnIndex("img"))
                val options = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeByteArray(img, 0, img.size, options)
                clienteExist = Cliente(id, nome, email, fone, bitmap)
            }
        }
        cursor.close()
        return clienteExist
    }

    fun update(cliente: Cliente): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        //convertendo o formato de imagem para byteArray
        val stream = ByteArrayOutputStream()
        cliente.img?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()

        values.put("id", cliente.id)
        values.put("nome", cliente.nome)
        values.put("email", cliente.email)
        values.put("fone", cliente.fone)
        values.put("img", image)

        val _success = db.update("clientes", values, "id = ?", arrayOf(cliente.id.toString()))
        return ("$_success").toInt() != -1
    }

    fun delete(id:Int): Boolean {
        val db = this.writableDatabase
        val _sucess = db.delete("clientes", "id = ?", arrayOf(id.toString()))
        return ("$_sucess").toInt() != -1
    }
}