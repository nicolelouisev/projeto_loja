import { Component, OnInit } from '@angular/core';
import { ProdutoService } from '../../services/produto.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-lista-produtos',
  templateUrl: './lista-produtos.component.html',
  styleUrls: ['./lista-produtos.component.css'],
  imports: [CommonModule, FormsModule, RouterModule]
})
export class ListaProdutosComponent implements OnInit {
  produtos: any[] = [];
  filtroNome: string = '';
  filtroCategoria: string = '';

  constructor(private produtoService: ProdutoService) {}

  ngOnInit(): void {
    this.carregarProdutos();
  }

  carregarProdutos(): void {
    this.produtoService.listarProdutos().subscribe(dados => {
      this.produtos = dados.content;
    });
  }

  buscarComFiltros(): void {
    this.produtoService.buscarPorNomeECategoria(this.filtroNome, this.filtroCategoria).subscribe(dados => {
      this.produtos = dados;
    });
  }

  deletar(produto: any) {
    if (produto.quantidadeEstoque > 0) {
      alert('Este produto não pode ser excluído porque ainda possui itens em estoque.');
      return;
    }

    if (confirm('Tem certeza que deseja excluir?')) {
      this.produtoService.deletar(produto.id).subscribe(() => {
        alert('Produto excluído com sucesso!');
        this.carregarProdutos();
      });
    }
  }


}
