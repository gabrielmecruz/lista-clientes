package com.example.clientes

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    internal var dbHelper = ClientesDB(this)
    var busca: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNC = findViewById<Button>(R.id.btn_novo_cliente)
        val listClient = findViewById<ListView>(R.id.list_cliente) as ListView
        val txt_cliente = findViewById<EditText>(R.id.txt_cliente)

        val clienteAdapter = ClienteAdapter(this)
        listClient.adapter = clienteAdapter

        //define a abertura de uma nova activity
        btnNC.setOnClickListener {
            //criando intent
            val intent = Intent(this, CadastroCliente::class.java)
            //iniciando activity
            startActivity(intent)
        }

        //dá a opção de atualizar cliente ao selecionar
        listClient.setOnItemClickListener { parent, view, position, id ->
            val cliente = listClient.adapter.getItem(position) as Cliente
            val att = Intent(this, CadastroCliente::class.java).apply {
                putExtra("id", cliente.id)
            }
            startActivity(att)
        }

        //remove ao segurar o click do mouse
        listClient.setOnItemLongClickListener { parent, view, position, id ->
            val cliente = listClient.adapter.getItem(position) as Cliente
            clienteAdapter.remove(clienteAdapter.getItem(position))
            dbHelper.delete(cliente.id)
            updateData()

            //exibir como toast qual itens foram removidos
            Toast.makeText(applicationContext, "'${cliente.nome}' removido(a)!", Toast.LENGTH_LONG).show()

            return@setOnItemLongClickListener (true)
        }

        //define a busca de algum cliente já adicionado na lista
        txt_cliente.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                busca = s.toString()
                updateData()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    //função que esconde o teclado do dispositivo
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun updateData() {
        val clienteAdapter = findViewById<ListView>(R.id.list_cliente).adapter as ClienteAdapter
        clienteAdapter.clear()
        clienteAdapter.addAll(dbHelper.read(termoBusca = busca))
    }

    override fun onResume() {
        super.onResume()
        updateData()
    }
}