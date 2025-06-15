package org.wolve.geofilm.utils.pagination

import org.springframework.data.domain.Page

data class PaginatedResponse<T>(
    val data: List<T>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int
) {
    companion object {
        fun <T> fromPage(page: Page<T>): PaginatedResponse<T> {
            return PaginatedResponse(
                data = page.content,
                totalItems = page.totalElements,
                totalPages = page.totalPages,
                currentPage = page.number,
                pageSize = page.size
            )
        }
    }
}