package org.wolve.geofilm.utils.storage.backup.service

import org.springframework.stereotype.Service
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Service
class BackupService {

    private val backupDir = File("data/backups")

    private val dbHost = "postgres"
    private val dbPort = "5432"
    private val dbUser = "wolverine307"
    private val dbName = "geofilm"
    private val dbPassword = "admin307204"

    init {
        if (!backupDir.exists()) backupDir.mkdirs()
    }

    fun exportBackup(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val filename = "backup_$timestamp.sql"
        val file = File(backupDir, filename)

        val command = listOf(
            "pg_dump",
            "-h", dbHost,
            "-p", dbPort,
            "-U", dbUser,
            "-d", dbName,
            "--data-only", "--no-owner", "--no-privileges", "--inserts"
        )

        val processBuilder = ProcessBuilder(command).apply {
            redirectOutput(file)
            environment()["PGPASSWORD"] = dbPassword
        }

        val process = processBuilder.start()
        val exitCode = process.waitFor()

        if (exitCode != 0) {
            val error = process.errorStream.bufferedReader().readText()
            throw RuntimeException("Error al exportar: $error")
        }

        return file
    }

    fun importBackup(file: File): String {
        val dropScript = """
            DO $$
            DECLARE
                _r RECORD;
            BEGIN
                -- Elimina todas las tablas del esquema público
                FOR _r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
                    EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(_r.tablename) || ' CASCADE';
                END LOOP;
            END $$;
        """.trimIndent()

        val dropCommand = listOf(
            "psql", "-h", dbHost, "-p", dbPort,
            "-U", dbUser, "-d", dbName, "-c", dropScript
        )

        val dropProcess = ProcessBuilder(dropCommand).apply {
            environment()["PGPASSWORD"] = dbPassword
        }.start()

        if (dropProcess.waitFor() != 0) {
            val error = dropProcess.errorStream.bufferedReader().readText()
            throw RuntimeException("Error al eliminar las tablas: $error")
        }

        val importCommand = listOf(
            "psql", "-h", dbHost, "-p", dbPort,
            "-U", dbUser, "-d", dbName
        )

        val importProcess = ProcessBuilder(importCommand).apply {
            environment()["PGPASSWORD"] = dbPassword
            redirectInput(file)
        }.start()

        if (importProcess.waitFor() != 0) {
            val error = importProcess.errorStream.bufferedReader().readText()
            throw RuntimeException("Error al importar backup: $error")
        }

        return "Backup importado correctamente desde ${file.name}"
    }

    fun listBackups(): List<File> = backupDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()

    fun getBackupFile(name: String): File {
        val file = File(backupDir, name)
        if (!file.exists()) throw IllegalArgumentException("No existe el archivo")
        return file
    }

    fun saveUploadedFile(filename: String, bytes: ByteArray): File {
        val dest = File(backupDir, filename)
        dest.writeBytes(bytes)
        return dest
    }
}
