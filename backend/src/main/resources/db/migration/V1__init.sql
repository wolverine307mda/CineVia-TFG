create table profesionales
(
    id               varchar(255)              not null
        primary key,
    biografia        text,
    fecha_inicio     date default CURRENT_DATE not null,
    fecha_nacimiento date                      not null,
    foto             varchar(255),
    lugar_nacimiento varchar(255)              not null,
    nombre           varchar(255)              not null
);

alter table profesionales
    owner to wolverine307;

create table sagas
(
    id           varchar(255) not null
        primary key,
    descripcion  varchar(1000),
    fecha_fin    date,
    fecha_inicio date         not null,
    imagen       varchar(255),
    is_acabada   boolean      not null,
    nombre       varchar(255) not null
        constraint ukgci323oiewm4iqoswtns2kgpm
            unique
);

alter table sagas
    owner to wolverine307;

create table producciones
(
    id                 varchar(255)  not null
        primary key,
    clasificacion_edad smallint      not null
        constraint producciones_clasificacion_edad_check
            check ((clasificacion_edad >= 0) AND (clasificacion_edad <= 4)),
    duracion           integer       not null,
    estreno            date          not null,
    imagen             varchar(255),
    informacion        text,
    puntuacion         double precision,
    sinopsis           varchar(1000) not null,
    tipo               varchar(255)  not null
        constraint producciones_tipo_check
            check ((tipo)::text = ANY
        ((ARRAY ['PELICULA'::character varying, 'SERIE'::character varying, 'CORTO'::character varying])::text[])),
    titulo             varchar(255)  not null
        constraint ukid7b4m7xvx7p0ii99e5ksaif9
            unique,
    saga_id            varchar(255)
        constraint fkt8i9u36alvgj6j2sgvk5d9f8h
            references sagas
);

alter table producciones
    owner to wolverine307;

create table participaciones
(
    id             varchar(255) not null
        primary key,
    created_at     timestamp(6) not null,
    papel          varchar(255),
    rol            varchar(255) not null
        constraint participaciones_rol_check
            check ((rol)::text = ANY ((ARRAY ['ACTOR'::character varying, 'DIRECTOR'::character varying])::text[])),
    updated_at     timestamp(6) not null,
    produccion_id  varchar(255) not null
        constraint fkeli1tkd6duymt15apbm5d9j6r
            references producciones,
    profesional_id varchar(255) not null
        constraint fkbpvhdlyt988gk9djktykgqnmc
            references profesionales
);

alter table participaciones
    owner to wolverine307;

create table produccion_categorias
(
    produccion_id varchar(255) not null
        constraint fktjsq5432nhblp05iqgiqlyj44
            references producciones,
    categoria     varchar(255) not null
        constraint produccion_categorias_categoria_check
            check ((categoria)::text = ANY
        ((ARRAY ['FICCION'::character varying, 'AVENTURA'::character varying, 'TERROR'::character varying, 'COMEDIA'::character varying, 'DRAMA'::character varying, 'DOCUMENTAL'::character varying, 'ANIMACION'::character varying, 'ROMANCE'::character varying, 'OTROS'::character varying, 'CRIMEN'::character varying, 'ACCION'::character varying, 'CIENCIA_FICCION'::character varying, 'FANTASIA'::character varying, 'MUSICAL'::character varying, 'WESTERN'::character varying, 'SUSPENSE'::character varying])::text[])),
    primary key (produccion_id, categoria)
);

alter table produccion_categorias
    owner to wolverine307;

create table ubicaciones
(
    id            varchar(255)     not null
        primary key,
    created_at    timestamp(6),
    latitud       double precision not null,
    longitud      double precision not null,
    nombre        varchar(255)     not null,
    updated_at    timestamp(6),
    produccion_id varchar(255)
        constraint fk23n8m7isptd30htk8yk5tsf0g
            references producciones
);

alter table ubicaciones
    owner to wolverine307;

create table usuarios
(
    id               varchar(255) not null
        primary key,
    apellido         varchar(255),
    avatar           varchar(255),
    created_at       timestamp(6),
    email            varchar(255) not null
        constraint ukkfsp0s1tflm1cwlj8idhqsad0
            unique,
    fecha_nacimiento timestamp(6),
    id_delete        uuid,
    nombre           varchar(255) not null,
    password         varchar(255) not null,
    rol              varchar(255) not null
        constraint usuarios_rol_check
            check ((rol)::text = ANY
        ((ARRAY ['USUARIO'::character varying, 'ADMINISTRADOR'::character varying])::text[])),
    telefono         varchar(255),
    updated_at       timestamp(6),
    username         varchar(255) not null
        constraint ukm2dvbwfge291euvmk6vkkocao
            unique
);

alter table usuarios
    owner to wolverine307;

create table reviews
(
    id            varchar(255) not null
        primary key,
    comentario    text,
    created_at    timestamp(6),
    puntuacion    integer      not null,
    updated_at    timestamp(6),
    produccion_id varchar(255) not null
        constraint fk9v4o3grbujl46dpq4cs8mg4w5
            references producciones,
    usuario_id    varchar(255) not null
        constraint fkjd9p9wtjtsu9abh29mo0soknp
            references usuarios
);

alter table reviews
    owner to wolverine307;

create table flyway_schema_history
(
    installed_rank integer                 not null
        constraint flyway_schema_history_pk
            primary key,
    version        varchar(50),
    description    varchar(200)            not null,
    type           varchar(20)             not null,
    script         varchar(1000)           not null,
    checksum       integer,
    installed_by   varchar(100)            not null,
    installed_on   timestamp default now() not null,
    execution_time integer                 not null,
    success        boolean                 not null
);

alter table flyway_schema_history
    owner to wolverine307;

create index flyway_schema_history_s_idx
    on flyway_schema_history (success);