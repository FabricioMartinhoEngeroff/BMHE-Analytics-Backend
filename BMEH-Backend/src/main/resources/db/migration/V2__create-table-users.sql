CREATE TABLE users (
    id UUID PRIMARY KEY,
    login VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    rua VARCHAR(255) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    bairro VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(10) NOT NULL
);

INSERT INTO users (id, login, email, password, cpf, telefone, rua, numero, bairro, cidade, estado, cep)
VALUES (
    'd193afd4-9222-4150-aadb-5167405a771c',
    'newuser',
    'newuser@example.com',
    '$2a$10$3yHNGw3SkUYZECFGm3N9tOmXWQiS.K5/VYj3wVlTZzDrMGo5q6fRu',
    '12345678901',
    '11999999999',
    'Rua A',
    '123',
    'Bairro B',
    'Cidade C',
    'SP',
    '01001000'
);