package com.castul.AwsREST.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.castul.AwsREST.models.Profesor;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
}