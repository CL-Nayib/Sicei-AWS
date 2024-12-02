package com.castul.AwsREST.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castul.AwsREST.models.Alumno;
import com.castul.AwsREST.repositories.AlumnoRepository;

@Service
public class AlumnoService {

   @Autowired
   private AlumnoRepository alumnoRepository;

   public List<Alumno> findAll() {
      return alumnoRepository.findAll();
   }

   public Optional<Alumno> findById(int id) {
      return alumnoRepository.findById(id);
   }

   public Alumno save(Alumno alumno) {
      return alumnoRepository.save(alumno);
   }

   public Optional<Alumno> update(int id, Alumno alumnoDetails) {
      return alumnoRepository.findById(id).map(alumno -> {
         alumno.setNombres(alumnoDetails.getNombres());
         alumno.setApellidos(alumnoDetails.getApellidos());
         alumno.setMatricula(alumnoDetails.getMatricula());
         alumno.setPromedio(alumnoDetails.getPromedio());
         return alumnoRepository.save(alumno);
      });
   }

   public boolean deleteById(int id) {
      if (alumnoRepository.existsById(id)) {
         alumnoRepository.deleteById(id);
         return true;
      }
      return false;
   }

}
