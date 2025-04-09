-- Criação de categorias
INSERT INTO categoria (id, nome) VALUES
(1, 'Roupas'),
(2, 'Eletrônicos'),
(3, 'Livros');

-- Criação de produtos
INSERT INTO produto (nome, descricao, preco, quantidade_estoque, categoria_id) VALUES
('Camiseta básica', 'Camiseta algodão tamanho M', 49.90, 100, 1),
('Calça jeans', 'Calça jeans azul escuro', 89.90, 50, 1),
('Notebook Dell', 'Notebook com 8GB RAM e SSD 256GB', 3499.90, 10, 2),
('Fone Bluetooth', 'Fone de ouvido sem fio com microfone', 199.90, 25, 2),
('O Senhor dos Anéis', 'Livro de fantasia épica', 59.90, 40, 3),
('Clean Code', 'Livro sobre boas práticas de programação', 89.90, 30, 3);
