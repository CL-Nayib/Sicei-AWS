package com.castul.AwsREST.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.castul.AwsREST.models.Alumno;
import com.castul.AwsREST.services.AlumnoService;
import com.castul.AwsREST.services.S3Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

   @Autowired
   private final AlumnoService alumnoService;

   @Autowired
   private S3Service s3Service;

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

   @PostMapping("/{id}/fotoPerfil")
   public ResponseEntity<?> uploadFotoPerfil(@PathVariable int id, @RequestParam("file") MultipartFile file) {

      Alumno alumno = alumnoService.findById(id).orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

      try {
         Path tempFile = Files.createTempFile("fotoPerfil", file.getOriginalFilename());
         file.transferTo(tempFile);

         String fotoPerfilUrl = s3Service.uploadFile(tempFile, "fotos-perfil/" + file.getOriginalFilename());

         alumno.setFotoPerfilUrl(fotoPerfilUrl);
         alumnoService.save(alumno);
         return ResponseEntity.ok("Foto de perfil subida con éxito: " + fotoPerfilUrl);
      } catch (IOException e) {
         return ResponseEntity.status(500).body("Error al procesar el archivo");
      }
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
