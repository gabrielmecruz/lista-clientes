package com.example.clientes

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class CadastroCliente : AppCompatActivity() {
    val COD_IMG = 101
    var imageBitmap: Bitmap? = null
    internal var dbHelper = ClientesDB(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        var cliente: Cliente
        val btnCadastrar = findViewById<Button>(R.id.btn_cadastrar)
        val fotoCliente = findViewById<ImageView>(R.id.img_cliente)
        val nome = findViewById<EditText>(R.id.txt_nome)
        val email = findViewById<EditText>(R.id.txt_email)
        val fone = findViewById<EditText>(R.id.txt_fone)

        val intent = intent
        val id = intent.getIntExtra("id", 0)


        //verifica se já existe id ao item
        if (id != 0) {
            cliente = dbHelper.read(id)
            nome.setText(cliente.nome)
            email.setText(cliente.email)
            fone.setText(cliente.fone)
            if (cliente.img != null)
                fotoCliente.setImageBitmap(cliente.img)
        }

        btnCadastrar.setOnClickListener {
            if (nome.text.toString().isNotEmpty() && email.text.toString().isNotEmpty() && fone.text.toString().isNotEmpty()) {
                val cliente = Cliente(
                    id,
                    nome.text.toString(),
                    email.text.toString(),
                    fone.text.toString(),
                    imageBitmap
                )

                if (id != 0) {
                    Toast.makeText(applicationContext, "Cadastro atualizado", Toast.LENGTH_SHORT).show()
                    dbHelper.update(cliente)
                }else {

                    Toast.makeText(applicationContext, "Cliente '${cliente.nome}' cadastrado", Toast.LENGTH_SHORT).show()
                    dbHelper.create(cliente)
                }

                super.onBackPressed()
            } else {
                nome.error = if(nome.text.isEmpty()) "Preencha com o nome do cliente" else null
                email.error = if(email.text.isEmpty()) "Preencha com o e-mail do cliente" else null
                fone.error = if(fone.text.isEmpty()) "Preencha com o telefone do cliente" else null
            }
        }

        fotoCliente.setOnClickListener {
            openPhoto()
        }
    }

    fun openPhoto() {
        //variavel de armazenamento da ação
        val intentPhoto = Intent(Intent.ACTION_GET_CONTENT)
        //define o filtro para imagens
        intentPhoto.type = "image/*"
        //inicializa a activity com o resultado
        startActivityForResult(Intent.createChooser(intentPhoto, "Escolha uma foto"), COD_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fotoCliente = findViewById<ImageView>(R.id.img_cliente)

        if(requestCode == COD_IMG && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                //metodo de acesso a imagem escolhida atraves da variavel 'data'
                val inputStream = data.data?.let { contentResolver.openInputStream(it) }
                //converte o resultado da seleção em bitmap
                imageBitmap = BitmapFactory.decodeStream(inputStream)
                //exibir imagem
                fotoCliente.setImageBitmap(imageBitmap)
            }
        }
    }
}