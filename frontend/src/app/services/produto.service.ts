import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Categoria {
  id: number;
  nome: string;
}

export interface Produto {
  id?: number;
  nome: string;
  descricao: string;
  preco: number;
  quantidadeEstoque: number;
  categoria: Categoria;
}

@Injectable({
  providedIn: 'root'
})
export class ProdutoService {

  private readonly API = 'http://localhost:8080/produtos';

  constructor(private http: HttpClient) {}

  listarProdutos(): Observable<any> {
    return this.http.get<any>(this.API); // retorna paginado
  }

  criar(produto: Produto): Observable<Produto> {
    return this.http.post<Produto>(this.API, produto);
  }

  buscarPorNomeECategoria(nome: string, categoria: string): Observable<Produto[]> {
    const params = new HttpParams()
      .set('nome', nome)
      .set('categoria', categoria);
    return this.http.get<Produto[]>(`${this.API}/buscar`, { params });
  }

  atualizar(id: number, produto: Produto): Observable<Produto> {
    return this.http.put<Produto>(`${this.API}/${id}`, produto);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
