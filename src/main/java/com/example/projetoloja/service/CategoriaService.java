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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaService.class);

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Map<String, Object>> obterEstoqueTotalPorCategoria() {
        log.info("Obtendo quantidade total de produtos em estoque por categoria.");
        List<Object[]> resultados = produtoRepository.quantidadeEstoquePorCategoria();
        List<Map<String, Object>> resposta = new java.util.ArrayList<>();

        for (Object[] linha : resultados) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("categoria", linha[0]);
            mapa.put("quantidadeTotalEstoque", linha[1]);
            resposta.add(mapa);
        }

        log.info("Consulta de estoque por categoria retornou {} registros.", resposta.size());
        return resposta;
    }

    public List<Categoria> listarTodas() {
        log.info("Listando todas as categorias.");
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        log.info("Buscando categoria por ID: {}", id);
        return categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoria com ID {} não encontrada.", id);
                    return new EntityNotFoundException("Categoria não encontrada com id: " + id);
                });
    }

    public Categoria criar(Categoria categoria) {
        log.info("Tentando criar categoria: {}", categoria.getNome());

        if (categoriaRepository.existsByNome(categoria.getNome())) {
            log.warn("Categoria '{}' já existe.", categoria.getNome());
            throw new DataIntegrityViolationException("Já existe uma categoria com esse nome.");
        }

        Categoria salva = categoriaRepository.save(categoria);
        log.info("Categoria '{}' criada com sucesso (ID: {}).", salva.getNome(), salva.getId());
        return salva;
    }

    public Categoria atualizar(Long id, Categoria categoriaAtualizada) {
        log.info("Atualizando categoria ID: {}", id);
        Categoria categoriaExistente = buscarPorId(id);

        if (!categoriaExistente.getNome().equals(categoriaAtualizada.getNome())
                && categoriaRepository.existsByNome(categoriaAtualizada.getNome())) {
            log.warn("Tentativa de atualizar para nome de categoria já existente: '{}'", categoriaAtualizada.getNome());
            throw new DataIntegrityViolationException("Já existe uma categoria com esse nome.");
        }

        categoriaExistente.setNome(categoriaAtualizada.getNome());
        Categoria atualizada = categoriaRepository.save(categoriaExistente);
        log.info("Categoria ID {} atualizada para nome: {}", id, atualizada.getNome());
        return atualizada;
    }

    public void deletar(Long id) {
        log.info("Deletando categoria ID: {}", id);
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
        log.info("Categoria ID {} deletada com sucesso.", id);
    }
}
