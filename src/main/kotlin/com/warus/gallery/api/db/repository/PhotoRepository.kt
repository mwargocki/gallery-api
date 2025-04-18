package com.warus.gallery.api.db.repository

import com.warus.gallery.api.db.model.Photo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface PhotoRepository : JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

   @Query("SELECT DISTINCT p.color FROM Photo p WHERE p.color IS NOT NULL")
   fun findAllDistinctColors(): List<String>

   @Query("SELECT DISTINCT p.type FROM Photo p WHERE p.type IS NOT NULL")
   fun findAllDistinctTypes(): List<String>

   @Query("SELECT DISTINCT p.material FROM Photo p WHERE p.material IS NOT NULL")
   fun findAllDistinctMaterials(): List<String>

}
