package org.wolve.geofilm.utils.pagination

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

object PaginationUtils {
    data class PaginatedResponse<T>(
        val data: List<T>,
        val totalItems: Long,
        val totalPages: Int,
        val currentPage: Int,
        val pageSize: Int
    )

    fun <T> toPaginatedResponse(page: Page<T>): PaginatedResponse<T> {
        return PaginatedResponse(
            data = page.content,
            totalItems = page.totalElements,
            totalPages = page.totalPages,
            currentPage = page.number,
            pageSize = page.size
        )
    }

    fun createPageable(
        page: Int,
        size: Int,
        sortBy: List<String> = listOf("id"),
        sortDirection: String = "asc",
        allowedSortFields: Set<String> = emptySet()
    ): Pageable {
        val validPage = page.coerceAtLeast(0)
        val validSize = size.coerceIn(1, 100)

        val direction = if (sortDirection.equals("desc", ignoreCase = true)) {
            Sort.Direction.DESC
        } else {
            Sort.Direction.ASC
        }

        val validatedSortFields = if (allowedSortFields.isNotEmpty()) {
            sortBy.filter { it in allowedSortFields }.ifEmpty { listOf("id") }
        } else {
            sortBy
        }

        val sort = Sort.by(validatedSortFields.map { Sort.Order(direction, it) })
        return PageRequest.of(validPage, validSize, sort)
    }
}