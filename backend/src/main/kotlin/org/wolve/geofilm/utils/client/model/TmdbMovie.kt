package org.wolve.geofilm.utils.client.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TmdbMovie(
    @JsonProperty("id")            val id: Int,
    @JsonProperty("vote_average")  val voteAverage: Double?,
    @JsonProperty("poster_path")   val posterPath: String?,
    @JsonProperty("release_date")  val releaseDate: String?,
    @JsonProperty("overview")      val overview: String?
)