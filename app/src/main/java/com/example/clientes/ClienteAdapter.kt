package com.example.clientes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ClienteAdapter(context: Context) : ArrayAdapter<Cliente>(context, 0) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val auxV: View

        if (convertView != null){
            auxV = convertView
        } else {
            auxV = LayoutInflater.from(context).inflate(R.layout.list_view_cliente, parent, false);
        }

        val cliente = getItem(position)

        val id = auxV.findViewById<TextView>(R.id.txt_id_cliente)
        val nome = auxV.findViewById<TextView>(R.id.txt_nome_cliente)
        val email = auxV.findViewById<TextView>(R.id.txt_email_cliente)
        val fone = auxV.findViewById<TextView>(R.id.txt_fone_cliente)
        val img = auxV.findViewById<ImageView>(R.id.img_foto_cliente)

        if (cliente?.id != 0) {
            id.text = cliente?.id.toString()
        }
        nome.text = cliente?.nome
        email.text = cliente?.email
        fone.text = cliente?.fone
        if (cliente?.img != null) {
            img.setImageBitmap(cliente.img)
        }
        return auxV
    }
}