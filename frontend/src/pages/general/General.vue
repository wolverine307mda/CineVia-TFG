<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode"/>

  <div class="general-container" :class="{ 'dark-mode': darkMode }">

    <!-- Sección de Producciones -->
    <section class="category-section">
      <div class="section-header">
        <h2 class="section-title">Producciones</h2>
        <div class="section-controls">
          <button class="nav-btn prev" @click="scrollLeft('productions')" aria-label="Anterior">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn next" @click="scrollRight('productions')" aria-label="Siguiente">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>

      <div class="items-container" ref="productionsContainer">
        <div class="items-scroll">
          <div v-for="(production, index) in productions" :key="'prod-'+index" class="item-card">
            <div class="card-image">
              <img :src="production.imagen" :alt="production.titulo" loading="lazy">
              <button class="favorite-btn" @click="toggleFavorite(production.id, 'PRODUCCION')" aria-label="Añadir a favoritos">
                <i :class="isFavorite(production.id, 'PRODUCCION') ? 'fas fa-heart' : 'far fa-heart'"></i>
              </button>
              <div class="card-badge">{{ production.tipo }}</div>
            </div>
            <div class="card-body">
              <h3>{{ production.titulo }}</h3>
              <div class="card-meta">
                <span><i class="fas fa-calendar-alt"></i> {{ production.estreno.getFullYear() }}</span>
                <span><i class="fas fa-clock"></i> {{ production.duracion }} min</span>
              </div>
              <p class="card-description">{{ truncateText(production.sinopsis, 100) }}</p>
              <div class="card-footer">
                <span class="age-rating">{{ production.clasificacionEdad }}+</span>
                <router-link :to="`/produccion/${production.id}`" class="details-link">
                  Ver más <i class="fas fa-arrow-right"></i>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
      <button class="view-more-btn" @click="viewMore('productions')">
        Ver más producciones <i class="fas fa-chevron-right"></i>
      </button>
    </section>

    <!-- Sección de Sagas -->
    <section class="category-section">
      <div class="section-header">
        <h2 class="section-title">Sagas</h2>
        <div class="section-controls">
          <button class="nav-btn prev" @click="scrollLeft('sagas')" aria-label="Anterior">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn next" @click="scrollRight('sagas')" aria-label="Siguiente">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>

      <div class="items-container" ref="sagasContainer">
        <div class="items-scroll">
          <div v-for="(saga, index) in sagas" :key="'saga-'+index" class="item-card">
            <div class="card-image saga-image">
              <img :src="saga.imagen || getSagaImage(saga)" :alt="saga.nombre" loading="lazy">
              <div class="saga-overlay">
                <div class="saga-info">
                  <h3>{{ saga.nombre }}</h3>
                  <p>{{ saga.isAcabada ? 'Completada' : 'En progreso' }}</p>
                  <span>{{ getSagaProductionsCount(saga.id) }} películas</span>
                </div>
              </div>
            </div>
            <div class="card-body">
              <h3>{{ saga.nombre }}</h3>
              <p class="card-description">{{ truncateText(saga.descripción, 100) }}</p>
              <div class="card-footer">
                <span>{{ saga.fechaInicio.getFullYear() }} - {{ saga.isAcabada ? saga.fechaFin.getFullYear() : 'Actual' }}</span>
                <router-link :to="`/saga/${saga.id}`" class="details-link">
                  Explorar <i class="fas fa-arrow-right"></i>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
      <button class="view-more-btn" @click="viewMore('sagas')">
        Ver más sagas <i class="fas fa-chevron-right"></i>
      </button>
    </section>

    <!-- Sección de Profesionales -->
    <section class="category-section">
      <div class="section-header">
        <h2 class="section-title">Profesionales</h2>
        <div class="section-controls">
          <button class="nav-btn prev" @click="scrollLeft('professionals')" aria-label="Anterior">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn next" @click="scrollRight('professionals')" aria-label="Siguiente">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>

      <div class="items-container" ref="professionalsContainer">
        <div class="items-scroll">
          <div v-for="(professional, index) in professionals" :key="'prof-'+index" class="item-card">
            <div class="card-image professional-image">
              <img :src="professional.foto || 'https://via.placeholder.com/300x450?text='+professional.nombre" :alt="professional.nombre" loading="lazy">
              <button class="favorite-btn" @click="toggleFavorite(professional.id, 'PARTICIPANTE')" aria-label="Añadir a favoritos">
                <i :class="isFavorite(professional.id, 'PARTICIPANTE') ? 'fas fa-heart' : 'far fa-heart'"></i>
              </button>
            </div>
            <div class="card-body">
              <h3>{{ professional.nombre }}</h3>
              <div class="professional-roles">
                  <span v-for="(role, i) in getProfessionalRoles(professional.id)" :key="i" class="role-tag">
                    {{ role }}
                  </span>
              </div>
              <p class="card-description">{{ truncateText(professional.biografía, 100) }}</p>
              <div class="card-footer">
                <span>{{ calculateAge(professional.fechaNacimiento) }} años</span>
                <router-link :to="`/profesional/${professional.id}`" class="details-link">
                  Filmografía <i class="fas fa-arrow-right"></i>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
      <button class="view-more-btn" @click="viewMore('professionals')">
        Ver más profesionales <i class="fas fa-chevron-right"></i>
      </button>
    </section>

  </div>

  <Footer :dark-mode="darkMode" />

</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: 'GeneralPage',
  components: {Footer, Header},
  data() {
    return {
      darkMode: false,
      productions: [
        {
          id: '1',
          titulo: 'El Padrino',
          tipo: 'PELICULA',
          estreno: new Date(1972, 2, 24),
          duracion: 175,
          sinopsis: 'La historia de la familia Corleone, una dinastía de la mafia italiana en Nueva York.',
          imagen: 'https://image.tmdb.org/t/p/w500/r4gnMXoY1efvaolNDjn3nj4046S.jpg',
          información: 'Filmada en Nueva York y Sicilia',
          categoría: 'DRAMA',
          clasificacionEdad: 18,
          sagaId: null
        },
        {
          id: '2',
          titulo: 'Interstellar',
          tipo: 'PELICULA',
          estreno: new Date(2014, 10, 7),
          duracion: 169,
          sinopsis: 'Un grupo de exploradores viaja a través de un agujero de gusano en el espacio en busca de un nuevo hogar para la humanidad.',
          imagen: 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg',
          información: 'Rodada en Islandia, Canadá y EE.UU.',
          categoría: 'CIENCIA FICCIÓN',
          clasificacionEdad: 12,
          sagaId: null
        },
        {
          id: '3',
          titulo: 'Stranger Things',
          tipo: 'SERIE',
          estreno: new Date(2016, 6, 15),
          duracion: 51,
          sinopsis: 'Un grupo de niños se enfrenta a fenómenos sobrenaturales en un pequeño pueblo.',
          imagen: 'https://image.tmdb.org/t/p/w500/x2LSRK2Cm7MZhjluni1msVJ3wDF.jpg',
          información: 'Rodada en Georgia, EE.UU.',
          categoría: 'FANTASÍA',
          clasificacionEdad: 16,
          sagaId: null
        },
        {
          id: '4',
          titulo: 'Amélie',
          tipo: 'PELICULA',
          estreno: new Date(2001, 3, 25),
          duracion: 122,
          sinopsis: 'Una joven camarera parisina decide cambiar la vida de quienes la rodean usando pequeñas acciones mágicas.',
          imagen: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSM6YyXGJdYofrDduqekgxxqDtBIteFlJQsig&s',
          información: 'Filmada en Montmartre, París',
          categoría: 'ROMANCE',
          clasificacionEdad: 7,
          sagaId: null
        },
        {
          id: '5',
          titulo: 'Breaking Bad',
          tipo: 'SERIE',
          estreno: new Date(2008, 0, 20),
          duracion: 47,
          sinopsis: 'Un profesor de química con cáncer terminal se convierte en fabricante de metanfetamina.',
          imagen: 'https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg',
          información: 'Rodada en Albuquerque, Nuevo México',
          categoría: 'DRAMA',
          clasificacionEdad: 18,
          sagaId: null
        },
        {
          id: '6',
          titulo: 'El viaje de Chihiro',
          tipo: 'PELICULA',
          estreno: new Date(2001, 6, 20),
          duracion: 125,
          sinopsis: 'Una niña queda atrapada en un mundo de espíritus y debe encontrar la forma de liberar a sus padres.',
          imagen: 'https://image.tmdb.org/t/p/w500/dL11DBPcRhWWnJcFXl9A07MrqTI.jpg',
          información: 'Animación japonesa de Studio Ghibli',
          categoría: 'FANTASÍA',
          clasificacionEdad: 7,
          sagaId: null
        },
        {
          id: '7',
          titulo: 'La La Land',
          tipo: 'PELICULA',
          estreno: new Date(2016, 11, 9),
          duracion: 128,
          sinopsis: 'Una aspirante a actriz y un músico de jazz luchan por alcanzar sus sueños en Los Ángeles.',
          imagen: 'https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg',
          información: 'Rodada en múltiples localizaciones de Los Ángeles',
          categoría: 'MUSICAL',
          clasificacionEdad: 12,
          sagaId: null
        },
        {
          id: '8',
          titulo: 'El Señor de los Anillos: La Comunidad del Anillo',
          tipo: 'PELICULA',
          estreno: new Date(2001, 11, 19),
          duracion: 178,
          sinopsis: 'Un hobbit inicia un peligroso viaje para destruir un anillo que podría acabar con la Tierra Media.',
          imagen: 'https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg',
          información: 'Rodada en Nueva Zelanda',
          categoría: 'FANTASÍA',
          clasificacionEdad: 13,
          sagaId: 'LOTR'
        }
      ],
      sagas: [
        {
          id: '1',
          nombre: 'El Señor de los Anillos',
          descripción: 'La épica trilogía basada en las novelas de J.R.R. Tolkien sobre la Tierra Media.',
          isAcabada: true,
          fechaInicio: new Date(2001, 0, 1),
          fechaFin: new Date(2003, 0, 1),
          imagen: 'https://upload.wikimedia.org/wikipedia/commons/7/7d/El_Se%C3%B1or_de_los_Anillos_lectura.jpg'
        },
        {
          id: '2',
          nombre: 'Harry Potter',
          descripción: 'La saga de magia y aventuras de J.K. Rowling que sigue a un joven mago y sus amigos en Hogwarts.',
          isAcabada: true,
          fechaInicio: new Date(2001, 10, 10),
          fechaFin: new Date(2011, 6, 15),
          imagen: 'https://static.eldiario.es/clip/5d8732e3-d173-41ba-9203-2407686f323e_16-9-discover-aspect-ratio_default_0.jpg'
        },
        {
          id: '3',
          nombre: 'Star Wars',
          descripción: 'La legendaria franquicia de ciencia ficción creada por George Lucas que narra la lucha entre el bien y el mal en una galaxia muy, muy lejana.',
          isAcabada: false,
          fechaInicio: new Date(1977, 4, 25),
          fechaFin: null,
          imagen: 'https://hips.hearstapps.com/hmg-prod/images/el-despertar-de-la-fuerza-1575448990.jpg?crop=1xw:1xh;center,top&resize=980:*'
        },
        {
          id: '4',
          nombre: 'The Dark Knight Trilogy',
          descripción: 'La trilogía de Batman dirigida por Christopher Nolan, con Christian Bale como el Caballero Oscuro.',
          isAcabada: true,
          fechaInicio: new Date(2005, 5, 15),
          fechaFin: new Date(2012, 6, 20),
          imagen: 'https://m.media-amazon.com/images/I/718QGlCrp6L._AC_UF894,1000_QL80_.jpg'
        },
        {
          id: '5',
          nombre: 'The Hunger Games',
          descripción: 'La saga post-apocalíptica de Suzanne Collins que sigue a Katniss Everdeen en su lucha por sobrevivir en un mundo brutal.',
          isAcabada: true,
          fechaInicio: new Date(2012, 2, 23),
          fechaFin: new Date(2015, 10, 20),
          imagen: 'https://m.media-amazon.com/images/I/71WSzS6zvCL._AC_UF894,1000_QL80_.jpg'
        },
        {
          id: '6',
          nombre: 'Jurassic Park',
          descripción: 'La saga que comienza con la historia de un parque de dinosaurios clonados y los problemas que surgen cuando estos animales se liberan.',
          isAcabada: false,
          fechaInicio: new Date(1993, 5, 11),
          fechaFin: null,
          imagen: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStWg20oWjrdvNt8UjMEZ9_TcyGc41oRvf-9w&s'
        },
        {
          id: '7',
          nombre: 'Pirates of the Caribbean',
          descripción: 'Una serie de películas sobre las aventuras del capitán Jack Sparrow y otros piratas en el Caribe.',
          isAcabada: false,
          fechaInicio: new Date(2003, 6, 9),
          fechaFin: null,
          imagen: 'https://wp.thenewdaily.com.au/wp-content/uploads/2014/09/newdaily_020914_pirates.jpg?resize=1313,876&quality=90'
        },
        {
          id: '8',
          nombre: 'Indiana Jones',
          descripción: 'Las épicas aventuras del arqueólogo Indiana Jones, enfrentándose a nazis, criaturas sobrenaturales y antiguos misterios.',
          isAcabada: false,
          fechaInicio: new Date(1981, 5, 12),
          fechaFin: null,
          imagen: 'https://media.revistagq.com/photos/5f521fbe2692b780329d4859/16:9/w_2560%2Cc_limit/indiana-jones.jpg'
        }
      ],
      professionals: [
        {
          id: '1',
          nombre: 'Francis Ford Coppola',
          foto: 'https://m.media-amazon.com/images/M/MV5BMTM5NDU3OTgyNV5BMl5BanBnXkFtZTcwMzQxODA0NA@@._V1_.jpg',
          fechaNacimiento: new Date(1939, 3, 7),
          lugarNacimiento: 'Detroit, Michigan, USA',
          biografía: 'Director, productor y guionista estadounidense, conocido por El Padrino y Apocalypse Now.'
        },
        {
          id: '2',
          nombre: 'Steven Spielberg',
          foto: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQjDEn3p46OtGOvGlWDlbKGGh2dYUh5gvypLcn1uswZ7P5UdfgA_WmKXBOpT5oSD8JJ6G-abw_Bdaa6YcNKp7x0ng',
          fechaNacimiento: new Date(1946, 11, 18),
          lugarNacimiento: 'Cincinnati, Ohio, USA',
          biografía: 'Director y productor icónico, creador de clásicos como E.T., Jurassic Park y Schindler’s List.'
        },
        {
          id: '3',
          nombre: 'Meryl Streep',
          foto: 'https://m.media-amazon.com/images/M/MV5BMTU4Mjk5MDExOF5BMl5BanBnXkFtZTcwOTU1MTMyMw@@._V1_FMjpg_UX1000_.jpg',
          fechaNacimiento: new Date(1949, 5, 22),
          lugarNacimiento: 'Summit, New Jersey, USA',
          biografía: 'Actriz reconocida por su versatilidad y por múltiples premios Oscar a lo largo de su carrera.'
        },
        {
          id: '4',
          nombre: 'Leonardo DiCaprio',
          foto: 'https://encrypted-tbn2.gstatic.com/licensed-image?q=tbn:ANd9GcSwLp-COysT7ukjE2mk6WglJk2dHvB5SI3JAjfUyUYFl7KNo6VuHu_hCeE6b9v0vgm-u6ZMm2PWqEv1S_g',
          fechaNacimiento: new Date(1974, 10, 11),
          lugarNacimiento: 'Los Angeles, California, USA',
          biografía: 'Actor y productor, conocido por Titanic, Inception y The Revenant.'
        },
        {
          id: '5',
          nombre: 'Kathryn Bigelow',
          foto: 'https://upload.wikimedia.org/wikipedia/commons/6/6c/Kathryn_Bigelow_by_David_Shankbone.jpg',
          fechaNacimiento: new Date(1951, 10, 27),
          lugarNacimiento: 'San Carlos, California, USA',
          biografía: 'Directora y productora, ganadora del Oscar por The Hurt Locker.'
        },
        {
          id: '6',
          nombre: 'Christopher Nolan',
          foto: 'https://es.web.img3.acsta.net/pictures/14/10/30/10/59/215487.jpg',
          fechaNacimiento: new Date(1970, 6, 30),
          lugarNacimiento: 'Londres, Inglaterra',
          biografía: 'Director, guionista y productor británico, conocido por Inception, Interstellar y The Dark Knight.'
        },
        {
          id: '7',
          nombre: 'Emma Stone',
          foto: 'https://es.web.img3.acsta.net/pictures/15/05/15/16/30/134942.jpg',
          fechaNacimiento: new Date(1988, 10, 6),
          lugarNacimiento: 'Scottsdale, Arizona, USA',
          biografía: 'Actriz ganadora del Oscar, conocida por La La Land, The Favourite y Poor Things.'
        },
        {
          id: '8',
          nombre: 'Martin Scorsese',
          foto: 'https://cdn.britannica.com/76/156176-050-90A36E79/Martin-Scorsese-2008.jpg',
          fechaNacimiento: new Date(1942, 10, 17),
          lugarNacimiento: 'New York City, New York, USA',
          biografía: 'Director y productor legendario, autor de Goodfellas, The Irishman y Taxi Driver.'
        }
      ],
      participaciones: [
        {
          id: '1',
          produccionId: '1',
          ProfesionalId: '1',
          rol: 'DIRECTOR',
          papel: 'Director'
        },
        // Más participaciones...
      ],
      favoritos: []
    }
  },
  created() {
    // Verificar preferencia de modo oscuro
    const savedMode = localStorage.getItem('darkMode');
    if (savedMode) {
      this.darkMode = savedMode === 'true';
    } else if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      this.darkMode = true;
    }

    this.generateSampleData();
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },
    scrollLeft(section) {
      const container = this.$refs[`${section}Container`];
      container.scrollBy({ left: -300, behavior: 'smooth' });
    },
    scrollRight(section) {
      const container = this.$refs[`${section}Container`];
      container.scrollBy({ left: 300, behavior: 'smooth' });
    },
    viewMore(section) {
      // En una aplicación real, esto cargaría más elementos de la API
      alert(`Cargando más ${section}...`);
    },
    getSagaImage(saga) {
      const production = this.productions.find(p => p.sagaId === saga.id);
      return production ? production.imagen : 'https://via.placeholder.com/300x450?text=' + saga.nombre;
    },
    getSagaProductionsCount(sagaId) {
      return this.productions.filter(p => p.sagaId === sagaId).length;
    },
    getProfessionalRoles(professionalId) {
      const roles = new Set();
      this.participaciones.forEach(p => {
        if (p.ProfesionalId === professionalId) {
          roles.add(p.rol);
        }
      });
      return Array.from(roles);
    },
    truncateText(text, length) {
      if (!text) return '';
      return text.length > length ? text.substring(0, length) + '...' : text;
    },
    calculateAge(birthDate) {
      const today = new Date();
      let age = today.getFullYear() - birthDate.getFullYear();
      const m = today.getMonth() - birthDate.getMonth();
      if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      return age;
    },
    toggleFavorite(id, type) {
      const index = this.favoritos.findIndex(f => f.referenciaId === id && f.tipo === type);
      if (index >= 0) {
        this.favoritos.splice(index, 1);
      } else {
        this.favoritos.push({
          id: Date.now().toString(),
          usuarioId: 'current-user',
          tipo: type,
          referenciaId: id,
          fecha: new Date()
        });
      }
    },
    isFavorite(id, type) {
      return this.favoritos.some(f => f.referenciaId === id && f.tipo === type);
    },
    generateSampleData() {
      // Generar datos de ejemplo para demostración
      const sampleProductions = [
        { titulo: 'Pulp Fiction', tipo: 'PELICULA', estreno: new Date(1994, 9, 14), duracion: 154 },
        { titulo: 'The Dark Knight', tipo: 'PELICULA', estreno: new Date(2008, 6, 18), duracion: 152 },
        { titulo: 'Inception', tipo: 'PELICULA', estreno: new Date(2010, 6, 16), duracion: 148 },
        { titulo: 'Breaking Bad', tipo: 'SERIE', estreno: new Date(2008, 0, 20), duracion: 49 },
        { titulo: 'The Shawshank Redemption', tipo: 'PELICULA', estreno: new Date(1994, 8, 23), duracion: 142 },
        { titulo: 'The Godfather: Part II', tipo: 'PELICULA', estreno: new Date(1974, 11, 20), duracion: 202 },
        { titulo: 'Stranger Things', tipo: 'SERIE', estreno: new Date(2016, 6, 15), duracion: 51 }
      ];

      const sampleSagas = [
        { nombre: 'Star Wars', isAcabada: false, fechaInicio: new Date(1977, 4, 25) },
        { nombre: 'Harry Potter', isAcabada: true, fechaInicio: new Date(2001, 10, 16), fechaFin: new Date(2011, 6, 15) },
        { nombre: 'The Avengers', isAcabada: false, fechaInicio: new Date(2012, 3, 11) },
        { nombre: 'Toy Story', isAcabada: false, fechaInicio: new Date(1995, 10, 22) },
        { nombre: 'James Bond', isAcabada: false, fechaInicio: new Date(1962, 9, 5) },
        { nombre: 'The Matrix', isAcabada: false, fechaInicio: new Date(1999, 2, 31) },
        { nombre: 'Indiana Jones', isAcabada: false, fechaInicio: new Date(1981, 5, 12) }
      ];

      const sampleProfessionals = [
        { nombre: 'Quentin Tarantino', fechaNacimiento: new Date(1963, 2, 27) },
        { nombre: 'Christopher Nolan', fechaNacimiento: new Date(1970, 6, 30) },
        { nombre: 'Martin Scorsese', fechaNacimiento: new Date(1942, 10, 17) },
        { nombre: 'Steven Spielberg', fechaNacimiento: new Date(1946, 11, 18) },
        { nombre: 'Alfred Hitchcock', fechaNacimiento: new Date(1899, 7, 13) },
        { nombre: 'Stanley Kubrick', fechaNacimiento: new Date(1928, 6, 26) },
        { nombre: 'Hayao Miyazaki', fechaNacimiento: new Date(1941, 0, 5) }
      ];

      // Añadir datos de ejemplo si no hay suficientes
      if (this.productions.length < 8) {
        this.productions = [...this.productions, ...sampleProductions.slice(0, 8 - this.productions.length)];
      }

      if (this.sagas.length < 8) {
        this.sagas = [...this.sagas, ...sampleSagas.slice(0, 8 - this.sagas.length)];
      }

      if (this.professionals.length < 8) {
        this.professionals = [...this.professionals, ...sampleProfessionals.slice(0, 8 - this.professionals.length)];
      }
    }
  }
}
</script>

<style scoped>
:root {
  --color-primary: #6c5ce7;
  --color-primary-light: #a29bfe;
  --color-primary-dark: #5649c0;
  --color-secondary: #f8f9fa;
  --color-text: #2d3436;
  --color-text-light: #f8f9fa;
  --color-bg: #ffffff;
  --color-bg-dark: #1a1a2e;
  --color-card: #ffffff;
  --color-card-dark: #2a2a3a;
  --color-overlay: rgba(0, 0, 0, 0.7);
  --color-overlay-dark: rgba(0, 0, 0, 0.8);
  --transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.dark-mode {
  --color-secondary: #2a2a3a;
  --color-text: #e9ecef;
  --color-bg: #121212;
  --color-card: #2a2a3a;
  --color-overlay: rgba(0, 0, 0, 0.8);
}

.general-container {
  padding: 2rem;
  color: var(--color-text);
  transition: var(--transition);
  min-height: 100vh;
  position: relative;
  background-image: url("https://images.unsplash.com/photo-1482686115713-0fbcaced6e28?q=80&w=2067&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
  overflow: hidden;
}

.general-container::before {
  content: "";
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.55);
  z-index: 0;
}

.general-container > * {
  position: relative;
  z-index: 1;
}

/* Botón de modo oscuro */
.toggle-dark {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 0.8rem 1.2rem;
  border-radius: 50px;
  background-color: var(--color-primary);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  font-weight: 600;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: var(--transition);
  gap: 0.5rem;
}

.toggle-dark:hover {
  transform: translateY(-2px);
  background-color: var(--color-primary-dark);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3);
}

/* Secciones */
.category-section {
  margin: 3rem 0;
  position: relative;
  padding: 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.dark-mode .category-section {
  background: rgba(15, 15, 25, 0.7);
  border-color: rgba(255, 255, 255, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 2rem;
  font-weight: 700;
  color: white;
  position: relative;
  display: inline-block;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 50px;
  height: 4px;
  background: linear-gradient(to right, var(--color-primary), var(--color-primary-light));
  border-radius: 2px;
}

.section-controls {
  display: flex;
  gap: 0.5rem;
}

.nav-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--color-primary);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: var(--transition);
}

.nav-btn:hover {
  background-color: var(--color-primary-dark);
  transform: scale(1.1);
}

.nav-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Contenedor de items con scroll horizontal */
.items-container {
  width: 100%;
  overflow-x: auto;
  padding-bottom: 1rem;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
}

.items-container::-webkit-scrollbar {
  display: none; /* Chrome/Safari/Opera */
}

.items-scroll {
  display: inline-flex;
  gap: 1.5rem;
  padding: 0.5rem;
}

/* Tarjetas de items */
.item-card {
  width: 280px;
  background-color: var(--color-card);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: var(--transition);
  flex-shrink: 0;
}

.dark-mode .item-card {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.item-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.card-image {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: var(--transition);
}

.item-card:hover .card-image img {
  transform: scale(1.05);
}

.favorite-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: var(--transition);
  z-index: 2;
}

.favorite-btn:hover {
  background-color: var(--color-primary);
}

.favorite-btn i.fas {
  color: #ff4757;
}

.card-badge {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: bold;
  text-transform: uppercase;
  z-index: 2;
}

.card-body {
  padding: 1.2rem;
}

.item-card h3 {
  margin: 0 0 0.5rem;
  font-size: 1.2rem;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-meta {
  display: flex;
  gap: 0.8rem;
  margin-bottom: 0.8rem;
  font-size: 0.85rem;
  color: var(--color-text);
  opacity: 0.8;
}

.card-meta i {
  margin-right: 0.3rem;
  color: var(--color-primary);
}

.card-description {
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 1.2rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.age-rating {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background-color: var(--color-primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 0.8rem;
}

.details-link {
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 600;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  transition: var(--transition);
}

.details-link:hover {
  color: var(--color-primary-dark);
}

.details-link i {
  font-size: 0.8rem;
}

/* Estilos específicos para sagas */
.saga-image {
  height: 180px;
}

.saga-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to top, var(--color-overlay), transparent);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 1rem;
  color: white;
  opacity: 0;
  transition: var(--transition);
}

.item-card:hover .saga-overlay {
  opacity: 1;
}

.saga-info {
  transform: translateY(20px);
  transition: var(--transition);
}

.item-card:hover .saga-info {
  transform: translateY(0);
}

.saga-info h3 {
  color: white;
  margin-bottom: 0.3rem;
}

.saga-info p {
  margin: 0;
  font-size: 0.9rem;
}

.saga-info span {
  font-size: 0.8rem;
  opacity: 0.9;
}

/* Estilos específicos para profesionales */
.professional-image {
  height: 180px;
}

.professional-image img {
  object-fit: cover;
}

.professional-roles {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 0.8rem;
}

.role-tag {
  background-color: rgba(108, 92, 231, 0.2);
  color: var(--color-primary);
  padding: 0.3rem 0.6rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
}

/* Botón Ver Más */
.view-more-btn {
  display: block;
  width: 100%;
  max-width: 300px;
  margin: 1.5rem auto 0;
  padding: 0.8rem 1.5rem;
  background-color: transparent;
  color: var(--color-primary);
  border: 2px solid var(--color-primary);
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition);
  text-align: center;
}

.view-more-btn:hover {
  background-color: var(--color-primary);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(108, 92, 231, 0.3);
}

/* Responsive */
@media (max-width: 768px) {
  .general-container {
    padding: 1.5rem;
  }

  .section-title {
    font-size: 1.7rem;
  }

  .item-card {
    width: 240px;
  }

  .toggle-dark {
    top: 10px;
    right: 10px;
    padding: 0.6rem 1rem;
    font-size: 0.9rem;
  }
}

@media (max-width: 480px) {
  .general-container {
    padding: 1rem;
  }

  .section-title {
    font-size: 1.5rem;
  }

  .section-controls {
    gap: 0.3rem;
  }

  .nav-btn {
    width: 34px;
    height: 34px;
    font-size: 0.9rem;
  }

  .item-card {
    width: 200px;
  }

  .card-image {
    height: 150px;
  }
}

.general-container {
  background-color: #f9f9f9;
  color: #333;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.general-container.dark-mode {
  background-color: #121212;
  color: #e0e0e0;
}

/* Botón de modo oscuro */
.toggle-dark {
  background: none;
  border: none;
  color: inherit;
  font-size: 1rem;
  cursor: pointer;
  margin: 1rem;
}

/* Tarjetas */
.item-card {
  background-color: #ffffff;
  color: #333;
  border-radius: 0.5rem;
  overflow: hidden;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.dark-mode .item-card {
  background-color: #1e1e1e;
  color: #e0e0e0;
}

/* Imagen y overlays */
.card-image img,
.professional-image img,
.saga-image img {
  filter: brightness(1);
  transition: filter 0.3s ease;
}

.dark-mode .card-image img,
.dark-mode .professional-image img,
.dark-mode .saga-image img {
  filter: brightness(0.8);
}

/* Botones de navegación */
.nav-btn {
  background: #e0e0e0;
  color: #333;
  border: none;
  padding: 0.5rem;
  border-radius: 0.25rem;
  transition: background 0.3s ease, color 0.3s ease;
}

.dark-mode .nav-btn {
  background: #333;
  color: #e0e0e0;
}

/* Vínculos */
.details-link {
  color: #007bff;
  text-decoration: none;
  transition: color 0.3s ease;
}

.dark-mode .details-link {
  color: #90caf9;
}

/* Botón "Ver más" */
.view-more-btn {
  background-color: #007bff;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  margin: 1rem auto;
  display: block;
  border-radius: 0.25rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.dark-mode .view-more-btn {
  background-color: rgba(63, 63, 63, 0.27);
}

/* Otros detalles */
.card-footer,
.card-meta,
.card-description,
.section-title,
.section-header {
  transition: color 0.3s ease;
}

.dark-mode .card-footer,
.dark-mode .card-meta,
.dark-mode .card-description,
.dark-mode .section-title,
.dark-mode .section-header {
  color: #ccc;
}

/* Botón Ver Más */
.view-more-btn {
  display: block;
  width: fit-content;
  margin: 1.5rem 0 0 auto;
  padding: 0.8rem 1.8rem;
  background: linear-gradient(90deg, var(--color-primary), var(--color-primary-light));
  color: white;
  border: none;
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition);
  text-align: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(108, 92, 231, 0.4);
}

.view-more-btn::after {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: 0.5s;
}

.view-more-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(108, 92, 231, 0.6);
}

.view-more-btn:hover::after {
  left: 100%;
}

.view-more-btn i {
  margin-left: 0.5rem;
  transition: var(--transition);
}

.view-more-btn:hover i {
  transform: translateY(2px);
}
</style>