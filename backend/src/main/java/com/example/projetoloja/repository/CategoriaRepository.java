package com.example.projetoloja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projetoloja.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNome(String nome);
}
