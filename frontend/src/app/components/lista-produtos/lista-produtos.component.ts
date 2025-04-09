import { Component, OnInit } from '@angular/core';
import { ProdutoService } from '../../services/produto.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-lista-produtos',
  templateUrl: './lista-produtos.component.html',
  styleUrls: ['./lista-produtos.component.css'],
  imports: [CommonModule]
})

export class ListaProdutosComponent implements OnInit {

  produtos: any[] = [];

  constructor(private produtoService: ProdutoService) {}

  ngOnInit(): void {
    this.produtoService.listarProdutos().subscribe(dados => {
      this.produtos = dados.content;
    });
  }

}

