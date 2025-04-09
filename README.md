# Projeto Loja

Este é um projeto Full stack de uma aplicação de loja virtual, desenvolvido como parte de um teste técnico. A aplicação permite o cadastro, edição, listagem e exclusão de produtos, com filtros por nome e categoria. O projeto inclui back-end em Spring Boot, front-end em Angular 17 e uma estrutura inicial para rodar via Docker com banco de dados PostgreSQL.

---

## ✅ Funcionalidades Implementadas

### 🖥️ Front-end (Angular 17)

- Tela de **listagem de produtos**
  - Filtros por **nome** e **categoria**
  - Exibição de nome, descrição, preço, quantidade em estoque e categoria
- Botões de:
  - **Editar produto**
  - **Excluir produto**
  - **Criar novo produto**
- Formulário para:
  - **Cadastro e edição de produto**
  - Com **validações de campos** (nome, preço, estoque e categoria obrigatórios)

### 🚰 Back-end (Spring Boot)

- API REST para gerenciamento de produtos e categorias
- Regras de negócio:
- Tratamento global de erros e logs de serviço

---

## ⚙️ Tecnologias Utilizadas

- **Back-end:** Java 17, Spring Boot 3, JPA, PostgreSQL
- **Front-end:** Angular 17, TypeScript
- **Outros:** Maven, Docker (em desenvolvimento)

---

## 🚀 Como Executar Localmente

### Pré-requisitos

- Java 17
- Node.js e Angular CLI
- PostgreSQL

### 1. Back-end

- Configure o banco no `application.properties`:
  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/db_loja
  spring.datasource.username=postgres
  spring.datasource.password=G@mora
  ```
- Execute o projeto (`SpringBootApplication`)

### 2. Front-end

```bash
cd frontend
npm install
ng serve
```

Acesse: `http://localhost:4200`

---

## 🐳 Docker (opcional)

Existe uma **branch separada `docker-setup`** com a estrutura inicial para rodar a aplicação com Docker Compose. Essa branch contém:

- `docker-compose.yml`
- `init.sql` com dados de exemplo para popular o banco
- `Dockerfile` no backend
- Configuração para rodar PostgreSQL e o app juntos

> **Observação:** devido ao tempo, essa parte não foi finalizada/testada por completo, mas a estrutura está pronta para continuar.

---

## 📁 Organização do Repositório

```
projetoloja/
│
├── backend/                → Projeto Spring Boot
│   ├── src/                → Código-fonte Java
│   └── Dockerfile          → Configuração Docker do backend
│
├── frontend/               → Projeto Angular 17
│   └── src/                → Código-fonte Angular
│
├── db-init/init.sql        → Dados iniciais para o banco
├── docker-compose.yml      → Docker Compose com banco + backend
```

---

## ✉️ Contato

Fico à disposição para esclarecimentos ou dúvidas!
