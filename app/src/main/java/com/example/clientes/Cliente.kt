package com.example.clientes

import android.R
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup

data class Cliente (
    val id:Int,
    val nome:String,
    val email:String,
    val fone:String,
    val img:Bitmap? = null
)