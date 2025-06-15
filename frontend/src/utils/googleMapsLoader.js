import { Loader } from '@googlemaps/js-api-loader';

// Carga la API Key desde las variables de entorno
const apiKey = import.meta.env.VITE_GOOGLE_MAPS_API_KEY;

const loaderInstance = new Loader({
    apiKey,
    version: 'weekly',
    libraries: ['places', 'marker']
});

export function getGoogleMapsLoader() {
    return loaderInstance;
}
