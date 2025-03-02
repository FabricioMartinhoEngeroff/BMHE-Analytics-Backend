CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    rua VARCHAR(255) NOT NULL,
    bairro VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    cep VARCHAR(9) NOT NULL
);

INSERT INTO users (id, name, email, password, cpf, telefone, rua, bairro, cidade, estado, cep)
VALUES (
    'd193afd4-9222-4150-aadb-5167405a771c',
    'Fabricio',
    'fa.engeroff1996@gmail.com',
    '$2a$10$3yHNGw3SkUYZECFGm3N9tOmXWQiS.K5/VYj3wVlTZzDrMGo5q6fRu',
    '031.044.320-23',
    '(51) 99640-7776',
    'Rua Das Bergamoteiras',
    'Nova Columbia',
    'Bom Princípio',
    'RS',
    '95765-000'
);