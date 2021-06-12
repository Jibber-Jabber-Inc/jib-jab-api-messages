package com.jibberjabber.jibjab_message.factory

interface AbstractFactory<T, V> {

    fun convert(input : T): V

}
