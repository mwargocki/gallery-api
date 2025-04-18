package com.warus.gallery.api.controller

import com.warus.gallery.api.service.FilterService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/filters")
class FilterController(
   private val filterService: FilterService
) {

   @GetMapping("/colors")
   fun getColors(): List<String> = filterService.getAllColors()

   @GetMapping("/types")
   fun getTypes(): List<String> = filterService.getAllTypes()

   @GetMapping("/materials")
   fun getMaterials(): List<String> = filterService.getAllMaterials()
}
