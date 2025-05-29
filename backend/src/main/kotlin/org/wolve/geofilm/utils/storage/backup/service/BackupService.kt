package org.wolve.geofilm.utils.storage.backup.service

import org.springframework.stereotype.Service
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Service
class BackupService {

    private val backupDir = File("data/backups")

    init {
        if (!backupDir.exists()) backupDir.mkdirs()
    }

    fun exportBackup(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val filename = "backup_$timestamp.sql"
        val file = File(backupDir, filename)

        // Ejecutamos pg_dump desde el contenedor de Docker
        val command = listOf(
            "docker", "exec", "postgres_db",
            "pg_dump", "-U", "wolverine307", "-d", "geofilm",
            "--data-only", "--no-owner", "--no-privileges",
            "--inserts"
        )

        val processBuilder = ProcessBuilder(command)
        processBuilder.redirectOutput(file)
        processBuilder.environment()["PGPASSWORD"] = "admin307204"

        val process = processBuilder.start()
        val exitCode = process.waitFor()

        if (exitCode != 0) {
            val error = process.errorStream.bufferedReader().readText()
            throw RuntimeException("Error al exportar: $error")
        }

        return file
    }

    fun importBackup(file: File): String {
        // Script para eliminar todas las tablas (si existen)
        val dropScript = """
        DROP TABLE IF EXISTS participaciones CASCADE;
        DROP TABLE IF EXISTS produccion_categorias CASCADE;
        DROP TABLE IF EXISTS profesionales CASCADE;
        DROP TABLE IF EXISTS reviews CASCADE;
        DROP TABLE IF EXISTS rodaje_imagenes CASCADE;
        DROP TABLE IF EXISTS rodajes CASCADE;
        DROP TABLE IF EXISTS producciones CASCADE;
        DROP TABLE IF EXISTS sagas CASCADE;
        DROP TABLE IF EXISTS ubicaciones CASCADE;
        DROP TABLE IF EXISTS usuarios CASCADE;
        DROP TABLE IF EXISTS flyway_schema_history CASCADE;
    """.trimIndent()

        // Ejecutar el script de borrado dentro del contenedor
        val dropCommand = listOf(
            "docker", "exec", "-i", "postgres_db", "bash", "-c",
            "echo \"$dropScript\" | psql -U wolverine307 -d geofilm"
        )

        val dropProcess = ProcessBuilder(dropCommand).apply {
            environment()["PGPASSWORD"] = "admin307204"
        }.start()

        val dropExitCode = dropProcess.waitFor()
        if (dropExitCode != 0) {
            val error = dropProcess.errorStream.bufferedReader().readText()
            throw RuntimeException("Error al eliminar las tablas: $error")
        }

        // Importar el archivo SQL
        val importCommand = listOf(
            "docker", "exec", "-i", "postgres_db",
            "psql", "-U", "wolverine307", "-d", "geofilm"
        )

        val importProcess = ProcessBuilder(importCommand).apply {
            environment()["PGPASSWORD"] = "admin307204"
            redirectInput(file)
        }.start()

        val importExitCode = importProcess.waitFor()
        if (importExitCode != 0) {
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