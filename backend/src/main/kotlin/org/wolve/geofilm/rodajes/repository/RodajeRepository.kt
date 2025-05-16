package org.wolve.geofilm.rodajes.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.wolve.geofilm.rodajes.models.Rodaje

interface RodajeRepository : JpaRepository<Rodaje, String>