package com.example.projetoloja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projetoloja.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
