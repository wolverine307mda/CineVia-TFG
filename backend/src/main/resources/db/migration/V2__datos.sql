ALTER TABLE usuarios
    ALTER COLUMN is_delete SET DEFAULT false;

-- Insertar sagas
INSERT INTO sagas (id, nombre, descripcion, fecha_inicio, fecha_fin, is_acabada, imagen) VALUES
('s001', 'El Señor de los Anillos', 'La trilogía cinematográfica de El Señor de los Anillos, basada en la novela homónima del escritor británico J. R. R. Tolkien, comprende tres películas épicas de fantasía, acción y aventuras: El Señor de los Anillos: la Comunidad del Anillo (2001), El Señor de los Anillos: las dos torres (2002) y El Señor de los Anillos: el retorno del Rey (2003).

Las tres películas fueron escritas, producidas y dirigidas por Peter Jackson, coescritas por Fran Walsh y Philippa Boyens y distribuidas por New Line Cinema. Considerado como uno de los mayores proyectos cinematográficos alguna vez acometidos, con una recaudación global de más de 2900 millones de dólares,[Nota el proyecto completo duró ocho años, con la filmación simultánea de las tres películas y rodadas enteramente en la tierra natal de Jackson, Nueva Zelanda.

Aun con algunas variaciones, la trilogía se ciñe en líneas generales a la trama principal de la novela sobre la que se basa. Ambientada en el ficticio mundo de la Tierra Media, sigue las aventuras del hobbit Frodo Bolsón y sus compañeros en su misión de destruir el Anillo Único y asegurar así la aniquilación del Señor Oscuro, Sauron. Sin embargo, esa comunidad se rompe y Frodo continúa la aventura junto a su fiel compañero Sam y el traicionero Gollum. Por otro lado, el mago Gandalf y Aragorn, heredero en el exilio del trono de Gondor, se unen y lideran a los pueblos libres de la Tierra Media durante varias batallas contra las fuerzas del mal.

La trilogía cosechó un gran éxito de taquilla: las películas alcanzaron, respectivamente, los puestos 26.º, 19.º y 6.º entre las más taquilleras de la historia. También fueron aclamadas por la crítica, al obtener un total de diecisiete premios Óscar, diez premios BAFTA y cuatro premios Globo de Oro, así como amplias alabanzas hacia el reparto y las innovaciones en lo referente a efectos especiales digitales. Cada película de la trilogía fue reeditada como versión extendida, lanzada un año después del lanzamiento en DVD de la versión proyectada en las salas de cine.', '2001-12-19', '2003-12-17', true, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVKgKemtNYpNTqlCMkl3DgptGwhulkT8U1Jg&s'),
('s002', 'Star Wars', 'Star Wars, conocida también en español como La guerra de las galaxias, es una franquicia de medios de fantasía compuesta primordialmente de una serie de películas concebidas por el cineasta estadounidense George Lucas en la década de 1970, y producidas y distribuidas inicialmente por 20th Century Fox y posteriormente por The Walt Disney Company a partir de 2012. Su trama describe las vivencias de un grupo de personajes que habitan en una galaxia ficticia e interactúan con elementos como «la Fuerza», un campo de energía metafísico y omnipresente que posee un «lado luminoso» impulsado por la sabiduría, la nobleza y la justicia y utilizado por los Jedi, y un «lado oscuro» usado por los Sith y provocado por la ira, el miedo, el odio y la desesperación.', '1977-05-25', NULL, false, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREHA9BWCRD8LBeUe498kzy7ciKcIfpXc7oXw&s'),
('s003', 'Harry Potter', 'Harry Potter es una serie de novelas fantásticas escrita por la autora británica J. K. Rowling, en la que se describen las aventuras del joven aprendiz de magia y hechicería Harry Potter y sus amigos Hermione Granger y Ron Weasley, durante los años que pasan en el Colegio Hogwarts de Magia y Hechicería. El argumento se centra en la lucha entre Harry Potter y el malvado mago Lord Voldemort, quien asesinó a los padres de Harry en su afán de conquistar el mundo mágico.

Desde el lanzamiento de la primera novela, Harry Potter y la piedra filosofal, en 1997, la serie logró una inmensa popularidad, críticas favorables y éxito comercial alrededor del mundo. Hasta febrero de 2023, Harry Potter ha vendido más de 600 millones de copias en todo el mundo, lo que los convierte en la serie de libros más vendida de la historia. Para julio de 2013 se habían vendido entre 400 y 450 millones de ejemplares de los siete libros, que los ubican como la segunda serie de libros más vendida de la historia y los cuales han sido traducidos a más de 65 idiomas, entre los que se incluyen el latín y el griego antiguo. El séptimo y último libro, Harry Potter y las reliquias de la Muerte, fue lanzado mundialmente en inglés el 21 de julio de 2007, mientras que en español se publicó el 21 de febrero de 2008.[', '2001-11-16', '2011-07-15', true, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeYG5eD76ri_YGeKPIUDPODd5y-2pdBAjTKg&s'),
('s100', 'Marvel Cinematic Universe', 'El Universo Cinematográfico de Marvel (MCU) es una franquicia de películas de superhéroes basadas en personajes de Marvel Comics. Comenzó con Iron Man en 2008 y ha crecido para incluir múltiples fases de películas interconectadas.', '2008-05-02', NULL, false, 'https://m.media-amazon.com/images/I/91iqen3IYJL.jpg'),
('s005', 'James Bond', 'James Bond es un personaje de ficción creado por el periodista y novelista inglés Ian Fleming en 1953. James Bond es el protagonista de la serie de novelas, películas, cómics y videojuegos homónimos, en las que protagoniza sus propias misiones como James Bond. Su profesión le otorga la denominación de agente encubierto con «licencia para matar», afiliado al servicio secreto de inteligencia británico, conocido actualmente como MI6.', '1962-10-05', NULL, false, 'https://m.media-amazon.com/images/I/71T2aDKfmIL.jpg'),
('s006', 'El Padrino', 'El padrino es el nombre que reciben las películas dirigidas por Francis Ford Coppola y escritas por él mismo junto con el novelista Mario Puzo. La trilogía consta de las tres películas: El padrino, El padrino II y El padrino III. La primera entrega ganó tres premios Óscar (mejor película, mejor actor para Marlon Brando y mejor guion adaptado) de once candidaturas posibles, mientras que la segunda entrega ganó seis (mejor película, mejor director para Coppola, mejor actor de reparto para Robert De Niro, mejor guion adaptado, mejor banda sonora y mejor dirección artística) de once nominaciones posibles, en total nueve premios Óscar. Por su parte, la tercera no tuvo la misma repercusión de público, y además obtuvo siete nominaciones al Óscar pero no consiguió ningún premio.', '1972-03-24', '1990-12-25', true, 'https://image.tmdb.org/t/p/w500/r4gnMXoY1efvaolNDjn3nj4046S.jpg'),
('s007', 'Trilogía de Batman de Nolan', 'La trilogía de The Dark Knight (titulada El caballero oscuro en España y Batman: el caballero de la noche en Hispanoamérica) es una trilogía cinematográfica de superhéroes, basada en el personaje Batman de DC Comics. La trilogía consistió en Batman Begins (2005), The Dark Knight (2008), y The Dark Knight Rises (2012), dirigidas, producidas y escritas principalmente por Christopher Nolan.

Después del fracaso crítico y la decepción taquillera de Batman y Robin (1997), Warner Bros. decidió reiniciar la franquicia. La trilogía está protagonizada por Christian Bale como Bruce Wayne/Batman, Michael Caine como Alfred Pennyworth, Gary Oldman como James Gordon, y Morgan Freeman como Lucius Fox. Las tres películas fueron escritas y dirigidas por Christopher Nolan.

Cada película en la serie fue un éxito taquillero, en particular la segunda y tercera película, ambas de las cuales recaudaron más de $1000 millones mundialmente. Las películas también recibieron aclamaciones casi universales de los críticos y las audiencias, siendo elogiadas por la dirección, los temas y el desarrollo de personajes de Nolan, las actuaciones del reparto, una reinvención más realista y oscura del material original, un mayor uso de efectos prácticos sobre las imágenes generadas por computadora, y la música de Hans Zimmer para cada película. La trilogía de The Dark Knight ha obtenido un amplio reconocimiento no solo siendo una mejora masiva sobre la serie anterior de películas de Batman, sino también el mayor logro del género de las películas de superhéroes, así como tres de las mejores películas de todos los tiempos. Debido a que la trilogía pretendía ser una serie independiente, el personaje fue reiniciado nuevamente para el universo extendido de DC, iniciando con El hombre de acero en 2013.', '2005-06-15', '2012-07-20', true, 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg'),
('s008', 'Matrix', 'Matrix es una tetralogía de películas de ciencia ficción escritas y dirigidas por las hermanas Wachowski. Se compone de The Matrix (1999), The Matrix Reloaded (2003), The Matrix Revolutions (2003) y The Matrix Resurrections (2021) y están protagonizadas en sus papeles principales por Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss y Hugo Weaving.

La trilogía fue vista por aproximadamente 103 millones de estadounidenses en cines. La segunda y la tercera fueron rodadas a la vez, pero estrenadas con un tiempo de diferencia, aprovechando el éxito de la primera.

Adicionalmente, la serie de cortometrajes Animatrix muestra material extra, sirviendo a la vez de enlace entre la primera y las otras dos películas, y de trasfondo para la trilogía, explicando por ejemplo el origen y el desarrollo de la guerra entre la humanidad y sus creaciones.', '1999-03-31', '2021-12-22', false, 'https://m.media-amazon.com/images/M/MV5BNGE1YzI4NzMtZTUxNi00Y2I5LTg2MmQtODE0NThmYTFmMDk0XkEyXkFqcGc@._V1_.jpg'),
('s010', 'Universo de Tarantino', 'El "universo de Tarantino" o "universos de Tarantino" no es una saga en el sentido tradicional, sino una serie de películas que se conectan entre sí mediante personajes, referencias y elementos estilísticos. Existen dos tipos principales de universos: el "más real que la realidad" y el "universo cinematográfico".
El universo "más real que la realidad" incluye películas como:
Reservoir Dogs, True Romance, Pulp Fiction, Death Proof, Inglourious Basterds, Django Unchained, The Hateful Eight.
El universo "cinematográfico" o "películas dentro de películas" incluye:
Natural Born Killers, From Dusk Till Dawn, Kill Bill.
Algunos personajes y referencias aparecen en ambos universos, creando una red de conexiones que enriquecen la experiencia del espectador que conoce la filmografía de Tarantino. ', '1992-09-02', NULL, false, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3wX8Gn97oi1xvGT4uBZ1_oK3qCWhkBeSf7g&s'),
('s012', 'Indiana Jones', 'Indiana Jones es una franquicia de medios concebida por el cineasta estadounidense George Lucas y producida por Lucasfilm, cuyo argumento relata primordialmente las vivencias del aventurero homónimo ficticio para localizar objetos de relevancia histórica tales como el Arca de la Alianza y el Santo Grial, a la vez que se enfrenta a adversarios nazis y soviéticos para evitar que utilicen tales reliquias con fines siniestros.[', '1981-06-12', '2023-06-30', false, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6y6M128p9P5KE_NVlnMUSWtmI-6LUiQCgng&s'),
('s013', 'Jurassic Park', 'Parque Jurásico (Jurassic Park en inglés) es una franquicia literaria, cinematográfica y de videojuegos que comenzó con la novela de mismo título del escritor estadounidense Michael Crichton, publicada por primera vez en Estados Unidos en 1990. Posteriormente fue adaptada como una película del mismo nombre dirigida por Steven Spielberg. Es considerada a menudo como un relato de preocupación sobre la manipulación biológica del mismo modo que lo hizo la novela Frankenstein de Mary Shelley en su tiempo. En este relato se utiliza el concepto matemático de la teoría del caos y sus implicaciones filosóficas para explicar el colapso de un parque de atracciones que tiene como espectáculo principal ciertas especies de dinosaurios recreadas artificialmente.', '1993-06-11', '2022-06-10', false, 'https://m.media-amazon.com/images/I/71X6YzwV0gL._AC_UF1000,1000_QL80_.jpg'),
('s014', 'Misión Imposible', 'Misión: Imposible es una serie de películas de acción y espionaje estadounidense basada en la serie televisiva del mismo título. La serie es coproducida y protagonizada por Tom Cruise como Ethan Hunt, un agente de la ficticia Fuerza de Misiones Imposibles (IMF).

La serie inició en 1996 con el estreno de Misión imposible y relata las misiones de Hunt, quien es forzado a encargarse del equipo principal de la IMF después de que este sea traicionado en la primera cinta de la serie. A diferencia de la serie televisiva que gira alrededor de varios personajes, las películas se centran en un personaje principal (Hunt), aunque otros personajes como Luther Stickell (interpretado por Ving Rhames) y Benji Dunn (interpretado por Simon Pegg) poseen papeles recurrentes a lo largo de la serie cinematográfica.', '1996-05-22', NULL, false, 'https://m.media-amazon.com/images/S/pv-target-images/1b642f970195bc58824ddcb57315348cf896e4468dc10ab61a8e7ff537e40d67.jpg'),
('s015', 'Fast & Furious', 'Fast & Furious (también conocida como Rápidos y furiosos en Hispanoamérica, y The Fast and Furious: A todo gas en España) es una franquicia de medios estadounidense centrada en una serie de películas de acción que se ocupan en gran medida de automóviles. La saga también incluye cortometrajes, una serie de televisión, espectáculos en vivo, videojuegos y atracciones de parques temáticos. Está distribuida por Universal Pictures. Si bien la saga ha tenido una baja muy importante del elenco con la muerte de Paul Walker en 2013, Vin Diesel continuó con las series de películas que le prometió al actor ya fallecido, recalcando que junto con el resto del elenco consideran que aun la saga está muy activa.', '2001-06-22', NULL, false, 'https://www.universalpictures.es/tl_files/content/movies/fast9/fast9_header_mobile.jpg'),
('s016', 'Canción de Hielo y Fuego', 'Una saga de fantasía épica creada por George R.R. Martin que incluye las novelas "Juego de Tronos" y sus secuelas, adaptadas a la televisión como "Game of Thrones" y su precuela "House of the Dragon". La historia se desarrolla en los continentes ficticios de Poniente y Essos, siguiendo las luchas de poder entre familias nobles por el control del Trono de Hierro.', '2011-04-17', '2022-08-21', false, 'https://m.media-amazon.com/images/M/MV5BN2IzYzBiOTQtNGZmMi00NDI5LTgxMzMtN2EzZjA1NjhlOGMxXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg');

-- Insertar tus producciones de ejemplo (películas)
INSERT INTO producciones (id, titulo, tipo, estreno, duracion, clasificacion_edad, sinopsis, informacion, imagen, saga_id, puntuacion) VALUES
('pr018', 'El Padrino', 'PELICULA', '1972-03-24', 175, 3, 'América, años 40. Don Vito Corleone (Marlon Brando) es el respetado y temido jefe de una de las cinco familias de la mafia de Nueva York. Tiene cuatro hijos: Connie (Talia Shire), el impulsivo Sonny (James Caan), el pusilánime Fredo (John Cazale) y Michael (Al Pacino), que no quiere saber nada de los negocios de su padre. Cuando Corleone, en contra de los consejos de ''Il consigliere'' Tom Hagen (Robert Duvall), se niega a participar en el negocio de las drogas, el jefe de otra banda ordena su asesinato. Empieza entonces una violenta y cruenta guerra entre las familias mafiosas.', 'El padrino (título original en inglés: The Godfather) es una película estadounidense de 1972 dirigida por Francis Ford Coppola. La película fue producida por Albert S. Ruddy, de la compañía Paramount Pictures. Está basada en la novela homónima (que a su vez está basada en la familia real de los Mortillaro de Sicilia, Italia) de Mario Puzo, quien adaptó el guion junto a Coppola y Robert Towne, este último sin ser acreditado. Protagonizada por Marlon Brando y Al Pacino como los líderes de una poderosa familia criminal ficticia de Nueva York, la historia, ambientada desde 1945 a 1955, cuenta las crónicas de la familia Corleone liderada por Vito Corleone (Brando), enfocándose en el personaje de Michael Corleone (Pacino), y su transformación de un reacio joven ajeno a los asuntos familiares a un implacable jefe de la mafia ítalo-estadounidense.', 'https://image.tmdb.org/t/p/w500/r4gnMXoY1efvaolNDjn3nj4046S.jpg', 's006', 8.6),
('pr019', 'El Caballero Oscuro', 'PELICULA', '2008-07-18', 152, 2, 'Batman tiene que aceptar la ayuda de un nuevo fiscal para combatir a un criminal anarquista conocido como el Joker.', 'Segunda película de la trilogía de Nolan.', 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg', 's007', 6.5),
('pr020', 'Pulp Fiction', 'PELICULA', '1994-10-14', 154, 4, 'Las vidas de dos sicarios, un boxeador, la esposa de un gánster y dos bandidos se entrelazan en cuatro historias de violencia y redención.', 'Ganadora de la Palma de Oro en Cannes.', 'https://m.media-amazon.com/images/I/81UTs3sC5hL._AC_UF894,1000_QL80_.jpg', 's010', 8.5),
('pr021', 'Forrest Gump', 'PELICULA', '1994-07-06', 142, 2, 'La presidencia de Kennedy y Johnson, Vietnam, Watergate y otros eventos históricos se desarrollan desde la perspectiva de un hombre de Alabama con un coeficiente intelectual de 75.', 'Ganadora de 6 premios Oscar.', 'https://media.es.wired.com/photos/6679bb407faf643236b6024c/2:3/w_576,h_864,c_limit/Forrest%20Gump.jpg', NULL, 6.5),
('pr022', 'Inception', 'PELICULA', '2010-07-16', 148, 2, 'Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños es encargado con la tarea inversa de implantar una idea en la mente de un CEO.', 'Dirigida por Christopher Nolan.', 'https://m.media-amazon.com/images/M/MV5BMjExMjkwNTQ0Nl5BMl5BanBnXkFtZTcwNTY0OTk1Mw@@._V1_.jpg', null, 6.5),
('pr023', 'The Matrix', 'PELICULA', '1999-03-31', 136, 2, 'Un hacker aprende de misteriosos rebeldes sobre la verdadera naturaleza de su realidad y su papel en la guerra contra sus controladores.', 'Revolucionó los efectos visuales.', 'https://m.media-amazon.com/images/M/MV5BNGE1YzI4NzMtZTUxNi00Y2I5LTg2MmQtODE0NThmYTFmMDk0XkEyXkFqcGc@._V1_.jpg', 's008', 6.5),
('pr024', 'Titanic', 'PELICULA', '1997-12-19', 195, 2, 'Una aristócrata de 17 años se enamora de un artista amable pero pobre a bordo del lujoso e infortunado R.M.S. Titanic.', 'Ganadora de 11 premios Oscar.', 'https://m.media-amazon.com/images/I/811lT7khIrL.jpg', NULL, 6.5),
('pr025', 'Gladiator', 'PELICULA', '2000-05-05', 155, 3, 'Un ex general romano busca venganza contra el corrupto emperador que asesinó a su familia y lo envió a la esclavitud.', 'Ganadora de 5 premios Oscar.', 'https://m.media-amazon.com/images/I/51GA6V6VE1L._AC_UF894,1000_QL80_.jpg', NULL, 6.5),
('pr026', 'The Shawshank Redemption', 'PELICULA', '1994-09-23', 142, 3, 'Dos hombres encarcelados se vinculan durante varios años, encontrando consuelo y eventual redención a través de actos de decencia común.', 'Basada en la novela de Stephen King.', 'https://pics.filmaffinity.com/Cadena_perpetua-576140557-large.jpg', NULL, 6.5),
('pr027', 'The Godfather Part II', 'PELICULA', '1974-12-20', 202, 3, 'La temprana vida y carrera de Vito Corleone en 1920 es representada mientras su hijo, Michael, expande y refuerza su control sobre el sindicato del crimen familiar.', 'Secuela de El Padrino.', 'https://s3.amazonaws.com/nightjarprod/content/uploads/sites/192/2022/10/17140449/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg', 's006', 6.5),
('pr028', 'The Lord of the Rings: The Return of the King', 'PELICULA', '2003-12-17', 201, 2, 'Gandalf y Aragorn lideran el Mundo de los Hombres contra el ejército de Sauron para distraer su atención de Frodo y Sam mientras se acercan al Monte del Destino con el Anillo Único.', 'Tercera parte de la trilogía.', 'https://m.media-amazon.com/images/M/MV5BMTZkMjBjNWMtZGI5OC00MGU0LTk4ZTItODg2NWM3NTVmNWQ4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg', 's001', 6.5),
('pr029', 'Interstellar', 'PELICULA', '2014-11-07', 169, 2, 'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento por asegurar la supervivencia de la humanidad.', 'Dirigida por Christopher Nolan.', 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg', null, 6.5),
('pr030', 'Whiplash', 'PELICULA', '2014-10-15', 106, 3, 'Un joven baterista ambicioso se inscribe en un conservatorio de música donde su instructor abusivo no se detendrá ante nada para hacer que un estudiante alcance la grandeza.', 'Ganadora de 3 premios Oscar.', 'https://image.tmdb.org/t/p/w500/oPxnRhyAIzJKGUEdSiwTJQBa3NM.jpg', NULL, 6.5),
('pr031', 'La La Land', 'PELICULA', '2016-12-09', 128, 2, 'Mientras navegan por sus carreras en Los Ángeles, un pianista y una actriz se enamoran mientras intentan reconciliar sus aspiraciones para el futuro.', 'Ganadora de 6 premios Oscar.', 'https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg', NULL, 6.5),
('pr032', 'Django Desencadenado', 'PELICULA', '2012-12-25', 165, 4, 'Con la ayuda de un cazarrecompensas alemán, un esclavo liberado se embarca en una misión para rescatar a su esposa de un brutal propietario de una plantación en Mississippi.', 'Dirigida por Quentin Tarantino.', 'https://image.tmdb.org/t/p/w500/7oWY8VDWW7thTzWh3OKYRkWUlD5.jpg', 's010', 6.5),
('pr033', 'Indiana Jones y los cazadores del arca perdida', 'PELICULA', '1981-06-12', 115, 2, 'El arqueólogo Indiana Jones busca el Arca de la Alianza', 'Dirigida por Steven Spielberg', 'https://m.media-amazon.com/images/M/MV5BNjEwNjY0ZTAtMjk0MS00MDhkLTkwYWUtMjM3ZmI2M2JhYjJhXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg', 's012', 6.5),
('pr034', 'Jurassic Park', 'PELICULA', '1993-06-11', 127, 2, 'Un parque temático con dinosaurios clonados sale mal', 'Basada en la novela de Michael Crichton', 'https://i.pinimg.com/736x/51/ba/64/51ba64b2e61f820e0e86bdd2f4c6e92c.jpg', 's013', 6.5),
('pr035', 'Misión Imposible', 'PELICULA', '1996-05-22', 110, 2, 'Ethan Hunt es acusado de traición y debe limpiar su nombre', 'Primera película de la saga', 'https://m.media-amazon.com/images/S/pv-target-images/1b642f970195bc58824ddcb57315348cf896e4468dc10ab61a8e7ff537e40d67.jpg', 's014', 6.5),
('pr036', 'Fast & Furious', 'PELICULA', '2001-06-22', 106, 3, 'Un policía se infiltra en el mundo de las carreras callejeras', 'Inicio de la franquicia', 'https://www.universalpictures.es/tl_files/content/movies/fast9/fast9_header_mobile.jpg', 's015', 6.5),
('pr037', 'El silencio de los corderos', 'PELICULA', '1991-02-14', 118, 4, 'Una agente del FBI busca ayuda de un asesino en serie', 'Ganadora de 5 Oscars', 'https://m.media-amazon.com/images/I/61DMhNjjJ3L._AC_UF894,1000_QL80_.jpg', NULL, 6.5),
('pr038', 'El club de la lucha', 'PELICULA', '1999-10-15', 139, 4, 'Un oficinista y un vendedor forman un club de lucha subterráneo', 'Basada en la novela de Chuck Palahniuk', 'https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjptGkW4UwD3BdMJMcAPCcuYD9U9M8GJD3QQ76FIanqoxspZk2q9sdKXSOIx7yiQsF5T-ZdkTHUM21_AEN6vH2OXucUbTnIhQ4CEUbMtWr7TZ-FIklXl5vrGg1fl7vib3E2of5DWCGmE3H3/s2048/Club-de-la-lucha.jpg', NULL, 6.5),

--Marvel
-- Phase 1
('pr100', 'Iron Man', 'PELICULA', '2008-05-02', 126, 2, 'Tony Stark, un industrial multimillonario y genio inventor, es secuestrado y obligado a construir un arma devastadora. En su lugar, usando su inteligencia e ingenio, construye una armadura de alta tecnología y escapa de su cautiverio.', 'Primera película del MCU que estableció el tono para el universo compartido.', 'https://m.media-amazon.com/images/M/MV5BMTczNTI2ODUwOF5BMl5BanBnXkFtZTcwMTU0NTIzMw@@._V1_.jpg', 's100', 7.9),
('pr101', 'The Incredible Hulk', 'PELICULA', '2008-06-13', 112, 2, 'Bruce Banner busca una cura para su condición de Hulk mientras es perseguido por el ejército y se enfrenta a un nuevo enemigo, el Abominación.', 'La única película de Hulk en el MCU con Edward Norton como Bruce Banner.', 'https://m.media-amazon.com/images/M/MV5BMTUyNzk3MjA1OF5BMl5BanBnXkFtZTcwMTE1Njg2MQ@@._V1_.jpg', 's100', 6.6),
('pr102', 'Iron Man 2', 'PELICULA', '2010-05-07', 124, 2, 'Con el mundo ahora consciente de que él es Iron Man, Tony Stark debe enfrentarse a nuevos enemigos mientras lidia con su salud en declive.', 'Introdujo a Black Widow y profundizó en la historia de Tony Stark.', 'https://m.media-amazon.com/images/M/MV5BMTM0MDgwNjMyMl5BMl5BanBnXkFtZTcwNTg3NzAzMw@@._V1_.jpg', 's100', 6.9),
('pr103', 'Thor', 'PELICULA', '2011-05-06', 115, 2, 'El poderoso Thor es desterrado de Asgard y enviado a vivir entre los humanos en la Tierra, donde aprende lo que se necesita para ser un verdadero héroe.', 'Presentó el mundo de Asgard y el concepto de los Nueve Reinos.', 'https://m.media-amazon.com/images/M/MV5BOGE4NzU1YTAtNzA3Mi00ZTA2LTg2YmYtMDJmMThiMjlkYjg2XkEyXkFqcGdeQXVyNTgzMDMzMTg@._V1_.jpg', 's100', 7.0),
('pr104', 'Captain America: The First Avenger', 'PELICULA', '2011-07-22', 124, 2, 'Durante la Segunda Guerra Mundial, Steve Rogers, un hombre débil pero de gran corazón, se transforma en el supersoldado Capitán América para luchar contra las fuerzas de HYDRA.', 'Película de época que estableció los orígenes del Capitán América.', 'https://m.media-amazon.com/images/M/MV5BMTYzOTc2NzU3N15BMl5BanBnXkFtZTcwNjY3MDE3NQ@@._V1_.jpg', 's100', 6.9),
('pr105', 'The Avengers', 'PELICULA', '2012-05-04', 143, 2, 'Nick Fury recluta a un equipo de superhéroes para salvar al mundo de la destrucción inminente cuando Loki intenta conquistar la Tierra.', 'Primera película crossover del MCU que unió a los héroes principales.', 'https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLWFmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg', 's100', 8.0),

-- Phase 2
('pr106', 'Iron Man 3', 'PELICULA', '2013-05-03', 130, 2, 'Cuando Tony Stark se enfrenta a un enemigo cuyo alcance no conoce límites, se embarca en una angustiosa búsqueda para destruir a los responsables.', 'Exploró el trauma de Tony Stark después de los eventos de The Avengers.', 'https://m.media-amazon.com/images/M/MV5BMjE5MzcyNjk1M15BMl5BanBnXkFtZTcwMjQ4MjcxOQ@@._V1_.jpg', 's100', 7.1),
('pr107', 'Thor: The Dark World', 'PELICULA', '2013-11-08', 112, 2, 'Thor se une a Loki para salvar los Nueve Reinos de un enemigo más antiguo que el universo mismo mientras Jane Foster descubre una misteriosa sustancia llamada Aether.', 'Introdujo la Gema de la Realidad y profundizó en la mitología de Thor.', 'https://comicsen8mm.com/wp-content/uploads/2013/09/Thor-TDW-Poster-09.jpg', 's100', 6.8),
('pr108', 'Captain America: The Winter Soldier', 'PELICULA', '2014-04-04', 136, 2, 'Steve Rogers lucha por exponer una conspiración mientras se enfrenta a un asesino misterioso conocido como el Soldado del Invierno.', 'Película de espionaje que cambió el status quo del MCU.', 'https://m.media-amazon.com/images/M/MV5BMzA2NDkwODAwM15BMl5BanBnXkFtZTgwODk5MTgzMTE@._V1_.jpg', 's100', 7.7),
('pr109', 'Guardians of the Galaxy', 'PELICULA', '2014-08-01', 121, 2, 'Un grupo de inadaptados intergalácticos se unen para proteger una poderosa gema del malvado Ronan.', 'Expandió el MCU al cosmos con un tono más humorístico.', 'https://m.media-amazon.com/images/M/MV5BMTAwMjU5OTgxNjZeQTJeQWpwZ15BbWU4MDUxNDYxODEx._V1_.jpg', 's100', 8.0),
('pr110', 'Avengers: Age of Ultron', 'PELICULA', '2015-05-01', 141, 2, 'Los Vengadores se enfrentan a Ultron, una inteligencia artificial creada por Tony Stark que amenaza con destruir la humanidad.', 'Introdujo a Visión, Bruja Escarlata y profundizó en las relaciones entre los héroes.', 'https://m.media-amazon.com/images/M/MV5BMTM4OGJmNWMtOTM4Ni00NTE3LTg3MDItZmQxYjc4N2JhNmUxXkEyXkFqcGdeQXVyNTgzMDMzMTg@._V1_.jpg', 's100', 7.3),
('pr111', 'Ant-Man', 'PELICULA', '2015-07-17', 117, 2, 'Un ladrón con la capacidad de encogerse en tamaño pero aumentar en fuerza debe ayudar a su mentor a proteger el secreto de su traje y salvar al mundo.', 'Película más pequeña en escala pero importante para el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjM2NTQ5Mzc2M15BMl5BanBnXkFtZTgwNTcxMDI2NTE@._V1_.jpg', 's100', 7.3),

-- Phase 3
('pr112', 'Captain America: Civil War', 'PELICULA', '2016-05-06', 147, 2, 'Los Vengadores se dividen en dos bandos, uno liderado por Steve Rogers y otro por Tony Stark, debido a un desacuerdo sobre la supervisión gubernamental.', 'Introdujo a Black Panther y Spider-Man en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjQ0MTgyNjAxMV5BMl5BanBnXkFtZTgwNjUzMDkyODE@._V1_.jpg', 's100', 7.8),
('pr113', 'Doctor Strange', 'PELICULA', '2016-11-04', 115, 2, 'Un neurocirujano arrogante aprende los secretos de las artes místicas después de un accidente que destruye su carrera.', 'Introdujo la magia y las dimensiones alternativas al MCU.', 'https://m.media-amazon.com/images/M/MV5BNjgwNzAzNjk1Nl5BMl5BanBnXkFtZTgwMzQ2NjI1OTE@._V1_.jpg', 's100', 7.5),
('pr114', 'Guardians of the Galaxy Vol. 2', 'PELICULA', '2017-05-05', 136, 2, 'Los Guardianes luchan por mantenerse unidos mientras descubren los secretos del verdadero padre de Peter Quill.', 'Profundizó en los personajes y su dinámica familiar.', 'https://m.media-amazon.com/images/M/MV5BMTg2MzI1MTg3OF5BMl5BanBnXkFtZTgwNTU3NDA2MTI@._V1_.jpg', 's100', 7.6),
('pr115', 'Spider-Man: Homecoming', 'PELICULA', '2017-07-07', 133, 2, 'Peter Parker equilibra su vida como estudiante de secundaria con ser Spider-Man mientras se enfrenta al Buitre.', 'Primera película de Spider-Man en el MCU.', 'https://m.media-amazon.com/images/M/MV5BNTk4ODQ1MzgzNl5BMl5BanBnXkFtZTgwMTMyMzM4MTI@._V1_.jpg', 's100', 7.4),
('pr116', 'Thor: Ragnarok', 'PELICULA', '2017-11-03', 130, 2, 'Thor está atrapado en el otro lado del universo y debe luchar por su vida en una carrera de gladiadores para salvar a su pueblo de la poderosa Hela.', 'Cambió el tono de las películas de Thor a un estilo más cómico.', 'https://m.media-amazon.com/images/M/MV5BMjMyNDkzMzI1OF5BMl5BanBnXkFtZTgwODcxODg5MjI@._V1_.jpg', 's100', 7.9),
('pr117', 'Black Panther', 'PELICULA', '2018-02-16', 134, 2, 'TChalla, rey de Wakanda, debe defender su nación y su legado cuando un viejo enemigo reaparece.', 'Primera película de superhéroes negros nominada al Oscar a Mejor Película.', 'https://m.media-amazon.com/images/M/MV5BMTg1MTY2MjYzNV5BMl5BanBnXkFtZTgwMTc4NTMwNDI@._V1_.jpg', 's100', 7.3),
('pr118', 'Avengers: Infinity War', 'PELICULA', '2018-04-27', 149, 2, 'Los Vengadores y sus aliados deben estar dispuestos a sacrificarlo todo para derrotar al poderoso Thanos antes de que su ataque de devastación y ruina acabe con el universo.', 'Primera parte del clímax de la Saga del Infinito.', 'https://m.media-amazon.com/images/M/MV5BMjMxNjY2MDU1OV5BMl5BanBnXkFtZTgwNzY1MTUwNTM@._V1_.jpg', 's100', 8.4),
('pr119', 'Ant-Man and the Wasp', 'PELICULA', '2018-07-06', 118, 2, 'Scott Lang lucha por equilibrar su vida como padre y como Ant-Man mientras Hope van Dyne y el Dr. Hank Pym lo reclaman para una nueva misión urgente.', 'Película más ligera después del intenso final de Infinity War.', 'https://m.media-amazon.com/images/M/MV5BYjcyYTk0N2YtMzc4ZC00Y2E0LWFkNDgtNjE1MzZmMGE1YjY1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg', 's100', 7.0),
('pr120', 'Captain Marvel', 'PELICULA', '2019-03-08', 124, 2, 'Carol Danvers se convierte en uno de los héroes más poderosos del universo cuando la Tierra queda atrapada en medio de una guerra galáctica entre dos razas alienígenas.', 'Primera película del MCU protagonizada por una mujer.', 'https://m.media-amazon.com/images/M/MV5BMTE0YWFmOTMtYTU2ZS00ZTIxLWE3OTEtYTNiYzBkZjViZThiXkEyXkFqcGdeQXVyODMzMzQ4OTI@._V1_.jpg', 's100', 6.8),
('pr121', 'Avengers: Endgame', 'PELICULA', '2019-04-26', 181, 2, 'Después de los eventos devastadores de "Avengers: Infinity War", el universo está en ruinas debido a las acciones de Thanos, el Titán Loco. Con la ayuda de los aliados que quedaron, los Vengadores deberán reunirse una vez más para intentar detenerlo y restaurar el orden en el universo de una vez por todas.', 'Avengers: Endgame (Vengadores: Endgame en España es una película de superhéroes estadounidense de 2019 basada en el equipo de superhéroes de Marvel Comics, Los Vengadores. Producida por Marvel Studios y distribuida por Walt Disney Studios Motion Pictures, es la secuela directa de Avengers: Infinity War (2018) y la película número 22 del Universo cinematográfico de Marvel (UCM), y hasta la fecha la más larga. Dirigida por Anthony y Joe Russo y escrita por Christopher Markus y Stephen McFeely, la película cuenta con un reparto coral que incluye a Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth, Scarlett Johansson, Jeremy Renner, Don Cheadle, Paul Rudd, Brie Larson, Karen Gillan, Danai Gurira, Benedict Wong, Jon Favreau, Bradley Cooper, Gwyneth Paltrow y Josh Brolin. En la película, los miembros supervivientes de los Vengadores y sus aliados intentan revertir el daño causado por Thanos en Infinity War.', 'https://m.media-amazon.com/images/M/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_.jpg', 's100', 8.4),
('pr122', 'Spider-Man: Far From Home', 'PELICULA', '2019-07-02', 129, 2, 'Peter Parker va de vacaciones a Europa con sus amigos, pero su plan de dejar de ser Spider-Man por un tiempo se ve interrumpido cuando Nick Fury lo reclama para una misión.', 'Primera película del MCU después de Endgame.', 'https://m.media-amazon.com/images/M/MV5BMGZlNTY1ZWUtYTMzNC00ZjUyLWE0MjQtMTMxN2E3ODYxMWVmXkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_.jpg', 's100', 7.4),

-- Phase 4
('pr123', 'Black Widow', 'PELICULA', '2021-07-09', 134, 3, 'Natasha Romanoff se enfrenta a las partes más oscuras de su historia cuando surge una peligrosa conspiración relacionada con su pasado.', 'Primera película en solitario de Black Widow.', 'https://m.media-amazon.com/images/M/MV5BNjRmNDI5MjMtMmFhZi00YzcwLWI4ZGItMGI2MjI0N2Q3YmIwXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg', 's100', 6.7),
('pr124', 'Shang-Chi and the Legend of the Ten Rings', 'PELICULA', '2021-09-03', 132, 2, 'Shang-Chi, un maestro de artes marciales, se ve arrastrado al misterioso mundo de los Diez Anillos.', 'Primera película del MCU protagonizada por un superhéroe asiático.', 'https://m.media-amazon.com/images/M/MV5BNTliYjlkNDQtMjFlNS00NjgzLWFmMWEtYmM2Mzc2Zjg3ZjEyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg', 's100', 7.4),
('pr125', 'Eternals', 'PELICULA', '2021-11-05', 156, 2, 'Los Eternos, una raza de seres inmortales, emergen de las sombras para proteger a la Tierra de sus antiguos enemigos, los Deviants.', 'Introdujo un nuevo grupo de héroes cósmicos al MCU.', 'https://m.media-amazon.com/images/M/MV5BMTExZmVjY2ItYTAzYi00MDdlLWFlOWItNTJhMDRjMzQ5ZGY0XkEyXkFqcGdeQXVyODIyOTEyMzY@._V1_.jpg', 's100', 6.3),
('pr126', 'Spider-Man: No Way Home', 'PELICULA', '2021-12-17', 148, 2, 'Peter Parker pide ayuda al Doctor Strange para que el mundo olvide que es Spider-Man, pero el hechizo sale mal y abre el multiverso.', 'Película crossover que unió a múltiples versiones de Spider-Man.', 'https://m.media-amazon.com/images/M/MV5BZWMyYzFjYTYtNTRjYi00OGExLWE2YzgtOGRmYjAxZTU3NzBiXkEyXkFqcGdeQXVyMzQ0MzA0NTM@._V1_.jpg', 's100', 8.2),
('pr127', 'Doctor Strange in the Multiverse of Madness', 'PELICULA', '2022-05-06', 126, 3, 'El Doctor Strange viaja a través de realidades alternativas para enfrentarse a una nueva y misteriosa amenaza.', 'Primera película de terror del MCU dirigida por Sam Raimi.', 'https://m.media-amazon.com/images/M/MV5BNWM0ZGJlMzMtZmYwMi00NzI3LTgzMzMtNjMzNjliNDRmZmFlXkEyXkFqcGdeQXVyMTM1MTE1NDMx._V1_.jpg', 's100', 6.9),
('pr128', 'Thor: Love and Thunder', 'PELICULA', '2022-07-08', 119, 3, 'Thor intenta encontrar la paz interior, pero debe regresar a la acción para detener a Gorr el Dios Carnicero.', 'Cuarta película en solitario de Thor con Natalie Portman como Thor.', 'https://m.media-amazon.com/images/M/MV5BYmMxZWRiMTgtZjM0Ny00NDQxLWIxYWQtZDdlNDNkOTEzYTdlXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg', 's100', 6.2),
('pr129', 'Black Panther: Wakanda Forever', 'PELICULA', '2022-11-11', 161, 3, 'El pueblo de Wakanda lucha por proteger su nación de las potencias mundiales tras la muerte del rey TChalla.', 'Homenaje a Chadwick Boseman y continuación de la historia de Wakanda.', 'https://m.media-amazon.com/images/M/MV5BNTM4NjIxNmEtYWE5NS00NDczLTkyNWQtYThhNmQyZGQzMjM0XkEyXkFqcGdeQXVyODk4OTc3MTY@._V1_.jpg', 's100', 6.7),

-- Phase 5
('pr130', 'Ant-Man and the Wasp: Quantumania', 'PELICULA', '2023-02-17', 125, 2, 'Scott Lang y Hope van Dyne son arrastrados al Reino Cuántico, donde se enfrentan a Kang el Conquistador.', 'Introdujo a Kang como el nuevo gran villano del MCU.', 'https://m.media-amazon.com/images/M/MV5BODZhNzlmOGItMWUyYS00Y2Q5LWFlNzMtM2I2NDFkM2ZkYmE1XkEyXkFqcGdeQXVyMTU5OTA4NTIz._V1_.jpg', 's100', 6.1),
('pr131', 'Guardians of the Galaxy Vol. 3', 'PELICULA', '2023-05-05', 150, 3, 'Los Guardianes protegen a uno de los suyos mientras buscan completar su misión final.', 'Conclusión de la trilogía de Guardianes de la Galaxia.', 'https://www.dolby.com/siteassets/xf-site/content-detail-pages/gotg_1280x1920.jpg', 's100', 8.0),
('pr132', 'The Marvels', 'PELICULA', '2023-11-10', 105, 2, 'Carol Danvers, Monica Rambeau y Kamala Khan deben trabajar juntas cuando sus poderes se entrelazan.', 'Secuela de Captain Marvel que une a tres heroínas.', 'https://www.pantalla90.es/wp-content/uploads/2023/11/nuevo-poster-de-the-marvels-original.jpg', 's100', 5.8),

-- la casa del dragon:
('pr133', 'Juego de Tronos', 'SERIE', '2011-04-17', 4200, 3, 'En un mundo donde las estaciones duran años, varias familias nobles luchan por el control del Trono de Hierro del continente de Poniente. Mientras, una antigua amenaza resurge en el norte y una joven exiliada reclama su legítimo lugar en el sur.', 'Serie de televisión basada en la saga de novelas "Canción de hielo y fuego" de George R.R. Martin. La serie se convirtió en un fenómeno cultural y ganó numerosos premios Emmy.', 'https://m.media-amazon.com/images/M/MV5BN2IzYzBiOTQtNGZmMi00NDI5LTgxMzMtN2EzZjA1NjhlOGMxXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg', 's016', 9.2),
('pr134', 'La Casa del Dragón', 'SERIE', '2022-08-21', 600,3, 'Prequela de "Juego de Tronos" que narra la historia de la Casa Targaryen, 200 años antes de los eventos de la serie original, centrándose en la Danza de los Dragones, una guerra civil por la sucesión al Trono de Hierro.', 'Basada en partes de la novela "Fire & Blood" de George R.R. Martin, la serie explora el apogeo de la dinastía Targaryen en Poniente.', 'https://m.media-amazon.com/images/M/MV5BZjBiOGIyY2YtOTA3OC00YzY1LThkYjktMGRkYTNhNTExY2I2XkEyXkFqcGdeQXVyMTEyMjM2NDc2._V1_.jpg', 's016', 8.5);

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
('pr032', 'DRAMA'), ('pr032', 'WESTERN'),

-- Marvel
-- Phase 1
('pr100', 'ACCION'), ('pr100', 'CIENCIA_FICCION'), ('pr100', 'AVENTURA'),
('pr101', 'ACCION'), ('pr101', 'CIENCIA_FICCION'), ('pr101', 'AVENTURA'),
('pr102', 'ACCION'), ('pr102', 'CIENCIA_FICCION'), ('pr102', 'AVENTURA'),
('pr103', 'ACCION'), ('pr103', 'FANTASIA'), ('pr103', 'AVENTURA'),
('pr104', 'ACCION'), ('pr104', 'CIENCIA_FICCION'),
('pr105', 'ACCION'), ('pr105', 'CIENCIA_FICCION'), ('pr105', 'AVENTURA'),

-- Phase 2
('pr106', 'ACCION'), ('pr106', 'CIENCIA_FICCION'), ('pr106', 'AVENTURA'),
('pr107', 'ACCION'), ('pr107', 'FANTASIA'), ('pr107', 'AVENTURA'),
('pr108', 'ACCION'), ('pr108', 'AVENTURA'),
('pr109', 'ACCION'), ('pr109', 'CIENCIA_FICCION'), ('pr109', 'COMEDIA'),
('pr110', 'ACCION'), ('pr110', 'CIENCIA_FICCION'), ('pr110', 'AVENTURA'),
('pr111', 'ACCION'), ('pr111', 'CIENCIA_FICCION'), ('pr111', 'COMEDIA'),

-- Phase 3
('pr112', 'ACCION'), ('pr112', 'CIENCIA_FICCION'), ('pr112', 'AVENTURA'),
('pr113', 'ACCION'), ('pr113', 'FANTASIA'), ('pr113', 'AVENTURA'),
('pr114', 'ACCION'), ('pr114', 'CIENCIA_FICCION'), ('pr114', 'COMEDIA'),
('pr115', 'ACCION'), ('pr115', 'CIENCIA_FICCION'), ('pr115', 'COMEDIA'),
('pr116', 'ACCION'), ('pr116', 'CIENCIA_FICCION'), ('pr116', 'COMEDIA'),
('pr117', 'ACCION'), ('pr117', 'CIENCIA_FICCION'), ('pr117', 'AVENTURA'),
('pr118', 'ACCION'), ('pr118', 'CIENCIA_FICCION'), ('pr118', 'AVENTURA'),
('pr119', 'ACCION'), ('pr119', 'CIENCIA_FICCION'), ('pr119', 'COMEDIA'),
('pr120', 'ACCION'), ('pr120', 'CIENCIA_FICCION'), ('pr120', 'AVENTURA'),
('pr121', 'ACCION'), ('pr121', 'CIENCIA_FICCION'), ('pr121', 'AVENTURA'),
('pr122', 'ACCION'), ('pr122', 'CIENCIA_FICCION'), ('pr122', 'COMEDIA'),

-- Phase 4
('pr123', 'ACCION'), ('pr123', 'AVENTURA'),
('pr124', 'ACCION'), ('pr124', 'FANTASIA'),
('pr125', 'ACCION'), ('pr125', 'CIENCIA_FICCION'), ('pr125', 'FANTASIA'),
('pr126', 'ACCION'), ('pr126', 'CIENCIA_FICCION'), ('pr126', 'AVENTURA'),
('pr127', 'ACCION'), ('pr127', 'FANTASIA'), ('pr127', 'TERROR'),
('pr128', 'ACCION'), ('pr128', 'FANTASIA'), ('pr128', 'COMEDIA'),
('pr129', 'ACCION'), ('pr129', 'CIENCIA_FICCION'), ('pr129', 'DRAMA'),

-- Phase 5
('pr130', 'ACCION'), ('pr130', 'CIENCIA_FICCION'), ('pr130', 'COMEDIA'),
('pr131', 'ACCION'), ('pr131', 'CIENCIA_FICCION'), ('pr131', 'COMEDIA'),
('pr132', 'ACCION'), ('pr132', 'CIENCIA_FICCION'), ('pr132', 'AVENTURA'),

--la casa del dragon
('pr133', 'FANTASIA'),
('pr133', 'DRAMA'),
('pr133', 'AVENTURA'),
('pr134', 'FANTASIA'),
('pr134', 'DRAMA'),
('pr134', 'ACCION');

-- Insertar profesionales adicionales (actores y directores de tus películas)
INSERT INTO profesionales (id, nombre, fecha_nacimiento, lugar_nacimiento, biografia, foto, fecha_inicio) VALUES
('p001', 'Tom Hanks', '1956-07-09', 'Concord, California, USA', 'Dos veces ganador del Oscar, conocido por Forrest Gump y Toy Story.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRMbEBRIFN_A3mQS7etzyoVo7UGJ8Xr3xMS1KmMaujuH7qDKttQdWs74OoYRgIj3C7OR0YRzTbjsSLL0VMXXLy_ug', '1978-01-01'),
('p003', 'Christopher Nolan', '1970-07-30', 'London, England', 'Director aclamado por Inception, The Dark Knight y Dunkirk.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Christopher_Nolan_Cannes_2018.jpg/640px-Christopher_Nolan_Cannes_2018.jpg', '1998-01-01'),
('p005', 'Leonardo DiCaprio', '1974-11-11', 'Los Angeles, California, USA', 'Ganador del Oscar por The Revenant.', 'https://encrypted-tbn2.gstatic.com/licensed-image?q=tbn:ANd9GcSwLp-COysT7ukjE2mk6WglJk2dHvB5SI3JAjfUyUYFl7KNo6VuHu_hCeE6b9v0vgm-u6ZMm2PWqEv1S_g', '1989-01-01'),
('p006', 'Quentin Tarantino', '1963-03-27', 'Knoxville, Tennessee, USA', 'Director conocido por Pulp Fiction y Kill Bill.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQLFccL-uOxuyzMNc8uiorY9ok9Um2s5JKegklQvCrgggv1LmUOPvjeUJXiu9xTKKHoYd96H5NDa5CuEM28EjukbZDlc9NaCJtCyKdX9-o', '1987-01-01'),
('p011', 'Marlon Brando', '1924-04-03', 'Omaha, Nebraska, USA', 'Actor legendario conocido por El Padrino y Un tranvía llamado Deseo.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Marlon_Brando_publicity_for_One-Eyed_Jacks.png/960px-Marlon_Brando_publicity_for_One-Eyed_Jacks.png', '1944-01-01'),
('p012', 'Al Pacino', '1940-04-25', 'New York City, USA', 'Icono del cine ganador de un Oscar por Esencia de mujer.', 'https://preview.redd.it/twcbfnb82b3e1.jpeg?width=640&crop=smart&auto=webp&s=629f0e775f09188984f034c2f9b263814e6ba1d9', '1967-01-01'),
('p013', 'James Caan', '1940-03-26', 'New York City, USA', 'Actor conocido por El Padrino y Misery.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1940/3/w300/bGyOCCOIgcIyKjOGLoXoyp0XWHf.jpg', '1961-01-01'),
('p014', 'Francis Ford Coppola', '1939-04-07', 'Detroit, Michigan, USA', 'Aclamado director de El Padrino y Apocalypse Now.', 'https://upload.wikimedia.org/wikipedia/commons/4/41/Francis_Ford_Coppola.jpg', '1962-01-01'),
('p015', 'Christian Bale', '1974-01-30', 'Haverfordwest, Wales', 'Actor versátil conocido por El Caballero Oscuro y El luchador.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQOdncgz6XwkeU5omE8IOCU7emrzvTWKYR4O1CfZw5MnJwC8G0YZe6oJEWBVS0TOR2qB3MaO3CjvpePnX41umfcE1T6MbnjPO1yF3wRZBE', '1982-01-01'),
('p016', 'Heath Ledger', '1979-04-04', 'Perth, Australia', 'Actor ganador del Oscar por su papel del Joker.', 'https://m.media-amazon.com/images/M/MV5BMTI2NTY0NzA4MF5BMl5BanBnXkFtZTYwMjE1MDE0._V1_FMjpg_UX1000_.jpg', '1992-01-01'),
('p017', 'Aaron Eckhart', '1968-03-12', 'Cupertino, California, USA', 'Actor conocido por El Caballero Oscuro y Thank You for Smoking.', 'https://www.lavanguardia.com/peliculas-series/images/all/profile/1968/3/6383/w1280/6rI3cYhdxOaOFSRB4C29MalpwDC.jpg', '1992-01-01'),
('p018', 'John Travolta', '1954-02-18', 'Englewood, New Jersey, USA', 'Actor de Pulp Fiction y Fiebre del sábado noche.', 'https://es.web.img2.acsta.net/pictures/18/05/15/15/20/5209194.jpg', '1969-01-01'),
('p019', 'Uma Thurman', '1970-04-29', 'Boston, Massachusetts, USA', 'Actriz conocida por Pulp Fiction y Kill Bill.', 'https://upload.wikimedia.org/wikipedia/commons/4/42/Uma_Thurman_Cannes_2017_%28cropped%29.jpg', '1985-01-01'),
('p020', 'Samuel L. Jackson', '1948-12-21', 'Washington, D.C., USA', 'Prolífico actor conocido por Pulp Fiction y Avengers.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1948/12/w300/nCJJ3NVksYNxIzEHcyC1XziwPVj.jpg', '1972-01-01'),
('p021', 'Robin Wright', '1966-04-08', 'Dallas, Texas, USA', 'Actriz conocida por Forrest Gump y House of Cards.', 'https://upload.wikimedia.org/wikipedia/commons/d/d9/Robin_Wright_Cannes_2017_%28cropped%29.jpg', '1983-01-01'),
('p022', 'Gary Sinise', '1955-03-17', 'Blue Island, Illinois, USA', 'Actor conocido por Forrest Gump y CSI: NY.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Gary_Sinise_2011_%28cropped%29.jpg/250px-Gary_Sinise_2011_%28cropped%29.jpg', '1982-01-01'),
('p023', 'Robert Zemeckis', '1952-05-14', 'Chicago, Illinois, USA', 'Director de Forrest Gump y Regreso al futuro.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTCAjFSuumds-l6ijrvhig2yBfq40-2dgziuD-5khAR-7C-RvjRE4QOGsNJDbvC6h4No-GqyT_ftG8IVmgJbQHwJdb3YApwoOAm0JgX4As', '1978-01-01'),
('p024', 'Joseph Gordon-Levitt', '1981-02-17', 'Los Angeles, California, USA', 'Actor conocido por Inception y 500 Days of Summer.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRNSAiomuqhFFFjpcJxirnpAdJf5zsf72Zej8XqWXCVEQadJEjQ6ndnMCPfyz2PWUep-_YHgGQN6zH7oWZs5sKIe6KzG-DkEmAx5s52QQ', '1988-01-01'),
('p025', 'Ellen Page', '1987-02-21', 'Halifax, Canada', 'Actriz conocida por Inception y Juno.', 'https://cdn.britannica.com/41/249341-050-E5F7039C/Actor-Elliot-Page-2022.jpg', '1997-01-01'),
('p026', 'Keanu Reeves', '1964-09-02', 'Beirut, Lebanon', 'Actor de Matrix y John Wick.', 'https://encrypted-tbn1.gstatic.com/licensed-image?q=tbn:ANd9GcR0COkpwnQLcIZ9VNbnRwEYaNuBa1WGVimsvHLdaR1xC6ehdKdQSN8V1NvT_vHBL9ewkcjEupGFVKXM_hI', '1984-01-01'),
('p027', 'Laurence Fishburne', '1961-07-30', 'Augusta, Georgia, USA', 'Actor conocido por Matrix y CSI.', 'https://m.media-amazon.com/images/M/MV5BMTc0NjczNDc1MV5BMl5BanBnXkFtZTYwMDU0Mjg1._V1_FMjpg_UX1000_.jpg', '1972-01-01'),
('p028', 'Carrie-Anne Moss', '1967-08-21', 'Burnaby, Canada', 'Actriz conocida por Matrix y Jessica Jones.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1967/8/w1280/xD4jTA3KmVp5Rq3aHcymL9DUGjD.jpg', '1989-01-01'),
('p029', 'Lana Wachowski', '1965-06-21', 'Chicago, Illinois, USA', 'Directora de Matrix y Sense8.', 'https://es.web.img3.acsta.net/pictures/16/03/09/16/29/317444.jpg', '1995-01-01'),
('p030', 'Lilly Wachowski', '1967-12-29', 'Chicago, Illinois, USA', 'Directora de Matrix y Sense8.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwf6WavP7dycZro5Ra7BlTz5u1r76_KvziYA_rMsW4_AtxM8tkhgPEoM37PEHrLBHXbcY&usqp=CAU', '1995-01-01'),
('p031', 'Kate Winslet', '1975-10-05', 'Reading, England', 'Actriz ganadora del Oscar por The Reader.', 'https://es.web.img2.acsta.net/pictures/15/09/15/10/01/065591.jpg', '1991-01-01'),
('p032', 'Billy Zane', '1966-02-24', 'Chicago, Illinois, USA', 'Actor conocido por Titanic y The Phantom.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQaMTksb-H0cCaClX92y74SiPT5RjiF4rQj6SSkM-QkobLB_gRtZjtyVLaf0cUHT5IwP66q8AIKyDohDAPVSOKWpt3j-9p1-kTppV4-d-w', '1985-01-01'),
('p033', 'James Cameron', '1954-08-16', 'Kapuskasing, Canada', 'Director de Titanic y Avatar.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSgACGReEEHBDtdjNcDAnSjUb93eTbC-bLdiD2xYkq0j7gw5lqPj4-w3mwNqdoRTM9tmxlPbilOE-wyyaBtyqmaI0WdliPao3iMVODLzrU', '1978-01-01'),
('p034', 'Russell Crowe', '1964-04-07', 'Wellington, New Zealand', 'Actor ganador del Oscar por Gladiator.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkhqkS4g6Q2dOd4ysYiWHSkRjdN-0nmweAi4YRV9BOfj60taz2o8c02IpJbsWXamObtrbDu6G-uJxJ9u9v5p8A6RGx8kMUeuW7PFZM1pY', '1986-01-01'),
('p035', 'Joaquin Phoenix', '1974-10-28', 'San Juan, Puerto Rico', 'Actor ganador del Oscar por Joker.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/dc/Joaquin_Phoenix-64908_%28cropped%29.jpg/960px-Joaquin_Phoenix-64908_%28cropped%29.jpg', '1982-01-01'),
('p036', 'Connie Nielsen', '1965-07-03', 'Frederikshavn, Denmark', 'Actriz conocida por Gladiator y Wonder Woman.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSAV2_re78oggyB-cJcubmDpxiXBahReEedwRmcDyQ2FTPd2aNQZ_yRGSoMQgGEb4CgvfB1AJ07DNkQ8pwSBHvnmw', '1984-01-01'),
('p037', 'Ridley Scott', '1937-11-30', 'South Shields, England', 'Director de Gladiator y Alien.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSuG95F4a_pTAFPT2Gv9Q6FDNaCN6Lind0EdyQptYczLG_IimGhN0ZyhTE32SiHEPn5idMrpFOMbWqXmPikEl6smcjGMaoxRcF61aWPkw', '1965-01-01'),
('p038', 'Tim Robbins', '1958-10-16', 'West Covina, California, USA', 'Actor ganador del Oscar por Mystic River.', 'https://pics.filmaffinity.com/061091445454557-nm_200.jpg', '1981-01-01'),
('p039', 'Morgan Freeman', '1937-06-01', 'Memphis, Tennessee, USA', 'Actor legendario conocido por The Shawshank Redemption.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQOm06uoUBPWKgKeW8r7abDSzYgJe7jOFgC4S99qqNBdloMoEq0CoIr0y25_B85m-89vL_qHy-JvusFqa2FlZt3zpo1CxnjbbOYmoa24OA', '1964-01-01'),
('p040', 'Bob Gunton', '1945-11-15', 'Santa Monica, California, USA', 'Actor conocido por The Shawshank Redemption.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1945/11/w1280/rr2KDCKK4t0f5YhZibCpLCAsJxc.jpg', '1981-01-01'),
('p041', 'Frank Darabont', '1959-01-28', 'Montbéliard, France', 'Director de The Shawshank Redemption.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcS5CoFUGXXmLNf3gqX7Emc4wG7e36Qa2C7PH2fIfWYvDF0JNBzOwXoMiGaz68_ShQMUDMxQhoJgBsuYWaXjSCQAag', '1981-01-01'),
('p042', 'Robert De Niro', '1943-08-17', 'New York City, USA', 'Actor legendario ganador de dos Oscars.', 'https://upload.wikimedia.org/wikipedia/commons/5/58/Robert_De_Niro_Cannes_2016.jpg', '1963-01-01'),
('p043', 'Robert Duvall', '1931-01-05', 'San Diego, California, USA', 'Actor ganador del Oscar por Tender Mercies.', 'https://m.media-amazon.com/images/M/MV5BMjk1MjA2Mjc2MF5BMl5BanBnXkFtZTcwOTE4MTUwMg@@._V1_FMjpg_UX1000_.jpg', '1952-01-01'),
('p044', 'Elijah Wood', '1981-01-28', 'Cedar Rapids, Iowa, USA', 'Actor conocido por El Señor de los Anillos.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZmP7arb0NFBcfM9CzwYZAO9OFPGabibMWvTxfX_MshIOTruxjlhFe-xccfLsNqFxZ5n_zU5uLnC9uLicS9fg7qJv03OigKeKzVLfHB7g', '1989-01-01'),
('p045', 'Viggo Mortensen', '1958-10-20', 'New York City, USA', 'Actor conocido por El Señor de los Anillos.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcR0Or-ciiIGW12vQqvEqY3lMNXs3jDUnFHuknUQtgNaHP3kiOeASOFEXZ0tlmHDXW8_Qyn8Odi6quQBrMJ7-lVLDEz6KS3t-cznA1G6kFY', '1984-01-01'),
('p046', 'Ian McKellen', '1939-05-25', 'Burnley, England', 'Actor conocido por El Señor de los Anillos y X-Men.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1939/5/w1280/5cnnnpnJG6TiYUSS7qgJheUZgnv.jpg', '1961-01-01'),
('p047', 'Peter Jackson', '1961-10-31', 'Pukerua Bay, New Zealand', 'Director de El Señor de los Anillos.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1961/10/w1280/bNc908d59Ba8VDNr4eCcm4G1cR.jpg', '1987-01-01'),
('p048', 'Matthew McConaughey', '1969-11-04', 'Uvalde, Texas, USA', 'Actor ganador del Oscar por Dallas Buyers Club.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSBwIVNbnqI0gOoarz5z06bRqBtZz-5-6AVpHfAq7CzVCyQyp7TxVbRh93sNt2Q1edPCM4UBx5KqoBRNQCctMCVqXbyfhPJ0IJZT03ODQ', '1991-01-01'),
('p049', 'Anne Hathaway', '1982-11-12', 'Brooklyn, New York, USA', 'Actriz ganadora del Oscar por Los Miserables.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Anne_Hathaway-68408_%28cropped%29.jpg/1200px-Anne_Hathaway-68408_%28cropped%29.jpg', '1999-01-01'),
('p050', 'Jessica Chastain', '1977-03-24', 'Sacramento, California, USA', 'Actriz nominada al Oscar por Zero Dark Thirty.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTd2KZyvJEYTBZ3RpbzaKlWZ5HuUUFIZcOtvoIaG7wsrKiGjWcEfO8ghm9UVaYQR9eifIaOZVl6TtQQ9sAscuQzzesavJKDD4K6oCX391M', '2004-01-01'),
('p051', 'Miles Teller', '1987-02-20', 'Downingtown, Pennsylvania, USA', 'Actor conocido por Whiplash y Top Gun: Maverick.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQn66z5CeEv4T1uUfqn7y3FNWgOvx60oSwMLEAxTYxFMK_x96xJ3idxCCGPocMazO8SfCm3wZV1oCQtGbLWhSOWFc4DSo3CtKDKi3tJ7HU', '2004-01-01'),
('p052', 'J.K. Simmons', '1955-01-09', 'Detroit, Michigan, USA', 'Actor ganador del Oscar por Whiplash.', 'https://encrypted-tbn2.gstatic.com/licensed-image?q=tbn:ANd9GcRZu-8-Q_milyQS2gNSCfeyjGbHixCEsYKL5OinguX4dbw4SYAHVELbza1RVQ2AiNs51j-iFcPmK-OBiEw', '1986-01-01'),
('p053', 'Paul Reiser', '1957-03-30', 'New York City, USA', 'Actor conocido por Whiplash y Aliens.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTfCDLCS7AnIv2zwkLYlRWTJY7UFr6zjGm4ynstaNp0wb3cCRh4cmjgzS8Z_kSI9HYWJHflKD7dfA5j4iwldOrIlw', '1982-01-01'),
('p054', 'Damien Chazelle', '1985-01-19', 'Providence, Rhode Island, USA', 'Director de Whiplash y La La Land.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRq6nmuoxlUN3nWglmWFqlXklO4EYSQygBdR70y_ftYsi3dWmxKOJa57BBVVdQi9FFAsWllXJ-n0AatgWRZ07a_MZJnYLw2v8nhcjOrbu4', '2009-01-01'),
('p055', 'Ryan Gosling', '1980-11-12', 'London, Ontario, Canada', 'Actor conocido por La La Land y Drive.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQL8QrJGO7NfdJCBwsONox-rI1E3hKvrchgcSVCQQSWbm7VjV6A9lAo_qwvnJmn4CLFd7-T3qGjDP9Tor81xBItUQ', '1993-01-01'),
('p056', 'Emma Stone', '1988-11-06', 'Scottsdale, Arizona, USA', 'Actriz ganadora del Oscar por La La Land.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQmPUAcjEoVIrrIVMI3U3k0IHDcYC8Ul3yx3q9tzU4Rm29dtyMlZ9IqsFJ5dbDS7n9fTBtUW2pKszb4rh-1Ge1WfsKvRVGUeMFqip5moQ', '2004-01-01'),
('p057', 'John Legend', '1978-12-28', 'Springfield, Ohio, USA', 'Cantante y actor conocido por La La Land.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fc/John_Legend_2019_by_Glenn_Francis_%28cropped%29.jpg/1200px-John_Legend_2019_by_Glenn_Francis_%28cropped%29.jpg', '1998-01-01'),
('p058', 'Jamie Foxx', '1967-12-13', 'Terrell, Texas, USA', 'Actor ganador del Oscar por Ray.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTKWAFtOdXxO8e8gwLuV3W4x5UAvgmuCxMBkGSndvEPr0SNXLfFhjw7hQ0A3MwY4Z2o-u9LXVlTtG6URXdEq5yYmDFU-tVT_P3fqKwiDR8', '1989-01-01'),
('p059', 'Christoph Waltz', '1956-10-04', 'Vienna, Austria', 'Actor ganador de dos Oscars por Django y Bastardos sin gloria.', 'https://www.lavanguardia.com/peliculas-series/images/all/profile/1956/10/27319/w1280/2Hhztd4mUEV9Y25rfkXDwzL9QI9.jpg', '1977-01-01'),
('p060', 'Harrison Ford', '1942-07-13', 'Chicago, Illinois, USA', 'Conocido por Indiana Jones y Star Wars', 'https://m.media-amazon.com/images/M/MV5BMTY4Mjg0NjIxOV5BMl5BanBnXkFtZTcwMTM2NTI3MQ@@._V1_.jpg', '1966-01-01'),
('p061', 'Steven Spielberg', '1946-12-18', 'Cincinnati, Ohio, USA', 'Director de ET y Jurassic Park', 'https://m.media-amazon.com/images/M/MV5BMTY1NjAzNzE1MV5BMl5BanBnXkFtZTYwNTk0ODc0._V1_.jpg', '1968-01-01'),
('p062', 'Laura Dern', '1967-02-10', 'Los Angeles, California, USA', 'Actriz ganadora del Oscar', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRA6woSlHuu8oZTg2pgrutLs6LPmuBWr7gMgsBEGkM2Wj-zCQMuQ_1-982INF_7YQ9ALxvIEbQYgCClxdbe-l9Bng', '1985-01-01'),
('p063', 'Tom Cruise', '1962-07-03', 'Syracuse, New York, USA', 'Actor de Misión Imposible', 'https://m.media-amazon.com/images/M/MV5BMTk1MjM3NTU5M15BMl5BanBnXkFtZTcwMTMyMjAyMg@@._V1_.jpg', '1981-01-01'),
('p065', 'Jodie Foster', '1962-11-19', 'Los Angeles, California, USA', 'Actriz ganadora del Oscar', 'https://hips.hearstapps.com/hmg-prod/images/gettyimages-2201633622-67e694c422665.jpg?crop=0.667xw:1.00xh;0.207xw,0&resize=640:*', '1968-01-01'),
('p066', 'Anthony Hopkins', '1937-12-31', 'Port Talbot, Wales', 'Actor ganador del Oscar', 'https://m.media-amazon.com/images/M/MV5BMTg5ODk1NTc5Ml5BMl5BanBnXkFtZTYwMjAwOTI4._V1_.jpg', '1960-01-01'),
('p067', 'David Fincher', '1962-08-28', 'Denver, Colorado, USA', 'Director de Seven y Zodiac', 'https://pics.filmaffinity.com/078315252655922-nm_200.jpg', '1984-01-01'),
('p200', 'Robert Downey Jr.', '1965-04-04', 'New York City, USA', 'Actor conocido por interpretar a Tony Stark/Iron Man en el MCU.', 'https://cdn.britannica.com/99/254199-050-98CF4E04/Robert-Downey-JR-UK-premier-Oppenheimer-movie-July-2023.jpg', '1970-01-01'),
('p201', 'Chris Evans', '1981-06-13', 'Boston, Massachusetts, USA', 'Actor conocido por interpretar a Steve Rogers/Capitán América en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMTU2NTg1OTQzMF5BMl5BanBnXkFtZTcwNjIyMjkyMg@@._V1_.jpg', '2000-01-01'),
('p202', 'Chris Hemsworth', '1983-08-11', 'Melbourne, Australia', 'Actor conocido por interpretar a Thor en el MCU.', 'https://m.media-amazon.com/images/M/MV5BOTU2MTI0NTIyNV5BMl5BanBnXkFtZTcwMTA4Nzc3OA@@._V1_.jpg', '2002-01-01'),
('p203', 'Mark Ruffalo', '1967-11-22', 'Kenosha, Wisconsin, USA', 'Actor conocido por interpretar a Bruce Banner/Hulk en el MCU.', 'https://m.media-amazon.com/images/M/MV5BM2JiYzA0ZGItNmFhYy00MjIyLWEwN2QtMzRmNDUyNjNiZjBiXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg', '1989-01-01'),
('p204', 'Scarlett Johansson', '1984-11-22', 'New York City, USA', 'Actriz conocida por interpretar a Natasha Romanoff/Black Widow en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMTM3OTUwMDYwNl5BMl5BanBnXkFtZTcwNTUyNzc3Nw@@._V1_.jpg', '1994-01-01'),
('p205', 'Jeremy Renner', '1971-01-07', 'Modesto, California, USA', 'Actor conocido por interpretar a Clint Barton/Hawkeye en el MCU.', 'https://m.media-amazon.com/images/M/MV5BOTk1MzgzOTg5OV5BMl5BanBnXkFtZTcwNDQ4NjMxOA@@._V1_.jpg', '1995-01-01'),
('p206', 'Tom Hiddleston', '1981-02-09', 'Westminster, London, UK', 'Actor conocido por interpretar a Loki en el MCU.', 'https://encrypted-tbn3.gstatic.com/licensed-image?q=tbn:ANd9GcTBp-Y_EOn5dTG11lSKJmxhkfNc7pjaZlblVUQEts7a2KCuQcHQfX0Y7feJDJa0pAIFaX--TML-U3OtN84', '2001-01-01'),
('p207', 'Paul Rudd', '1969-04-06', 'Passaic, New Jersey, USA', 'Actor conocido por interpretar a Scott Lang/Ant-Man en el MCU.', 'https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcS2OKC3DYoyzfOoj-ISOawxTht66fCgZsq8_05--YTxQ9SrHX2YEMtGPhsLpqhFifXlDaO_DTRFcptmV4k', '1992-01-01'),
('p208', 'Benedict Cumberbatch', '1976-07-19', 'Hammersmith, London, UK', 'Actor conocido por interpretar a Doctor Strange en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjE0MDkzMDQwOF5BMl5BanBnXkFtZTgwOTE1Mjg1MzE@._V1_.jpg', '2000-01-01'),
('p209', 'Chadwick Boseman', '1976-11-29', 'Anderson, South Carolina, USA', 'Actor conocido por interpretar a TChalla/Black Panther en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMTk2OTY5MzcwMV5BMl5BanBnXkFtZTgwODM4MDI5MjI@._V1_.jpg', '2003-01-01'),
('p210', 'Brie Larson', '1989-10-01', 'Sacramento, California, USA', 'Actriz conocida por interpretar a Carol Danvers/Captain Marvel en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjExODkxODU3NF5BMl5BanBnXkFtZTgwNTM0MTk3NjE@._V1_.jpg', '1998-01-01'),
('p211', 'Tom Holland', '1996-06-01', 'Kingston upon Thames, UK', 'Actor conocido por interpretar a Peter Parker/Spider-Man en el MCU.', 'https://m.media-amazon.com/images/M/MV5BNTAzMzA3NjQwOF5BMl5BanBnXkFtZTgwMDUzODQ5MTI@._V1_.jpg', '2012-01-01'),
('p212', 'Zoe Saldana', '1978-06-19', 'Passaic, New Jersey, USA', 'Actriz conocida por interpretar a Gamora en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHHUq_6IJEbOpFGI4zcLexY61rhsdRCksn6ly0sJDaqdYa9lZZc_9k6ra351afc7BqVxc&usqp=CAU', '1999-01-01'),
('p213', 'Karen Gillan', '1987-11-28', 'Inverness, Scotland, UK', 'Actriz conocida por interpretar a Nebula en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtwekoiYFvQMd0g6RABoLqI9UaJGuHhvmehw&s', '2006-01-01'),
('p214', 'Dave Bautista', '1969-01-18', 'Washington, D.C., USA', 'Actor conocido por interpretar a Drax en el MCU.', 'https://tr.web.img4.acsta.net/c_310_420/pictures/18/09/04/19/30/2609205.jpg', '2006-01-01'),
('p215', 'Pom Klementieff', '1986-05-03', 'Quebec City, Canada', 'Actriz conocida por interpretar a Mantis en el MCU.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Stuttgart_-Comic_Con_Germany_2019-_7d_by-RaBoe_037_%28cropped%29.jpg/1200px-Stuttgart_-Comic_Con_Germany_2019-_7d_by-RaBoe_037_%28cropped%29.jpg', '2007-01-01'),
('p216', 'Vin Diesel', '1967-07-18', 'Alameda County, California, USA', 'Actor conocido por interpretar a Groot en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjExNzA4MDYxN15BMl5BanBnXkFtZTcwOTI1MDAxOQ@@._V1_.jpg', '1990-01-01'),
('p217', 'Bradley Cooper', '1975-01-05', 'Philadelphia, Pennsylvania, USA', 'Actor conocido por interpretar a Rocket Raccoon en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTrYt1ziGXTEHZ9Cy20A1YNiDHWv999T_w-3bTBDkzGj09craKzKh_DroPRSUd8a1E9fwI&usqp=CAU', '1999-01-01'),
('p218', 'Josh Brolin', '1968-02-12', 'Santa Monica, California, USA', 'Actor conocido por interpretar a Thanos en el MCU.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1968/2/w1280/2WGHZaU5FUUKOgRNp23fgOfKSzU.jpg', '1985-01-01'),
('p219', 'Elizabeth Olsen', '1989-02-16', 'Sherman Oaks, California, USA', 'Actriz conocida por interpretar a Wanda Maximoff/Bruja Escarlata en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjEzMjA0ODk1OF5BMl5BanBnXkFtZTcwMTA4ODM3OQ@@._V1_.jpg', '2011-01-01'),
('p220', 'Paul Bettany', '1971-05-27', 'London, UK', 'Actor conocido por interpretar a Visión en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXwZrEMBsoMwLZ8BIuh-jolCzLV7eCgW28EQ&s', '1994-01-01'),
('p221', 'Anthony Mackie', '1978-09-23', 'New Orleans, Louisiana, USA', 'Actor conocido por interpretar a Sam Wilson/Falcon/Capitán América en el MCU.', 'https://pics.filmaffinity.com/246380527061114-nm_200.jpg', '2002-01-01'),
('p222', 'Sebastian Stan', '1982-08-13', 'Constanta, Romania', 'Actor conocido por interpretar a Bucky Barnes/Soldado del Invierno en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdDyCPgUcdnWtkIhIE-ykLKRkP8dcgQuxYPA&s', '2003-01-01'),
('p223', 'Don Cheadle', '1964-11-29', 'Kansas City, Missouri, USA', 'Actor conocido por interpretar a James Rhodes/Máquina de Guerra en el MCU.', 'https://cdn.britannica.com/95/216995-050-5E4D147A/American-actor-director-Don-Cheadle-2016.jpg', '1985-01-01'),
('p224', 'Samuel L. Jackson', '1948-12-21', 'Washington, D.C., USA', 'Actor conocido por interpretar a Nick Fury en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMTQ1NTQwMTYxNl5BMl5BanBnXkFtZTYwMjA1MzY1._V1_.jpg', '1972-01-01'),
('p225', 'Jon Favreau', '1966-10-19', 'Queens, New York, USA', 'Actor y director conocido por interpretar a Happy Hogan y dirigir Iron Man 1 y 2.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCU1450ui8eGLzjppKH3kVyZEIX0KnJcDqdw&s', '1992-01-01'),
('p226', 'Gwyneth Paltrow', '1972-09-27', 'Los Angeles, California, USA', 'Actriz conocida por interpretar a Pepper Potts en el MCU.', 'https://m.media-amazon.com/images/M/MV5BNzIxOTQ1NTU1OV5BMl5BanBnXkFtZTcwMTQ4MDY0Nw@@._V1_FMjpg_UX1000_.jpg', '1991-01-01'),
('p227', 'Tessa Thompson', '1983-10-03', 'Los Angeles, California, USA', 'Actriz conocida por interpretar a Valkyrie en el MCU.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTyJpl3hgn7SuPg8ia6gJQ3GqtTBeeQjHPayBgBUtgUn8ilXXWp6rn1g5Nomc5oByYz6rujc27YIQ4GsiHqaXvZ7A', '2002-01-01'),
('p228', 'Natalie Portman', '1981-06-09', 'Jerusalem, Israel', 'Actriz conocida por interpretar a Jane Foster/Mighty Thor en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMTQ3ODE3Mjg1NV5BMl5BanBnXkFtZTcwNzA4ODcxNA@@._V1_.jpg', '1994-01-01'),
('p313', 'Taika Waititi', '1975-08-16', 'Wellington, New Zealand', 'Actor y director conocido por interpretar a Korg y dirigir Thor: Ragnarok y Love and Thunder.', 'https://m.media-amazon.com/images/M/MV5BNzU2NDcxODMyOF5BMl5BanBnXkFtZTgwNDkwOTQ1NjE@._V1_.jpg', '1999-01-01'),
('p230', 'Letitia Wright', '1993-10-31', 'Georgetown, Guyana', 'Actriz conocida por interpretar a Shuri en el MCU.', 'https://encrypted-tbn3.gstatic.com/licensed-image?q=tbn:ANd9GcQRKCnHqFnXuqdh-c2-2pjeuCNak4FyEuXi88Vn7ZEDYwco_w_i6F-ZCw1Sis5JF9YQfDGrqAe_uUou0yI', '2011-01-01'),
('p231', 'Winston Duke', '1986-11-15', 'Argyle, Trinidad and Tobago', 'Actor conocido por interpretar a MBaku en el MCU.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTHFTMtDFuDWd2F0ZP53Z6r_lJpBpq7ku-YmUTHtnbfGQn3o9OhWS1m1Im6VWPs74LBZ_3xJ_fsxNjkaNlUbH4lLw', '2014-01-01'),
('p232', 'Danai Gurira', '1978-02-14', 'Grinnell, Iowa, USA', 'Actriz conocida por interpretar a Okoye en el MCU.', 'https://m.media-amazon.com/images/M/MV5BNjYyNjg1OTU1M15BMl5BanBnXkFtZTgwNzYyNTkzMDI@._V1_FMjpg_UX1000_.jpg', '2004-01-01'),
('p233', 'Angela Bassett', '1958-08-16', 'New York City, USA', 'Actriz conocida por interpretar a Ramonda en el MCU.', 'https://static.wikia.nocookie.net/doblaje/images/e/ef/Angela-bassett-2016.jpg/revision/latest?cb=20161122163426&path-prefix=es', '1985-01-01'),
('p234', 'Florence Kasumba', '1976-10-26', 'Kampala, Uganda', 'Actriz conocida por interpretar a Ayo en el MCU.', 'https://www.shutterstock.com/editorial/image-editorial/M6T8M837M0jbUe23MjUyMjU=/florence-kasumba-440nw-13606191ns.jpg', '1997-01-01'),
('p235', 'Michael B. Jordan', '1987-02-09', 'Santa Ana, California, USA', 'Actor conocido por interpretar a Erik Killmonger en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjExOTY3NzExM15BMl5BanBnXkFtZTgwOTg1OTAzMTE@._V1_.jpg', '1999-01-01'),
('p236', 'Lupita Nyong''o', '1983-03-01', 'Mexico City, Mexico', 'Actriz conocida por interpretar a Nakia en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtygz8Eq74TpAsqcDR5jlIpRgd_zEBXH_AhA&s', '2008-01-01'),
('p237', 'Simu Liu', '1989-04-19', 'Harbin, China', 'Actor conocido por interpretar a Shang-Chi en el MCU.', 'https://cdn.britannica.com/06/242206-050-E18E3ABD/Simu-Liu-actor-2022.jpg', '2012-01-01'),
('p238', 'Awkwafina', '1988-06-02', 'New York City, USA', 'Actriz conocida por interpretar a Katy en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDLHElIG9UJ7BHCTcxNIvO_GYRWQpi92uD7RLwWW619amweXRdG5ol1xoHK9iF56Mt2Ts&usqp=CAU', '2016-01-01'),
('p239', 'Tony Leung', '1962-06-27', 'Hong Kong', 'Actor conocido por interpretar a Wenwu/Mandarin en el MCU.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1962/6/w1280/nQbSQAws5BdakPEB5MtiqWVeaMV.jpg', '1982-01-01'),
('p240', 'Meng''er Zhang', '1962-06-27', 'Nanjing, China', 'Actriz conocida por interpretar a Xialing en el MCU.', 'https://ntvb.tmsimg.com/assets/assets/1634848_v9_aa.jpg', '2021-01-01'),
('p241', 'Gemma Chan', '1982-11-29', 'London, UK', 'Actriz conocida por interpretar a Sersi en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjE0MDkzMDQwOF5BMl5BanBnXkFtZTgwOTE1Mjg1MzE@._V1_.jpg', '2006-01-01'),
('p242', 'Richard Madden', '1986-06-18', 'Elderslie, Scotland, UK', 'Actor conocido por interpretar a Ikaris en el MCU.', 'https://harpersbazaaruk.cdnds.net/15/37/2048x2048/2048x2048-richardmaddensq-jpg-22951deb.jpg', '1999-01-01'),
('p243', 'Kumail Nanjiani', '1978-02-21', 'Karachi, Pakistan', 'Actor conocido por interpretar a Kingo en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZ8IMpXisNr-iHkX8f5xHLT1IgHr2x9ZEFbg&s', '2009-01-01'),
('p244', 'Lauren Ridloff', '1978-04-06', 'Chicago, Illinois, USA', 'Actriz conocida por interpretar a Makkari en el MCU.', 'https://upload.wikimedia.org/wikipedia/commons/1/1e/Lauren_Ridloff_by_Gage_Skidmore.jpg', '2018-01-01'),
('p245', 'Barry Keoghan', '1992-10-18', 'Dublin, Ireland', 'Actor conocido por interpretar a Druig en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwADkGugAr0GTaoXFn5ULpOTOapkYK0TsoXQ&shttps://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwADkGugAr0GTaoXFn5ULpOTOapkYK0TsoXQ&s', '2013-01-01'),
('p246', 'Brian Tyree Henry', '1982-03-31', 'Fayetteville, North Carolina, USA', 'Actor conocido por interpretar a Phastos en el MCU.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMUqS4r_zvo_u4ktn-WVnBDHZ3FVWHZiqUsA&s', '2007-01-01'),
('p247', 'Salma Hayek', '1966-09-02', 'Coatzacoalcos, Mexico', 'Actriz conocida por interpretar a Ajak en el MCU.', 'https://media.gettyimages.com/id/1498308778/es/foto/new-york-new-york-salma-hayek-attends-i-like-to-watch-live-with-trixie-mattel-katya-presenting.jpg?s=612x612&w=gi&k=20&c=ZIBbqlxVMv1t6Tu4qmvdxOKqq7hfZVFNyFlmiS5cR2k=', '1988-01-01'),
('p249', 'Mahershala Ali', '1974-02-16', 'Oakland, California, USA', 'Actor conocido por interpretar a Blade en el MCU.', 'https://ntvb.tmsimg.com/assets/assets/232748_v9_bc.jpg?w=360&h=480', '2001-01-01'),
('p250', 'Hailee Steinfeld', '1996-12-11', 'Los Angeles, California, USA', 'Actriz conocida por interpretar a Kate Bishop en el MCU.', 'https://m.media-amazon.com/images/M/MV5BMjI4NjM1NDkyN15BMl5BanBnXkFtZTgwODgyNTY1MjE@._V1_.jpg', '2007-01-01'),
('p251', 'Iman Vellani', '2002-08-12', 'Karachi, Pakistan', 'Actriz conocida por interpretar a Kamala Khan/Ms. Marvel en el MCU.', 'https://www.tebeosfera.com/T3content/img/T3_autores/n/i/iman_vellani.jpg', '2022-01-01'),
('p252', 'Jonathan Majors', '1989-09-07', 'Lompoc, California, USA', 'Actor conocido por interpretar a Kang el Conquistador en el MCU.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRC0kYTekh9-iZ8V1STc-AJ_M-rR_FFBq_N2pmNFMUWaz_MOZFKemo3fM3SgxfAPW2ezRhPW7f4GdI6hhsFmqKDXw', '2017-01-01'),
('p253', 'Kathryn Newton', '1997-02-08', 'Orlando, Florida, USA', 'Actriz conocida por interpretar a Cassie Lang en el MCU.', 'https://media.themoviedb.org/t/p/w500/exZuDe36AV1FSfEWxLISHXRPwJy.jpg', '2002-01-01'),
('p254', 'Michelle Pfeiffer', '1958-04-29', 'Santa Ana, California, USA', 'Actriz conocida por interpretar a Janet van Dyne en el MCU.', 'https://images.martincid.com/wp-content/uploads/2023/04/Depositphotos_16086453_XL.jpg?strip=all&lossy=1&ssl=1', '1978-01-01'),
('p255', 'Evangeline Lilly', '1979-08-03', 'Fort Saskatchewan, Canada', 'Actriz conocida por interpretar a Hope van Dyne/Avispa en el MCU.', 'https://www.hola.com/horizon/square/a8ffec3e2655-gettyimages-1839933338-t.jpg', '2002-01-01'),

-- Actores de Juego de Tronos
('p400', 'Emilia Clarke', '1986-10-23', 'London, England', 'Actriz británica conocida por su papel como Daenerys Targaryen en Juego de Tronos.', 'https://images.mubicdn.net/images/cast_member/415423/cache-115582-1438816557/image-w856.jpg', '2009-01-01'),
('p401', 'Kit Harington', '1986-12-26', 'London, England', 'Actor británico conocido por interpretar a Jon Snow en Juego de Tronos.', 'https://m.media-amazon.com/images/M/MV5BMTU1MDM5NjczOF5BMl5BanBnXkFtZTcwOTY2MDE4OA@@._V1_.jpg', '2008-01-01'),
('p402', 'Peter Dinklage', '1969-06-11', 'Morristown, New Jersey, USA', 'Actor estadounidense ganador de múltiples premios por su papel de Tyrion Lannister.', 'https://m.media-amazon.com/images/M/MV5BMTM1MTI5Mzc0MF5BMl5BanBnXkFtZTYwNzgzOTQz._V1_.jpg', '1995-01-01'),
('p403', 'Lena Headey', '1973-10-03', 'Bermuda', 'Actriz británica conocida por su interpretación de Cersei Lannister.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShMII-vbmuZpGwbNzpCC7z7w5OzjLB0gW1XDRKYbShYkBBckzTFx_UJ7VAbXOd7PWqj40&usqp=CAU', '1992-01-01'),
('p404', 'Sophie Turner', '1996-02-21', 'Northampton, England', 'Actriz británica que interpretó a Sansa Stark.', 'https://static.wikia.nocookie.net/xmen9260/images/d/d5/GettyImages-529786386.jpg/revision/latest?cb=20180607231944&path-prefix=es', '2011-01-01'),
('p405', 'Maisie Williams', '1997-04-15', 'Bristol, England', 'Actriz británica conocida por su papel de Arya Stark.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSjumnosVXw-sd-3WXuKCyMAWl0y6uTGw6O_PPaezZ3CyClIUEToivfVKsxC2YlA3LbKLi-nGX7Q-C3NAwTT5uBxg', '2011-01-01'),
('p406', 'Nikolaj Coster-Waldau', '1970-07-27', 'Rudkøbing, Denmark', 'Actor danés que interpretó a Jaime Lannister.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSt8G15u_Lab4MBZ7PO7kgYKfyVh7gwOE0OHuVwAuWpbQ_DWBHEIkwFDb44WcGHFZv14r3DhEizspxha88j1nxciA', '1993-01-01'),
('p407', 'Sean Bean', '1959-04-17', 'Sheffield, England', 'Actor inglés conocido por su papel de Ned Stark.', 'https://www.lavanguardia.com/peliculas-series/images/all/profile/1959/4/48/w1280/mKJgJdC0p5gEnJvPzTff1hpMVKm.jpg', '1984-01-01'),
('p408', 'David Benioff', '1970-09-25', 'New York City, USA', 'Creador y showrunner de Juego de Tronos.', 'https://m.media-amazon.com/images/M/MV5BMTAzNjQzMTEzMzJeQTJeQWpwZ15BbWU3MDkxNjA4NDc@._V1_.jpg', '2002-01-01'),
('p409', 'D.B. Weiss', '1971-04-23', 'Chicago, Illinois, USA', 'Creador y showrunner de Juego de Tronos.', 'https://media.themoviedb.org/t/p/w500/2RMejaT793U9KRk2IEbFfteQntE.jpg', '2002-01-01'),

-- Actores de La Casa del Dragón
('p410', 'Paddy Considine', '1973-09-05', 'Burton upon Trent, England', 'Actor británico que interpreta a Viserys Targaryen.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRwBP3WtophlwvYfopl_Q3yOySTogssqPYXegBY4zBOSLRgZyuovSvN7_upjDEO1GAEwZcbErUme-KOdF3B2V9KWAK6YBJbqTGb-wFa1A', '1999-01-01'),
('p411', 'Matt Smith', '1982-10-28', 'Northampton, England', 'Actor británico conocido por su papel del Doctor en Doctor Who, interpreta a Daemon Targaryen.', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/SDCC_2015_-_Matt_Smith.jpg/1200px-SDCC_2015_-_Matt_Smith.jpg', '2003-01-01'),
('p412', 'Emma D''Arcy', '1992-06-27', 'London, England', 'Actriz británica que interpreta a Rhaenyra Targaryen.', 'https://preview.redd.it/you-were-lucky-enough-to-meet-emma-darcy-in-person-and-got-v0-6uw0kh9xo8dd1.jpeg?width=640&crop=smart&auto=webp&s=0f3b19812fc84f06b94c5fa1b0579c9d9559a750', '2015-01-01'),
('p413', 'Olivia Cooke', '1993-12-27', 'Oldham, England', 'Actriz británica que interpreta a Alicent Hightower.', 'https://m.media-amazon.com/images/M/MV5BMTU1MDM5NjczOF5BMl5BanBnXkFtZTcwOTY2MDE4OA@@._V1_.jpg', '2012-01-01'),
('p414', 'Milly Alcock', '2000-04-11', 'Sydney, Australia', 'Actriz australiana que interpreta a la joven Rhaenyra Targaryen.', 'https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTLJrh6lnwfACQ-QJxQ65KwxnDX2z6-q_jo1xhs6GfPT6Mm3JfrSa5uN_XsfXvdqcxaJ1zAJNG24HpRfhpZAU5ivA', '2014-01-01'),
('p415', 'Emily Carey', '2003-04-30', 'London, England', 'Actriz británica que interpreta a la joven Alicent Hightower.', 'https://static.wikia.nocookie.net/doblaje/images/e/e5/Emily_Carey.jpg/revision/latest?cb=20240627054317&path-prefix=es', '2014-01-01'),
('p416', 'Ryan Condal', '1979-11-25', 'Hasbrouck Heights', 'Creador y showrunner de La Casa del Dragón.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRtaZZ2kTRe_VouqM7P2rM9_Vox3va9_Ut7Bfb_jkzXmeQ-hp7D9McbeCYQJ0I4PsRmGW9RLtvta7Spr9RmyqOqsYQ-MXY1W7WvIWyXTNc', '2010-01-01'),
('p417', 'Miguel Sapochnik', '1974-07-01', 'London, England', 'Director y showrunner de La Casa del Dragón.', 'https://i0.wp.com/cdn.visavis.com.ar/wp-content/uploads/2022/09/28235003/Miguel-Sapochnik.jpg?fit=520%2C346&ssl=1', '2000-01-01'),

-- Directors
('p301', 'Louis Leterrier', '1973-06-17', 'Paris, France', 'Director de The Incredible Hulk.', 'https://www.lavanguardia.com/peliculas-series/images/profile/1973/6/w1280/pJl1hNtZOIxrdzQjwGlicWWGSXg.jpg', '2000-01-01'),
('p302', 'Kenneth Branagh', '1960-12-10', 'Belfast, Northern Ireland', 'Director de Thor.', 'https://www.lavanguardia.com/peliculas-series/images/all/profile/1960/12/11181/w1280/AbCqqFxNi5w3nDUFdQt0DGMFh5H.jpg', '1982-01-01'),
('p303', 'Joe Johnston', '1950-05-13', 'Austin, Texas, USA', 'Director de Captain America: The First Avenger.', 'https://m.media-amazon.com/images/M/MV5BNzcxNDQwNzgxNV5BMl5BanBnXkFtZTYwNTQ1MTA0._V1_FMjpg_UX1000_.jpg', '1977-01-01'),
('p304', 'Joss Whedon', '1964-06-23', 'New York City, USA', 'Director de The Avengers y Avengers: Age of Ultron.', 'https://encrypted-tbn3.gstatic.com/licensed-image?q=tbn:ANd9GcTWb2nP7vTHNJGVWmO3KYmXtm2oyqhBzkrGOzO4fzpDHVCRX8CGkWTnjgGKNGbcDLmHudb4THADrKM3Zes', '1989-01-01'),
('p305', 'Shane Black', '1961-12-16', 'Pittsburgh, Pennsylvania, USA', 'Director de Iron Man 3.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsw-Bl10F11Tl-gAQiPLeTA5c2FLFurrRJ1g&s', '1987-01-01'),
('p306', 'Alan Taylor', '1965-06-01', 'California, USA', 'Director de Thor: The Dark World.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSyCSKfJfF5yJyVZOGOz_h5omXnH_5cI9fwlB7X8PdgZmuRc3v08T4b4KJ7GEiT-Z3lF4rElrqBmsDgxiDxKRUGXw', '1995-01-01'),
('p307', 'Anthony Russo', '1970-02-03', 'Cleveland, Ohio, USA', 'Co-director de Captain America: The Winter Soldier, Civil War, Infinity War y Endgame.', 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcT7cOsI6yOovKaYjizgX0PJLrGS8EVdx0zJF-9SPkxLLUPHSfr_B6QT_F_82T5h6JJvdt1U5SKf8EILlvpymnLsrg', '1997-01-01'),
('p308', 'Joe Russo', '1971-07-08', 'Cleveland, Ohio, USA', 'Co-director de Captain America: The Winter Soldier, Civil War, Infinity War y Endgame.', 'https://m.media-amazon.com/images/M/MV5BMTc2NzY1NTY5OF5BMl5BanBnXkFtZTgwNjY3ODczNjM@._V1_.jpg', '1997-01-01'),
('p309', 'James Gunn', '1966-08-05', 'St. Louis, Missouri, USA', 'Director de Guardians of the Galaxy 1, 2 y 3.', 'https://es.web.img3.acsta.net/c_310_420/pictures/18/08/07/15/45/5296325.jpg', '1996-01-01'),
('p310', 'Peyton Reed', '1964-07-03', 'Raleigh, North Carolina, USA', 'Director de Ant-Man, Ant-Man and the Wasp y Quantumania.', 'https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQMo64IFTx27j4G0DBDTbIrwmpou-OTnYqLUqg5P53kDTUtUJhRC8ot-XdeyP46ik_hto80G-iz8kmkWUtlJ-71bA', '1988-01-01'),
('p311', 'Scott Derrickson', '1966-07-16', 'Denver, Colorado, USA', 'Director de Doctor Strange.', 'https://m.media-amazon.com/images/M/MV5BMTY0MDA1MTUxM15BMl5BanBnXkFtZTgwNDI0Nzk2NDE@._V1_FMjpg_UX1000_.jpg', '1995-01-01'),
('p312', 'Jon Watts', '1981-06-28', 'Fountain, Colorado, USA', 'Director de Spider-Man: Homecoming, Far From Home y No Way Home.', 'https://encrypted-tbn2.gstatic.com/licensed-image?q=tbn:ANd9GcRvL8zFYUiqT-pd0YefrUD_k51tvGzHW9vwaeNn4fPQe00ddK_Wgw51I9pue36lSPUZd27o708RkzKzjU0', '2000-01-01'),
('p314', 'Ryan Coogler', '1986-05-23', 'Oakland, California, USA', 'Director de Black Panther y Wakanda Forever.', 'https://www.wvnstv.com/wp-content/uploads/sites/76/2025/04/67fe5f5fcc10b9.32433083.jpeg?w=2560&h=1440&crop=1', '2011-01-01'),
('p315', 'Anna Boden', '1979-09-20', 'Boston, Massachusetts, USA', 'Co-directora de Captain Marvel.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnmnZjZDuMjgFn97RyUbYEdKA-2VHJqUeCJw&s', '2002-01-01'),
('p316', 'Ryan Fleck', '1976-09-20', 'Berkeley, California, USA', 'Co-director de Captain Marvel.', 'https://pics.filmaffinity.com/261493895841737-nm_200.jpg', '2002-01-01'),
('p317', 'Cate Shortland', '1968-08-10', 'Temora, Australia', 'Directora de Black Widow.', 'https://m.media-amazon.com/images/M/MV5BNzk4Mzk2NTY2M15BMl5BanBnXkFtZTgwOTk4NTAyMTI@._V1_.jpg', '2000-01-01'),
('p318', 'Destin Daniel Cretton', '1978-11-23', 'Maui, Hawaii, USA', 'Director de Shang-Chi and the Legend of the Ten Rings.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTCm-GK_AWY3YSK2bzM9DfpmeOa6pjDyFWY_g&s', '2007-01-01'),
('p319', 'Chloé Zhao', '1982-03-31', 'Beijing, China', 'Directora de Eternals.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRoWhlcFfNm66LUK1EmAQhKD0UxD1J5eQXIQ&s', '2008-01-01'),
('p320', 'Sam Raimi', '1959-10-23', 'Royal Oak, Michigan, USA', 'Director de Doctor Strange in the Multiverse of Madness.', 'https://m.media-amazon.com/images/M/MV5BODQ0NjI0NzkzMV5BMl5BanBnXkFtZTYwMDc0ODk1._V1_FMjpg_UX1000_.jpg', '1977-01-01'),
('p321', 'Nia DaCosta', '1989-11-08', 'New York City, USA', 'Directora de The Marvels.', 'https://www.pantalla90.es/wp-content/uploads/2023/11/nuevo-poster-de-the-marvels-original.jpg', '2014-01-01');

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
('pa084', 'p216', 'pr036', 'ACTOR', 'Dominic Toretto', NOW(), NOW()),

-- El silencio de los corderos
('pa076', 'p012', 'pr037', 'ACTOR', 'Jack Crawford', NOW(), NOW()),
('pa085', 'p065', 'pr037', 'ACTOR', 'Clarice Starling', NOW(), NOW()),
('pa086', 'p066', 'pr037', 'ACTOR', 'Hannibal Lecter', NOW(), NOW()),

-- El club de la lucha
('pa077', 'p005', 'pr038', 'ACTOR', 'Tyler Durden', NOW(), NOW()),
('pa078', 'p039', 'pr038', 'ACTOR', 'Narrador', NOW(), NOW()),
('pa087', 'p067', 'pr038', 'DIRECTOR', NULL, NOW(), NOW()),

-- Iron Man (pr100)
('pa100', 'p200', 'pr100', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa101', 'p225', 'pr100', 'ACTOR', 'Happy Hogan', NOW(), NOW()),
('pa102', 'p226', 'pr100', 'ACTOR', 'Pepper Potts', NOW(), NOW()),
('pa103', 'p224', 'pr100', 'ACTOR', 'Nick Fury', NOW(), NOW()),
('pa104', 'p225', 'pr100', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Incredible Hulk (pr101)
('pa105', 'p203', 'pr101', 'ACTOR', 'Bruce Banner / Hulk', NOW(), NOW()),
('pa106', 'p224', 'pr101', 'ACTOR', 'Nick Fury', NOW(), NOW()),
('pa107', 'p301', 'pr101', 'DIRECTOR', NULL, NOW(), NOW()),

-- Iron Man 2 (pr102)
('pa108', 'p200', 'pr102', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa109', 'p225', 'pr102', 'ACTOR', 'Happy Hogan', NOW(), NOW()),
('pa110', 'p226', 'pr102', 'ACTOR', 'Pepper Potts', NOW(), NOW()),
('pa111', 'p204', 'pr102', 'ACTOR', 'Natasha Romanoff / Black Widow', NOW(), NOW()),
('pa112', 'p225', 'pr102', 'DIRECTOR', NULL, NOW(), NOW()),

-- Thor (pr103)
('pa113', 'p202', 'pr103', 'ACTOR', 'Thor', NOW(), NOW()),
('pa114', 'p206', 'pr103', 'ACTOR', 'Loki', NOW(), NOW()),
('pa115', 'p228', 'pr103', 'ACTOR', 'Jane Foster', NOW(), NOW()),
('pa116', 'p302', 'pr103', 'DIRECTOR', NULL, NOW(), NOW()),

-- Captain America: The First Avenger (pr104)
('pa117', 'p201', 'pr104', 'ACTOR', 'Steve Rogers / Captain America', NOW(), NOW()),
('pa118', 'p222', 'pr104', 'ACTOR', 'Bucky Barnes', NOW(), NOW()),
('pa119', 'p224', 'pr104', 'ACTOR', 'Nick Fury', NOW(), NOW()),
('pa120', 'p303', 'pr104', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Avengers (pr105)
('pa121', 'p200', 'pr105', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa122', 'p201', 'pr105', 'ACTOR', 'Steve Rogers / Captain America', NOW(), NOW()),
('pa123', 'p202', 'pr105', 'ACTOR', 'Thor', NOW(), NOW()),
('pa124', 'p203', 'pr105', 'ACTOR', 'Bruce Banner / Hulk', NOW(), NOW()),
('pa125', 'p204', 'pr105', 'ACTOR', 'Natasha Romanoff / Black Widow', NOW(), NOW()),
('pa126', 'p205', 'pr105', 'ACTOR', 'Clint Barton / Hawkeye', NOW(), NOW()),
('pa127', 'p206', 'pr105', 'ACTOR', 'Loki', NOW(), NOW()),
('pa128', 'p224', 'pr105', 'ACTOR', 'Nick Fury', NOW(), NOW()),
('pa129', 'p304', 'pr105', 'DIRECTOR', NULL, NOW(), NOW()),

-- Iron Man 3 (pr106)
('pa130', 'p200', 'pr106', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa131', 'p225', 'pr106', 'ACTOR', 'Happy Hogan', NOW(), NOW()),
('pa132', 'p226', 'pr106', 'ACTOR', 'Pepper Potts', NOW(), NOW()),
('pa133', 'p305', 'pr106', 'DIRECTOR', NULL, NOW(), NOW()),

-- Thor: The Dark World (pr107)
('pa134', 'p202', 'pr107', 'ACTOR', 'Thor', NOW(), NOW()),
('pa135', 'p206', 'pr107', 'ACTOR', 'Loki', NOW(), NOW()),
('pa136', 'p228', 'pr107', 'ACTOR', 'Jane Foster', NOW(), NOW()),
('pa137', 'p306', 'pr107', 'DIRECTOR', NULL, NOW(), NOW()),

-- Captain America: The Winter Soldier (pr108)
('pa138', 'p201', 'pr108', 'ACTOR', 'Steve Rogers / Captain America', NOW(), NOW()),
('pa139', 'p204', 'pr108', 'ACTOR', 'Natasha Romanoff / Black Widow', NOW(), NOW()),
('pa140', 'p222', 'pr108', 'ACTOR', 'Bucky Barnes / Winter Soldier', NOW(), NOW()),
('pa141', 'p224', 'pr108', 'ACTOR', 'Nick Fury', NOW(), NOW()),
('pa142', 'p221', 'pr108', 'ACTOR', 'Sam Wilson / Falcon', NOW(), NOW()),
('pa143', 'p307', 'pr108', 'DIRECTOR', NULL, NOW(), NOW()),
('pa144', 'p308', 'pr108', 'DIRECTOR', NULL, NOW(), NOW()),

-- Guardians of the Galaxy (pr109)
('pa145', 'p212', 'pr109', 'ACTOR', 'Gamora', NOW(), NOW()),
('pa146', 'p213', 'pr109', 'ACTOR', 'Nebula', NOW(), NOW()),
('pa147', 'p214', 'pr109', 'ACTOR', 'Drax', NOW(), NOW()),
('pa148', 'p215', 'pr109', 'ACTOR', 'Mantis', NOW(), NOW()),
('pa149', 'p216', 'pr109', 'ACTOR', 'Groot (voz)', NOW(), NOW()),
('pa150', 'p217', 'pr109', 'ACTOR', 'Rocket Raccoon (voz)', NOW(), NOW()),
('pa151', 'p309', 'pr109', 'DIRECTOR', NULL, NOW(), NOW()),

-- Avengers: Age of Ultron (pr110)
('pa152', 'p200', 'pr110', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa153', 'p201', 'pr110', 'ACTOR', 'Steve Rogers / Captain America', NOW(), NOW()),
('pa154', 'p202', 'pr110', 'ACTOR', 'Thor', NOW(), NOW()),
('pa155', 'p203', 'pr110', 'ACTOR', 'Bruce Banner / Hulk', NOW(), NOW()),
('pa156', 'p204', 'pr110', 'ACTOR', 'Natasha Romanoff / Black Widow', NOW(), NOW()),
('pa157', 'p205', 'pr110', 'ACTOR', 'Clint Barton / Hawkeye', NOW(), NOW()),
('pa158', 'p219', 'pr110', 'ACTOR', 'Wanda Maximoff / Scarlet Witch', NOW(), NOW()),
('pa159', 'p220', 'pr110', 'ACTOR', 'Vision', NOW(), NOW()),
('pa160', 'p304', 'pr110', 'DIRECTOR', NULL, NOW(), NOW()),

-- Ant-Man (pr111)
('pa161', 'p207', 'pr111', 'ACTOR', 'Scott Lang / Ant-Man', NOW(), NOW()),
('pa162', 'p255', 'pr111', 'ACTOR', 'Hope van Dyne', NOW(), NOW()),
('pa163', 'p310', 'pr111', 'DIRECTOR', NULL, NOW(), NOW()),

-- Captain America: Civil War (pr112)
('pa164', 'p200', 'pr112', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa165', 'p201', 'pr112', 'ACTOR', 'Steve Rogers / Captain America', NOW(), NOW()),
('pa166', 'p204', 'pr112', 'ACTOR', 'Natasha Romanoff / Black Widow', NOW(), NOW()),
('pa167', 'p205', 'pr112', 'ACTOR', 'Clint Barton / Hawkeye', NOW(), NOW()),
('pa168', 'p211', 'pr112', 'ACTOR', 'Peter Parker / Spider-Man', NOW(), NOW()),
('pa169', 'p219', 'pr112', 'ACTOR', 'Wanda Maximoff / Scarlet Witch', NOW(), NOW()),
('pa170', 'p220', 'pr112', 'ACTOR', 'Vision', NOW(), NOW()),
('pa171', 'p221', 'pr112', 'ACTOR', 'Sam Wilson / Falcon', NOW(), NOW()),
('pa172', 'p222', 'pr112', 'ACTOR', 'Bucky Barnes / Winter Soldier', NOW(), NOW()),
('pa173', 'p223', 'pr112', 'ACTOR', 'James Rhodes / War Machine', NOW(), NOW()),
('pa174', 'p307', 'pr112', 'DIRECTOR', NULL, NOW(), NOW()),
('pa175', 'p308', 'pr112', 'DIRECTOR', NULL, NOW(), NOW()),

-- Doctor Strange (pr113)
('pa176', 'p208', 'pr113', 'ACTOR', 'Doctor Stephen Strange', NOW(), NOW()),
('pa177', 'p311', 'pr113', 'DIRECTOR', NULL, NOW(), NOW()),

-- Guardians of the Galaxy Vol. 2 (pr114)
('pa178', 'p212', 'pr114', 'ACTOR', 'Gamora', NOW(), NOW()),
('pa179', 'p213', 'pr114', 'ACTOR', 'Nebula', NOW(), NOW()),
('pa180', 'p214', 'pr114', 'ACTOR', 'Drax', NOW(), NOW()),
('pa181', 'p215', 'pr114', 'ACTOR', 'Mantis', NOW(), NOW()),
('pa182', 'p216', 'pr114', 'ACTOR', 'Groot (voz)', NOW(), NOW()),
('pa183', 'p217', 'pr114', 'ACTOR', 'Rocket Raccoon (voz)', NOW(), NOW()),
('pa184', 'p309', 'pr114', 'DIRECTOR', NULL, NOW(), NOW()),

-- Spider-Man: Homecoming (pr115)
('pa185', 'p211', 'pr115', 'ACTOR', 'Peter Parker / Spider-Man', NOW(), NOW()),
('pa186', 'p200', 'pr115', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa187', 'p312', 'pr115', 'DIRECTOR', NULL, NOW(), NOW()),

-- Thor: Ragnarok (pr116)
('pa188', 'p202', 'pr116', 'ACTOR', 'Thor', NOW(), NOW()),
('pa189', 'p206', 'pr116', 'ACTOR', 'Loki', NOW(), NOW()),
('pa190', 'p213', 'pr116', 'ACTOR', 'Nebula', NOW(), NOW()),
('pa191', 'p214', 'pr116', 'ACTOR', 'Drax', NOW(), NOW()),
('pa192', 'p216', 'pr116', 'ACTOR', 'Groot (voz)', NOW(), NOW()),
('pa193', 'p217', 'pr116', 'ACTOR', 'Rocket Raccoon (voz)', NOW(), NOW()),
('pa194', 'p227', 'pr116', 'ACTOR', 'Valkyrie', NOW(), NOW()),
('pa195', 'p313', 'pr116', 'DIRECTOR', NULL, NOW(), NOW()),

-- Black Panther (pr117)
('pa196', 'p209', 'pr117', 'ACTOR', 'TChalla / Black Panther', NOW(), NOW()),
('pa197', 'p230', 'pr117', 'ACTOR', 'Shuri', NOW(), NOW()),
('pa198', 'p231', 'pr117', 'ACTOR', 'MBaku', NOW(), NOW()),
('pa199', 'p232', 'pr117', 'ACTOR', 'Okoye', NOW(), NOW()),
('pa200', 'p233', 'pr117', 'ACTOR', 'Ramonda', NOW(), NOW()),
('pa201', 'p234', 'pr117', 'ACTOR', 'Ayo', NOW(), NOW()),
('pa202', 'p235', 'pr117', 'ACTOR', 'Erik Killmonger', NOW(), NOW()),
('pa203', 'p236', 'pr117', 'ACTOR', 'Nakia', NOW(), NOW()),
('pa204', 'p314', 'pr117', 'DIRECTOR', NULL, NOW(), NOW()),

-- Avengers: Infinity War (pr118)
('pa205', 'p200', 'pr118', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa206', 'p201', 'pr118', 'ACTOR', 'Steve Rogers / Captain America', NOW(), NOW()),
('pa207', 'p202', 'pr118', 'ACTOR', 'Thor', NOW(), NOW()),
('pa208', 'p203', 'pr118', 'ACTOR', 'Bruce Banner / Hulk', NOW(), NOW()),
('pa209', 'p204', 'pr118', 'ACTOR', 'Natasha Romanoff / Black Widow', NOW(), NOW()),
('pa210', 'p205', 'pr118', 'ACTOR', 'Clint Barton / Hawkeye', NOW(), NOW()),
('pa211', 'p206', 'pr118', 'ACTOR', 'Loki', NOW(), NOW()),
('pa212', 'p207', 'pr118', 'ACTOR', 'Scott Lang / Ant-Man', NOW(), NOW()),
('pa213', 'p208', 'pr118', 'ACTOR', 'Doctor Stephen Strange', NOW(), NOW()),
('pa214', 'p209', 'pr118', 'ACTOR', 'TChalla / Black Panther', NOW(), NOW()),
('pa215', 'p211', 'pr118', 'ACTOR', 'Peter Parker / Spider-Man', NOW(), NOW()),
('pa216', 'p212', 'pr118', 'ACTOR', 'Gamora', NOW(), NOW()),
('pa217', 'p213', 'pr118', 'ACTOR', 'Nebula', NOW(), NOW()),
('pa218', 'p214', 'pr118', 'ACTOR', 'Drax', NOW(), NOW()),
('pa219', 'p215', 'pr118', 'ACTOR', 'Mantis', NOW(), NOW()),
('pa220', 'p216', 'pr118', 'ACTOR', 'Groot (voz)', NOW(), NOW()),
('pa221', 'p217', 'pr118', 'ACTOR', 'Rocket Raccoon (voz)', NOW(), NOW()),
('pa222', 'p218', 'pr118', 'ACTOR', 'Thanos', NOW(), NOW()),
('pa223', 'p219', 'pr118', 'ACTOR', 'Wanda Maximoff / Scarlet Witch', NOW(), NOW()),
('pa224', 'p220', 'pr118', 'ACTOR', 'Vision', NOW(), NOW()),
('pa225', 'p221', 'pr118', 'ACTOR', 'Sam Wilson / Falcon', NOW(), NOW()),
('pa226', 'p222', 'pr118', 'ACTOR', 'Bucky Barnes / Winter Soldier', NOW(), NOW()),
('pa227', 'p223', 'pr118', 'ACTOR', 'James Rhodes / War Machine', NOW(), NOW()),
('pa228', 'p227', 'pr118', 'ACTOR', 'Valkyrie', NOW(), NOW()),
('pa229', 'p230', 'pr118', 'ACTOR', 'Shuri', NOW(), NOW()),
('pa230', 'p232', 'pr118', 'ACTOR', 'Okoye', NOW(), NOW()),
('pa231', 'p236', 'pr118', 'ACTOR', 'Nakia', NOW(), NOW()),
('pa232', 'p307', 'pr118', 'DIRECTOR', NULL, NOW(), NOW()),
('pa233', 'p308', 'pr118', 'DIRECTOR', NULL, NOW(), NOW()),

-- Ant-Man and the Wasp (pr119)
('pa234', 'p207', 'pr119', 'ACTOR', 'Scott Lang / Ant-Man', NOW(), NOW()),
('pa235', 'p255', 'pr119', 'ACTOR', 'Hope van Dyne / Wasp', NOW(), NOW()),
('pa236', 'p254', 'pr119', 'ACTOR', 'Janet van Dyne', NOW(), NOW()),
('pa237', 'p310', 'pr119', 'DIRECTOR', NULL, NOW(), NOW()),

-- Captain Marvel (pr120)
('pa238', 'p210', 'pr120', 'ACTOR', 'Carol Danvers / Captain Marvel', NOW(), NOW()),
('pa239', 'p224', 'pr120', 'ACTOR', 'Nick Fury', NOW(), NOW()),
('pa240', 'p315', 'pr120', 'DIRECTOR', NULL, NOW(), NOW()),
('pa241', 'p316', 'pr120', 'DIRECTOR', NULL, NOW(), NOW()),

-- Avengers: Endgame (pr121)
('pa242', 'p200', 'pr121', 'ACTOR', 'Tony Stark / Iron Man', NOW(), NOW()),
('pa243', 'p201', 'pr121', 'ACTOR', 'Steve Rogers / Captain America', NOW(), NOW()),
('pa244', 'p202', 'pr121', 'ACTOR', 'Thor', NOW(), NOW()),
('pa245', 'p203', 'pr121', 'ACTOR', 'Bruce Banner / Hulk', NOW(), NOW()),
('pa246', 'p204', 'pr121', 'ACTOR', 'Natasha Romanoff / Black Widow', NOW(), NOW()),
('pa247', 'p205', 'pr121', 'ACTOR', 'Clint Barton / Hawkeye', NOW(), NOW()),
('pa248', 'p206', 'pr121', 'ACTOR', 'Loki', NOW(), NOW()),
('pa249', 'p207', 'pr121', 'ACTOR', 'Scott Lang / Ant-Man', NOW(), NOW()),
('pa250', 'p208', 'pr121', 'ACTOR', 'Doctor Stephen Strange', NOW(), NOW()),
('pa251', 'p209', 'pr121', 'ACTOR', 'TChalla / Black Panther', NOW(), NOW()),
('pa252', 'p210', 'pr121', 'ACTOR', 'Carol Danvers / Captain Marvel', NOW(), NOW()),
('pa253', 'p211', 'pr121', 'ACTOR', 'Peter Parker / Spider-Man', NOW(), NOW()),
('pa254', 'p212', 'pr121', 'ACTOR', 'Gamora', NOW(), NOW()),
('pa255', 'p213', 'pr121', 'ACTOR', 'Nebula', NOW(), NOW()),
('pa256', 'p214', 'pr121', 'ACTOR', 'Drax', NOW(), NOW()),
('pa257', 'p215', 'pr121', 'ACTOR', 'Mantis', NOW(), NOW()),
('pa258', 'p216', 'pr121', 'ACTOR', 'Groot (voz)', NOW(), NOW()),
('pa259', 'p217', 'pr121', 'ACTOR', 'Rocket Raccoon (voz)', NOW(), NOW()),
('pa260', 'p218', 'pr121', 'ACTOR', 'Thanos', NOW(), NOW()),
('pa261', 'p219', 'pr121', 'ACTOR', 'Wanda Maximoff / Scarlet Witch', NOW(), NOW()),
('pa262', 'p220', 'pr121', 'ACTOR', 'Vision', NOW(), NOW()),
('pa263', 'p221', 'pr121', 'ACTOR', 'Sam Wilson / Falcon', NOW(), NOW()),
('pa264', 'p222', 'pr121', 'ACTOR', 'Bucky Barnes / Winter Soldier', NOW(), NOW()),
('pa265', 'p223', 'pr121', 'ACTOR', 'James Rhodes / War Machine', NOW(), NOW()),
('pa266', 'p227', 'pr121', 'ACTOR', 'Valkyrie', NOW(), NOW()),
('pa267', 'p230', 'pr121', 'ACTOR', 'Shuri', NOW(), NOW()),
('pa268', 'p232', 'pr121', 'ACTOR', 'Okoye', NOW(), NOW()),
('pa269', 'p236', 'pr121', 'ACTOR', 'Nakia', NOW(), NOW()),
('pa270', 'p307', 'pr121', 'DIRECTOR', NULL, NOW(), NOW()),
('pa271', 'p308', 'pr121', 'DIRECTOR', NULL, NOW(), NOW()),

-- Spider-Man: Far From Home (pr122)
('pa272', 'p211', 'pr122', 'ACTOR', 'Peter Parker / Spider-Man', NOW(), NOW()),
('pa273', 'p312', 'pr122', 'DIRECTOR', NULL, NOW(), NOW()),

-- Black Widow (pr123)
('pa274', 'p204', 'pr123', 'ACTOR', 'Natasha Romanoff / Black Widow', NOW(), NOW()),
('pa275', 'p317', 'pr123', 'DIRECTOR', NULL, NOW(), NOW()),

-- Shang-Chi and the Legend of the Ten Rings (pr124)
('pa276', 'p237', 'pr124', 'ACTOR', 'Shang-Chi', NOW(), NOW()),
('pa277', 'p238', 'pr124', 'ACTOR', 'Katy', NOW(), NOW()),
('pa278', 'p239', 'pr124', 'ACTOR', 'Wenwu / The Mandarin', NOW(), NOW()),
('pa279', 'p240', 'pr124', 'ACTOR', 'Xialing', NOW(), NOW()),
('pa280', 'p318', 'pr124', 'DIRECTOR', NULL, NOW(), NOW()),

-- Eternals (pr125)
('pa281', 'p241', 'pr125', 'ACTOR', 'Sersi', NOW(), NOW()),
('pa282', 'p242', 'pr125', 'ACTOR', 'Ikaris', NOW(), NOW()),
('pa283', 'p243', 'pr125', 'ACTOR', 'Kingo', NOW(), NOW()),
('pa284', 'p244', 'pr125', 'ACTOR', 'Makkari', NOW(), NOW()),
('pa285', 'p245', 'pr125', 'ACTOR', 'Druig', NOW(), NOW()),
('pa286', 'p246', 'pr125', 'ACTOR', 'Phastos', NOW(), NOW()),
('pa287', 'p247', 'pr125', 'ACTOR', 'Ajak', NOW(), NOW()),
('pa288', 'p401', 'pr125', 'ACTOR', 'Dane Whitman', NOW(), NOW()),
('pa289', 'p319', 'pr125', 'DIRECTOR', NULL, NOW(), NOW()),

-- Spider-Man: No Way Home (pr126)
('pa290', 'p211', 'pr126', 'ACTOR', 'Peter Parker / Spider-Man', NOW(), NOW()),
('pa291', 'p208', 'pr126', 'ACTOR', 'Doctor Stephen Strange', NOW(), NOW()),
('pa292', 'p312', 'pr126', 'DIRECTOR', NULL, NOW(), NOW()),

-- Doctor Strange in the Multiverse of Madness (pr127)
('pa293', 'p208', 'pr127', 'ACTOR', 'Doctor Stephen Strange', NOW(), NOW()),
('pa294', 'p219', 'pr127', 'ACTOR', 'Wanda Maximoff / Scarlet Witch', NOW(), NOW()),
('pa295', 'p320', 'pr127', 'DIRECTOR', NULL, NOW(), NOW()),

-- Thor: Love and Thunder (pr128)
('pa296', 'p202', 'pr128', 'ACTOR', 'Thor', NOW(), NOW()),
('pa297', 'p228', 'pr128', 'ACTOR', 'Jane Foster / Mighty Thor', NOW(), NOW()),
('pa298', 'p313', 'pr128', 'ACTOR', 'Korg', NOW(), NOW()),
('pa299', 'p313', 'pr128', 'DIRECTOR', NULL, NOW(), NOW()),

-- Black Panther: Wakanda Forever (pr129)
('pa300', 'p230', 'pr129', 'ACTOR', 'Shuri / Black Panther', NOW(), NOW()),
('pa301', 'p231', 'pr129', 'ACTOR', 'MBaku', NOW(), NOW()),
('pa302', 'p232', 'pr129', 'ACTOR', 'Okoye', NOW(), NOW()),
('pa303', 'p233', 'pr129', 'ACTOR', 'Ramonda', NOW(), NOW()),
('pa304', 'p234', 'pr129', 'ACTOR', 'Ayo', NOW(), NOW()),
('pa305', 'p236', 'pr129', 'ACTOR', 'Nakia', NOW(), NOW()),
('pa306', 'p314', 'pr129', 'DIRECTOR', NULL, NOW(), NOW()),

-- Ant-Man and the Wasp: Quantumania (pr130)
('pa307', 'p207', 'pr130', 'ACTOR', 'Scott Lang / Ant-Man', NOW(), NOW()),
('pa308', 'p255', 'pr130', 'ACTOR', 'Hope van Dyne / Wasp', NOW(), NOW()),
('pa309', 'p253', 'pr130', 'ACTOR', 'Cassie Lang', NOW(), NOW()),
('pa310', 'p254', 'pr130', 'ACTOR', 'Janet van Dyne', NOW(), NOW()),
('pa311', 'p252', 'pr130', 'ACTOR', 'Kang the Conqueror', NOW(), NOW()),
('pa312', 'p310', 'pr130', 'DIRECTOR', NULL, NOW(), NOW()),

-- Guardians of the Galaxy Vol. 3 (pr131)
('pa313', 'p212', 'pr131', 'ACTOR', 'Gamora', NOW(), NOW()),
('pa314', 'p213', 'pr131', 'ACTOR', 'Nebula', NOW(), NOW()),
('pa315', 'p214', 'pr131', 'ACTOR', 'Drax', NOW(), NOW()),
('pa316', 'p215', 'pr131', 'ACTOR', 'Mantis', NOW(), NOW()),
('pa317', 'p216', 'pr131', 'ACTOR', 'Groot (voz)', NOW(), NOW()),
('pa318', 'p217', 'pr131', 'ACTOR', 'Rocket Raccoon (voz)', NOW(), NOW()),
('pa319', 'p309', 'pr131', 'DIRECTOR', NULL, NOW(), NOW()),

-- The Marvels (pr132)
('pa320', 'p210', 'pr132', 'ACTOR', 'Carol Danvers / Captain Marvel', NOW(), NOW()),
('pa321', 'p250', 'pr132', 'ACTOR', 'Kate Bishop', NOW(), NOW()),
('pa322', 'p251', 'pr132', 'ACTOR', 'Kamala Khan / Ms. Marvel', NOW(), NOW()),
('pa323', 'p321', 'pr132', 'DIRECTOR', NULL, NOW(), NOW()),

-- Juego de Tronos
('pa400', 'p400', 'pr133', 'ACTOR', 'Daenerys Targaryen', NOW(), NOW()),
('pa401', 'p401', 'pr133', 'ACTOR', 'Jon Snow', NOW(), NOW()),
('pa402', 'p402', 'pr133', 'ACTOR', 'Tyrion Lannister', NOW(), NOW()),
('pa403', 'p403', 'pr133', 'ACTOR', 'Cersei Lannister', NOW(), NOW()),
('pa404', 'p404', 'pr133', 'ACTOR', 'Sansa Stark', NOW(), NOW()),
('pa405', 'p405', 'pr133', 'ACTOR', 'Arya Stark', NOW(), NOW()),
('pa406', 'p406', 'pr133', 'ACTOR', 'Jaime Lannister', NOW(), NOW()),
('pa407', 'p407', 'pr133', 'ACTOR', 'Ned Stark', NOW(), NOW()),
('pa408', 'p408', 'pr133', 'DIRECTOR', NULL, NOW(), NOW()),
('pa409', 'p409', 'pr133', 'DIRECTOR', NULL, NOW(), NOW()),

-- La Casa del Dragón
('pa410', 'p410', 'pr134', 'ACTOR', 'Viserys I Targaryen', NOW(), NOW()),
('pa411', 'p411', 'pr134', 'ACTOR', 'Daemon Targaryen', NOW(), NOW()),
('pa412', 'p412', 'pr134', 'ACTOR', 'Rhaenyra Targaryen', NOW(), NOW()),
('pa413', 'p413', 'pr134', 'ACTOR', 'Alicent Hightower', NOW(), NOW()),
('pa414', 'p414', 'pr134', 'ACTOR', 'Rhaenyra Targaryen (joven)', NOW(), NOW()),
('pa415', 'p415', 'pr134', 'ACTOR', 'Alicent Hightower (joven)', NOW(), NOW()),
('pa416', 'p416', 'pr134', 'DIRECTOR', NULL, NOW(), NOW()),
('pa417', 'p417', 'pr134', 'DIRECTOR', NULL, NOW(), NOW());

-- Insertar ubicaciones de filmación para tus producciones
INSERT INTO ubicaciones (id, nombre, latitud, longitud) VALUES
('u006', 'Nueva York, USA', 40.7128, -74.0060),
('u007', 'Chicago, USA', 41.8781, -87.6298),
('u008', 'Los Ángeles, USA', 34.0522, -118.2437),
('u009', 'Londres, UK', 51.5074, -0.1278),
('u010', 'París, Francia', 48.8566, 2.3522),
('u011', 'Montreal, Canadá', 45.5017, -73.5673),
('u012', 'Sydney, Australia', -33.8688, 151.2093),
('u013', 'Alabama, USA', 32.3182, -86.9023),
('u014', 'Savannah, Georgia, USA', 32.0809, -81.0912),
('u015', 'Mississippi, USA', 32.3547, -89.3985),
('u016', 'Túnez', 33.8869, 9.5375),
('u017', 'Hawaii, USA', 19.8968, -155.5828),
('u018', 'Costa Rica', 9.7489, -83.7534),
('u019', 'Praga, República Checa', 50.0755, 14.4378),
('u020', 'Los Ángeles, USA', 34.0522, -118.2437),
('u021', 'Pittsburgh, Pennsylvania, USA', 40.4406, -79.9959),
('u022', 'Wilmington, North Carolina, USA', 34.2257, -77.9447),
('u023', 'Roma, Italia', 41.9028, 12.4964),
('u024', 'Barcelona, España', 41.3851, 2.1734),
('u025', 'Berlín, Alemania', 52.5200, 13.4050),
('u026', 'Ámsterdam, Países Bajos', 52.3676, 4.9041),
('u027', 'Viena, Austria', 48.2082, 16.3738),
('u028', 'Budapest, Hungría', 47.4979, 19.0402),
('u029', 'Estambul, Turquía', 41.0082, 28.9784),
('u030', 'Dubái, Emiratos Árabes', 25.2048, 55.2708),
('u031', 'Tokio, Japón', 35.6762, 139.6503),
('u032', 'Seúl, Corea del Sur', 37.5665, 126.9780),
('u033', 'Pekín, China', 39.9042, 116.4074),
('u034', 'Shanghái, China', 31.2304, 121.4737),
('u035', 'Hong Kong', 22.3193, 114.1694),
('u036', 'Singapur', 1.3521, 103.8198),
('u037', 'Bangkok, Tailandia', 13.7563, 100.5018),
('u038', 'Kuala Lumpur, Malasia', 3.1390, 101.6869),
('u039', 'Mumbai, India', 19.0760, 72.8777),
('u040', 'Nueva Delhi, India', 28.6139, 77.2090),
('u041', 'El Cairo, Egipto', 30.0444, 31.2357),
('u042', 'Ciudad del Cabo, Sudáfrica', -33.9249, 18.4241),
('u043', 'Nairobi, Kenia', -1.2864, 36.8172),
('u044', 'Marrakech, Marruecos', 31.6295, -7.9811),
('u045', 'Rio de Janeiro, Brasil', -22.9068, -43.1729),
('u046', 'Buenos Aires, Argentina', -34.6037, -58.3816),
('u047', 'Santiago, Chile', -33.4489, -70.6693),
('u048', 'Lima, Perú', -12.0464, -77.0428),
('u049', 'Ciudad de México, México', 19.4326, -99.1332),
('u050', 'Toronto, Canadá', 43.6532, -79.3832),
('u051', 'Vancouver, Canadá', 49.2827, -123.1207),
('u052', 'San Francisco, USA', 37.7749, -122.4194),
('u053', 'Las Vegas, USA', 36.1699, -115.1398),
('u054', 'Miami, USA', 25.7617, -80.1918),
('u055', 'Boston, USA', 42.3601, -71.0589),
('u056', 'Seattle, USA', 47.6062, -122.3321),
('u057', 'Washington D.C., USA', 38.9072, -77.0369),
('u058', 'Filadelfia, USA', 39.9526, -75.1652),
('u059', 'Atlanta, USA', 33.7490, -84.3880),
('u060', 'Dallas, USA', 32.7767, -96.7970),
('u061', 'Houston, USA', 29.7604, -95.3698),
('u062', 'Denver, USA', 39.7392, -104.9903),
('u063', 'Nashville, USA', 36.1627, -86.7816),
('u064', 'Nueva Orleans, USA', 29.9511, -90.0715),
('u065', 'San Diego, USA', 32.7157, -117.1611),
('u066', 'Phoenix, USA', 33.4484, -112.0740),
('u067', 'Salt Lake City, USA', 40.7608, -111.8910),
('u068', 'Portland, USA', 45.5051, -122.6750),
('u069', 'Austin, USA', 30.2672, -97.7431),
('u070', 'San Antonio, USA', 29.4241, -98.4936),
('u071', 'Orlando, USA', 28.5383, -81.3792),
('u072', 'Honolulu, USA', 21.3069, -157.8583),
('u073', 'Anchorage, USA', 61.2181, -149.9003),
('u074', 'Juneau, USA', 58.3019, -134.4197),
('u075', 'Fairbanks, USA', 64.8378, -147.7164),
('u076', 'Sitka, USA', 57.0531, -135.3300),
('u077', 'Ketchikan, USA', 55.3422, -131.6461),
('u078', 'Nome, USA', 64.5011, -165.4064),
('u079', 'Barrow, USA', 71.2906, -156.7886),
('u080', 'Valdez, USA', 61.1308, -146.3483),
('u081', 'Cordova, USA', 60.5438, -145.7580),
('u082', 'Homer, USA', 59.6430, -151.5489),
('u083', 'Seward, USA', 60.1042, -149.4422),
('u084', 'Kodiak, USA', 57.7900, -152.4072),
('u085', 'Unalaska, USA', 53.8736, -166.5267),
('u086', 'Bethel, USA', 60.7922, -161.7558),
('u087', 'Kotzebue, USA', 66.8986, -162.5981),
('u088', 'Unalakleet, USA', 63.8786, -160.7883),
('u089', 'Galena, USA', 64.7333, -156.9275),
('u090', 'Nome, USA', 64.5011, -165.4064),
('u091', 'Prudhoe Bay, USA', 70.2556, -148.3372),
('u092', 'Deadhorse, USA', 70.1947, -148.4650),
('u093', 'Coldfoot, USA', 67.2514, -150.1761),
('u094', 'Wiseman, USA', 67.4100, -150.1075),
('u095', 'Barrow, USA', 71.2906, -156.7886),
('u096', 'Wainwright, USA', 70.6369, -160.0383),
('u097', 'Point Hope, USA', 68.3478, -166.8081),
('u098', 'Kaktovik, USA', 70.1336, -143.6128),
('u099', 'Anaktuvuk Pass, USA', 68.1433, -151.7358),
('u100', 'Atqasuk, USA', 70.4692, -157.3994),
('u101', 'San Petersburgo, Rusia', 59.9343, 30.3351),
('u102', 'Moscú, Rusia', 55.7558, 37.6173),
('u103', 'Kiev, Ucrania', 50.4501, 30.5234),
('u104', 'Varsovia, Polonia', 52.2297, 21.0122),
('u105', 'Cracovia, Polonia', 50.0647, 19.9450),
('u106', 'Pamplona, España', 42.8125, -1.6458),
('u107', 'Sevilla, España', 37.3891, -5.9845),
('u108', 'Valencia, España', 39.4699, -0.3763),
('u109', 'Lisboa, Portugal', 38.7223, -9.1393),
('u110', 'Oporto, Portugal', 41.1579, -8.6291),
('u200', 'Los Angeles, California, USA', 34.0522, -118.2437),
('u201', 'New York City, New York, USA', 40.7128, -74.0060),
('u202', 'Atlanta, Georgia, USA', 33.7490, -84.3880),
('u203', 'London, UK', 51.5074, -0.1278),
('u204', 'Norway', 60.4720, 8.4689),
('u205', 'South Korea', 35.9078, 127.7669),
('u206', 'Australia', -25.2744, 133.7751),
('u207', 'China', 35.8617, 104.1954),
('u208', 'Wakanda (filmed in Atlanta and South Korea)', 33.7490, -84.3880),
('u209', 'Asgard (filmed in various locations)', 60.4720, 8.4689),
('u210', 'Knowhere (filmed in studio)', 0, 0),
('u211', 'Titan (filmed in studio)', 0, 0),
('u212', 'Quantum Realm (filmed in studio)', 0, 0),
('u213', 'Sakaar (filmed in Australia)', -25.2744, 133.7751),
('u214', 'San Francisco, California, USA', 37.7749, -122.4194),
('u215', 'Washington D.C., USA', 38.9072, -77.0369),
('u216', 'Siberia, Russia (filmed in Iceland)', 64.9631, -19.0208),
('u217', 'Edinburgh, Scotland', 55.9533, -3.1883),
('u218', 'Hong Kong', 22.3193, 114.1694),
('u219', 'Kathmandu, Nepal', 27.7172, 85.3240),
('u220', 'Morocco', 31.7917, -7.0926),
('u221', 'Brazil', -14.2350, -51.9253),
('u222', 'Germany', 51.1657, 10.4515),
('u223', 'Japan', 36.2048, 138.2529),
('u224', 'Madripoor (filmed in Atlanta)', 33.7490, -84.3880),
('u225', 'New Asgard (filmed in Norway)', 60.4720, 8.4689),

-- Ubicaciones de Juego de Tronos
('u300', 'Belfast, Northern Ireland', 54.5973, -5.9301),
('u301', 'Dubrovnik, Croatia', 42.6507, 18.0944),
('u302', 'Sevilla, Spain', 37.3891, -5.9845),
('u303', 'Iceland', 64.9631, -19.0208),
('u304', 'Morocco', 31.7917, -7.0926),
('u305', 'Malta', 35.9375, 14.3754),

-- Ubicaciones de La Casa del Dragón
('u306', 'Cornwall, England', 50.2660, -5.0527),
('u307', 'Derbyshire, England', 53.1048, -1.5624),
('u308', 'Cáceres, Spain', 39.4753, -6.3724),
('u309', 'Trujillo, Spain', 39.4596, -5.8804),
('u310', 'Portugal', 39.3999, -8.2245);

-- Insertar rodajes con el modelo actual
INSERT INTO rodajes (id, produccion_id, ubicacion_id, notas, created_at, updated_at) VALUES
-- El Padrino (pr018)
('r001', 'pr018', 'u006', 'Escenas en Little Italy, Nueva York', NOW(), NOW()),
('r002', 'pr018', 'u007', 'Escenas de la boda y exteriores en Chicago', NOW(), NOW()),

-- El Caballero Oscuro (pr019)
('r003', 'pr019', 'u008', 'Escenas principales en Los Ángeles', NOW(), NOW()),
('r004', 'pr019', 'u009', 'Escenas del hospital y persecuciones en Londres', NOW(), NOW()),

-- Inception (pr022)
('r005', 'pr022', 'u010', 'Escenas del sueño en París', NOW(), NOW()),
('r006', 'pr022', 'u011', 'Escenas del laberinto y hotel en Montreal', NOW(), NOW()),

-- The Matrix (pr023)
('r007', 'pr023', 'u012', 'Filmación principal en Sydney', NOW(), NOW()),

-- Forrest Gump (pr021)
('r008', 'pr021', 'u013', 'Escenas de la infancia en Alabama', NOW(), NOW()),
('r009', 'pr021', 'u014', 'Escenas de la carrera y parque en Savannah', NOW(), NOW()),

-- Django Unchained (pr032)
('r010', 'pr032', 'u015', 'Plantación y escenas del oeste en Mississippi', NOW(), NOW()),

-- Indiana Jones (pr033)
('r011', 'pr033', 'u016', 'Escenas del desierto en Túnez', NOW(), NOW()),
('r012', 'pr033', 'u017', 'Escenas de la jungla en Hawaii', NOW(), NOW()),

-- Jurassic Park (pr034)
('r013', 'pr034', 'u018', 'Escenas de la isla en Costa Rica', NOW(), NOW()),

-- Misión Imposible (pr035)
('r014', 'pr035', 'u019', 'Escenas de la embajada en Praga', NOW(), NOW()),

-- Fast & Furious (pr036)
('r015', 'pr036', 'u020', 'Carreras callejeras en Los Ángeles', NOW(), NOW()),

-- El silencio de los corderos (pr037)
('r016', 'pr037', 'u021', 'Escenas del FBI en Pittsburgh', NOW(), NOW()),

-- El club de la lucha (pr038)
('r017', 'pr038', 'u022', 'Escenas de la casa y oficinas en Wilmington', NOW(), NOW()),

-- Titanic (pr024)
('r018', 'pr024', 'u050', 'Escenas del barco filmadas en tanques gigantes en Toronto', NOW(), NOW()),
('r019', 'pr024', 'u051', 'Algunas escenas exteriores filmadas en Vancouver', NOW(), NOW()),

-- Gladiator (pr025)
('r020', 'pr025', 'u023', 'Escenas del Coliseo filmadas en Roma', NOW(), NOW()),
('r021', 'pr025', 'u044', 'Escenas de batallas filmadas en Marruecos', NOW(), NOW()),

-- The Shawshank Redemption (pr026)
('r022', 'pr026', 'u055', 'Exteriores de la prisión filmados en Ohio State Reformatory', NOW(), NOW()),

-- The Godfather Part II (pr027)
('r023', 'pr027', 'u006', 'Escenas de Nueva York filmadas en locaciones reales', NOW(), NOW()),
('r024', 'pr027', 'u049', 'Escenas de Cuba filmadas en Ciudad de México', NOW(), NOW()),

-- The Lord of the Rings: The Return of the King (pr028)
('r025', 'pr028', 'u101', 'Algunas escenas de batallas filmadas en Nueva Zelanda', NOW(), NOW()),

-- Interstellar (pr029)
('r026', 'pr029', 'u102', 'Escenas del espacio filmadas con sets en estudio', NOW(), NOW()),
('r027', 'pr029', 'u103', 'Escenas del planeta de hielo filmadas en Islandia', NOW(), NOW()),

-- Whiplash (pr030)
('r028', 'pr030', 'u008', 'Filmado principalmente en Los Ángeles', NOW(), NOW()),

-- La La Land (pr031)
('r029', 'pr031', 'u008', 'Numerosas locaciones en Los Ángeles', NOW(), NOW()),

-- The Godfather (pr018) - adicionales
('r030', 'pr018', 'u104', 'Algunas escenas adicionales filmadas en Polonia', NOW(), NOW()),

-- Pulp Fiction (pr020)
('r031', 'pr020', 'u008', 'Varias locaciones en Los Ángeles', NOW(), NOW()),

-- El Caballero Oscuro (pr019) - adicionales
('r032', 'pr019', 'u105', 'Escenas de persecución filmadas en Chicago', NOW(), NOW()),

-- Inception (pr022) - adicionales
('r033', 'pr022', 'u106', 'Escenas de la montaña filmadas en Canadá', NOW(), NOW()),

-- The Matrix (pr023) - adicionales
('r034', 'pr023', 'u107', 'Escenas del dojo filmadas en Australia', NOW(), NOW()),

-- Iron Man (pr100)
('r100', 'pr100', 'u200', 'Primary filming location for Stark Industries', NOW(), NOW()),
('r101', 'pr100', 'u201', 'New York scenes', NOW(), NOW()),

-- The Incredible Hulk (pr101)
('r102', 'pr101', 'u202', 'Filmed in Atlanta for tax incentives', NOW(), NOW()),
('r103', 'pr101', 'u201', 'New York scenes', NOW(), NOW()),

-- Iron Man 2 (pr102)
('r104', 'pr102', 'u200', 'Stark Expo scenes', NOW(), NOW()),
('r105', 'pr102', 'u203', 'Monaco racing scenes', NOW(), NOW()),

-- Thor (pr103)
('r106', 'pr103', 'u204', 'Asgard scenes filmed in Norway', NOW(), NOW()),
('r107', 'pr103', 'u200', 'New Mexico desert scenes', NOW(), NOW()),

-- Captain America: The First Avenger (pr104)
('r108', 'pr104', 'u205', 'European war scenes', NOW(), NOW()),
('r109', 'pr104', 'u201', 'New York scenes', NOW(), NOW()),

-- The Avengers (pr105)
('r110', 'pr105', 'u201', 'New York battle scenes', NOW(), NOW()),
('r111', 'pr105', 'u200', 'Stark Tower scenes', NOW(), NOW()),

-- Iron Man 3 (pr106)
('r112', 'pr106', 'u200', 'Malibu house scenes', NOW(), NOW()),
('r113', 'pr106', 'u206', 'Final battle scenes', NOW(), NOW()),

-- Thor: The Dark World (pr107)
('r114', 'pr107', 'u204', 'Asgard scenes', NOW(), NOW()),
('r115', 'pr107', 'u203', 'London scenes', NOW(), NOW()),

-- Captain America: The Winter Soldier (pr108)
('r116', 'pr108', 'u201', 'Washington D.C. scenes', NOW(), NOW()),
('r117', 'pr108', 'u215', 'SHIELD headquarters', NOW(), NOW()),

-- Guardians of the Galaxy (pr109)
('r118', 'pr109', 'u210', 'Knowhere scenes filmed in studio', NOW(), NOW()),
('r119', 'pr109', 'u211', 'Titan scenes filmed in studio', NOW(), NOW()),

-- Avengers: Age of Ultron (pr110)
('r120', 'pr110', 'u201', 'New York scenes', NOW(), NOW()),
('r121', 'pr110', 'u216', 'Sokovia battle scenes filmed in Iceland', NOW(), NOW()),

-- Ant-Man (pr111)
('r122', 'pr111', 'u214', 'San Francisco scenes', NOW(), NOW()),
('r123', 'pr111', 'u212', 'Quantum Realm scenes', NOW(), NOW()),

-- Captain America: Civil War (pr112)
('r124', 'pr112', 'u202', 'Airport battle filmed in Atlanta', NOW(), NOW()),
('r125', 'pr112', 'u217', 'Edinburgh scenes', NOW(), NOW()),

-- Doctor Strange (pr113)
('r126', 'pr113', 'u218', 'Hong Kong scenes', NOW(), NOW()),
('r127', 'pr113', 'u219', 'Kathmandu scenes', NOW(), NOW()),

-- Guardians of the Galaxy Vol. 2 (pr114)
('r128', 'pr114', 'u210', 'Knowhere scenes', NOW(), NOW()),
('r129', 'pr114', 'u211', 'Titan scenes', NOW(), NOW()),

-- Spider-Man: Homecoming (pr115)
('r130', 'pr115', 'u201', 'New York scenes', NOW(), NOW()),
('r131', 'pr115', 'u200', 'Stark Industries scenes', NOW(), NOW()),

-- Thor: Ragnarok (pr116)
('r132', 'pr116', 'u213', 'Sakaar scenes filmed in Australia', NOW(), NOW()),
('r133', 'pr116', 'u204', 'Asgard scenes', NOW(), NOW()),

-- Black Panther (pr117)
('r134', 'pr117', 'u208', 'Wakanda scenes filmed in Atlanta and South Korea', NOW(), NOW()),
('r135', 'pr117', 'u220', 'Jabari Land scenes filmed in Morocco', NOW(), NOW()),

-- Avengers: Infinity War (pr118)
('r136', 'pr118', 'u211', 'Titan battle scenes', NOW(), NOW()),
('r137', 'pr118', 'u208', 'Wakanda battle scenes', NOW(), NOW()),

-- Ant-Man and the Wasp (pr119)
('r138', 'pr119', 'u214', 'San Francisco scenes', NOW(), NOW()),
('r139', 'pr119', 'u212', 'Quantum Realm scenes', NOW(), NOW()),

-- Captain Marvel (pr120)
('r140', 'pr120', 'u200', 'Los Angeles scenes', NOW(), NOW()),
('r141', 'pr120', 'u221', 'Space scenes', NOW(), NOW()),

-- Avengers: Endgame (pr121)
('r142', 'pr121', 'u211', 'Titan scenes', NOW(), NOW()),
('r143', 'pr121', 'u208', 'Wakanda scenes', NOW(), NOW()),

-- Spider-Man: Far From Home (pr122)
('r144', 'pr122', 'u222', 'European locations', NOW(), NOW()),
('r145', 'pr122', 'u223', 'Japan scenes', NOW(), NOW()),

-- Black Widow (pr123)
('r146', 'pr123', 'u224', 'Madripoor scenes filmed in Atlanta', NOW(), NOW()),
('r147', 'pr123', 'u225', 'New Asgard scenes filmed in Norway', NOW(), NOW()),

-- Shang-Chi and the Legend of the Ten Rings (pr124)
('r148', 'pr124', 'u218', 'Hong Kong scenes', NOW(), NOW()),
('r149', 'pr124', 'u207', 'China scenes', NOW(), NOW()),

-- Eternals (pr125)
('r150', 'pr125', 'u200', 'Global locations', NOW(), NOW()),
('r151', 'pr125', 'u203', 'London scenes', NOW(), NOW()),

-- Spider-Man: No Way Home (pr126)
('r152', 'pr126', 'u201', 'New York scenes', NOW(), NOW()),
('r153', 'pr126', 'u225', 'Multiverse scenes', NOW(), NOW()),

-- Doctor Strange in the Multiverse of Madness (pr127)
('r154', 'pr127', 'u225', 'Multiverse scenes', NOW(), NOW()),
('r155', 'pr127', 'u217', 'San Francisco scenes', NOW(), NOW()),

-- Thor: Love and Thunder (pr128)
('r156', 'pr128', 'u218', 'Space scenes', NOW(), NOW()),
('r157', 'pr128', 'u219', 'New Asgard scenes', NOW(), NOW()),

-- Black Panther: Wakanda Forever (pr129)
('r158', 'pr129', 'u208', 'Wakanda scenes', NOW(), NOW()),
('r159', 'pr129', 'u222', 'Talokan scenes', NOW(), NOW()),

-- Ant-Man and the Wasp: Quantumania (pr130)
('r160', 'pr130', 'u221', 'Quantum Realm scenes', NOW(), NOW()),
('r161', 'pr130', 'u223', 'Quantum Realm scenes', NOW(), NOW()),

-- Guardians of the Galaxy Vol. 3 (pr131)
('r162', 'pr131', 'u225', 'Space scenes', NOW(), NOW()),
('r163', 'pr131', 'u216', 'Knowhere scenes', NOW(), NOW()),

-- The Marvels (pr132)
('r164', 'pr132', 'u215', 'Space scenes', NOW(), NOW()),
('r165', 'pr132', 'u216', 'Earth scenes', NOW(), NOW()),

-- Juego de Tronos
('r200', 'pr133', 'u300', 'Estudios Titanic en Belfast - Filmación principal', NOW(), NOW()),
('r201', 'pr133', 'u301', 'Dubrovnik como Desembarco del Rey', NOW(), NOW()),
('r202', 'pr133', 'u302', 'Alcázar de Sevilla como los jardines de Dorne', NOW(), NOW()),
('r203', 'pr133', 'u303', 'Islandia como más allá del Muro', NOW(), NOW()),
('r204', 'pr133', 'u304', 'Marruecos como Pentos y Yunkai', NOW(), NOW()),
('r205', 'pr133', 'u305', 'Malta como escenas iniciales de Desembarco del Rey', NOW(), NOW()),

-- La Casa del Dragón
('r206', 'pr134', 'u300', 'Estudios Titanic en Belfast - Filmación principal', NOW(), NOW()),
('r207', 'pr134', 'u306', 'Cornwall como Rocadragón', NOW(), NOW()),
('r208', 'pr134', 'u307', 'Derbyshire como escenas de bosques', NOW(), NOW()),
('r209', 'pr134', 'u308', 'Cáceres como Desembarco del Rey', NOW(), NOW()),
('r210', 'pr134', 'u309', 'Trujillo como escenas de exteriores', NOW(), NOW()),
('r211', 'pr134', 'u310', 'Portugal como localizaciones costeras', NOW(), NOW());

-- Insertar imágenes para los rodajes (tabla rodaje_imagenes)
-- Insertar imágenes para los rodajes (tabla rodaje_imagenes)
INSERT INTO rodaje_imagenes (rodaje_id, imagen_url) VALUES
-- Imágenes para El Padrino en NY
('r001', 'https://upload.wikimedia.org/wikipedia/commons/6/6c/Mulberry_Street_NYC.jpg'),
('r001', 'https://upload.wikimedia.org/wikipedia/commons/3/3b/Little_Italy_NYC.jpg'),

-- Imágenes para El Caballero Oscuro en Chicago
('r002', 'https://upload.wikimedia.org/wikipedia/commons/0/0e/Chicago_Skyline.jpg'),
('r002', 'https://upload.wikimedia.org/wikipedia/commons/5/5f/Chicago_Street.jpg'),

-- Imágenes para Inception en París
('r005', 'https://upload.wikimedia.org/wikipedia/commons/a/a6/Paris_Metro.jpg'),
('r005', 'https://upload.wikimedia.org/wikipedia/commons/9/9e/Paris_Street.jpg'),

-- Imágenes para The Matrix en Sydney
('r007', 'https://upload.wikimedia.org/wikipedia/commons/4/4e/Sydney_CBD.jpg'),
('r007', 'https://upload.wikimedia.org/wikipedia/commons/1/1e/Sydney_Street.jpg'),

-- Imágenes para Forrest Gump en Alabama
('r008', 'https://upload.wikimedia.org/wikipedia/commons/2/2d/Alabama_Countryside.jpg'),

-- Imágenes para Django en Mississippi
('r010', 'https://upload.wikimedia.org/wikipedia/commons/3/3d/Mississippi_Plantation.jpg'),
('r010', 'https://upload.wikimedia.org/wikipedia/commons/6/6e/Mississippi_River.jpg'),

-- Imágenes para Indiana Jones en Túnez
('r011', 'https://upload.wikimedia.org/wikipedia/commons/7/7f/Tunisia_Desert.jpg'),

-- Imágenes para Jurassic Park en Costa Rica
('r013', 'https://upload.wikimedia.org/wikipedia/commons/8/8e/Costa_Rica_Jungle.jpg'),

-- Imágenes para Misión Imposible en Praga
('r014', 'https://upload.wikimedia.org/wikipedia/commons/9/9c/Prague_Old_Town.jpg'),

-- Imágenes para El club de la lucha en Wilmington
('r017', 'https://upload.wikimedia.org/wikipedia/commons/5/5d/Wilmington_NC.jpg'),
('r017', 'https://upload.wikimedia.org/wikipedia/commons/2/2b/Wilmington_Street.jpg'),

-- Titanic en Toronto
('r018', 'https://www.narcity.com/media-library/a-replica-of-the-titanic-staircase-right-an-image-of-the-titanic-sinking.jpg?id=60214318&width=1245&height=700&coordinates=2%2C0%2C3%2C0'),
('r018', 'https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F7bd8af74-e2dc-4319-bcae-0b5d45dfe29e_1480x1036.jpeg'),

-- Gladiator en Roma
('r020', 'https://upload.wikimedia.org/wikipedia/commons/5/5c/Colosseum_in_Rome.jpg'),
('r020', 'https://upload.wikimedia.org/wikipedia/commons/7/7d/Roman_Forum.jpg'),

-- Shawshank Redemption
('r022', 'https://upload.wikimedia.org/wikipedia/commons/9/9e/Mansfield_Reformatory.jpg'),

-- Godfather Part II en NYC
('r023', 'https://upload.wikimedia.org/wikipedia/commons/0/0a/NYC_Little_Italy.jpg'),

-- Lord of the Rings en NZ
('r025', 'https://upload.wikimedia.org/wikipedia/commons/8/8e/Hobbiton_Matamata.jpg'),

-- Interstellar en Islandia
('r027', 'https://upload.wikimedia.org/wikipedia/commons/6/6f/Iceland_Glacier.jpg'),

-- La La Land en LA
('r029', 'https://upload.wikimedia.org/wikipedia/commons/3/3e/Los_Angeles_Skyline.jpg'),
('r029', 'https://upload.wikimedia.org/wikipedia/commons/2/2f/Griffith_Observatory.jpg'),

-- Pulp Fiction en LA
('r031', 'https://upload.wikimedia.org/wikipedia/commons/4/4d/Los_Angeles_Street.jpg'),

-- The Matrix en Australia
('r034', 'https://upload.wikimedia.org/wikipedia/commons/0/0b/Sydney_Skyline.jpg'),

-- Juego de Tronos

-- r200 – Estudios Titanic en Belfast (Irlanda del Norte)
('r200', 'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/03/bc/05/ac/titanic-belfast.jpg?w=800&h=500&s=1'),
('r200', 'https://www.screenbie.com/wp-content/uploads/2022/08/studio-tour-juego-de-tronos.jpg'),
('r200', 'https://lossietereinos.com/wp-content/uploads/2018/03/Paint-Hall-Kings-Landing-Gate-Front-7-696x401.jpeg'),
('r200', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVCeGsvF05Gi95eSiixnr5p9u6qlDIZ-UZ9Q&s'),

-- r201 – Dubrovnik como Desembarco del Rey (Croacia)
('r201', 'https://img.remediosdigitales.com/97d3f9/revive-desembarco-del-rey-y-juego-de-tronos-paseando-por-las-calles-de-dubrovnik/1366_2000.jpg'),
('r201', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6T4atMM07JZYvVfhapHkYwCadJtC1Lj3NCg&s'),
('r201', 'https://www.marijobarcelona.com/wp-content/uploads/2016/09/6-01-copia.jpg'),
('r201', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTvjJ2VinjJUelQNYan-qABovc_NuI0H9dbFA&s'),

-- r202 – Alcázar de Sevilla como los jardines de Dorne (España)
('r202', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8H5uv61VvAa_nv8iu2d9lvPzvtH8SoJTi5g&s'),
('r202', 'https://www.alcazarsevilla.info/img/Juego%20de%20Tronos/dorne-got.jpg'),
('r202', 'https://www.alcazarsevilla.info/img/Juego%20de%20Tronos/dorne3-got.jpg'),
('r202', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRya9oFInM7PPMrWDTcnQ6ewlvE2T9ZdbLFjw&s'),

-- r203 – Islandia como más allá del Muro
('r203', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQROiWmZTD32HxgCOx1xRi-ccDfRpNbNDuTsQ&s'),
('r203', 'https://static.emol.cl/emol50/Fotos/2017/07/17/file_20170717171148.jpg'),
('r203', 'https://media.tacdn.com/media/attractions-splice-spp-674x446/07/98/bb/ea.jpg'),
('r203', 'https://i0.wp.com/www.callejeandoporelplaneta.com/wp-content/uploads/CASCADA-SVARTIFOSS.jpg?ssl=1'),

-- r204 – Marruecos como Pentos y Yunkai
('r204', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSoD6g0-BAjdO4d9oHIcm0vNAtxZFzeDuAkWw&s'),
('r204', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtzROpkpmGtjmOBEOfz-hYd5qhl_HaujD_PQ&s'),
('r204', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpuaU9cUqrPHxv27kmUPu5gFKebMzFq8023qGw3t1JB05EA0Z7pR_WajtUYMqrAREfK0Q&usqp=CAU'),
('r204', 'https://www.screenbie.com/wp-content/uploads/2022/08/Ai%CC%88t-Ben-Haddou-marruecos.png'),

-- r205 – Malta como escenas iniciales de Desembarco del Rey
('r205', 'https://visitarmalta.com/wp-content/uploads/2019/11/Azure-Window-1.jpg'),
('r205', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4gJM6a44u1GU2gHW-6AGHb9-Xxr_qTzC6VQ&s'),
('r205', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQWUO-owreh4TOAQ-bXipU71P6PcW7uiSLaQ&s'),
('r205', 'https://visitarmalta.com/wp-content/uploads/2019/11/Game-of-Thrones-Malte-2-1.jpg'),

-- La Casa del Dragón

-- r206 – Estudios Titanic en Belfast (Irlanda del Norte)
('r206', 'https://dynamic-media-cdn.tripadvisor.com/media/photo-o/03/bc/05/ac/titanic-belfast.jpg?w=800&h=500&s=1'),
('r206', 'https://www.screenbie.com/wp-content/uploads/2022/08/studio-tour-juego-de-tronos.jpg'),
('r206', 'https://lossietereinos.com/wp-content/uploads/2018/03/Paint-Hall-Kings-Landing-Gate-Front-7-696x401.jpeg'),
('r206', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVCeGsvF05Gi95eSiixnr5p9u6qlDIZ-UZ9Q&s'),

-- r207 – Cornwall como Rocadragón (Reino Unido)
('r207', 'https://lossietereinos.com/wp-content/uploads/2021/04/2_Image-from-iOS-50.jpeg'),
('r207', 'https://media-cdn.tripadvisor.com/media/photo-s/14/72/6c/bf/emozione-e-anche-un-po.jpg'),
('r207', 'https://www.talesofawanderer.com/blog/wp-content/uploads/2016/02/downhill_strand_00-1024x683.jpg'),
('r207', 'https://estaticosgn-cdn.deia.eus/clip/9dc7c505-4913-4eb3-90e7-b2a61efeb399_16-9-aspect-ratio_default_0.jpg'),

-- r208 – Derbyshire como escenas de bosques (Reino Unido)
('r208', 'https://a.travel-assets.com/findyours-php/viewfinder/images/res40/488000/488368-peak-district.jpg'),
('r208', 'https://previews.123rf.com/images/khrizmo/khrizmo1310/khrizmo131000015/23859663-vista-del-r%C3%ADo-derwent-bordeada-por-%C3%A1rboles-en-matlock-bath-en-derbyshire-inglaterra.jpg'),
('r208', 'https://media.revistaad.es/photos/668b966728ce022297600028/master/w_1600%2Cc_limit/milly-alcock-matt-smith.jpg'),
('r208', 'https://imagenes.20minutos.es/files/image_640_auto/uploads/imagenes/2024/07/29/olivia-cooke-como-alicent-en-la-casa-del-drago-n-temporada-2-episodio-7.jpeg'),

-- r209 – Cáceres como Desembarco del Rey (España)
('r209', 'https://planvex.es/web/wp-content/uploads/2022/10/house-of-the-dragon-plaza-de-san-jorge-caceres-episodio-9.jpg'),
('r209', 'https://lossietereinos.com/wp-content/uploads/2021/10/caballosrodaje1-RsOj2xWhOm5zQEiDTyrm7yK-984x608@Hoy.jpeg'),
('r209', 'https://estaticos-cdn.prensaiberica.es/clip/269e7db5-fc53-498f-8298-a09224f0ec91_source-aspect-ratio_default_0.jpg'),
('r209', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTwtCM1IHLznkgrbnoAzmyVogbeI6xh3k7kQ&s'),

-- r210 – Trujillo como escenas de exteriores (España)
('r210', 'https://visitasguiadastrujillo.es/wp-content/uploads/2023/03/Casa-del-Dragon-1024x633.jpg'),
('r210', 'https://hips.hearstapps.com/hmg-prod/images/trujillo-1633528946.jpg'),
('r210', 'https://www.hola.com/horizon/landscape/8999c7abc57f-ayy-t4fmya-t.jpg'),
('r210', 'https://media.traveler.es/photos/6137719a86b46eac7cf5a737/master/w_1600%2Cc_limit/126599.jpg'),

-- r211 – Portugal como localizaciones costeras
('r211', 'https://www.cataloniahotels.com/es/blog/wp-content/uploads/2023/08/oporto-portugal.jpg'),
('r211', 'https://img.salamancartvaldia.es/simg/2023-12/image005_231226183441.jpg'),
('r211', 'https://e00-marca.uecdn.es/assets/multimedia/imagenes/2024/07/24/17218225660088.jpg'),
('r211', 'https://phantom-marca.unidadeditorial.es/2eca16afdb3a3f514812a1845f8b565d/resize/828/f/jpg/assets/multimedia/imagenes/2024/07/24/17218225655670.jpg');

-- Insertar usuarios
INSERT INTO usuarios (id, nombre, apellido, email, username, password, rol, telefono, fecha_nacimiento, created_at, avatar) VALUES
('kvsbfjvbjr', 'Mario', 'de Domingo Álvarez', 'wolverine.mda.307@gmail.com', 'superAdmin', '$2a$12$hdb/kuYrrChi4PtOP2AFh.zx9hC7AmduUv4EBELRMWzGz63d/viUm', 'ADMINISTRADOR', '681013487', '2004-07-30', NOW(), 'https://sm.ign.com/t/ign_latam/screenshot/default/wolver_9stu.1280.jpg'),
('t4fr2yj444', 'Eva', 'Rodrígez Rodrígez', 'evarosa2rr08@gmail.com', 'evanyways_', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'ADMINISTRADOR', '626749341', '2004-10-26', NOW(), 'https://media.licdn.com/dms/image/v2/D4E03AQGz-7Rm3kuq0A/profile-displayphoto-shrink_100_100/B4EZYFUfTYHkAY-/0/1743845980367?e=2147483647&v=beta&t=6VsNpThupphWmwiZpgvE4gQJTxAGYOakLn_oW9WL77Q'),
('vjvfjshvd2', 'Andrea', 'Isabel de la Torre', 'Andrealokiloveyou@gmail.com', '_andrea.it_', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'ADMINISTRADOR', '676168344', '2003-07-31', NOW(), 'https://media.licdn.com/dms/image/v2/D4E03AQGz-7Rm3kuq0A/profile-displayphoto-shrink_400_400/B4EZYFUfTYHkAk-/0/1743845980350?e=1753315200&v=beta&t=JjODtntY9MU1i6ikUwwbjTjDpVzT3jB74Yy1KLM5t68'),
('usr00001', 'Germán', 'Fernández', 'gfernandez@gmail.com', 'germanfdez', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000001', '1995-04-12', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00002', 'Raúl', 'Seisdedos', 'rseisdedos@gmail.com', 'raul6d2', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000002', '1998-09-23', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00003', 'Víctor', 'Pérez', 'vperez@gmail.com', 'vic_perez', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000003', '2000-01-15', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00004', 'Laura', 'Gómez', 'lgomez@gmail.com', 'laurag', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000004', '1999-05-21', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00005', 'Alejandro', 'Martínez', 'amartinez@gmail.com', 'alexmtz', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000005', '2001-03-30', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00006', 'María', 'López', 'mlopez@gmail.com', 'marialpz', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000006', '1997-11-10', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00007', 'Carlos', 'Ruiz', 'cruiz@gmail.com', 'carlosruiz7', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000007', '1994-02-18', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00008', 'Lucía', 'Sánchez', 'lsanchez@gmail.com', 'luciasz', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000008', '2002-08-05', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00009', 'David', 'Moreno', 'dmoreno@gmail.com', 'davidmoreno', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000009', '1993-12-27', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00010', 'Sandra', 'Jiménez', 'sjimenez@gmail.com', 'sandrita_j', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000010', '1996-06-06', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00011', 'Daniel', 'Ortega', 'dortega@gmail.com', 'danielort', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000011', '2000-07-14', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00012', 'Patricia', 'Navarro', 'pnavarro@gmail.com', 'patri_nava', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000012', '1998-01-09', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00013', 'Jorge', 'Castro', 'jcastro@gmail.com', 'jorgecast', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000013', '1995-10-01', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00014', 'Sofía', 'Domínguez', 'sdominguez@gmail.com', 'sofiadom', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000014', '2003-03-04', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00015', 'Álvaro', 'Iglesias', 'aliglesias@gmail.com', 'alvaroi', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000015', '1999-09-12', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg'),
('usr00016', 'Elena', 'Cano', 'ecano@gmail.com', 'elenac', '$2a$12$wPceCOw9JTCZEcUyia64SePvT1RSvKj.dkYFVyKXmhNl.MKDQ0IzG', 'USUARIO', '600000016', '2001-02-19', NOW(), 'https://static.vecteezy.com/system/resources/previews/021/548/095/non_2x/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg');