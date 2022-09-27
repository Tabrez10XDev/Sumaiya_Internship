package com.example.demo2.ui.screens

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.epoxy.CarouselModel_
import com.example.demo2.*
import com.example.demo2.helper.SimpleModel
import com.example.demo2.helper.SocketHandler
import com.example.demo2.helper.getSimpleData
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val simpleModels = getSimpleData()
        populateList(simpleModels)
        imageButton.setOnClickListener {
            Toast.makeText(this,"hey",Toast.LENGTH_SHORT).show()
            Log.d("Taby","heyyy")
            showdialog()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun populateList(simpleModels: MutableList<SimpleModel>) {

        recyclerView.withModels {
                    header {
                        id(1)
                        headerContent("Messenger")
                    }

                    val lis = listOf<Int>(1,2,3,4,5)
                    val photoModels = mutableListOf<AdvertisementBindingModel_>()
                    lis.forEachIndexed { index, i ->
                        photoModels.add(
                            AdvertisementBindingModel_()
                            .id(index)
                        )

                    }

                    CarouselModel_()
                        .id("carousel")
                        .models(photoModels)
                        .addTo(this);

                    simpleModels.forEachIndexed { index, _simpleModel ->
                        content{
                            id(index)
                            simpleModel(_simpleModel)
                            onClickContent { view ->
                                val intent = Intent(applicationContext, ServerActivity::class.java)
                                val v = view.findViewById<TextView>(R.id.chatlisthead)
                                intent.putExtra("name",v.text.toString())
                                startActivity(intent)
                            }

                        }
                    }
            }
        }

    private fun showdialog(){
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Profile Update")

        val input = EditText(this)
        input.setHint("Enter ID")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            var m_Text = input.text.toString()
            saveKey(m_Text)

        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun saveKey(key: String){

        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("key", key)
        myEdit.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}


