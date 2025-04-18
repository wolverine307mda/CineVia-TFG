-- Insertar datos en la tabla profesionales (actores y directores)
INSERT INTO profesionales (id, nombre, fecha_nacimiento, lugar_nacimiento, biografia, foto, fecha_inicio) VALUES
                                                                                                              ('p001', 'Tom Hanks', '1956-07-09', 'Concord, California, USA', 'Dos veces ganador del Oscar, conocido por películas como Forrest Gump y Toy Story.', 'tom_hanks.jpg', '1978-01-01'),
                                                                                                              ('p002', 'Meryl Streep', '1949-06-22', 'Summit, New Jersey, USA', 'Actriz con más nominaciones al Oscar en la historia.', 'meryl_streep.jpg', '1971-01-01'),
                                                                                                              ('p003', 'Christopher Nolan', '1970-07-30', 'London, England', 'Director aclamado por Inception, The Dark Knight y Dunkirk.', 'chris_nolan.jpg', '1998-01-01'),
                                                                                                              ('p004', 'Penélope Cruz', '1974-04-28', 'Alcobendas, Spain', 'Primera actriz española en ganar un Oscar.', 'penelope_cruz.jpg', '1989-01-01'),
                                                                                                              ('p005', 'Leonardo DiCaprio', '1974-11-11', 'Los Angeles, California, USA', 'Ganador del Oscar por The Revenant.', 'leo_dicaprio.jpg', '1989-01-01'),
                                                                                                              ('p006', 'Quentin Tarantino', '1963-03-27', 'Knoxville, Tennessee, USA', 'Director conocido por Pulp Fiction y Kill Bill.', 'tarantino.jpg', '1987-01-01'),
                                                                                                              ('p007', 'Scarlett Johansson', '1984-11-22', 'New York City, USA', 'Actriz conocida por su papel de Black Widow en Marvel.', 'scarlett_j.jpg', '1994-01-01'),
                                                                                                              ('p008', 'Guillermo del Toro', '1964-10-09', 'Guadalajara, Mexico', 'Director ganador del Oscar por The Shape of Water.', 'del_toro.jpg', '1985-01-01'),
                                                                                                              ('p009', 'Cate Blanchett', '1969-05-14', 'Melbourne, Australia', 'Actriz ganadora de dos premios Oscar.', 'cate_blanchett.jpg', '1992-01-01'),
                                                                                                              ('p010', 'Denzel Washington', '1954-12-28', 'Mount Vernon, New York, USA', 'Uno de los actores más respetados de Hollywood.', 'denzel_w.jpg', '1977-01-01');

-- Insertar sagas
INSERT INTO sagas (id, nombre, descripcion, fecha_inicio, fecha_fin, is_acabada, imagen) VALUES
                                                                                             ('s001', 'El Señor de los Anillos', 'Trilogía épica de fantasía basada en los libros de J.R.R. Tolkien.', '2001-12-19', '2003-12-17', true, 'lotr_saga.jpg'),
                                                                                             ('s002', 'Star Wars', 'Saga de space opera creada por George Lucas.', '1977-05-25', NULL, false, 'star_wars.jpg'),
                                                                                             ('s003', 'Harry Potter', 'Saga de fantasía basada en los libros de J.K. Rowling.', '2001-11-16', '2011-07-15', true, 'harry_potter.jpg'),
                                                                                             ('s004', 'Marvel Cinematic Universe', 'Universo cinematográfico de superhéroes de Marvel.', '2008-05-02', NULL, false, 'mcu.jpg'),
                                                                                             ('s005', 'James Bond', 'Serie de películas de espionaje basadas en Ian Fleming.', '1962-10-05', NULL, false, 'james_bond.jpg');

-- Insertar producciones (películas y series)
INSERT INTO producciones (id, titulo, tipo, estreno, duracion, clasificacion_edad, sinopsis, informacion, imagen, saga_id) VALUES
-- Películas del Señor de los Anillos
('pr001', 'El Señor de los Anillos: La Comunidad del Anillo', 'PELICULA', '2001-12-19', 178, 12, 'Un hobbit recibe un anillo con poderes y debe destruirlo.', 'Filmada en Nueva Zelanda.', 'lotr1.jpg', 's001'),
('pr002', 'El Señor de los Anillos: Las Dos Torres', 'PELICULA', '2002-12-18', 179, 12, 'La comunidad se separa mientras continúan su misión.', 'Ganadora de 2 Oscars.', 'lotr2.jpg', 's001'),
('pr003', 'El Señor de los Anillos: El Retorno del Rey', 'PELICULA', '2003-12-17', 201, 12, 'La batalla final por la Tierra Media.', 'Ganadora de 11 Oscars.', 'lotr3.jpg', 's001'),

-- Películas de Star Wars
('pr004', 'Star Wars: Episodio IV - Una Nueva Esperanza', 'PELICULA', '1977-05-25', 121, 7, 'Luke Skywalker se une a la Rebelión.', 'Película que revolucionó el cine.', 'starwars4.jpg', 's002'),
('pr005', 'Star Wars: Episodio V - El Imperio Contraataca', 'PELICULA', '1980-05-21', 124, 7, 'La Rebelión es perseguida por el Imperio.', 'Considerada la mejor de la saga.', 'starwars5.jpg', 's002'),

-- Películas de Harry Potter
('pr006', 'Harry Potter y la Piedra Filosofal', 'PELICULA', '2001-11-16', 152, 7, 'Harry descubre que es un mago.', 'Primera película de la saga.', 'hp1.jpg', 's003'),
('pr007', 'Harry Potter y las Reliquias de la Muerte - Parte 2', 'PELICULA', '2011-07-15', 130, 12, 'La batalla final contra Voldemort.', 'Final de la saga.', 'hp8.jpg', 's003'),

-- Películas del MCU
('pr008', 'Iron Man', 'PELICULA', '2008-05-02', 126, 12, 'Tony Stark construye una armadura para salvar al mundo.', 'Inicio del MCU.', 'ironman.jpg', 's004'),
('pr009', 'Avengers: Endgame', 'PELICULA', '2019-04-26', 181, 12, 'Los Vengadores intentan revertir el Snap.', 'Película más taquillera.', 'endgame.jpg', 's004'),

-- Películas independientes
('pr010', 'Forrest Gump', 'PELICULA', '1994-07-06', 142, 12, 'La vida de un hombre con discapacidad intelectual.', 'Ganadora de 6 Oscars.', 'forrest_gump.jpg', NULL),
('pr011', 'El Laberinto del Fauno', 'PELICULA', '2006-10-11', 118, 16, 'Una niña en la España posguerra descubre un mundo mágico.', 'Dirigida por Guillermo del Toro.', 'fauno.jpg', NULL),
('pr012', 'Pulp Fiction', 'PELICULA', '1994-10-14', 154, 18, 'Historias entrelazadas de criminales en Los Ángeles.', 'Obra maestra de Tarantino.', 'pulp_fiction.jpg', NULL),
('pr013', 'Titanic', 'PELICULA', '1997-12-19', 195, 12, 'Historia de amor en el famoso barco hundido.', 'Ganadora de 11 Oscars.', 'titanic.jpg', NULL),
('pr014', 'El Padrino', 'PELICULA', '1972-03-24', 175, 16, 'La historia de la familia Corleone.', 'Considerada una de las mejores películas.', 'godfather.jpg', NULL),

-- Series
('pr015', 'Breaking Bad', 'SERIE', '2008-01-20', 45, 16, 'Un profesor de química se convierte en narcotraficante.', 'Considerada una de las mejores series.', 'breaking_bad.jpg', NULL),
('pr016', 'Juego de Tronos', 'SERIE', '2011-04-17', 55, 18, 'Luchas por el poder en los Siete Reinos.', 'Basada en las novelas de George R.R. Martin.', 'got.jpg', NULL),
('pr017', 'Stranger Things', 'SERIE', '2016-07-15', 50, 12, 'Niños enfrentan fenómenos sobrenaturales en los 80s.', 'Homenaje al cine de los 80.', 'stranger_things.jpg', NULL);

-- Insertar categorías de producciones
INSERT INTO produccion_categorias (produccion_id, categoria) VALUES
-- LOTR
('pr001', 'FICCION'), ('pr001', 'AVENTURA'),
('pr002', 'FICCION'), ('pr002', 'AVENTURA'),
('pr003', 'FICCION'), ('pr003', 'AVENTURA'),

-- Star Wars
('pr004', 'FICCION'), ('pr004', 'AVENTURA'),
('pr005', 'FICCION'), ('pr005', 'AVENTURA'),

-- Harry Potter
('pr006', 'FICCION'), ('pr006', 'AVENTURA'),
('pr007', 'FICCION'), ('pr007', 'AVENTURA'),

-- MCU
('pr008', 'FICCION'), ('pr008', 'AVENTURA'),
('pr009', 'FICCION'), ('pr009', 'AVENTURA'),

-- Otras películas
('pr010', 'DRAMA'),
('pr011', 'FICCION'), ('pr011', 'TERROR'),
('pr012', 'DRAMA'), ('pr012', 'COMEDIA'),
('pr013', 'DRAMA'), ('pr013', 'ROMANCE'),
('pr014', 'DRAMA'),

-- Series
('pr015', 'DRAMA'),
('pr016', 'FICCION'), ('pr016', 'AVENTURA'),
('pr017', 'FICCION'), ('pr017', 'TERROR');

-- Insertar participaciones (actores y directores en producciones)
INSERT INTO participaciones (id, profesional_id, produccion_id, rol, papel, created_at, updated_at) VALUES
-- El Señor de los Anillos
('pa001', 'p001', 'pr001', 'ACTOR', 'Frodo Bolsón', NOW(), NOW()),
('pa002', 'p002', 'pr001', 'ACTOR', 'Galadriel', NOW(), NOW()),
('pa003', 'p003', 'pr001', 'DIRECTOR', NULL, NOW(), NOW()),

-- Forrest Gump
('pa004', 'p001', 'pr010', 'ACTOR', 'Forrest Gump', NOW(), NOW()),

-- El Laberinto del Fauno
('pa005', 'p004', 'pr011', 'ACTOR', 'Carmen', NOW(), NOW()),
('pa006', 'p008', 'pr011', 'DIRECTOR', NULL, NOW(), NOW()),

-- Titanic
('pa007', 'p005', 'pr013', 'ACTOR', 'Jack Dawson', NOW(), NOW()),

-- Pulp Fiction
('pa008', 'p006', 'pr012', 'DIRECTOR', NULL, NOW(), NOW()),
('pa009', 'p010', 'pr012', 'ACTOR', 'Vincent Vega', NOW(), NOW()),

-- Marvel
('pa010', 'p007', 'pr009', 'ACTOR', 'Black Widow', NOW(), NOW());

-- Insertar ubicaciones de filmación
INSERT INTO ubicaciones (id, nombre, latitud, longitud, produccion_id) VALUES
-- LOTR
('u001', 'Hobbiton, Nueva Zelanda', -37.8722, 175.6829, 'pr001'),
('u002', 'Parque Nacional Tongariro, NZ', -39.2727, 175.5622, 'pr001'),

-- Titanic
('u003', 'Estudios Baja, México', 31.8651, -116.6243, 'pr013'),

-- El Laberinto del Fauno
('u004', 'Madrid, España', 40.4168, -3.7038, 'pr011'),

-- Breaking Bad
('u005', 'Albuquerque, Nuevo México', 35.0844, -106.6504, 'pr015');

-- Insertar usuarios
INSERT INTO usuarios (id, nombre, apellido, email, username, password, rol, telefono, fecha_nacimiento, created_at, avatar) VALUES
                                                                                                                                ('11111111-1111-1111-1111-111111111111', 'Admin', 'Admin', 'admin@geofilm.com', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ADMINISTRADOR', '600000000', '1980-01-01', NOW(), 'admin_avatar.jpg'),
                                                                                                                                ('22222222-2222-2222-2222-222222222222', 'Usuario', 'Normal', 'user@geofilm.com', 'user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USUARIO', '611111111', '1990-05-15', NOW(), 'user_avatar.jpg'),
                                                                                                                                ('33333333-3333-3333-3333-333333333333', 'María', 'García', 'maria@geofilm.com', 'maria', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USUARIO', '622222222', '1995-08-20', NOW(), 'maria_avatar.jpg');

-- Insertar reviews
INSERT INTO reviews (id, usuario_id, produccion_id, comentario, puntuacion) VALUES
                                                                                ('r001', '22222222-2222-2222-2222-222222222222', 'pr001', 'Una obra maestra del cine fantástico.', 5),
                                                                                ('r002', '33333333-3333-3333-3333-333333333333', 'pr001', 'Los paisajes de Nueva Zelanda son increíbles.', 4),
                                                                                ('r003', '22222222-2222-2222-2222-222222222222', 'pr010', 'Tom Hanks está brillante en este papel.', 5),
                                                                                ('r004', '33333333-3333-3333-3333-333333333333', 'pr011', 'Una película mágica y oscura a la vez.', 5);
