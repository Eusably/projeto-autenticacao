create table eusably.papel_sistema (
	codigo varchar(255),
    nome varchar(255),
    primary key (codigo)
);

create table eusably.usuario_sistema (
	id int not null auto_increment,
    nome varchar(255),
    email varchar(255),
    senha varchar(255),
    cpf_cnpj varchar(255),
    data_cadastro datetime,
    data_atualizacao datetime,
    primary key (id)
);

create table eusably.usuario_papel (
	id_usuario int not null,
    codigo_papel varchar(255) not null,
    primary key(id_usuario, codigo_papel),
    CONSTRAINT fk_usuario_papel1 FOREIGN KEY (id_usuario) REFERENCES eusably.usuario_sistema(id),
    CONSTRAINT fk_usuario_papel2 FOREIGN KEY (codigo_papel) REFERENCES eusably.papel_sistema(codigo)
);