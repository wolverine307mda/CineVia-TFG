package org.wolve.geofilm.utils.storage.images

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream
import java.util.* // Importa java.util.* si usas UUID en otro lugar, aunque no en este snippet

@Service
class FirebaseStorageService {

    private val storage: Storage
    private val bucketName = "movietrip-e3a91.firebasestorage.app"

    init {
        val serviceAccountStream = this::class.java.classLoader
            .getResourceAsStream("mi-clave-firebase.json")
            ?: throw IllegalStateException("No se encontró el archivo 'mi-clave-firebase.json' en resources.")

        val credentials = GoogleCredentials.fromStream(serviceAccountStream)

        this.storage = StorageOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .service
    }

    /**
     * Sube una imagen a Firebase Storage y devuelve la URL pública.
     * @param file archivo recibido (MultipartFile)
     * @param folder carpeta destino en el bucket (ej: "producciones", "usuarios")
     * @return URL pública accesible para mostrar directamente en el frontend
     */
    fun uploadImage(file: MultipartFile, folder: String, filename: String? = null): String {
        val safeName = filename ?: file.originalFilename?.replace(" ", "_") ?: "imagen.jpg"
        val fullPath = "$folder/$safeName"

        val blobInfo = BlobInfo.newBuilder(bucketName, fullPath)
            .setContentType(file.contentType)
            .build()

        storage.create(blobInfo, file.bytes)

        storage.createAcl(blobInfo.blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))

        return "https://storage-download.googleapis.com/$bucketName/$fullPath"
    }

    /**
     * Elimina una imagen del bucket dado su path relativo.
     * @param filePath ruta dentro del bucket (ej: "usuarios/abc.jpg")
     * @return true si fue eliminado correctamente
     * @throws IllegalArgumentException si no existe
     */
    fun deleteImage(filePath: String): Boolean {
        val blobId = BlobId.of(bucketName, filePath)
        val deleted = storage.delete(blobId)

        if (!deleted) {
            throw IllegalArgumentException("⚠️ La imagen '$filePath' no está alojada en este servidor.")
        }

        return true
    }
}
