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

import com.castul.AwsREST.models.Alumno;
import com.castul.AwsREST.services.AlumnoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

   private final AlumnoService alumnoService;

   public AlumnoController(AlumnoService alumnoService) {
      this.alumnoService = alumnoService;
   }

   @GetMapping
   public ResponseEntity<List<Alumno>> getAllAlumnos() {
      return ResponseEntity.ok(alumnoService.findAll());
   }

   @GetMapping("/{id}")
   public ResponseEntity<Alumno> getAlumnoById(@PathVariable int id) {
      return alumnoService.findById(id).map(ResponseEntity::ok)
            .orElse(ResponseEntity
                  .status(HttpStatus.NOT_FOUND).build());
   }

   @PostMapping
   public ResponseEntity<Alumno> createAlumno(@RequestBody @Valid Alumno alumno) {
      return ResponseEntity.status(HttpStatus.CREATED).body(alumnoService.save(alumno));
   }

   @PutMapping("/{id}")
   public ResponseEntity<Alumno> updateAlumno(@PathVariable int id, @RequestBody @Valid Alumno alumno) {
      return alumnoService.update(id, alumno).map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Alumno> deleteAlumno(@PathVariable int id) {
      return alumnoService.deleteById(id)
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
   }

}
