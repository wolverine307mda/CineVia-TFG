-- Insertar sagas
INSERT INTO sagas (id, nombre, descripcion, fecha_inicio, fecha_fin, is_acabada, imagen) VALUES
('s001', 'El Señor de los Anillos', 'Trilogía épica de fantasía basada en los libros de J.R.R. Tolkien.', '2001-12-19', '2003-12-17', true, 'lotr_saga.jpg'),
('s002', 'Star Wars', 'Saga de space opera creada por George Lucas.', '1977-05-25', NULL, false, 'star_wars.jpg'),
('s003', 'Harry Potter', 'Saga de fantasía basada en los libros de J.K. Rowling.', '2001-11-16', '2011-07-15', true, 'harry_potter.jpg'),
('s004', 'Marvel Cinematic Universe', 'Universo cinematográfico de superhéroes de Marvel.', '2008-05-02', NULL, false, 'mcu.jpg'),
('s005', 'James Bond', 'Serie de películas de espionaje basadas en Ian Fleming.', '1962-10-05', NULL, false, 'james_bond.jpg'),
('s006', 'El Padrino', 'Trilogía sobre la familia criminal Corleone.', '1972-03-24', '1990-12-25', true, 'https://image.tmdb.org/t/p/w500/r4gnMXoY1efvaolNDjn3nj4046S.jpg'),
('s007', 'Trilogía de Batman de Nolan', 'Trilogía oscura del Caballero Oscuro dirigida por Christopher Nolan.', '2005-06-15', '2012-07-20', true, 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg'),
('s008', 'Matrix', 'Saga de ciencia ficción sobre la realidad simulada.', '1999-03-31', '2021-12-22', false, 'https://m.media-amazon.com/images/M/MV5BNGE1YzI4NzMtZTUxNi00Y2I5LTg2MmQtODE0NThmYTFmMDk0XkEyXkFqcGc@._V1_.jpg'),
('s010', 'Universo de Tarantino', 'Películas conectadas en el mismo universo cinematográfico.', '1992-09-02', NULL, false, 'https://m.media-amazon.com/images/I/81UTs3sC5hL._AC_UF894,1000_QL80_.jpg'),
('s011', 'Universo de Nolan', 'Películas de Christopher Nolan con temas similares.', '1998-01-01', NULL, false, 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg'),
('s012', 'Indiana Jones', 'Aventuras del arqueólogo Indiana Jones', '1981-06-12', '2023-06-30', false, 'https://m.media-amazon.com/images/M/MV5BMmU0YWRmODQtYjIxYy00M2VkLWI2OTEtNDNlYTM0NGQ0ZThkXkEyXkFqcGdeQXVyNjc5NjEzNA@@._V1_.jpg'),
('s013', 'Jurassic Park', 'Dinosaurios clonados causan caos', '1993-06-11', '2022-06-10', false, 'https://m.media-amazon.com/images/I/71X6YzwV0gL._AC_UF1000,1000_QL80_.jpg'),
('s014', 'Misión Imposible', 'Misiones de espionaje de Ethan Hunt', '1996-05-22', NULL, false, 'https://m.media-amazon.com/images/I/71niXI3lxlL._AC_UF1000,1000_QL80_.jpg'),
('s015', 'Fast & Furious', 'Carreras y crímenes internacionales', '2001-06-22', NULL, false, 'https://m.media-amazon.com/images/I/81UGCOgXJbL._AC_UF1000,1000_QL80_.jpg');

-- Insertar tus producciones de ejemplo (películas)
INSERT INTO producciones (id, titulo, tipo, estreno, duracion, clasificacion_edad, sinopsis, informacion, imagen, saga_id, puntuacion) VALUES
('pr018', 'El Padrino', 'PELICULA', '1972-03-24', 175, 3, 'El patriarca de una familia mafiosa transfiere el control de su imperio clandestino a su hijo reacio.', 'Basada en la novela de Mario Puzo.', 'https://image.tmdb.org/t/p/w500/r4gnMXoY1efvaolNDjn3nj4046S.jpg', 's006', 8.6),
('pr019', 'El Caballero Oscuro', 'PELICULA', '2008-07-18', 152, 2, 'Batman tiene que aceptar la ayuda de un nuevo fiscal para combatir a un criminal anarquista conocido como el Joker.', 'Segunda película de la trilogía de Nolan.', 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg', 's007', 6.5),
('pr020', 'Pulp Fiction', 'PELICULA', '1994-10-14', 154, 4, 'Las vidas de dos sicarios, un boxeador, la esposa de un gánster y dos bandidos se entrelazan en cuatro historias de violencia y redención.', 'Ganadora de la Palma de Oro en Cannes.', 'https://m.media-amazon.com/images/I/81UTs3sC5hL._AC_UF894,1000_QL80_.jpg', 's010', 8.5),
('pr021', 'Forrest Gump', 'PELICULA', '1994-07-06', 142, 2, 'La presidencia de Kennedy y Johnson, Vietnam, Watergate y otros eventos históricos se desarrollan desde la perspectiva de un hombre de Alabama con un coeficiente intelectual de 75.', 'Ganadora de 6 premios Oscar.', 'https://media.es.wired.com/photos/6679bb407faf643236b6024c/2:3/w_576,h_864,c_limit/Forrest%20Gump.jpg', NULL, 6.5),
('pr022', 'Inception', 'PELICULA', '2010-07-16', 148, 2, 'Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños es encargado con la tarea inversa de implantar una idea en la mente de un CEO.', 'Dirigida por Christopher Nolan.', 'https://m.media-amazon.com/images/M/MV5BMjExMjkwNTQ0Nl5BMl5BanBnXkFtZTcwNTY0OTk1Mw@@._V1_.jpg', 's011', 6.5),
('pr023', 'The Matrix', 'PELICULA', '1999-03-31', 136, 2, 'Un hacker aprende de misteriosos rebeldes sobre la verdadera naturaleza de su realidad y su papel en la guerra contra sus controladores.', 'Revolucionó los efectos visuales.', 'https://m.media-amazon.com/images/M/MV5BNGE1YzI4NzMtZTUxNi00Y2I5LTg2MmQtODE0NThmYTFmMDk0XkEyXkFqcGc@._V1_.jpg', 's008', 6.5),
('pr024', 'Titanic', 'PELICULA', '1997-12-19', 195, 2, 'Una aristócrata de 17 años se enamora de un artista amable pero pobre a bordo del lujoso e infortunado R.M.S. Titanic.', 'Ganadora de 11 premios Oscar.', 'https://pics.filmaffinity.com/Titanic-321994924-large.jpg', NULL, 6.5),
('pr025', 'Gladiator', 'PELICULA', '2000-05-05', 155, 3, 'Un ex general romano busca venganza contra el corrupto emperador que asesinó a su familia y lo envió a la esclavitud.', 'Ganadora de 5 premios Oscar.', 'https://m.media-amazon.com/images/I/51GA6V6VE1L._AC_UF894,1000_QL80_.jpg', NULL, 6.5),
('pr026', 'The Shawshank Redemption', 'PELICULA', '1994-09-23', 142, 3, 'Dos hombres encarcelados se vinculan durante varios años, encontrando consuelo y eventual redención a través de actos de decencia común.', 'Basada en la novela de Stephen King.', 'https://pics.filmaffinity.com/Cadena_perpetua-576140557-large.jpg', NULL, 6.5),
('pr027', 'The Godfather Part II', 'PELICULA', '1974-12-20', 202, 3, 'La temprana vida y carrera de Vito Corleone en 1920 es representada mientras su hijo, Michael, expande y refuerza su control sobre el sindicato del crimen familiar.', 'Secuela de El Padrino.', 'https://s3.amazonaws.com/nightjarprod/content/uploads/sites/192/2022/10/17140449/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg', 's006', 6.5),
('pr028', 'The Lord of the Rings: The Return of the King', 'PELICULA', '2003-12-17', 201, 2, 'Gandalf y Aragorn lideran el Mundo de los Hombres contra el ejército de Sauron para distraer su atención de Frodo y Sam mientras se acercan al Monte del Destino con el Anillo Único.', 'Tercera parte de la trilogía.', 'https://m.media-amazon.com/images/M/MV5BMTZkMjBjNWMtZGI5OC00MGU0LTk4ZTItODg2NWM3NTVmNWQ4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg', 's001', 6.5),
('pr029', 'Interstellar', 'PELICULA', '2014-11-07', 169, 2, 'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento por asegurar la supervivencia de la humanidad.', 'Dirigida por Christopher Nolan.', 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg', 's011', 6.5),
('pr030', 'Whiplash', 'PELICULA', '2014-10-15', 106, 3, 'Un joven baterista ambicioso se inscribe en un conservatorio de música donde su instructor abusivo no se detendrá ante nada para hacer que un estudiante alcance la grandeza.', 'Ganadora de 3 premios Oscar.', 'https://image.tmdb.org/t/p/w500/oPxnRhyAIzJKGUEdSiwTJQBa3NM.jpg', NULL, 6.5),
('pr031', 'La La Land', 'PELICULA', '2016-12-09', 128, 2, 'Mientras navegan por sus carreras en Los Ángeles, un pianista y una actriz se enamoran mientras intentan reconciliar sus aspiraciones para el futuro.', 'Ganadora de 6 premios Oscar.', 'https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg', NULL, 6.5),
('pr032', 'Django Desencadenado', 'PELICULA', '2012-12-25', 165, 4, 'Con la ayuda de un cazarrecompensas alemán, un esclavo liberado se embarca en una misión para rescatar a su esposa de un brutal propietario de una plantación en Mississippi.', 'Dirigida por Quentin Tarantino.', 'https://image.tmdb.org/t/p/w500/7oWY8VDWW7thTzWh3OKYRkWUlD5.jpg', 's010', 6.5),
('pr033', 'Indiana Jones y los cazadores del arca perdida', 'PELICULA', '1981-06-12', 115, 2, 'El arqueólogo Indiana Jones busca el Arca de la Alianza', 'Dirigida por Steven Spielberg', 'https://m.media-amazon.com/images/M/MV5BNjEwNjY0ZTAtMjk0MS00MDhkLTkwYWUtMjM3ZmI2M2JhYjJhXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg', 's012', 6.5),
('pr034', 'Jurassic Park', 'PELICULA', '1993-06-11', 127, 2, 'Un parque temático con dinosaurios clonados sale mal', 'Basada en la novela de Michael Crichton', 'https://i.pinimg.com/736x/51/ba/64/51ba64b2e61f820e0e86bdd2f4c6e92c.jpg', 's013', 6.5),
('pr035', 'Misión Imposible', 'PELICULA', '1996-05-22', 110, 2, 'Ethan Hunt es acusado de traición y debe limpiar su nombre', 'Primera película de la saga', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYbruGRibbv4xHpmsYZM8x8QR2FfxUIR94WQ&s', 's014', 6.5),
('pr036', 'Fast & Furious', 'PELICULA', '2001-06-22', 106, 3, 'Un policía se infiltra en el mundo de las carreras callejeras', 'Inicio de la franquicia', 'https://www.universalpictures.es/tl_files/content/movies/fast9/fast9_header_mobile.jpg', 's015', 6.5),
('pr037', 'El silencio de los corderos', 'PELICULA', '1991-02-14', 118, 4, 'Una agente del FBI busca ayuda de un asesino en serie', 'Ganadora de 5 Oscars', 'https://m.media-amazon.com/images/I/61DMhNjjJ3L._AC_UF894,1000_QL80_.jpg', NULL, 6.5),
('pr038', 'El club de la lucha', 'PELICULA', '1999-10-15', 139, 4, 'Un oficinista y un vendedor forman un club de lucha subterráneo', 'Basada en la novela de Chuck Palahniuk', 'https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjptGkW4UwD3BdMJMcAPCcuYD9U9M8GJD3QQ76FIanqoxspZk2q9sdKXSOIx7yiQsF5T-ZdkTHUM21_AEN6vH2OXucUbTnIhQ4CEUbMtWr7TZ-FIklXl5vrGg1fl7vib3E2of5DWCGmE3H3/s2048/Club-de-la-lucha.jpg', NULL, 6.5);

-- Insertar categorías para tus producciones
INSERT INTO produccion_categorias (produccion_id, categoria) VALUES
('pr018', 'DRAMA'), ('pr018', 'CRIMEN'),
('pr019', 'ACCION'), ('pr019', 'CRIMEN'), ('pr019', 'DRAMA'),
('pr020', 'DRAMA'), ('pr020', 'CRIMEN'),
('pr021', 'DRAMA'), ('pr021', 'ROMANCE'),
('pr022', 'ACCION'), ('pr022', 'CIENCIA_FICCION'),
('pr023', 'ACCION'), ('pr023', 'CIENCIA_FICCION'),
('pr024', 'DRAMA'), ('pr024', 'ROMANCE'),
('pr025', 'ACCION'), ('pr025', 'AVENTURA'), ('pr025', 'DRAMA'),
('pr026', 'DRAMA'),
('pr027', 'DRAMA'), ('pr027', 'CRIMEN'),
('pr028', 'AVENTURA'), ('pr028', 'DRAMA'), ('pr028', 'FANTASIA'),
('pr029', 'AVENTURA'), ('pr029', 'DRAMA'), ('pr029', 'CIENCIA_FICCION'),
('pr030', 'DRAMA'), ('pr030', 'MUSICAL'),
('pr031', 'COMEDIA'), ('pr031', 'DRAMA'), ('pr031', 'MUSICAL'), ('pr031', 'ROMANCE'),
('pr033', 'AVENTURA'), ('pr033', 'ACCION'),
('pr034', 'AVENTURA'), ('pr034', 'CIENCIA_FICCION'),
('pr035', 'ACCION'), ('pr035', 'SUSPENSE'),
('pr036', 'ACCION'), ('pr036', 'CRIMEN'),
('pr037', 'SUSPENSE'), ('pr037', 'CRIMEN'), ('pr037', 'DRAMA'),
('pr038', 'DRAMA'), ('pr038', 'SUSPENSE'),
('pr032', 'DRAMA'), ('pr032', 'WESTERN');

-- Insertar profesionales adicionales (actores y directores de tus películas)
INSERT INTO profesionales (id, nombre, fecha_nacimiento, lugar_nacimiento, biografia, foto, fecha_inicio) VALUES
('p001', 'Tom Hanks', '1956-07-09', 'Concord, California, USA', 'Dos veces ganador del Oscar, conocido por Forrest Gump y Toy Story.', 'tom_hanks.jpg', '1978-01-01'),
('p003', 'Christopher Nolan', '1970-07-30', 'London, England', 'Director aclamado por Inception, The Dark Knight y Dunkirk.', 'chris_nolan.jpg', '1998-01-01'),
('p005', 'Leonardo DiCaprio', '1974-11-11', 'Los Angeles, California, USA', 'Ganador del Oscar por The Revenant.', 'leo_dicaprio.jpg', '1989-01-01'),
('p006', 'Quentin Tarantino', '1963-03-27', 'Knoxville, Tennessee, USA', 'Director conocido por Pulp Fiction y Kill Bill.', 'tarantino.jpg', '1987-01-01'),
('p011', 'Marlon Brando', '1924-04-03', 'Omaha, Nebraska, USA', 'Actor legendario conocido por El Padrino y Un tranvía llamado Deseo.', 'marlon_brando.jpg', '1944-01-01'),
('p012', 'Al Pacino', '1940-04-25', 'New York City, USA', 'Icono del cine ganador de un Oscar por Esencia de mujer.', 'al_pacino.jpg', '1967-01-01'),
('p013', 'James Caan', '1940-03-26', 'New York City, USA', 'Actor conocido por El Padrino y Misery.', 'james_caan.jpg', '1961-01-01'),
('p014', 'Francis Ford Coppola', '1939-04-07', 'Detroit, Michigan, USA', 'Aclamado director de El Padrino y Apocalypse Now.', 'coppola.jpg', '1962-01-01'),
('p015', 'Christian Bale', '1974-01-30', 'Haverfordwest, Wales', 'Actor versátil conocido por El Caballero Oscuro y El luchador.', 'christian_bale.jpg', '1982-01-01'),
('p016', 'Heath Ledger', '1979-04-04', 'Perth, Australia', 'Actor ganador del Oscar por su papel del Joker.', 'heath_ledger.jpg', '1992-01-01'),
('p017', 'Aaron Eckhart', '1968-03-12', 'Cupertino, California, USA', 'Actor conocido por El Caballero Oscuro y Thank You for Smoking.', 'aaron_eckhart.jpg', '1992-01-01'),
('p018', 'John Travolta', '1954-02-18', 'Englewood, New Jersey, USA', 'Actor de Pulp Fiction y Fiebre del sábado noche.', 'john_travolta.jpg', '1969-01-01'),
('p019', 'Uma Thurman', '1970-04-29', 'Boston, Massachusetts, USA', 'Actriz conocida por Pulp Fiction y Kill Bill.', 'uma_thurman.jpg', '1985-01-01'),
('p020', 'Samuel L. Jackson', '1948-12-21', 'Washington, D.C., USA', 'Prolífico actor conocido por Pulp Fiction y Avengers.', 'samuel_jackson.jpg', '1972-01-01'),
('p021', 'Robin Wright', '1966-04-08', 'Dallas, Texas, USA', 'Actriz conocida por Forrest Gump y House of Cards.', 'robin_wright.jpg', '1983-01-01'),
('p022', 'Gary Sinise', '1955-03-17', 'Blue Island, Illinois, USA', 'Actor conocido por Forrest Gump y CSI: NY.', 'gary_sinise.jpg', '1982-01-01'),
('p023', 'Robert Zemeckis', '1952-05-14', 'Chicago, Illinois, USA', 'Director de Forrest Gump y Regreso al futuro.', 'zemeckis.jpg', '1978-01-01'),
('p024', 'Joseph Gordon-Levitt', '1981-02-17', 'Los Angeles, California, USA', 'Actor conocido por Inception y 500 Days of Summer.', 'joseph_gordon.jpg', '1988-01-01'),
('p025', 'Ellen Page', '1987-02-21', 'Halifax, Canada', 'Actriz conocida por Inception y Juno.', 'ellen_page.jpg', '1997-01-01'),
('p026', 'Keanu Reeves', '1964-09-02', 'Beirut, Lebanon', 'Actor de Matrix y John Wick.', 'keanu_reeves.jpg', '1984-01-01'),
('p027', 'Laurence Fishburne', '1961-07-30', 'Augusta, Georgia, USA', 'Actor conocido por Matrix y CSI.', 'fishburne.jpg', '1972-01-01'),
('p028', 'Carrie-Anne Moss', '1967-08-21', 'Burnaby, Canada', 'Actriz conocida por Matrix y Jessica Jones.', 'carrie_moss.jpg', '1989-01-01'),
('p029', 'Lana Wachowski', '1965-06-21', 'Chicago, Illinois, USA', 'Directora de Matrix y Sense8.', 'lana_wachowski.jpg', '1995-01-01'),
('p030', 'Lilly Wachowski', '1967-12-29', 'Chicago, Illinois, USA', 'Directora de Matrix y Sense8.', 'lilly_wachowski.jpg', '1995-01-01'),
('p031', 'Kate Winslet', '1975-10-05', 'Reading, England', 'Actriz ganadora del Oscar por The Reader.', 'kate_winslet.jpg', '1991-01-01'),
('p032', 'Billy Zane', '1966-02-24', 'Chicago, Illinois, USA', 'Actor conocido por Titanic y The Phantom.', 'billy_zane.jpg', '1985-01-01'),
('p033', 'James Cameron', '1954-08-16', 'Kapuskasing, Canada', 'Director de Titanic y Avatar.', 'james_cameron.jpg', '1978-01-01'),
('p034', 'Russell Crowe', '1964-04-07', 'Wellington, New Zealand', 'Actor ganador del Oscar por Gladiator.', 'russell_crowe.jpg', '1986-01-01'),
('p035', 'Joaquin Phoenix', '1974-10-28', 'San Juan, Puerto Rico', 'Actor ganador del Oscar por Joker.', 'joaquin_phoenix.jpg', '1982-01-01'),
('p036', 'Connie Nielsen', '1965-07-03', 'Frederikshavn, Denmark', 'Actriz conocida por Gladiator y Wonder Woman.', 'connie_nielsen.jpg', '1984-01-01'),
('p037', 'Ridley Scott', '1937-11-30', 'South Shields, England', 'Director de Gladiator y Alien.', 'ridley_scott.jpg', '1965-01-01'),
('p038', 'Tim Robbins', '1958-10-16', 'West Covina, California, USA', 'Actor ganador del Oscar por Mystic River.', 'tim_robbins.jpg', '1981-01-01'),
('p039', 'Morgan Freeman', '1937-06-01', 'Memphis, Tennessee, USA', 'Actor legendario conocido por The Shawshank Redemption.', 'morgan_freeman.jpg', '1964-01-01'),
('p040', 'Bob Gunton', '1945-11-15', 'Santa Monica, California, USA', 'Actor conocido por The Shawshank Redemption.', 'bob_gunton.jpg', '1981-01-01'),
('p041', 'Frank Darabont', '1959-01-28', 'Montbéliard, France', 'Director de The Shawshank Redemption.', 'darabont.jpg', '1981-01-01'),
('p042', 'Robert De Niro', '1943-08-17', 'New York City, USA', 'Actor legendario ganador de dos Oscars.', 'de_niro.jpg', '1963-01-01'),
('p043', 'Robert Duvall', '1931-01-05', 'San Diego, California, USA', 'Actor ganador del Oscar por Tender Mercies.', 'robert_duvall.jpg', '1952-01-01'),
('p044', 'Elijah Wood', '1981-01-28', 'Cedar Rapids, Iowa, USA', 'Actor conocido por El Señor de los Anillos.', 'elijah_wood.jpg', '1989-01-01'),
('p045', 'Viggo Mortensen', '1958-10-20', 'New York City, USA', 'Actor conocido por El Señor de los Anillos.', 'viggo_mortensen.jpg', '1984-01-01'),
('p046', 'Ian McKellen', '1939-05-25', 'Burnley, England', 'Actor conocido por El Señor de los Anillos y X-Men.', 'ian_mckellen.jpg', '1961-01-01'),
('p047', 'Peter Jackson', '1961-10-31', 'Pukerua Bay, New Zealand', 'Director de El Señor de los Anillos.', 'peter_jackson.jpg', '1987-01-01'),
('p048', 'Matthew McConaughey', '1969-11-04', 'Uvalde, Texas, USA', 'Actor ganador del Oscar por Dallas Buyers Club.', 'matthew_mcconaughey.jpg', '1991-01-01'),
('p049', 'Anne Hathaway', '1982-11-12', 'Brooklyn, New York, USA', 'Actriz ganadora del Oscar por Los Miserables.', 'anne_hathaway.jpg', '1999-01-01'),
('p050', 'Jessica Chastain', '1977-03-24', 'Sacramento, California, USA', 'Actriz nominada al Oscar por Zero Dark Thirty.', 'jessica_chastain.jpg', '2004-01-01'),
('p051', 'Miles Teller', '1987-02-20', 'Downingtown, Pennsylvania, USA', 'Actor conocido por Whiplash y Top Gun: Maverick.', 'miles_teller.jpg', '2004-01-01'),
('p052', 'J.K. Simmons', '1955-01-09', 'Detroit, Michigan, USA', 'Actor ganador del Oscar por Whiplash.', 'jk_simmons.jpg', '1986-01-01'),
('p053', 'Paul Reiser', '1957-03-30', 'New York City, USA', 'Actor conocido por Whiplash y Aliens.', 'paul_reiser.jpg', '1982-01-01'),
('p054', 'Damien Chazelle', '1985-01-19', 'Providence, Rhode Island, USA', 'Director de Whiplash y La La Land.', 'damien_chazelle.jpg', '2009-01-01'),
('p055', 'Ryan Gosling', '1980-11-12', 'London, Ontario, Canada', 'Actor conocido por La La Land y Drive.', 'ryan_gosling.jpg', '1993-01-01'),
('p056', 'Emma Stone', '1988-11-06', 'Scottsdale, Arizona, USA', 'Actriz ganadora del Oscar por La La Land.', 'emma_stone.jpg', '2004-01-01'),
('p057', 'John Legend', '1978-12-28', 'Springfield, Ohio, USA', 'Cantante y actor conocido por La La Land.', 'john_legend.jpg', '1998-01-01'),
('p058', 'Jamie Foxx', '1967-12-13', 'Terrell, Texas, USA', 'Actor ganador del Oscar por Ray.', 'jamie_foxx.jpg', '1989-01-01'),
('p059', 'Christoph Waltz', '1956-10-04', 'Vienna, Austria', 'Actor ganador de dos Oscars por Django y Bastardos sin gloria.', 'christoph_waltz.jpg', '1977-01-01'),
('p060', 'Harrison Ford', '1942-07-13', 'Chicago, Illinois, USA', 'Conocido por Indiana Jones y Star Wars', 'https://m.media-amazon.com/images/M/MV5BMTY4Mjg0NjIxOV5BMl5BanBnXkFtZTcwMTM2NTI3MQ@@._V1_.jpg', '1966-01-01'),
('p061', 'Steven Spielberg', '1946-12-18', 'Cincinnati, Ohio, USA', 'Director de ET y Jurassic Park', 'https://m.media-amazon.com/images/M/MV5BMTY1NjAzNzE1MV5BMl5BanBnXkFtZTYwNTk0ODc0._V1_.jpg', '1968-01-01'),
('p062', 'Laura Dern', '1967-02-10', 'Los Angeles, California, USA', 'Actriz ganadora del Oscar', 'https://m.media-amazon.com/images/M/MV5BMTU4MjU0NjI4NF5BMl5BanBnXkFtZTgwODU1NTUwMjE@._V1_.jpg', '1985-01-01'),
('p063', 'Tom Cruise', '1962-07-03', 'Syracuse, New York, USA', 'Actor de Misión Imposible', 'https://m.media-amazon.com/images/M/MV5BMTk1MjM3NTU5M15BMl5BanBnXkFtZTcwMTMyMjAyMg@@._V1_.jpg', '1981-01-01'),
('p064', 'Vin Diesel', '1967-07-18', 'Alameda County, California, USA', 'Actor de Fast & Furious', 'https://m.media-amazon.com/images/M/MV5BMjExNzA4MDYxN15BMl5BanBnXkFtZTcwOTI1MDAxOQ@@._V1_.jpg', '1990-01-01'),
('p065', 'Jodie Foster', '1962-11-19', 'Los Angeles, California, USA', 'Actriz ganadora del Oscar', 'https://m.media-amazon.com/images/M/MV5BMTM1NjQ5NTY3N15BMl5BanBnXkFtZTcwOTU0OTk1Mw@@._V1_.jpg', '1968-01-01'),
('p066', 'Anthony Hopkins', '1937-12-31', 'Port Talbot, Wales', 'Actor ganador del Oscar', 'https://m.media-amazon.com/images/M/MV5BMTg5ODk1NTc5Ml5BMl5BanBnXkFtZTYwMjAwOTI4._V1_.jpg', '1960-01-01'),
('p067', 'David Fincher', '1962-08-28', 'Denver, Colorado, USA', 'Director de Seven y Zodiac', 'https://m.media-amazon.com/images/M/MV5BMTY0MjQ0MTE5NF5BMl5BanBnXkFtZTgwNjY5NjY5NDE@._V1_.jpg', '1984-01-01');

-- Insertar participaciones para tus producciones
INSERT INTO participaciones (id, profesional_id, produccion_id, rol, papel, created_at, updated_at) VALUES
-- El Padrino
('pa011', 'p011', 'pr018', 'ACTOR', 'Don Vito Corleone', NOW(), NOW()),
('pa012', 'p012', 'pr018', 'ACTOR', 'Michael Corleone', NOW(), NOW()),
('pa013', 'p013', 'pr018', 'ACTOR', 'Sonny Corleone', NOW(), NOW()),
('pa014', 'p014', 'pr018', 'DIRECTOR', NULL, NOW(), NOW()),

-- El Caballero Oscuro
('pa015', 'p015', 'pr019', 'ACTOR', 'Bruce Wayne / Batman', NOW(), NOW()),
('pa016', 'p016', 'pr019', 'ACTOR', 'Joker', NOW(), NOW()),
('pa017', 'p017', 'pr019', 'ACTOR', 'Harvey Dent', NOW(), NOW()),
('pa018', 'p003', 'pr019', 'DIRECTOR', NULL, NOW(), NOW()),

-- Pulp Fiction
('pa019', 'p018', 'pr020', 'ACTOR', 'Vincent Vega', NOW(), NOW()),
('pa020', 'p019', 'pr020', 'ACTOR', 'Mia Wallace', NOW(), NOW()),
('pa021', 'p020', 'pr020', 'ACTOR', 'Jules Winnfield', NOW(), NOW()),
('pa022', 'p006', 'pr020', 'DIRECTOR', NULL, NOW(), NOW()),

-- Forrest Gump
('pa023', 'p001', 'pr021', 'ACTOR', 'Forrest Gump', NOW(), NOW()),
('pa024', 'p021', 'pr021', 'ACTOR', 'Jenny Curran', NOW(), NOW()),
('pa025', 'p022', 'pr021', 'ACTOR', 'Teniente Dan Taylor', NOW(), NOW()),
('pa026', 'p023', 'pr021', 'DIRECTOR', NULL, NOW(), NOW()),

-- Inception
('pa027', 'p005', 'pr022', 'ACTOR', 'Dom Cobb', NOW(), NOW()),
('pa028', 'p024', 'pr022', 'ACTOR', 'Arthur', NOW(), NOW()),
('pa029', 'p025', 'pr022', 'ACTOR', 'Ariadne', NOW(), NOW()),
('pa030', 'p003', 'pr022', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Matrix
('pa031', 'p026', 'pr023', 'ACTOR', 'Neo', NOW(), NOW()),
('pa032', 'p027', 'pr023', 'ACTOR', 'Morpheus', NOW(), NOW()),
('pa033', 'p028', 'pr023', 'ACTOR', 'Trinity', NOW(), NOW()),
('pa034', 'p029', 'pr023', 'DIRECTOR', NULL, NOW(), NOW()),
('pa035', 'p030', 'pr023', 'DIRECTOR', NULL, NOW(), NOW()),

-- Titanic
('pa036', 'p005', 'pr024', 'ACTOR', 'Jack Dawson', NOW(), NOW()),
('pa037', 'p031', 'pr024', 'ACTOR', 'Rose DeWitt Bukater', NOW(), NOW()),
('pa038', 'p032', 'pr024', 'ACTOR', 'Cal Hockley', NOW(), NOW()),
('pa039', 'p033', 'pr024', 'DIRECTOR', NULL, NOW(), NOW()),

-- Gladiator
('pa040', 'p034', 'pr025', 'ACTOR', 'Máximo Décimo Meridio', NOW(), NOW()),
('pa041', 'p035', 'pr025', 'ACTOR', 'Cómodo', NOW(), NOW()),
('pa042', 'p036', 'pr025', 'ACTOR', 'Lucila', NOW(), NOW()),
('pa043', 'p037', 'pr025', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Shawshank Redemption
('pa044', 'p038', 'pr026', 'ACTOR', 'Andy Dufresne', NOW(), NOW()),
('pa045', 'p039', 'pr026', 'ACTOR', 'Ellis Boyd "Red" Redding', NOW(), NOW()),
('pa046', 'p040', 'pr026', 'ACTOR', 'Warden Norton', NOW(), NOW()),
('pa047', 'p041', 'pr026', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Godfather Part II
('pa048', 'p012', 'pr027', 'ACTOR', 'Michael Corleone', NOW(), NOW()),
('pa049', 'p042', 'pr027', 'ACTOR', 'Vito Corleone', NOW(), NOW()),
('pa050', 'p043', 'pr027', 'ACTOR', 'Tom Hagen', NOW(), NOW()),
('pa051', 'p014', 'pr027', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Lord of the Rings: The Return of the King
('pa052', 'p044', 'pr028', 'ACTOR', 'Frodo Baggins', NOW(), NOW()),
('pa053', 'p045', 'pr028', 'ACTOR', 'Aragorn', NOW(), NOW()),
('pa054', 'p046', 'pr028', 'ACTOR', 'Gandalf', NOW(), NOW()),
('pa055', 'p047', 'pr028', 'DIRECTOR', NULL, NOW(), NOW()),

-- Interstellar
('pa056', 'p048', 'pr029', 'ACTOR', 'Cooper', NOW(), NOW()),
('pa057', 'p049', 'pr029', 'ACTOR', 'Amelia Brand', NOW(), NOW()),
('pa058', 'p050', 'pr029', 'ACTOR', 'Murph', NOW(), NOW()),
('pa059', 'p003', 'pr029', 'DIRECTOR', NULL, NOW(), NOW()),

-- Whiplash
('pa060', 'p051', 'pr030', 'ACTOR', 'Andrew Neiman', NOW(), NOW()),
('pa061', 'p052', 'pr030', 'ACTOR', 'Terence Fletcher', NOW(), NOW()),
('pa062', 'p053', 'pr030', 'ACTOR', 'Jim Neiman', NOW(), NOW()),
('pa063', 'p054', 'pr030', 'DIRECTOR', NULL, NOW(), NOW()),

-- La La Land
('pa064', 'p055', 'pr031', 'ACTOR', 'Sebastian', NOW(), NOW()),
('pa065', 'p056', 'pr031', 'ACTOR', 'Mia', NOW(), NOW()),
('pa066', 'p057', 'pr031', 'ACTOR', 'Keith', NOW(), NOW()),
('pa067', 'p054', 'pr031', 'DIRECTOR', NULL, NOW(), NOW()),

-- Django Desencadenado
('pa068', 'p058', 'pr032', 'ACTOR', 'Django', NOW(), NOW()),
('pa069', 'p059', 'pr032', 'ACTOR', 'Dr. King Schultz', NOW(), NOW()),
('pa070', 'p005', 'pr032', 'ACTOR', 'Calvin Candie', NOW(), NOW()),
('pa071', 'p006', 'pr032', 'DIRECTOR', NULL, NOW(), NOW()),

-- Indiana Jones
('pa072', 'p001', 'pr033', 'ACTOR', 'Indiana Jones', NOW(), NOW()),
('pa079', 'p060', 'pr033', 'ACTOR', 'Indiana Jones', NOW(), NOW()),
('pa080', 'p061', 'pr033', 'DIRECTOR', NULL, NOW(), NOW()),

-- Jurassic Park
('pa073', 'p001', 'pr034', 'ACTOR', 'Dr. Alan Grant', NOW(), NOW()),
('pa081', 'p062', 'pr034', 'ACTOR', 'Dr. Ellie Sattler', NOW(), NOW()),
('pa082', 'p061', 'pr034', 'DIRECTOR', NULL, NOW(), NOW()),

-- Misión Imposible
('pa074', 'p020', 'pr035', 'ACTOR', 'Luther Stickell', NOW(), NOW()),
('pa083', 'p063', 'pr035', 'ACTOR', 'Ethan Hunt', NOW(), NOW()),

-- Fast & Furious
('pa075', 'p018', 'pr036', 'ACTOR', 'Dominic Toretto', NOW(), NOW()),
('pa084', 'p064', 'pr036', 'ACTOR', 'Dominic Toretto', NOW(), NOW()),

-- El silencio de los corderos
('pa076', 'p012', 'pr037', 'ACTOR', 'Jack Crawford', NOW(), NOW()),
('pa085', 'p065', 'pr037', 'ACTOR', 'Clarice Starling', NOW(), NOW()),
('pa086', 'p066', 'pr037', 'ACTOR', 'Hannibal Lecter', NOW(), NOW()),

-- El club de la lucha
('pa077', 'p005', 'pr038', 'ACTOR', 'Tyler Durden', NOW(), NOW()),
('pa078', 'p039', 'pr038', 'ACTOR', 'Narrador', NOW(), NOW()),
('pa087', 'p067', 'pr038', 'DIRECTOR', NULL, NOW(), NOW());

-- Insertar ubicaciones de filmación para tus producciones
INSERT INTO ubicaciones (id, nombre, latitud, longitud, produccion_id) VALUES
('u006', 'Nueva York, USA', 40.7128, -74.0060, 'pr018'),
('u007', 'Chicago, USA', 41.8781, -87.6298, 'pr018'),
('u008', 'Los Ángeles, USA', 34.0522, -118.2437, 'pr019'),
('u009', 'Londres, UK', 51.5074, -0.1278, 'pr019'),
('u010', 'París, Francia', 48.8566, 2.3522, 'pr022'),
('u011', 'Montreal, Canadá', 45.5017, -73.5673, 'pr022'),
('u012', 'Sydney, Australia', -33.8688, 151.2093, 'pr023'),
('u013', 'Alabama, USA', 32.3182, -86.9023, 'pr021'),
('u014', 'Savannah, Georgia, USA', 32.0809, -81.0912, 'pr021'),
('u015', 'Mississippi, USA', 32.3547, -89.3985, 'pr032'),
('u016', 'Túnez', 33.8869, 9.5375, 'pr033'),
('u017', 'Hawaii, USA', 19.8968, -155.5828, 'pr033'),
('u018', 'Costa Rica', 9.7489, -83.7534, 'pr034'),
('u019', 'Praga, República Checa', 50.0755, 14.4378, 'pr035'),
('u020', 'Los Ángeles, USA', 34.0522, -118.2437, 'pr036'),
('u021', 'Pittsburgh, Pennsylvania, USA', 40.4406, -79.9959, 'pr037'),
('u022', 'Wilmington, North Carolina, USA', 34.2257, -77.9447, 'pr038');

-- Insertar usuarios
INSERT INTO usuarios (id, nombre, apellido, email, username, password, rol, telefono, fecha_nacimiento, created_at, avatar) VALUES
('11111111-1111-1111-1111-111111111111', 'Admin', 'Admin', 'admin@geofilm.com', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ADMINISTRADOR', '600000000', '1980-01-01', NOW(), 'admin_avatar.jpg'),
('22222222-2222-2222-2222-222222222222', 'Usuario', 'Normal', 'user@geofilm.com', 'user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USUARIO', '611111111', '1990-05-15', NOW(), 'user_avatar.jpg'),
('33333333-3333-3333-3333-333333333333', 'María', 'García', 'maria@geofilm.com', 'maria', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'USUARIO', '622222222', '1995-08-20', NOW(), 'maria_avatar.jpg'),
('44444444-4444-4444-4444-444444444444', 'Mario', 'de Domingo Álvarez', 'wolverine@admin.com', 'wolverineAdmin', '$2a$12$hdb/kuYrrChi4PtOP2AFh.zx9hC7AmduUv4EBELRMWzGz63d/viUm', 'ADMINISTRADOR', '600000000', '1980-01-01', NOW(), 'https://sm.ign.com/t/ign_latam/screenshot/default/wolver_9stu.1280.jpg'),
('55555555-5555-5555-5555-555555555555', 'Mario', 'de Domingo Álvarez', 'wolverine@user.com', 'wolverineUser', '$2a$12$hdb/kuYrrChi4PtOP2AFh.zx9hC7AmduUv4EBELRMWzGz63d/viUm', 'USUARIO', '681013487', '1980-01-01', NOW(), 'https://sm.ign.com/t/ign_latam/screenshot/default/wolver_9stu.1280.jpg');

-- Insertar reviews
-- INSERT INTO reviews (id, usuario_id, produccion_id, comentario, puntuacion) VALUES
