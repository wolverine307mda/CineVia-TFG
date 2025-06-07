// cypress/e2e/navigation_and_search.spec.js

describe('Navegación y búsqueda en Viaje de película', () => {
    const menuItems = [
        { label: 'General', path: '/general' },
        { label: 'Producciones', path: '/producciones' },
        { label: 'Profesionales', path: '/profesionales' },
        { label: 'Sagas', path: '/sagas' },
    ];

    beforeEach(() => {
        // Siempre partimos de la página de inicio
        cy.visit('/');
    });

    context('Navbar y modo móvil', () => {
        it('muestra logo con enlace a "/" y existe clase "dark" en <html>', () => {
            // Solo comprobamos la presencia de la clase "dark"; omitimos el atributo lang
            cy.document()
                .its('documentElement')
                .should('have.class', 'dark');

            cy.get('a.navbar-brand')
                .should('have.attr', 'href', '/')
                .within(() => {
                    cy.get('svg.logo-icon').should('exist');
                    cy.contains('Trip').should('exist').and('have.class', 'text-purple');
                    cy.contains('Movie').should('exist');
                });
        });

        it('itera sobre cada enlace del navbar y verifica navegación', () => {
            menuItems.forEach(({ label, path }) => {
                // Volver a la página de inicio antes de cada click
                cy.visit('/');
                cy.get('nav .navbar-nav').within(() => {
                    cy.contains('a.nav-link', label)
                        .should('have.attr', 'href', path)
                        .click();
                });
                cy.url().should('include', path);
                cy.get(`nav .navbar-nav a.nav-link[href="${path}"]`).should('have.class', 'router-link-active');
            });
        });

        it('el botón de alternar menú en vista móvil funciona', () => {
            cy.viewport(320, 640);
            cy.get('button.navbar-toggler').should('be.visible').click();
            cy.get('#navbarContent').should('have.class', 'show');
            cy.get('button.navbar-toggler').click();
            cy.get('#navbarContent').should('not.have.class', 'show');
        });

        it('el botón de modo oscuro y el botón de Iniciar sesión se ven correctamente', () => {
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

    context('Página de Producciones', () => {
        beforeEach(() => {
            cy.visit('/producciones');
            cy.get('h1.main-title').should('contain.text', 'Catálogo de Películas');
            cy.get('.movies-grid').scrollTo('right', { ensureScrollable: false });
            cy.wait(500);
            cy.get('.movies-grid').scrollTo('left', { ensureScrollable: false });
        });
        it('muestra barra de búsqueda y permite buscar por título', () => {
            cy.get('.search-bar .search-box input[type="text"]')
                .should('be.visible')
                .as('produccionesSearchInput');

            cy.get('.search-bar .search-btn')
                .should('be.visible')
                .as('produccionesSearchButton');

            // Filtrar por "Ant-Man"
            cy.get('@produccionesSearchInput').clear().type('Ant-Man');
            cy.get('@produccionesSearchButton').click();

            // Verificar que el listado se actualiza: al menos un título contiene "Ant-Man"
            cy.get('.movies-grid .card-title')
                .should('contain.text', 'Ant-Man');
        });

        it('permite paginar el listado de producciones', () => {
            cy.get('.pagination-controls .pagination-btn').as('paginationButtons');
            cy.get('.pagination-controls .page-indicator').as('pageIndicator');

            // Página 1: la flecha izquierda debe estar deshabilitada
            cy.get('@paginationButtons').first().should('be.disabled');
            cy.get('@pageIndicator').should('contain.text', 'Página 1');

            // Ir a la página siguiente
            cy.get('@paginationButtons').last().should('not.be.disabled').click();
            cy.get('@pageIndicator').should('contain.text', 'Página 2');

            // Volver a la página anterior
            cy.get('@paginationButtons').first().should('not.be.disabled').click();
            cy.get('@pageIndicator').should('contain.text', 'Página 1');
        });

        it('hace scroll horizontal por las tarjetas de producciones', () =>
            {cy.wait(2000);
            cy.get('.movies-grid').scrollTo('bottom', { ensureScrollable: false });
            cy.wait(500);
            cy.get('.movies-grid').scrollTo('top', { ensureScrollable: false });
        });
    });

    context('Página de Profesionales', () => {
        beforeEach(() => {
            cy.visit('/profesionales');
            cy.get('h1, .main-title').should(($h) => {
                expect($h.text().toLowerCase()).to.include('profesionales');
            });
        });

        it('muestra barra de búsqueda y permite filtrar por nombre', () => {
            cy.get('.search-bar .search-box input[type="text"]')
                .should('be.visible')
                .as('profesionalesSearchInput');

            cy.get('.search-bar .search-btn')
                .should('be.visible')
                .as('profesionalesSearchButton');

            // Filtrar por "Eva"
            cy.get('@profesionalesSearchInput').clear().type('Eva');
            cy.get('@profesionalesSearchButton').click();

            // Verificar que al menos una tarjeta de profesional aparece con "Eva"
            cy.get('.card-base').should('have.length.greaterThan', 0);
            cy.get('.card-base .card-title')
                .first()
                .should('contain.text', 'Eva');
        });

        it('muestra al menos una tarjeta de profesional en el listado', () => {
            cy.get('.card-base').its('length').should('be.gte', 1);
        });

        it('hace scroll vertical por las tarjetas de profesionales', () => {
            cy.wait(2000);
            cy.get('.professionals-grid').scrollTo('bottom', { ensureScrollable: false });
            cy.wait(500);
            cy.get('.professionals-grid').scrollTo('bottom', { ensureScrollable: false });
        });
    });

    context('Página de Sagas', () => {
        beforeEach(() => {
            cy.visit('/sagas');
            cy.get('h1, .main-title').should(($h) => {
                expect($h.text().toLowerCase()).to.include('sagas');
            });
        });

        it('muestra barra de búsqueda y permite filtrar sagas', () => {
            cy.get('.search-bar .search-box input[type="text"]')
                .should('be.visible')
                .as('sagasSearchInput');

            cy.get('.search-bar .search-btn')
                .should('be.visible')
                .as('sagasSearchButton');

            // Filtrar por "Marvel"
            cy.get('@sagasSearchInput').clear().type('Marvel');
            cy.get('@sagasSearchButton').click();

            // Verificar que al menos una tarjeta de saga aparece con "Marvel"
            cy.get('.card-base').should('have.length.greaterThan', 0);
            cy.get('.card-base .card-title')
                .first()
                .should('contain.text', 'Marvel');
        });

        it('permite paginar el listado de sagas', () => {
            cy.get('.pagination-controls .pagination-btn').as('paginationButtons');
            cy.get('.pagination-controls .page-indicator').as('pageIndicator');

            // Página 1: la flecha izquierda debe estar deshabilitada
            cy.get('@paginationButtons').first().should('be.disabled');
            cy.get('@pageIndicator').should('contain.text', 'Página 1');

            // Ir a la página siguiente
            cy.get('@paginationButtons').last().click();
            cy.get('@pageIndicator').should('contain.text', 'Página 2');
        });

        it('hace scroll vertical por las tarjetas de sagas', () => {
            cy.wait(2000);
            cy.get('.sagas-grid').scrollTo('bottom', { ensureScrollable: false });
            cy.wait(500);
            cy.get('.sagas-grid').scrollTo('top', { ensureScrollable: false });
        });
    });
});
