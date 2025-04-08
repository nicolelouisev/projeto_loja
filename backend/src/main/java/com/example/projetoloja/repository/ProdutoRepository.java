package com.example.projetoloja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.projetoloja.model.Categoria;
import com.example.projetoloja.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	boolean existsByNomeAndCategoria(String nome, Categoria categoria);
	
	@Query("SELECT p FROM Produto p " +
	           "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
	           "AND LOWER(p.categoria.nome) LIKE LOWER(CONCAT('%', :categoriaNome, '%'))")
	    List<Produto> buscarPorNomeECategoria(@Param("nome") String nome,
	                                          @Param("categoriaNome") String categoriaNome);
	
	@Query("SELECT p.categoria.nome, SUM(p.quantidadeEstoque) " +
		       "FROM Produto p GROUP BY p.categoria.nome")
		List<Object[]> quantidadeEstoquePorCategoria();
}
