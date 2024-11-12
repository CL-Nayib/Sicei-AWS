package com.castul.AwsREST.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class Alumno {

   @NotNull
   @PositiveOrZero
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

   public Alumno(int id, String nombres, String apellidos, String matricula, double promedio) {
      this.id = id;
      this.nombres = nombres;
      this.apellidos = apellidos;
      this.matricula = matricula;
      this.promedio = promedio;
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

}
