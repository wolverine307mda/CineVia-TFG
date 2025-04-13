<template>
  <footer class="footer-dark pt-5 pb-4">
    <div class="container">
      <div class="row g-4">
        <!-- Logo y descripción -->
        <div class="col-lg-4 col-md-6">
          <div class="d-flex align-items-center mb-3">
            <!-- Logo SVG -->
            <img
                src="../assets/logo.svg"
                alt="Logo MovieTrip"
                width="60"
                height="60"
                class="me-3"
            >
            <h3 class="m-0 text-white">
              MovieTrip
            </h3>
          </div>

          <p class="text-light">
            Tu plataforma favorita para descubrir, compartir y disfrutar del mundo del cine.
          </p>
          <div class="d-flex mt-3">
            <a
                href="#"
                class="btn btn-sm btn-outline-light me-2"
            >
              <i class="fas fa-download me-1" /> Descargar App
            </a>
            <a
                href="#"
                class="btn btn-sm btn-purple"
            >
              <i class="fas fa-play me-1" /> Demo
            </a>
          </div>
        </div>

        <!-- Enlaces rápidos -->
        <div class="col-lg-2 col-md-6">
          <h5 class="text-uppercase text-purple mb-4">
            Explorar
          </h5>
          <ul class="list-unstyled">
            <li class="mb-2">
              <a
                  href="/movies"
                  class="text-light text-decoration-none footer-link"
              >Películas</a>
            </li>
            <li class="mb-2">
              <a
                  href="/series"
                  class="text-light text-decoration-none footer-link"
              >Series</a>
            </li>
            <li class="mb-2">
              <a
                  href="/genres"
                  class="text-light text-decoration-none footer-link"
              >Géneros</a>
            </li>
            <li class="mb-2">
              <a
                  href="/trending"
                  class="text-light text-decoration-none footer-link"
              >Tendencias</a>
            </li>
            <li class="mb-2">
              <a
                  href="/calendar"
                  class="text-light text-decoration-none footer-link"
              >Estrenos</a>
            </li>
          </ul>
        </div>

        <!-- Información corporativa -->
        <div class="col-lg-2 col-md-6">
          <h5 class="text-uppercase text-purple mb-4">
            Empresa
          </h5>
          <ul class="list-unstyled">
            <li class="mb-2">
              <a
                  href="/about"
                  class="text-light text-decoration-none footer-link"
              >Quiénes somos</a>
            </li>
            <li class="mb-2">
              <a
                  href="/team"
                  class="text-light text-decoration-none footer-link"
              >Nuestro equipo</a>
            </li>
            <li class="mb-2">
              <a
                  href="/careers"
                  class="text-light text-decoration-none footer-link"
              >Carreras</a>
            </li>
            <li class="mb-2">
              <a
                  href="/press"
                  class="text-light text-decoration-none footer-link"
              >Prensa</a>
            </li>
            <li class="mb-2">
              <a
                  href="/partners"
                  class="text-light text-decoration-none footer-link"
              >Socios</a>
            </li>
          </ul>
        </div>

        <!-- Legal y contacto -->
        <div class="col-lg-2 col-md-6">
          <h5 class="text-uppercase text-purple mb-4">
            Legal
          </h5>
          <ul class="list-unstyled">
            <li class="mb-2">
              <a
                  href="/terms"
                  class="text-light text-decoration-none footer-link"
              >Términos</a>
            </li>
            <li class="mb-2">
              <a
                  href="/privacy"
                  class="text-light text-decoration-none footer-link"
              >Privacidad</a>
            </li>
            <li class="mb-2">
              <a
                  href="/cookies"
                  class="text-light text-decoration-none footer-link"
              >Cookies</a>
            </li>
            <li class="mb-2">
              <a
                  href="/copyright"
                  class="text-light text-decoration-none footer-link"
              >Copyright</a>
            </li>
            <li class="mb-2">
              <a
                  href="/guidelines"
                  class="text-light text-decoration-none footer-link"
              >Directrices</a>
            </li>
          </ul>
        </div>

        <!-- Contacto y redes -->
        <div class="col-lg-2 col-md-6">
          <h5 class="text-uppercase text-purple mb-4">
            Contacto
          </h5>
          <ul class="list-unstyled">
            <li class="mb-3">
              <a
                  href="mailto:info@movietrip.com"
                  class="text-light text-decoration-none footer-link"
              ><i class="fas fa-envelope me-2" /> info@movietrip.com</a>
            </li>
            <li class="mb-3">
              <a
                  href="tel:+123456789"
                  class="text-light text-decoration-none footer-link"
              ><i class="fas fa-phone me-2" /> +1 234 567 89</a>
            </li>
            <li class="mb-3 text-light">
              <i class="fas fa-map-marker-alt me-2 text-light text-decoration-none" /> Hollywood, CA
            </li>
          </ul>
          <div class="social-icons mt-4">
            <a
                href="#"
                class="text-light me-3"
            ><i class="fab fa-facebook-f" /></a>
            <a
                href="#"
                class="text-light me-3"
            ><i class="fab fa-twitter" /></a>
            <a
                href="#"
                class="text-light me-3"
            ><i class="fab fa-instagram" /></a>
            <a
                href="#"
                class="text-light me-3"
            ><i class="fab fa-linkedin-in" /></a>
            <a
                href="#"
                class="text-light"
            ><i class="fab fa-youtube" /></a>
          </div>
        </div>
      </div>

      <hr class="my-4 border-light opacity-10">

      <div class="row align-items-center">
        <div class="col-md-6 text-center text-md-start">
          <p class="m-0 text-light">
            &copy; 2025 MovieTrip. Todos los derechos reservados.
          </p>
        </div>
        <div class="col-md-6 text-center text-md-end">
          <div class="payment-methods">
            <i class="fab fa-cc-visa text-light me-2" />
            <i class="fab fa-cc-mastercard text-light me-2" />
            <i class="fab fa-cc-paypal text-light me-2" />
            <i class="fab fa-cc-apple-pay text-light" />
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter'
}
</script>

<style scoped>
.footer-dark {
  background: linear-gradient(135deg, #0f0c29 0%, #07051a 100%);
  border-top: 4px solid #a78bfa;
}

.text-purple {
  color: #a78bfa;
}

.btn-purple {
  background-color: #a78bfa;
  color: #0f0c29;
  border: none;
}

.btn-purple:hover {
  background-color: #c4b5fd;
  color: #0f0c29;
}

.footer-link {
  transition: all 0.3s ease;
  display: inline-block;
}

.footer-link:hover {
  color: #a78bfa !important;
  transform: translateX(5px);
}

.social-icons a {
  font-size: 1.1rem;
  transition: all 0.3s ease;
  display: inline-block;
}

.social-icons a:hover {
  color: #a78bfa !important;
  transform: translateY(-3px);
}

.payment-methods i {
  font-size: 1.5rem;
  opacity: 0.7;
  transition: all 0.3s ease;
}

.payment-methods i:hover {
  opacity: 1;
  color: #a78bfa;
}

.text-light {
  color: #f8fafc !important;
}

.btn-outline-light:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
</style>
