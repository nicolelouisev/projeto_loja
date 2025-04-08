package com.example.projetoloja.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.projetoloja.model.Produto;
import com.example.projetoloja.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        Produto salvo = produtoService.salvarProduto(produto);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<Page<Produto>> listarProdutos(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarProdutos(pageable));
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Produto>> buscarPorNomeECategoria(
            @RequestParam String nome,
            @RequestParam String categoria) {
        List<Produto> produtos = produtoService.buscarPorNomeECategoria(nome, categoria);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }
    
    @GetMapping("/estoque-por-categoria")
    public ResponseEntity<List<Map<String, Object>>> estoquePorCategoria() {
        return ResponseEntity.ok(produtoService.obterEstoquePorCategoria());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        Produto atualizado = produtoService.atualizarProduto(id, produto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
