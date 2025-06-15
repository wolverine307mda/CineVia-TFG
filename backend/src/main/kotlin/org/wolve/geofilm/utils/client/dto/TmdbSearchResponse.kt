package org.wolve.geofilm.utils.client.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.wolve.geofilm.utils.client.model.TmdbMovie

data class TmdbSearchResponse(
    @JsonProperty("results") val results: List<TmdbMovie>?
)