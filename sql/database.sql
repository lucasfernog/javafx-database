DROP SCHEMA IF EXISTS onibus CASCADE;
CREATE SCHEMA onibus;

DROP TABLE IF EXISTS onibus.fabricantes;
CREATE TABLE onibus.fabricantes (
	codigo SERIAL NOT NULL PRIMARY KEY,
	nome VARCHAR (255)
);

DROP TABLE IF EXISTS onibus.modelos;
CREATE TABLE onibus.modelos (
	codigo SERIAL NOT NULL PRIMARY KEY,
	nome VARCHAR (255),
	fabricante INTEGER NOT NULL CONSTRAINT fabricante_fk REFERENCES onibus.fabricantes (codigo)
);

DROP TABLE IF EXISTS onibus.veiculos;
CREATE TABLE onibus.veiculos (
	codigo SERIAL NOT NULL PRIMARY KEY,
	placa VARCHAR (7) NOT NULL UNIQUE,
	modelo INTEGER NOT NULL CONSTRAINT modelo_fk REFERENCES onibus.modelos (codigo)
);

DROP TABLE IF EXISTS onibus.motoristas;
CREATE TABLE onibus.motoristas (
	codigo SERIAL NOT NULL PRIMARY KEY,
	cpf BIGINT NOT NULL UNIQUE,
	ctps BIGINT NOT NULL UNIQUE,
	nome VARCHAR (255) NOT NULL,
	nascimento DATE NOT NULL,
	contratacao DATE NOT NULL,
	salario NUMERIC (8, 2) NOT NULL CHECK (salario > 0)
);

DROP TABLE IF EXISTS onibus.linhas;
CREATE TABLE onibus.linhas (
	codigo SERIAL NOT NULL PRIMARY KEY,
	nome VARCHAR (255) NOT NULL,
	linha_contraria INTEGER CONSTRAINT linha_contraria_fk REFERENCES onibus.linhas (codigo)
);

DROP TABLE IF EXISTS onibus.tipos;
CREATE TABLE onibus.tipos (
	codigo SERIAL NOT NULL PRIMARY KEY,
	descricao VARCHAR (255) NOT NULL
);

DROP TABLE IF EXISTS onibus.horarios;
CREATE TABLE onibus.horarios (
	saida TIME NOT NULL,
	tipo INTEGER NOT NULL CONSTRAINT horarios_tipo_fk REFERENCES onibus.tipos (codigo) ON DELETE CASCADE ON UPDATE NO ACTION,
	veiculo INTEGER NOT NULL CONSTRAINT horarios_veiculo_fk REFERENCES onibus.veiculos (codigo) ON DELETE CASCADE ON UPDATE NO ACTION,
	linha INTEGER NOT NULL CONSTRAINT horarios_linha_fk REFERENCES onibus.linhas (codigo) ON DELETE CASCADE ON UPDATE NO ACTION,
	motorista INTEGER NOT NULL CONSTRAINT horarios_motorista_fk REFERENCES onibus.motoristas (codigo) ON DELETE CASCADE ON UPDATE NO ACTION,
	CONSTRAINT horarios_pk PRIMARY KEY (saida, tipo, linha)
);

DROP TABLE IF EXISTS onibus.paradas;
CREATE TABLE onibus.paradas (
	codigo SERIAL NOT NULL PRIMARY KEY,
	cep INTEGER NOT NULL,
	rua VARCHAR (255) NOT NULL,
	numero INTEGER NOT NULL
);

DROP TABLE IF EXISTS onibus.itinerarios;
CREATE TABLE onibus.itinerarios (
	codigo SERIAL NOT NULL PRIMARY KEY,
	linha INTEGER NOT NULL CONSTRAINT itinerarios_linha_fk REFERENCES onibus.linhas (codigo) ON DELETE CASCADE ON UPDATE NO ACTION,
	rua VARCHAR (255) NOT NULL,
	inicio SMALLINT NOT NULL,
	fim SMALLINT NOT NULL,
	ordem SMALLINT NOT NULL
);

DROP TABLE IF EXISTS onibus.paradas_linhas;
CREATE TABLE onibus.paradas_linhas (
	linha INTEGER NOT NULL CONSTRAINT paradas_linhas_linha_fk REFERENCES onibus.linhas (codigo) ON DELETE CASCADE ON UPDATE NO ACTION,
	parada INTEGER NOT NULL CONSTRAINT paradas_linhas_parada_fk REFERENCES onibus.paradas (codigo) ON DELETE CASCADE ON UPDATE NO ACTION,
	CONSTRAINT paradas_linhas_pk PRIMARY KEY (linha, parada)
);

DROP TABLE IF EXISTS onibus.usuarios;
CREATE TABLE onibus.usuarios (
	codigo SERIAL NOT NULL PRIMARY KEY,
	cpf BIGINT NOT NULL UNIQUE,
	nome VARCHAR (255) NOT NULL,
	saldo FLOAT NOT NULL CHECK (saldo >= 0)
);

DROP TABLE IF EXISTS onibus.estudantes;
CREATE TABLE onibus.estudantes (
	usuario INTEGER NOT NULL PRIMARY KEY CONSTRAINT estudantes_usuario_fk REFERENCES onibus.usuarios (codigo) ON DELETE CASCADE ON UPDATE NO ACTION,
	vencimento DATE NOT NULL,
	saldo FLOAT NOT NULL CHECK (saldo >= 0)
);

INSERT INTO onibus.tipos (descricao) VALUES ('Normal'), ('Sábado'), ('Feriado'), ('Férias');

CREATE USER usuario PASSWORD 'senha';
GRANT USAGE ON SCHEMA onibus TO usuario;
GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON ALL TABLES IN SCHEMA onibus TO usuario;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA onibus TO usuario;
REVOKE INSERT, UPDATE, DELETE ON onibus.tipos FROM usuario;