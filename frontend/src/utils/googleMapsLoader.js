// src/utils/googleMapsLoader.js
import { Loader } from '@googlemaps/js-api-loader';

let loaderInstance = null;

export function getGoogleMapsLoader(apiKey) {
    if (!loaderInstance) {
        loaderInstance = new Loader({
            apiKey,
            version: 'weekly',
            libraries: ['places'],
        });
    }
    return loaderInstance;
}
