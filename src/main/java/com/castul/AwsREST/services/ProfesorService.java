package com.castul.AwsREST.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castul.AwsREST.models.Profesor;
import com.castul.AwsREST.repositories.ProfesorRepository;

@Service
public class ProfesorService {

   @Autowired
   private ProfesorRepository profesorRepository;

   public List<Profesor> findAll() {
      return profesorRepository.findAll();
   }

   public Optional<Profesor> findById(int id) {
      return profesorRepository.findById(id);
   }

   public Profesor save(Profesor profesor) {
      return profesorRepository.save(profesor);
   }

   public Optional<Profesor> update(int id, Profesor profesorDetails) {
      return profesorRepository.findById(id).map(profesor -> {
         profesor.setNombres(profesorDetails.getNombres());
         profesor.setApellidos(profesorDetails.getApellidos());
         profesor.setNumeroEmpleado(profesorDetails.getNumeroEmpleado());
         profesor.setHorasClase(profesorDetails.getHorasClase());
         return profesorRepository.save(profesor);
      });
   }

   public boolean deleteById(int id) {
      if (profesorRepository.existsById(id)) {
         profesorRepository.deleteById(id);
         return true;
      }
      return false;
   }
}
