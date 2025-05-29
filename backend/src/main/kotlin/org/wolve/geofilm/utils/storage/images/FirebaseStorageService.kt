import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FirebaseStorageService {

    private val storage: Storage = StorageOptions.getDefaultInstance().service
    private val bucketName = "movietrip-e3a91.appspot.com" // Tu bucket oficial

    /**
     * Sube una imagen a Firebase Storage y devuelve la URL pública.
     * @param file archivo recibido (MultipartFile)
     * @param folder carpeta destino en el bucket (ej: "producciones", "usuarios")
     * @return URL pública accesible para mostrar directamente en el frontend
     */
    fun uploadImage(file: MultipartFile, folder: String): String {
        val safeName = file.originalFilename?.replace(" ", "_") ?: "imagen.jpg"
        val filename = "$folder/${UUID.randomUUID()}_$safeName"

        val blobInfo = BlobInfo.newBuilder(bucketName, filename)
            .setContentType(file.contentType)
            .build()

        // Sube archivo
        storage.create(blobInfo, file.bytes)

        // Lo hace público
        storage.createAcl(blobInfo.blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))

        // Devuelve la URL pública (se puede usar directamente en <img>)
        return "https://storage.googleapis.com/$bucketName/$filename"
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
