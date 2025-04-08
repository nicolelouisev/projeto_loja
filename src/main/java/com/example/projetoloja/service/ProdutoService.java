package com.example.projetoloja.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.projetoloja.model.Produto;
import com.example.projetoloja.repository.CategoriaRepository;
import com.example.projetoloja.repository.ProdutoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProdutoService {

    private static final Logger log = LoggerFactory.getLogger(ProdutoService.class);

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto salvarProduto(Produto produto) {
        log.info("Tentando salvar produto: {}", produto.getNome());

        boolean existe = produtoRepository.existsByNomeAndCategoria(produto.getNome(), produto.getCategoria());
        if (existe) {
            log.warn("Produto '{}' já existe na categoria '{}'", produto.getNome(), produto.getCategoria().getNome());
            throw new RuntimeException("Produto com esse nome já existe na mesma categoria.");
        }

        Produto salvo = produtoRepository.save(produto);
        log.info("Produto '{}' salvo com sucesso (ID: {})", salvo.getNome(), salvo.getId());
        return salvo;
    }

    public Page<Produto> listarProdutos(Pageable pageable) {
        log.info("Listando produtos de forma paginada. Página: {}, Tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        return produtoRepository.findAll(pageable);
    }

    public List<Produto> buscarPorNomeECategoria(String nome, String categoria) {
        log.info("Buscando produtos com nome contendo '{}' e categoria contendo '{}'", nome, categoria);
        return produtoRepository.buscarPorNomeECategoria(nome, categoria);
    }

    public Produto buscarPorId(Long id) {
        log.info("Buscando produto por ID: {}", id);
        return produtoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Produto com ID {} não encontrado", id);
                    return new NoSuchElementException("Produto não encontrado com id: " + id);
                });
    }

    public Produto atualizarProduto(Long id, Produto produto) {
        log.info("Atualizando produto ID: {}", id);
        Produto existente = buscarPorId(id);

        if (!produto.getNome().equalsIgnoreCase(existente.getNome()) ||
            !produto.getCategoria().equals(existente.getCategoria())) {

            boolean existe = produtoRepository.existsByNomeAndCategoria(
                    produto.getNome(), produto.getCategoria());

            if (existe) {
                log.warn("Produto duplicado: nome '{}' já existe na categoria '{}'", 
                    produto.getNome(), produto.getCategoria().getNome());
                throw new RuntimeException("Produto com esse nome já existe na mesma categoria.");
            }
        }

        validarProduto(produto);
        produto.setId(id);
        Produto atualizado = produtoRepository.save(produto);
        log.info("Produto ID {} atualizado com sucesso", id);
        return atualizado;
    }

    public void deletarProduto(Long id) {
        log.info("Tentando deletar produto ID: {}", id);
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Produto com ID {} não encontrado para exclusão", id);
                return new RuntimeException("Produto não encontrado");
            });

        if (produto.getQuantidadeEstoque() > 0) {
            log.warn("Produto ID {} não pode ser excluído. Estoque: {}", id, produto.getQuantidadeEstoque());
            throw new RuntimeException("Não é possível excluir produtos com estoque maior que 0.");
        }

        produtoRepository.delete(produto);
        log.info("Produto ID {} deletado com sucesso", id);
    }

    private void validarProduto(Produto produto) {
        log.info("Validando produto: {}", produto.getNome());

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            log.error("Validação falhou: nome do produto é vazio.");
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }

        if (produto.getPreco() == null) {
            log.error("Validação falhou: preço não informado.");
            throw new IllegalArgumentException("Preço é obrigatório.");
        }

        if (produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            log.error("Validação falhou: preço negativo.");
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }

        if (produto.getQuantidadeEstoque() == null) {
            log.error("Validação falhou: quantidade em estoque não informada.");
            throw new IllegalArgumentException("Quantidade em estoque é obrigatória.");
        }

        if (produto.getQuantidadeEstoque() < 0) {
            log.error("Validação falhou: quantidade em estoque negativa.");
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa.");
        }

        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            log.error("Validação falhou: categoria ausente.");
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }

        if (!categoriaRepository.existsById(produto.getCategoria().getId())) {
            log.error("Validação falhou: categoria ID {} não encontrada no banco.", produto.getCategoria().getId());
            throw new IllegalArgumentException("Categoria informada não existe.");
        }

        log.info("Produto '{}' validado com sucesso.", produto.getNome());
    }

    public List<Map<String, Object>> obterEstoquePorCategoria() {
        log.info("Obtendo total de produtos em estoque por categoria.");
        List<Object[]> resultados = produtoRepository.quantidadeEstoquePorCategoria();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Map<String, Object> item = new HashMap<>();
            item.put("categoria", resultado[0]);
            item.put("quantidadeTotal", resultado[1]);
            lista.add(item);
        }

        log.info("Consulta de estoque por categoria retornou {} registros.", lista.size());
        return lista;
    }
}
