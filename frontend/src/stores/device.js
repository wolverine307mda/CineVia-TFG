import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useDeviceStore = defineStore('device', () => {
    const deviceInfo = ref({
        ip: null,
        type: 'desktop',
        os: 'unknown',
        browser: 'unknown',
        screenResolution: 'unknown',
        location: null
    });

    const setDeviceInfo = (info) => {
        deviceInfo.value = {
            ...deviceInfo.value,
            ...info
        };
    };

    const detectDevice = () => {
        const userAgent = navigator.userAgent;
        let deviceType = 'desktop';

        if (/Mobile|Android|iPhone|iPad|iPod/i.test(userAgent)) {
            deviceType = /Tablet|iPad/i.test(userAgent) ? 'tablet' : 'mobile';
        }

        const os = /Windows/i.test(userAgent) ? 'windows' :
            /Mac/i.test(userAgent) ? 'macos' :
                /Linux/i.test(userAgent) ? 'linux' :
                    /Android/i.test(userAgent) ? 'android' :
                        /iOS|iPhone|iPad|iPod/i.test(userAgent) ? 'ios' : 'unknown';

        const browser = /Chrome/i.test(userAgent) ? 'chrome' :
            /Firefox/i.test(userAgent) ? 'firefox' :
                /Safari/i.test(userAgent) ? 'safari' :
                    /Edge/i.test(userAgent) ? 'edge' :
                        /Opera/i.test(userAgent) ? 'opera' : 'unknown';

        const screenResolution = `${window.screen.width}x${window.screen.height}`;

        setDeviceInfo({
            type: deviceType,
            os,
            browser,
            screenResolution
        });
    };

    const fetchIPAndLocation = async () => {
        try {
            // Usamos ipapi.co para obtener la IP y ubicación aproximada
            const response = await fetch('https://ipapi.co/json/');
            const data = await response.json();

            setDeviceInfo({
                ip: data.ip,
                location: {
                    country: data.country_name,
                    region: data.region,
                    city: data.city,
                    timezone: data.timezone
                }
            });
        } catch (error) {
            console.error('Error fetching IP info:', error);
        }
    };

    // Detectamos la información básica al inicializar el store
    detectDevice();

    return {
        deviceInfo,
        setDeviceInfo,
        detectDevice,
        fetchIPAndLocation
    };
});