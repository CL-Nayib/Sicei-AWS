package com.castul.AwsREST.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Alumno {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

   @NotNull
   private String nombres;

   @NotNull
   private String apellidos;

   @NotNull
   private String matricula;

   @NotNull
   @PositiveOrZero
   private double promedio;

   private String fotoPerfilUrl;

   @NotNull
   private String password;

   public Alumno() {
   }

   public Alumno(int id, String nombres, String apellidos, String matricula, double promedio, String fotoPerfilUrl,
         String password) {
      this.id = id;
      this.nombres = nombres;
      this.apellidos = apellidos;
      this.matricula = matricula;
      this.promedio = promedio;
      this.fotoPerfilUrl = fotoPerfilUrl;
      this.password = password;
   }

   public int getId() {
      return id;
   }

   public String getNombres() {
      return nombres;
   }

   public String getApellidos() {
      return apellidos;
   }

   public String getMatricula() {
      return matricula;
   }

   public double getPromedio() {
      return promedio;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setNombres(String nombres) {
      this.nombres = nombres;
   }

   public void setApellidos(String apellidos) {
      this.apellidos = apellidos;
   }

   public void setMatricula(String matricula) {
      this.matricula = matricula;
   }

   public void setPromedio(double promedio) {
      this.promedio = promedio;
   }

   public String getFotoPerfilUrl() {
      return fotoPerfilUrl;
   }

   public void setFotoPerfilUrl(String fotoPerfilUrl) {
      this.fotoPerfilUrl = fotoPerfilUrl;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

}
