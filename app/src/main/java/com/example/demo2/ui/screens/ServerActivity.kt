package com.example.demo2.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.demo2.*
import com.example.demo2.Message
import com.example.demo2.Room.MessageDB
import com.example.demo2.Room.MessageRepository
import com.example.demo2.helper.SocketHandler
import io.socket.client.Socket
import kotlinx.android.synthetic.main.server_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ServerActivity: AppCompatActivity() {

    private lateinit var mSocket: Socket
    lateinit var userKey: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.server_screen)
        SocketHandler.setSocket()
        userKey = getKey()
        mSocket = SocketHandler.getSocket()
        val name = intent.getStringExtra("name") ?: "NULL"
        val nameLower = name?.lowercase()
        if(! SocketHandler.getSocket().isActive){
            SocketHandler.establishConnection()
            mSocket.emit(
                "join",
                nameLower
            )
        }

        mSocket = SocketHandler.getSocket()

        textView.text = name?.uppercase()
        var messages = mutableListOf<Message>()



        val repository = MessageRepository(MessageDB(this))
        CoroutineScope(Dispatchers.IO).launch {
            val allMessages = repository.getAllText()
            allMessages.forEach{
                if(it.room == name){
                    messages.add(it)
                }
            }
            runOnUiThread {
                populateList(messages)
                Log.d("Taby",messages.toString())
            }
        }



        textField.setEndIconOnClickListener {
            mSocket.emit(
                nameLower,
                textEdit.text.toString()+ "$" + userKey
            )
            textEdit.setText("")
        }

        mSocket.on(nameLower) { args ->
            if (args[0] != null) {
                val counter = args[0].toString()
                val currentTime: String = SimpleDateFormat("HH:mm:", Locale.getDefault()).format(Date())
                val _message = Message(
                    time = currentTime,
                    text = counter.substringBefore("$"),
                    from = counter.substringAfter("$"),
                    room = name
                )
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("Taby","Recv")
                    repository.upsert(_message)
                }
                runOnUiThread {
                    messages.add(_message)
                    populateList(messages)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mSocket.close()
    }

    private fun populateList(messages: MutableList<Message>) {
        recyclerViewServer.withModels {
            messages.forEachIndexed { index, _message ->
                when(_message.from){
                    userKey -> {
                        from {
                            id(index)
                            message(_message)
                        }
                    }
                    else -> {
                        to{
                            id(index)
                            message(_message)
                            onClickContent { view ->
                                val v = view.findViewById<TextView>(R.id.text_gchat_user_other)
                                if(v.isVisible){
                                    v.visibility = View.GONE
                                }else{
                                    v.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }


        }
    }

    private fun getKey(): String{
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val s1 = sh.getString("key", "NULL")
        return s1!!
    }
}
