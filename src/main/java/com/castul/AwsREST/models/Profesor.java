package com.castul.AwsREST.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Profesor {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

   @NotNull
   private int numeroEmpleado;

   @NotNull
   private String nombres;

   @NotNull
   private String apellidos;

   @NotNull
   @PositiveOrZero
   private int horasClase;

   public Profesor() {
   }

   public Profesor(int id, int numeroEmpleado, String nombres, String apellidos, int horasClase) {
      this.id = id;
      this.numeroEmpleado = numeroEmpleado;
      this.nombres = nombres;
      this.apellidos = apellidos;
      this.horasClase = horasClase;
   }

   public int getId() {
      return id;
   }

   public int getNumeroEmpleado() {
      return numeroEmpleado;
   }

   public String getNombres() {
      return nombres;
   }

   public String getApellidos() {
      return apellidos;
   }

   public int getHorasClase() {
      return horasClase;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setNumeroEmpleado(int numeroEmpleado) {
      this.numeroEmpleado = numeroEmpleado;
   }

   public void setNombres(String nombres) {
      this.nombres = nombres;
   }

   public void setApellidos(String apellidos) {
      this.apellidos = apellidos;
   }

   public void setHorasClase(int horasClase) {
      this.horasClase = horasClase;
   }

}
