// src/utils/googleMapsLoader.js
import { Loader } from '@googlemaps/js-api-loader';

// 🚨 Esto se ejecuta solo una vez y todos los componentes comparten la instancia
const loaderInstance = new Loader({
    apiKey: 'AIzaSyCtMIHC_PntZzdioTFmRcamhjRNKZMw4hc',
    version: 'weekly',
    libraries: ['places', 'marker'] // ✅ Incluye TODO lo que necesitas
});

export function getGoogleMapsLoader() {
    return loaderInstance;
}
