import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProdutoService, Produto } from '../../services/produto.service';
import { CategoriaService } from '../../services/categoria.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-form-produto',
  templateUrl: './form-produto.component.html',
  styleUrls: ['./form-produto.component.css'],
  imports: [CommonModule, FormsModule]
})
export class FormProdutoComponent implements OnInit {

  produto: Produto = {
    nome: '',
    descricao: '',
    preco: 0,
    quantidadeEstoque: 0,
    categoria: { id: 0, nome: '' }
  };

  categorias: any[] = [];
  modoEdicao: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private produtoService: ProdutoService,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.modoEdicao = true;
      this.produtoService.buscarPorId(Number(id)).subscribe(dados => {
        this.produto = dados;
      });
    }

    this.categoriaService.listar().subscribe(dados => {
      this.categorias = dados;
    });
  }

  salvar() {
    if (this.produto.categoria && typeof this.produto.categoria.id === 'string') {
      this.produto.categoria.id = parseInt(this.produto.categoria.id, 10);
    }

    const produtoCorrigido = {
      ...this.produto,
      categoria: {
        id: this.produto.categoria.id,
        nome: this.produto.categoria.nome
      }
    };

    console.log('Produto FINAL para envio (JSON):', JSON.stringify(produtoCorrigido));

    if (this.modoEdicao) {
      this.produtoService.atualizar(this.produto.id!, produtoCorrigido).subscribe(() => {
        alert('Produto atualizado com sucesso!');
        this.router.navigate(['/']);
      });
    } else {
      this.produtoService.criar(produtoCorrigido).subscribe(() => {
        alert('Produto criado com sucesso!');
        this.router.navigate(['/']);
      });
    }
  }


  cancelar() {
    this.router.navigate(['/']);
  }
}
