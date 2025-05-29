package org.wolve.geofilm.auth.dto

data class DeviceInfo(
    val type: String,
    val os: String,
    val browser: String,
    val userAgent: String
)