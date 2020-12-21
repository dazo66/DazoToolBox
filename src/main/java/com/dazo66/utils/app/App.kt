package com.dazo66.utils.app

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

class App(val name: String,
          val path: String,
          val dataPath: String,
          val updateDate: Long,
          val policy: LaunchPolicy,
          val rowName: String) {

    var isAble = AtomicBoolean(true)
    var process: LoggerProcess = LoggerProcess(name, path, File(dataPath))
    private var confObject : JsonObject
    private var gson = GsonBuilder().setPrettyPrinting().create()

    init {
        val path = File(dataPath)
        if (!path.exists()) {
            path.mkdirs()
        }
        val conf = File(dataPath + "/app.conf")
        if (!conf.exists()) {
            conf.createNewFile()
            val fileWriter = FileWriter(conf)
            fileWriter.write("{}")
            fileWriter.close()
        }

        val fileReader = FileReader(conf)
        var jsonParser = JsonParser()
        try {
            confObject = jsonParser.parse(IOUtils.toString(fileReader)) as JsonObject
        } catch (e : Exception) {
            confObject = JsonObject()
            save()
        }
        if (confObject.get("isAble") != null) {
            isAble.set(confObject.get("isAble").asBoolean)
        }
    }

    fun setConf(key: String, o : Any) {
        confObject.add(key, gson.toJsonTree(o))
        save()
    }

    fun save(){
        IOUtils.write(gson.toJson(confObject), FileWriter(File(dataPath + "/app.conf")))

    }

    fun reStart(){
        process.exit()
        process = LoggerProcess(name, path, File(dataPath))
    }

    fun disable(){
        process.exit()
        isAble.set(false)
        setConf("isAble", false)
    }

    fun enable() {
        isAble.set(true)
        reStart()
        setConf("isAble", true)
    }

    fun stop() {
        process.exit()
    }

    fun isAlive():Boolean {
        return process.isAlive()
    }

}
