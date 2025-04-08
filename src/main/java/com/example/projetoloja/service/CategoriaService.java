package com.example.projetoloja.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.projetoloja.model.Categoria;
import com.example.projetoloja.repository.CategoriaRepository;
import com.example.projetoloja.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository; 

    public CategoriaService(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Map<String, Object>> obterEstoqueTotalPorCategoria() {
        List<Object[]> resultados = produtoRepository.quantidadeEstoquePorCategoria();
        List<Map<String, Object>> resposta = new java.util.ArrayList<>();

        for (Object[] linha : resultados) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("categoria", linha[0]);
            mapa.put("quantidadeTotalEstoque", linha[1]);
            resposta.add(mapa);
        }

        return resposta;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com id: " + id));
    }

    public Categoria criar(Categoria categoria) {
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            throw new DataIntegrityViolationException("Já existe uma categoria com esse nome.");
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria categoriaAtualizada) {
        Categoria categoriaExistente = buscarPorId(id);

        if (!categoriaExistente.getNome().equals(categoriaAtualizada.getNome())
                && categoriaRepository.existsByNome(categoriaAtualizada.getNome())) {
            throw new DataIntegrityViolationException("Já existe uma categoria com esse nome.");
        }

        categoriaExistente.setNome(categoriaAtualizada.getNome());
        return categoriaRepository.save(categoriaExistente);
    }

    public void deletar(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}

