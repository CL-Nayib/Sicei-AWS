package com.castul.AwsREST.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.castul.AwsREST.models.Alumno;

@Service
public class AlumnoService {

   private final List<Alumno> alumnos = new ArrayList<>();
   // private int idCounter = 1;

   public List<Alumno> findAll() {
      return alumnos;
   }

   public Optional<Alumno> findById(int id) {
      return alumnos.stream().filter(alumno -> alumno.getId() == id).findFirst();
   }

   public Alumno save(Alumno alumno) {
      // alumno.setId(idCounter++); Ay profe, pq no avisa que el id no se debe auto
      // generar >:v
      alumnos.add(alumno);
      return alumno;
   }

   public Optional<Alumno> update(int id, Alumno alumnoDetails) {
      Optional<Alumno> alumnoOpt = findById(id);
      alumnoOpt.ifPresent(alumno -> {
         alumno.setNombres(alumnoDetails.getNombres());
         alumno.setApellidos(alumnoDetails.getApellidos());
         alumno.setMatricula(alumnoDetails.getMatricula());
         alumno.setPromedio(alumnoDetails.getPromedio());
      });
      return alumnoOpt;
   }

   public boolean deleteById(int id) {
      return alumnos.removeIf(alumno -> alumno.getId() == id);
   }

}
