package com.castul.AwsREST.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.castul.AwsREST.models.Profesor;
import com.castul.AwsREST.services.ProfesorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

   private final ProfesorService profesorService;

   public ProfesorController(ProfesorService profesorService) {
      this.profesorService = profesorService;
   }

   @GetMapping
   public ResponseEntity<List<Profesor>> getAllProfesores() {
      return ResponseEntity.ok(profesorService.findAll());
   }

   @GetMapping("/{id}")
   public ResponseEntity<Profesor> getProfesoresById(@PathVariable int id) {
      return profesorService.findById(id).map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
   }

   @PostMapping
   public ResponseEntity<Profesor> createrProfesor(@RequestBody @Valid Profesor profesor) {
      return ResponseEntity.status(HttpStatus.CREATED).body(profesorService.save(profesor));
   }

   @PutMapping("/{id}")
   public ResponseEntity<Profesor> updateProfesor(@PathVariable int id, @RequestBody @Valid Profesor profesor) {
      return profesorService.update(id, profesor).map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Profesor> deleteProfesor(@PathVariable int id) {
      return profesorService.deleteById(id)
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
   }
}
