package org.wolve.geofilm.utils.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URLEncoder

/**
 * Mapeamos los campos de TMDb que necesitamos: id, vote_average, poster_path, release_date y overview.
 */
data class TmdbSearchResponse(
    @JsonProperty("results") val results: List<TmdbMovie>?
)

data class TmdbMovie(
    @JsonProperty("id")            val id: Int,
    @JsonProperty("vote_average")  val voteAverage: Double?,
    @JsonProperty("poster_path")   val posterPath: String?,
    @JsonProperty("release_date")  val releaseDate: String?,  // p.ej. "2018-12-21"
    @JsonProperty("overview")      val overview: String?      // sinopsis
)

@Component
class TmdbClient(
    private val restTemplate: RestTemplate,
    @Value("\${tmdb.api.key}") private val apiKey: String
) {
    private val logger = LoggerFactory.getLogger(TmdbClient::class.java)

    /**
     * Busca en TMDb por título (+ año opcional). Devuelve el primer resultado o null.
     */
    fun searchMovie(title: String, year: Int?): TmdbMovie? {
        logger.info("[TMDb] Buscando película: '$title' (año=$year)")
        return try {
            val encodedTitle = URLEncoder.encode(title, "UTF-8")
            val urlBuilder = StringBuilder("https://api.themoviedb.org/3/search/movie")
            urlBuilder.append("?api_key=").append(apiKey)
            urlBuilder.append("&query=").append(encodedTitle)
            year?.let { urlBuilder.append("&year=").append(it) }
            // aquí solicitamos los resultados en Español:
            urlBuilder.append("&language=es-ES")


            val url = urlBuilder.toString()
            val response = restTemplate.getForObject(url, TmdbSearchResponse::class.java)
            val movies = response?.results

            if (!movies.isNullOrEmpty()) {
                logger.info("[TMDb] Primer resultado para '$title': ${movies.first()}")
                movies.first()
            } else {
                logger.info("[TMDb] No hay resultados para '$title' (año=$year)")
                null
            }
        } catch (ex: HttpClientErrorException.Unauthorized) {
            logger.warn("[TMDb] 401 Unauthorized al consultar '$title' (año=$year)")
            null
        } catch (ex: Exception) {
            logger.warn("[TMDb] Error al buscar '$title' (año=$year): ${ex.message}")
            null
        }
    }

    /**
     * Construye la URL completa del póster (tamaño w500). Devuelve null si posterPath es nulo o vacío.
     */
    fun buildPosterUrl(posterPath: String?): String? {
        return posterPath?.takeIf { it.isNotBlank() }?.let { "https://image.tmdb.org/t/p/w500$it" }
    }
}
