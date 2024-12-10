package com.castul.AwsREST.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.castul.AwsREST.services.SessionService;
import com.castul.AwsREST.services.SimpleNotificationService;

import jakarta.validation.Valid;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

   @Autowired
   private final AlumnoService alumnoService;
   private final SimpleNotificationService simpleNotificationService;

   @Autowired
   private S3Service s3Service;

   private final SessionService sessionService;

   public AlumnoController(AlumnoService alumnoService, SimpleNotificationService simpleNotificationService,
         SessionService sessionService) {
      this.alumnoService = alumnoService;
      this.simpleNotificationService = simpleNotificationService;
      this.sessionService = sessionService;
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
   public ResponseEntity<Map<String, Object>> uploadFotoPerfil(@PathVariable int id,
         @RequestParam("foto") MultipartFile file) {

      Alumno alumno = alumnoService.findById(id).orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

      try {
         // Crear archivo temporal
         Path tempFile = Files.createTempFile("fotoPerfil", file.getOriginalFilename());
         file.transferTo(tempFile);

         String fotoPerfilUrl = s3Service.uploadFile(tempFile, "fotoPerfilUrl/" + file.getOriginalFilename());
         alumno.setFotoPerfilUrl(fotoPerfilUrl);
         alumnoService.save(alumno);

         Map<String, Object> response = new HashMap<>();
         response.put("message", "Foto de perfil subida con éxito");
         response.put("fotoPerfilUrl", fotoPerfilUrl);
         response.put("success", true);

         return ResponseEntity.ok(response);
      } catch (IOException e) {
         Map<String, Object> response = new HashMap<>();
         response.put("message", "Error al procesar el archivo");
         response.put("success", false);

         return ResponseEntity.status(500).body(response);
      }
   }

   @PostMapping("/{id}/email")
   public ResponseEntity<Map<String, Object>> sendEmailNotification(@PathVariable int id) {
      Alumno alumno = alumnoService.findById(id).orElse(null);

      if (alumno == null) {
         Map<String, Object> response = new HashMap<>();
         response.put("message", "Alumno no encontrado");
         response.put("success", false);
         return ResponseEntity.status(404).body(response);
      }

      String subject = "Calificaciones de " + alumno.getNombres() + " " + alumno.getApellidos();
      String message = "Nombre: " + alumno.getNombres() + " " + alumno.getApellidos() +
            "\nMatrícula: " + alumno.getMatricula() +
            "\nPromedio: " + alumno.getPromedio();

      String messageId = simpleNotificationService.sendNotification(subject, message);

      Map<String, Object> response = new HashMap<>();
      response.put("message", "Notificación enviada");
      response.put("data", messageId);
      response.put("success", true);

      return ResponseEntity.ok(response);
   }

   @PostMapping("/{id}/session/login")
   public ResponseEntity<Map<String, Object>> login(@PathVariable int id, @RequestBody Map<String, String> body) {
      String password = body.get("password");
      Alumno alumno = alumnoService.findById(id).orElse(null);

      if (alumno == null || !alumno.getPassword().equals(password)) {
         Map<String, Object> response = new HashMap<>();
         response.put("message", "Credenciales incorrectas");
         response.put("success", false);
         return ResponseEntity.status(400).body(response);
      }

      String sessionString = generateRandomString(128);
      sessionService.createSession(id, sessionString);

      // Responder con el sessionString en formato JSON
      Map<String, Object> response = new HashMap<>();
      response.put("message", "Sesión creada con éxito");
      response.put("sessionString", sessionString);
      response.put("success", true);

      return ResponseEntity.ok(response);
   }

   @PostMapping("/{id}/session/verify")
   public ResponseEntity<Map<String, Object>> verifySession(@PathVariable int id,
         @RequestBody Map<String, String> body) {
      String sessionString = body.get("sessionString");

      Map<String, AttributeValue> session = sessionService.getSession(id, sessionString);
      Map<String, Object> response = new HashMap<>();
      if (session == null || !Boolean.parseBoolean(session.get("active").bool().toString())) {
         response.put("message", "Sesión no válida");
         response.put("success", false);
         return ResponseEntity.status(400).body(response);
      }

      response.put("message", "Sesión válida");
      response.put("success", true);
      return ResponseEntity.ok(response);
   }

   @PostMapping("/{id}/session/logout")
   public ResponseEntity<Map<String, Object>> logoutSession(@PathVariable String id,
         @RequestBody Map<String, String> body) {
      String sessionString = body.get("sessionString");

      boolean success = sessionService.logoutSession(sessionString);

      Map<String, Object> response = new HashMap<>();
      if (success) {
         response.put("message", "Sesión cerrada exitosamente");
         response.put("success", true);
         return ResponseEntity.ok(response);
      } else {
         response.put("message", "No se pudo cerrar la sesión");
         response.put("success", false);
         return ResponseEntity.status(400).body(response);
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

   private String generateRandomString(int length) {
      String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      StringBuilder result = new StringBuilder();
      for (int i = 0; i < length; i++) {
         int index = (int) (Math.random() * characters.length());
         result.append(characters.charAt(index));
      }
      return result.toString();
   }

}
