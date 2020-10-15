package com.tenisme.roomtest

class Example(a: Int, b: Int, c: Int) {

    private val d: Int = a
    private val e: Int = b
    private val f: Int = c

    fun sum(): Int {
        return d + e
    }

    fun returnF(): Int {
        return f
    }
}