package org.wolve.geofilm.producciones.produccion.dto

import org.springframework.data.domain.Page

data class ProduccionListResponse(
    val producciones: List<ProduccionResponse>,
    val total: Long,
    val page: Int,
    val size: Int
) {
    constructor(page: Page<ProduccionResponse>) : this(
        producciones = page.content,
        total = page.totalElements,
        page = page.number,
        size = page.size
    )
}