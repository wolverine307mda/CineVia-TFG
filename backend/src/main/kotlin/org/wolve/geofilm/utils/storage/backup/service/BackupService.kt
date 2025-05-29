package org.wolve.geofilm.utils.storage.backup

@Service
class BackupService {

    private val backupFolder = File("data/backups") // puedes poner otra ruta

    init {
        if (!backupFolder.exists()) backupFolder.mkdirs()
    }

    fun exportBackup(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val backupFile = File(backupFolder, "backup_$timestamp.sql")

        val process = ProcessBuilder(
            "pg_dump",
            "--data-only", // solo datos, no estructura
            "--no-owner",
            "--no-privileges",
            "--file=${backupFile.absolutePath}",
            "--username=tu_usuario",       // ⚠️ CAMBIA esto
            "--dbname=tu_base_de_datos"    // ⚠️ CAMBIA esto
        )
            .inheritIO() // para mostrar errores en consola
            .start()

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("Error al exportar la base de datos, código de salida $exitCode")
        }

        return backupFile
    }
}