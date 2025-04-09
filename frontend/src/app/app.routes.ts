import { Routes } from '@angular/router';
import { ListaProdutosComponent } from './components/lista-produtos/lista-produtos.component';
import { FormProdutoComponent } from './components/form-produto/form-produto.component';

export const routes: Routes = [
  { path: '', component: ListaProdutosComponent },
  { path: 'produto/novo', component: FormProdutoComponent },
  { path: 'produto/editar/:id', component: FormProdutoComponent },
  { path: '**', redirectTo: '' }
];
