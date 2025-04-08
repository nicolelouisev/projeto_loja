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

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto salvarProduto(Produto produto) {
        boolean existe = produtoRepository.existsByNomeAndCategoria(produto.getNome(), produto.getCategoria());
        if (existe) {
            throw new RuntimeException("Produto com esse nome já existe na mesma categoria.");
        }
        return produtoRepository.save(produto);
    }
    
    public Page<Produto> listarProdutos(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }
    
    public List<Produto> buscarPorNomeECategoria(String nome, String categoria) {
        return produtoRepository.buscarPorNomeECategoria(nome, categoria);
    }
    
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado com id: " + id));
    }

    public Produto atualizarProduto(Long id, Produto produto) {
        Produto existente = buscarPorId(id);

        // Se o nome ou categoria mudar, verifica duplicidade
        if (!produto.getNome().equalsIgnoreCase(existente.getNome()) ||
            !produto.getCategoria().equals(existente.getCategoria())) {
            
            boolean existe = produtoRepository.existsByNomeAndCategoria(
                    produto.getNome(), produto.getCategoria());

            if (existe) {
                throw new RuntimeException("Produto com esse nome já existe na mesma categoria.");
            }
        }

        validarProduto(produto);
        produto.setId(id);
        return produtoRepository.save(produto);
    }

    
    public void deletarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (produto.getQuantidadeEstoque() > 0) {
            throw new RuntimeException("Não é possível excluir produtos com estoque maior que 0.");
        }

        produtoRepository.delete(produto);
    }

    private void validarProduto(Produto produto) {
        // Nome não pode ser nulo ou vazio
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }

        // Preço não pode ser nulo ou negativo
        if (produto.getPreco() == null) {
            throw new IllegalArgumentException("Preço é obrigatório.");
        }
        if (produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }

        // Quantidade em estoque não pode ser nula ou negativa
        if (produto.getQuantidadeEstoque() == null) {
            throw new IllegalArgumentException("Quantidade em estoque é obrigatória.");
        }
        if (produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa.");
        }

        // Categoria não pode ser nula e deve existir no banco
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }
        if (!categoriaRepository.existsById(produto.getCategoria().getId())) {
            throw new IllegalArgumentException("Categoria informada não existe.");
        }
    }
    
    public List<Map<String, Object>> obterEstoquePorCategoria() {
        List<Object[]> resultados = produtoRepository.quantidadeEstoquePorCategoria();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Map<String, Object> item = new HashMap<>();
            item.put("categoria", resultado[0]);
            item.put("quantidadeTotal", resultado[1]);
            lista.add(item);
        }

        return lista;
    }
}
