package com.castul.AwsREST.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.castul.AwsREST.models.Profesor;

@Service
public class ProfesorService {

   private final List<Profesor> profesores = new ArrayList<>();
   // private int idCounter = 1;

   public List<Profesor> findAll() {
      return profesores;
   }

   public Optional<Profesor> findById(int id) {
      return profesores.stream().filter(profesor -> profesor.getId() == id).findFirst();
   }

   public Profesor save(Profesor profesor) {
      // profesor.setId(idCounter++); >:V
      profesores.add(profesor);
      return profesor;
   }

   public Optional<Profesor> update(int id, Profesor profesorDetails) {
      Optional<Profesor> profesorOpt = findById(id);
      profesorOpt.ifPresent(profesor -> {
         profesor.setNombres(profesorDetails.getNombres());
         profesor.setApellidos(profesorDetails.getApellidos());
         profesor.setHorasClase(profesorDetails.getHorasClase());
         profesor.setNumeroEmpleado(profesorDetails.getNumeroEmpleado());
      });
      return profesorOpt;
   }

   public boolean deleteById(int id) {
      return profesores.removeIf(profesor -> profesor.getId() == id);
   }
}
