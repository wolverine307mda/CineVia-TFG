// cypress/e2e/homepage.spec.js

describe('Página de inicio - MovieTrip', () => {
    beforeEach(() => {
        cy.visit('/');
    });

    context('Navegación principal (Navbar)', () => {
        it('muestra el logo y la marca con enlace a "/"', () => {
            cy.get('a.navbar-brand')
                .should('have.attr', 'href', '/')
                .within(() => {
                    cy.get('svg.logo-icon').should('exist');
                    cy.contains('Movie').should('exist');
                    cy.contains('Trip').should('exist').and('have.class', 'text-purple');
                });
        });

        it('contiene enlaces de navegación con textos e íconos correctos', () => {
            // General
            cy.get('nav .navbar-nav').within(() => {
                cy.get('a.nav-link')
                    .contains('General')
                    .should('have.attr', 'href', '/general')
                    .find('i.fas.fa-th-large')
                    .should('exist');

                // Producciones
                cy.get('a.nav-link')
                    .contains('Producciones')
                    .should('have.attr', 'href', '/producciones')
                    .find('i.fas.fa-film')
                    .should('exist');

                // Profesionales
                cy.get('a.nav-link')
                    .contains('Profesionales')
                    .should('have.attr', 'href', '/profesionales')
                    .find('i.fas.fa-user-tie')
                    .should('exist');

                // Sagas
                cy.get('a.nav-link')
                    .contains('Sagas')
                    .should('have.attr', 'href', '/sagas')
                    .find('i.fas.fa-stream')
                    .should('exist');
            });
        });

        it('tiene botón de alternar modo oscuro y botón de iniciar sesión', () => {
            cy.get('button.dark-mode-toggle').within(() => {
                cy.get('i.fas.fa-sun').should('exist');
            });
            cy.get('.user-actions a.login-btn')
                .should('have.attr', 'href', '/auth/login')
                .and('contain.text', 'Iniciar sesión')
                .find('i.fas.fa-user')
                .should('exist');
        });
    });

    context('Sección Hero', () => {
        it('muestra título y subtítulo correctos', () => {
            cy.get('section.hero-section').within(() => {
                cy.get('.hero-title')
                    .should('contain.text', 'Bienvenido a')
                    .and('contain.text', 'MovieTrip');
                cy.get('.hero-subtitle')
                    .should('contain.text', 'Descubre los lugares donde se filmaron tus películas y series favoritas')
                    .and('contain.text', 'Explora el cine en todo el mundo');
            });
        });

        it('tiene botones de llamada a la acción con enlaces correctos', () => {
            cy.get('section.hero-section .cta-buttons').within(() => {
                cy.get('a.btn.btn-primary')
                    .should('have.attr', 'href', '/producciones')
                    .and('contain.text', 'Explorar Producciones')
                    .find('i.fas.fa-film')
                    .should('exist');

                cy.get('a.btn.btn-secondary')
                    .should('have.attr', 'href', '/auth/login')
                    .and('contain.text', 'Mi Cuenta')
                    .find('i.fas.fa-ghost')
                    .should('exist');
            });
        });
    });

    context('Sección Películas Destacadas', () => {
        it('muestra el título de sección "Películas Destacadas"', () => {
            cy.get('section.featured-section .section-title')
                .should('have.text', 'Películas Destacadas');
        });

        it('tiene botón "Explorar más" con enlace a "/general"', () => {
            cy.get('section.featured-section .explore-more-container a.explore-btn')
                .should('have.attr', 'href', '/general')
                .and('contain.text', 'Explorar más')
                .find('i.fas.fa-map-marked-alt')
                .should('exist');
        });
    });

    context('Sección Características Principales', () => {
        it('muestra el título "Características Principales"', () => {
            cy.get('section.features-section .section-title')
                .should('have.text', 'Características Principales');
        });

        it('renderiza tres tarjetas de características con iconos y textos', () => {
            cy.get('section.features-section .features-grid .feature-card').should('have.length', 3);

            // Primera característica: Mapas Interactivos
            cy.get('section.features-section .features-grid .feature-card').eq(0).within(() => {
                cy.get('.feature-icon i.fas.fa-map-marked-alt').should('exist');
                cy.get('h3').should('have.text', 'Mapas Interactivos');
                cy.get('p').should('contain.text', 'Ubica en el mapa los escenarios más icónicos del cine');
            });

            // Segunda característica: Información Detallada
            cy.get('section.features-section .features-grid .feature-card').eq(1).within(() => {
                cy.get('.feature-icon i.fas.fa-info-circle').should('exist');
                cy.get('h3').should('have.text', 'Información Detallada');
                cy.get('p').should('contain.text', 'Datos completos sobre producciones');
            });

            // Tercera característica: Conoce a los Profesionales
            cy.get('section.features-section .features-grid .feature-card').eq(2).within(() => {
                cy.get('.feature-icon i.fas.fa-user-tie').should('exist');
                cy.get('h3').should('have.text', 'Conoce a los Profesionales');
                cy.get('p').should('contain.text', 'Descubre directores, actores y técnicos');
            });
        });
    });

    context('Sección Testimonios', () => {
        it('muestra el título "Lo que dicen nuestros usuarios"', () => {
            cy.get('section.testimonials-section .section-title')
                .should('have.text', 'Lo que dicen nuestros usuarios');
        });

        it('renderiza dos tarjetas de testimonio con autores e imágenes', () => {
            cy.get('section.testimonials-section .testimonials-grid .testimonial-card')
                .should('have.length', 2);

            // Primer testimonio (Eva)
            cy.get('section.testimonials-section .testimonials-grid .testimonial-card').eq(0).within(() => {
                cy.get('.testimonial-content p')
                    .should('contain.text', 'La herramienta perfecta para los cinéfilos');
                cy.get('.testimonial-author img')
                    .should('have.attr', 'alt', 'Eva')
                    .and('have.attr', 'src')
                    .and('include', 'profile-displayphoto-shrink_100_100');
                cy.get('.testimonial-author h4').should('have.text', 'Eva');
                cy.get('.testimonial-author p').should('have.text', 'Estudiante y Cinéfila');
            });

            // Segundo testimonio (Andrea)
            cy.get('section.testimonials-section .testimonials-grid .testimonial-card').eq(1).within(() => {
                cy.get('.testimonial-content p')
                    .should('contain.text', '¡Una app espectacular! Me encanta ver los lugares');
                cy.get('.testimonial-author img')
                    .should('have.attr', 'alt', 'Andrea')
                    .and('have.attr', 'src')
                    .and('include', 'profile-displayphoto-shrink_800_800');
                cy.get('.testimonial-author h4').should('have.text', 'Andrea');
                cy.get('.testimonial-author p').should('have.text', 'Viajera y amante del cine');
            });
        });
    });

    context('Footer', () => {

        it('tiene secciones de enlaces "Explorar", "Empresa", "Legal" y "Contacto"', () => {
            // Explorar
            cy.get('footer .col-lg-2').eq(0).within(() => {
                cy.get('h5').should('contain.text', 'Explorar');
                cy.get('a').contains('Películas').should('have.attr', 'href', '/producciones');
                cy.get('a').contains('Sagas').should('have.attr', 'href', '/sagas');
                cy.get('a').contains('Profesionales').should('have.attr', 'href', '/profesionales');
            });

            // Empresa
            cy.get('footer .col-lg-2').eq(1).within(() => {
                cy.get('h5').should('contain.text', 'Empresa');
                cy.get('a').contains('Quién soy').should('have.attr', 'href', '/about');
                cy.get('a').contains('Colaboradores').should('have.attr', 'href', '/careers');
                cy.get('a').contains('Socios').should('have.attr', 'href', '/partners');
            });

            // Legal
            cy.get('footer .col-lg-2').eq(2).within(() => {
                cy.get('h5').should('contain.text', 'Legal');
                cy.get('a').contains('Términos').should('have.attr', 'href', '/terms');
                cy.get('a').contains('Privacidad').should('have.attr', 'href', '/privacy');
                cy.get('a').contains('Cookies').should('have.attr', 'href', '/admin');
            });

            // Contacto
            cy.get('footer .col-lg-2').eq(3).within(() => {
                cy.get('h5').should('contain.text', 'Contacto');
                cy.get('a[href^="mailto:"]')
                    .should('have.attr', 'href', 'mailto:info@movietrip.com')
                    .and('contain.text', 'wolverine307@gmail.com');
                cy.get('a[href^="tel:"]')
                    .should('have.attr', 'href', 'tel:+123456789')
                    .and('contain.text', '+34 681 01 34 87');
            });
        });

        it('muestra íconos de redes sociales y métodos de pago', () => {
            cy.get('footer .social-icons').within(() => {
                cy.get('i.fab.fa-facebook-f').should('exist');
                cy.get('i.fab.fa-twitter').should('exist');
                cy.get('i.fab.fa-instagram').should('exist');
                cy.get('i.fab.fa-linkedin-in').should('exist');
                cy.get('i.fab.fa-youtube').should('exist');
            });
            cy.get('footer .payment-methods').within(() => {
                cy.get('i.fab.fa-cc-visa').should('exist');
                cy.get('i.fab.fa-cc-mastercard').should('exist');
                cy.get('i.fab.fa-cc-paypal').should('exist');
                cy.get('i.fab.fa-cc-apple-pay').should('exist');
            });
        });

        it('incluye el texto de derechos reservados con el año 2025', () => {
            cy.get('footer').contains('© 2025 MovieTrip. Todos los derechos reservados.').should('exist');
        });
    });
});
