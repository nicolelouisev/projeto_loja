import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Categoria {
  id: number;
  nome?: string;
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

  buscarPorId(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.API}/${id}`);
  }


  criar(produto: Produto): Observable<Produto> {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    return this.http.post<Produto>(this.API, produto, httpOptions);
  }

  buscarPorNomeECategoria(nome: string, categoria: string): Observable<any> {
    return this.http.get(`${this.API}/buscar?nome=${nome}&categoria=${categoria}`);
  }


  atualizar(id: number, produto: Produto): Observable<Produto> {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    return this.http.put<Produto>(`${this.API}/${id}`, produto, httpOptions);
  }



  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
