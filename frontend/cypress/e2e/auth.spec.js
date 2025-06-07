// cypress/e2e/auth.spec.js

describe('Autenticación - MovieTrip', () => {

    context('Página de Inicio de Sesión (/auth/login)', () => {
        beforeEach(() => {
            cy.visit('/auth/login');
        });

        it('muestra el encabezado y subtítulo correctos', () => {
            cy.contains('h2', 'Inicia sesión en tu cuenta').should('be.visible');
            cy.contains('p', 'Gestiona tus películas y listas favoritas').should('be.visible');
        });

        it('tiene el logo y título "MovieTrip" en el header', () => {
            cy.get('.login-header').within(() => {
                cy.get('.logo svg').should('exist');
                cy.contains('h1', 'MovieTrip').should('be.visible');
            });
        });

        it('contiene los campos de correo electrónico y contraseña con sus labels', () => {
            // Correo electrónico
            cy.get('form.login-form').within(() => {
                cy.get('label[for="email"]').should('have.text', 'Correo electrónico');
                cy.get('input#email')
                    .should('have.attr', 'type', 'email')
                    .and('have.attr', 'placeholder', 'tu@email.com')
                    .and('have.attr', 'required');
            });

            // Contraseña
            cy.get('form.login-form').within(() => {
                cy.get('label[for="password"]').should('have.text', 'Contraseña');
                cy.get('input#password')
                    .should('have.attr', 'type', 'password')
                    .and('have.attr', 'placeholder', '••••••••')
                    .and('have.attr', 'required');
            });
        });

        it('ofrece enlace de "¿Olvidaste tu contraseña?" que apunta a /auth/forgot-password', () => {
            cy.get('form.login-form')
                .find('a.forgot-password')
                .should('have.attr', 'href', '/auth/forgot-password')
                .and('contain.text', '¿Olvidaste tu contraseña?');
        });

        it('tiene el botón para mostrar/ocultar la contraseña', () => {
            cy.get('button.password-toggle')
                .should('have.attr', 'aria-label', 'Show password')
                .find('i.fas.fa-eye')
                .should('exist');
        });

        it('incluye el checkbox "Mantener sesión iniciada"', () => {
            cy.get('form.login-form').within(() => {
                cy.get('input[type="checkbox"]#remember').should('exist');
                cy.get('label[for="remember"]').should('have.text', 'Mantener sesión iniciada');
            });
        });

        it('muestra el botón "Iniciar sesión" habilitado', () => {
            cy.get('button.login-button').should('be.visible').and('not.be.disabled');
            cy.get('button.login-button').contains('Iniciar sesión');
        });

        it('ofrece login social con Google y Facebook', () => {
            cy.get('button.social-button.google')
                .should('have.attr', 'type', 'button')
                .and('contain.text', 'Google')
                .find('i.fab.fa-google')
                .should('exist');

            cy.get('button.social-button.facebook')
                .should('have.attr', 'type', 'button')
                .and('contain.text', 'Facebook')
                .find('i.fab.fa-facebook-f')
                .should('exist');
        });

        it('tiene enlace para registrarse que lleva a /auth/register', () => {
            cy.get('div.signup-link a')
                .should('have.attr', 'href', '/auth/register')
                .and('contain.text', 'Regístrate');
        });
    });

    context('Página de Registro (/auth/register)', () => {
        beforeEach(() => {
            cy.visit('/auth/register');
        });

        it('muestra el encabezado y subtítulo correctos', () => {
            cy.contains('h2', 'Crea tu cuenta').should('be.visible');
            cy.contains('p', 'Completa los siguientes pasos para registrarte').should('be.visible');
        });

        it('presenta el paso 1 con campos "Nombre", "Apellidos" y "Nombre de usuario"', () => {
            // Paso 1 visible
            cy.get('.stepper .step.active').within(() => {
                cy.contains('.step-number', '1').should('exist');
                cy.contains('.step-label', 'Información personal').should('exist');
            });

            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('label[for="firstName"]').should('have.text', 'Nombre');
                cy.get('input#firstName')
                    .should('have.attr', 'type', 'text')
                    .and('have.attr', 'placeholder', 'Ej: Juan')
                    .and('have.attr', 'required');

                cy.get('label[for="lastName"]').should('have.text', 'Apellidos');
                cy.get('input#lastName')
                    .should('have.attr', 'type', 'text')
                    .and('have.attr', 'placeholder', 'Ej: Pérez García')
                    .and('have.attr', 'required');

                cy.get('label[for="username"]').should('have.text', 'Nombre de usuario');
                cy.get('input#username')
                    .should('have.attr', 'type', 'text')
                    .and('have.attr', 'placeholder', 'Ej: juanpg')
                    .and('have.attr', 'required');

                cy.contains('.input-hint', 'Este será tu identificador único').should('exist');

                cy.get('button.step-button.next')
                    .should('be.disabled')
                    .and('contain.text', 'Siguiente');
            });
        });

        it('habilita el botón "Siguiente" en paso 1 al completar los campos', () => {
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('input#firstName').type('Juan');
                cy.get('input#lastName').type('Pérez García');
                cy.get('input#username').type('juanpg');

                cy.get('button.step-button.next').should('not.be.disabled');
            });
        });

        it('navega al paso 2 al hacer clic en "Siguiente" y luego puede regresar', () => {
            // Completar paso 1
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('input#firstName').type('Juan');
                cy.get('input#lastName').type('Pérez García');
                cy.get('input#username').type('juanpg');
                cy.get('button.step-button.next').click();
            });

            // Paso 2 visible
            cy.get('.stepper .step').eq(1).within(() => {
                cy.contains('.step-number', '2').should('exist');
                cy.contains('.step-label', 'Contacto').should('exist');
            });
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('label[for="email"]').should('have.text', 'Correo electrónico');
                cy.get('input#email')
                    .should('have.attr', 'type', 'email')
                    .and('have.attr', 'placeholder', 'tu@email.com')
                    .and('have.attr', 'required');

                cy.get('label[for="phone"]').should('have.text', 'Teléfono');
                cy.get('input#phone')
                    .should('have.attr', 'type', 'tel')
                    .and('have.attr', 'placeholder', 'Ej: +34 123 456 789');

                // Botones "Anterior" y "Siguiente"
                cy.get('button.step-button.back')
                    .should('contain.text', 'Anterior')
                    .click();
            });

            // De vuelta a paso 1
            cy.get('.stepper .step.active').within(() => {
                cy.contains('.step-number', '1').should('exist');
                cy.contains('.step-label', 'Información personal').should('exist');
            });
        });

        it('navega correctamente entre los tres pasos del formulario', () => {
            // Paso 1 → Paso 2
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('input#firstName').type('Ana');
                cy.get('input#lastName').type('López');
                cy.get('input#username').type('analopez');
                cy.get('button.step-button.next').click();
            });

            // Paso 2 → Paso 3
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('input#email').type('ana@example.com');
                cy.get('input#phone').type('+34 600 123 456');
                cy.get('button.step-button.next').should('not.be.disabled').click();
            });

            // Paso 3 visible
            cy.get('.stepper .step').eq(2).within(() => {
                cy.contains('.step-number', '3').should('exist');
                cy.contains('.step-label', 'Seguridad').should('exist');
            });
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('label[for="password"]').should('have.text', 'Contraseña');
                cy.get('input#password')
                    .should('have.attr', 'type', 'password')
                    .and('have.attr', 'placeholder', '••••••••')
                    .and('have.attr', 'required');

                cy.get('label[for="confirmPassword"]').should('have.text', 'Confirmar contraseña');
                cy.get('input#confirmPassword')
                    .should('have.attr', 'type', 'password')
                    .and('have.attr', 'placeholder', '••••••••')
                    .and('have.attr', 'required');

                cy.get('.password-strength').should('contain.text', 'Seguridad: Débil');

                cy.get('button.step-button.back')
                    .should('contain.text', 'Anterior')
                    .click();
            });

            // Regreso a paso 2 tras "Anterior"
            cy.get('.stepper .step.active').within(() => {
                cy.contains('.step-number', '2').should('exist');
                cy.contains('.step-label', 'Contacto').should('exist');
            });
        });

        it('habilita el botón de registro solo cuando contraseñas coinciden y se aceptan términos', () => {
            // Completar paso 1
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('input#firstName').type('Luis');
                cy.get('input#lastName').type('Martínez');
                cy.get('input#username').type('luism');
                cy.get('button.step-button.next').click();
            });
            // Completar paso 2
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('input#email').type('luis@example.com');
                cy.get('input#phone').type('+34 611 222 333');
                cy.get('button.step-button.next').click();
            });
            // Paso 3: validar botón deshabilitado inicialmente
            cy.get('form.register-form').filter(':visible').within(() => {
                cy.get('button.register-button').should('be.disabled');
                // Escribir contraseñas que no coinciden
                cy.get('input#password').type('Password123');
                cy.get('input#confirmPassword').type('Different123');
                cy.get('input#terms').check();
                cy.get('button.register-button').should('be.disabled');

                // Corregir para que coincidan
                cy.get('input#confirmPassword').clear().type('Password123');
                cy.get('input#terms').uncheck();
                cy.get('input#terms').check();
                cy.get('button.register-button').should('not.be.disabled');
            });
        });// cypress/e2e/auth.spec.js

        describe('Autenticación - MovieTrip', () => {
            const baseUrl = Cypress.config('baseUrl') || 'http://localhost:3000';

            context('Página de Inicio de Sesión (/auth/login)', () => {
                beforeEach(() => {
                    cy.visit('/auth/login');
                });

                it('muestra el encabezado y subtítulo correctos', () => {
                    cy.contains('h2', 'Inicia sesión en tu cuenta').should('be.visible');
                    cy.contains('p', 'Gestiona tus películas y listas favoritas').should('be.visible');
                });

                it('tiene el logo y título "MovieTrip" en el header', () => {
                    cy.get('.login-header').within(() => {
                        cy.get('.logo svg').should('exist');
                        cy.contains('h1', 'MovieTrip').should('be.visible');
                    });
                });

                it('contiene los campos de correo electrónico y contraseña con sus labels', () => {
                    // Correo electrónico
                    cy.get('form.login-form').within(() => {
                        cy.get('label[for="email"]').should('have.text', 'Correo electrónico');
                        cy.get('input#email')
                            .should('have.attr', 'type', 'email')
                            .and('have.attr', 'placeholder', 'tu@email.com')
                            .and('have.attr', 'required');
                    });

                    // Contraseña
                    cy.get('form.login-form').within(() => {
                        cy.get('label[for="password"]').should('have.text', 'Contraseña');
                        cy.get('input#password')
                            .should('have.attr', 'type', 'password')
                            .and('have.attr', 'placeholder', '••••••••')
                            .and('have.attr', 'required');
                    });
                });

                it('ofrece enlace de "¿Olvidaste tu contraseña?" que apunta a /auth/forgot-password', () => {
                    cy.get('form.login-form')
                        .find('a.forgot-password')
                        .should('have.attr', 'href', '/auth/forgot-password')
                        .and('contain.text', '¿Olvidaste tu contraseña?');
                });

                it('tiene el botón para mostrar/ocultar la contraseña', () => {
                    cy.get('button.password-toggle')
                        .should('have.attr', 'aria-label', 'Show password')
                        .find('i.fas.fa-eye')
                        .should('exist');
                });

                it('incluye el checkbox "Mantener sesión iniciada"', () => {
                    cy.get('form.login-form').within(() => {
                        cy.get('input[type="checkbox"]#remember').should('exist');
                        cy.get('label[for="remember"]').should('have.text', 'Mantener sesión iniciada');
                    });
                });

                it('muestra el botón "Iniciar sesión" habilitado', () => {
                    cy.get('button.login-button').should('be.visible').and('not.be.disabled');
                    cy.get('button.login-button').contains('Iniciar sesión');
                });

                it('ofrece login social con Google y Facebook', () => {
                    cy.get('button.social-button.google')
                        .should('have.attr', 'type', 'button')
                        .and('contain.text', 'Google')
                        .find('i.fab.fa-google')
                        .should('exist');

                    cy.get('button.social-button.facebook')
                        .should('have.attr', 'type', 'button')
                        .and('contain.text', 'Facebook')
                        .find('i.fab.fa-facebook-f')
                        .should('exist');
                });

                it('tiene enlace para registrarse que lleva a /auth/register', () => {
                    cy.get('div.signup-link a')
                        .should('have.attr', 'href', '/auth/register')
                        .and('contain.text', 'Regístrate');
                });
            });

            context('Página de Registro (/auth/register)', () => {
                beforeEach(() => {
                    cy.visit('/auth/register');
                });

                it('muestra el encabezado y subtítulo correctos', () => {
                    cy.contains('h2', 'Crea tu cuenta').should('be.visible');
                    cy.contains('p', 'Completa los siguientes pasos para registrarte').should('be.visible');
                });

                it('presenta el paso 1 con campos "Nombre", "Apellidos" y "Nombre de usuario"', () => {
                    // Paso 1 visible
                    cy.get('.stepper .step.active').within(() => {
                        cy.contains('.step-number', '1').should('exist');
                        cy.contains('.step-label', 'Información personal').should('exist');
                    });

                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('label[for="firstName"]').should('have.text', 'Nombre');
                        cy.get('input#firstName')
                            .should('have.attr', 'type', 'text')
                            .and('have.attr', 'placeholder', 'Ej: Juan')
                            .and('have.attr', 'required');

                        cy.get('label[for="lastName"]').should('have.text', 'Apellidos');
                        cy.get('input#lastName')
                            .should('have.attr', 'type', 'text')
                            .and('have.attr', 'placeholder', 'Ej: Pérez García')
                            .and('have.attr', 'required');

                        cy.get('label[for="username"]').should('have.text', 'Nombre de usuario');
                        cy.get('input#username')
                            .should('have.attr', 'type', 'text')
                            .and('have.attr', 'placeholder', 'Ej: juanpg')
                            .and('have.attr', 'required');

                        cy.contains('.input-hint', 'Este será tu identificador único').should('exist');

                        cy.get('button.step-button.next')
                            .should('be.disabled')
                            .and('contain.text', 'Siguiente');
                    });
                });

                it('habilita el botón "Siguiente" en paso 1 al completar los campos', () => {
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('input#firstName').type('Juan');
                        cy.get('input#lastName').type('Pérez García');
                        cy.get('input#username').type('juanpg');

                        cy.get('button.step-button.next').should('not.be.disabled');
                    });
                });

                it('navega al paso 2 al hacer clic en "Siguiente" y luego puede regresar', () => {
                    // Completar paso 1
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('input#firstName').type('Juan');
                        cy.get('input#lastName').type('Pérez García');
                        cy.get('input#username').type('juanpg');
                        cy.get('button.step-button.next').click();
                    });

                    // Paso 2 visible
                    cy.get('.stepper .step').eq(1).within(() => {
                        cy.contains('.step-number', '2').should('exist');
                        cy.contains('.step-label', 'Contacto').should('exist');
                    });
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('label[for="email"]').should('have.text', 'Correo electrónico');
                        cy.get('input#email')
                            .should('have.attr', 'type', 'email')
                            .and('have.attr', 'placeholder', 'tu@email.com')
                            .and('have.attr', 'required');

                        cy.get('label[for="phone"]').should('have.text', 'Teléfono');
                        cy.get('input#phone')
                            .should('have.attr', 'type', 'tel')
                            .and('have.attr', 'placeholder', 'Ej: +34 123 456 789');

                        // Botones "Anterior" y "Siguiente"
                        cy.get('button.step-button.back')
                            .should('contain.text', 'Anterior')
                            .click();
                    });

                    // De vuelta a paso 1
                    cy.get('.stepper .step.active').within(() => {
                        cy.contains('.step-number', '1').should('exist');
                        cy.contains('.step-label', 'Información personal').should('exist');
                    });
                });

                it('navega correctamente entre los tres pasos del formulario', () => {
                    // Paso 1 → Paso 2
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('input#firstName').type('Ana');
                        cy.get('input#lastName').type('López');
                        cy.get('input#username').type('analopez');
                        cy.get('button.step-button.next').click();
                    });

                    // Paso 2 → Paso 3
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('input#email').type('ana@example.com');
                        cy.get('input#phone').type('+34 600 123 456');
                        cy.get('button.step-button.next').should('not.be.disabled').click();
                    });

                    // Paso 3 visible
                    cy.get('.stepper .step').eq(2).within(() => {
                        cy.contains('.step-number', '3').should('exist');
                        cy.contains('.step-label', 'Seguridad').should('exist');
                    });
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('label[for="password"]').should('have.text', 'Contraseña');
                        cy.get('input#password')
                            .should('have.attr', 'type', 'password')
                            .and('have.attr', 'placeholder', '••••••••')
                            .and('have.attr', 'required');

                        cy.get('label[for="confirmPassword"]').should('have.text', 'Confirmar contraseña');
                        cy.get('input#confirmPassword')
                            .should('have.attr', 'type', 'password')
                            .and('have.attr', 'placeholder', '••••••••')
                            .and('have.attr', 'required');

                        cy.get('.password-strength').should('contain.text', 'Seguridad: Débil');

                        cy.get('button.step-button.back')
                            .should('contain.text', 'Anterior')
                            .click();
                    });

                    // Regreso a paso 2 tras "Anterior"
                    cy.get('.stepper .step.active').within(() => {
                        cy.contains('.step-number', '2').should('exist');
                        cy.contains('.step-label', 'Contacto').should('exist');
                    });
                });

                it('habilita el botón de registro solo cuando contraseñas coinciden y se aceptan términos', () => {
                    // Completar paso 1
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('input#firstName').type('Luis');
                        cy.get('input#lastName').type('Martínez');
                        cy.get('input#username').type('luism');
                        cy.get('button.step-button.next').click();
                    });
                    // Completar paso 2
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('input#email').type('luis@example.com');
                        cy.get('input#phone').type('+34 611 222 333');
                        cy.get('button.step-button.next').click();
                    });
                    // Paso 3: validar botón deshabilitado inicialmente
                    cy.get('form.register-form').filter(':visible').within(() => {
                        cy.get('button.register-button').should('be.disabled');
                        // Escribir contraseñas que no coinciden
                        cy.get('input#password').type('Password123');
                        cy.get('input#confirmPassword').type('Different123');
                        cy.get('input#terms').check();
                        cy.get('button.register-button').should('be.disabled');

                        // Corregir para que coincidan
                        cy.get('input#confirmPassword').clear().type('Password123');
                        cy.get('input#terms').uncheck();
                        cy.get('input#terms').check();
                        cy.get('button.register-button').should('not.be.disabled');
                    });
                });

            });
        });
    });
});
