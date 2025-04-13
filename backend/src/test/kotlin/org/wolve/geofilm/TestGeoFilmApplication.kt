package org.wolve.geofilm

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<GeoFilmApplication>().with(TestcontainersConfiguration::class).run(*args)
}
