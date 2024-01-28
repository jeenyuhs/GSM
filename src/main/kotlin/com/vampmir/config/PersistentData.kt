package com.vampmir.config

import kotlinx.serialization.json.Json
import java.io.File
import java.io.Reader
import java.io.Writer
import kotlin.concurrent.fixedRateTimer

fun File.ensureFile() = (parentFile.exists() || parentFile.mkdirs()) && createNewFile()

// skytils
abstract class PersistentData(val file: File) {
    protected val json: Json = Json { prettyPrint = true }

    abstract fun write(writer: Writer)
    abstract fun read(reader: Reader)
    abstract fun setDefault(writer: Writer)

    private fun readSave() {
        try {
            file.ensureFile()
            file.bufferedReader().use {
                read(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                file.bufferedWriter().use {
                    setDefault(it)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun writeSave() {
        try {
            file.ensureFile()
            file.writer().use { writer ->
                write(writer)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    init {
        saves.add(this)
    }

    companion object {
        val saves: HashSet<PersistentData> = hashSetOf()

        fun loadData() {
            saves.forEach { it.readSave() }
        }

        init {
            fixedRateTimer("GSM-PersistentData-Write", period = 30000L) {
                for (save in saves) {
                    save.writeSave()
                }
            }
            Runtime.getRuntime().addShutdownHook(Thread({
                for (save in saves) {
                    save.writeSave()
                }
            }, "GSM-PersistentData-Shutdown"))
        }
    }
}