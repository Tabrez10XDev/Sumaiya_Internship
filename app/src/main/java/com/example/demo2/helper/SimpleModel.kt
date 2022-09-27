package com.example.demo2.helper


data class SimpleModel(val name: String)

fun getSimpleData() = mutableListOf(
    SimpleModel("Room1"),
    SimpleModel("Audio-Room"),
    SimpleModel("Gaming-Room")
)