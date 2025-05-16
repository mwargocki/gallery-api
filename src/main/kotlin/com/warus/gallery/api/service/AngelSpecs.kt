package com.warus.gallery.api.service

import com.warus.gallery.api.db.model.Angel
import org.springframework.data.jpa.domain.Specification

object AngelSpecs {
   fun hasColor(color: String) = Specification<Angel> { root, _, cb ->
      cb.equal(cb.lower(root.get("color")), color.lowercase())
   }

   fun hasAnyColor(colors: List<String>) = Specification<Angel> { root, _, cb ->
      val predicates = colors.map {
         cb.equal(cb.lower(root.get("color")), it.lowercase())
      }
      cb.or(*predicates.toTypedArray())
   }

   fun hasType(type: String) = Specification<Angel> { root, _, cb ->
      cb.equal(cb.lower(root.get("type")), type.lowercase())
   }

   fun hasAnyType(types: List<String>) = Specification<Angel> { root, _, cb ->
      val predicates = types.map {
         cb.equal(cb.lower(root.get("type")), it.lowercase())
      }
      cb.or(*predicates.toTypedArray())
   }

   fun hasMaterial(material: String) = Specification<Angel> { root, _, cb ->
      cb.equal(cb.lower(root.get("material")), material.lowercase())
   }

   fun hasAnyMaterial(materials: List<String>) = Specification<Angel> { root, _, cb ->
      val predicates = materials.map {
         cb.equal(cb.lower(root.get("material")), it.lowercase())
      }
      cb.or(*predicates.toTypedArray())
   }

   fun hasMinHeight(minHeight: Double) = Specification<Angel> { root, _, cb ->
      cb.greaterThanOrEqualTo(root.get("height"), minHeight)
   }

   fun hasMaxHeight(maxHeight: Double) = Specification<Angel> { root, _, cb ->
      cb.lessThanOrEqualTo(root.get("height"), maxHeight)
   }
}