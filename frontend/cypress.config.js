import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    baseUrl: 'http://44.225.190.62:5173/',
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
    specPattern: 'cypress/e2e/**/*.spec.js',
    supportFile: 'cypress/support/e2e.js',
    viewportHeight: 1200,
    viewportWidth: 1920,
  },
});
