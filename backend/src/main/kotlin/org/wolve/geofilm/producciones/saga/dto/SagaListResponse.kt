package org.wolve.geofilm.producciones.saga.dto

import org.springframework.data.domain.Page

data class SagaListResponse(
    val sagas: Page<SagaSimpleResponse>,
    val total: Long,
    val page: Int,
    val size: Int
)

