package com.warus.gallery.api.db.repository

import com.warus.gallery.api.db.model.Angel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface AngelRepository : JpaRepository<Angel, Long>, JpaSpecificationExecutor<Angel> {

   @Query("SELECT DISTINCT a.color FROM Angel a WHERE a.color IS NOT NULL")
   fun findAllDistinctColors(): List<String>

   @Query("SELECT DISTINCT a.type FROM Angel a WHERE a.type IS NOT NULL")
   fun findAllDistinctTypes(): List<String>

   @Query("SELECT DISTINCT a.material FROM Angel a WHERE a.material IS NOT NULL")
   fun findAllDistinctMaterials(): List<String>

}
