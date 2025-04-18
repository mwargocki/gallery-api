package com.warus.gallery.api.service

import com.warus.gallery.api.db.model.Photo
import org.springframework.data.jpa.domain.Specification

object PhotoSpecs {
   fun hasColor(color: String) = Specification<Photo> { root, _, cb ->
      cb.equal(cb.lower(root.get("color")), color.lowercase())
   }

   fun hasType(type: String) = Specification<Photo> { root, _, cb ->
      cb.equal(cb.lower(root.get("type")), type.lowercase())
   }

   fun hasMaterial(material: String) = Specification<Photo> { root, _, cb ->
      cb.equal(cb.lower(root.get("material")), material.lowercase())
   }

   fun hasMinHeight(minHeight: Int) = Specification<Photo> { root, _, cb ->
      cb.greaterThanOrEqualTo(root.get("height"), minHeight)
   }

   fun hasMaxHeight(maxHeight: Int) = Specification<Photo> { root, _, cb ->
      cb.lessThanOrEqualTo(root.get("height"), maxHeight)
   }
}