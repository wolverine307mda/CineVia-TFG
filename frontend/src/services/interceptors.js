import router from '@/router';
import { useAuthStore } from '@/stores/auth';

export function setupInterceptors(api) {
    api.interceptors.request.use(config => {
        const token = localStorage.getItem('token');
        if (token && !config.url.includes('/auth/')) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    }, error => Promise.reject(error));

    api.interceptors.response.use(response => response, error => {
        if (error.response?.status === 401 || error.response?.status === 403) {
            const authStore = useAuthStore();
            authStore.logout();

            if (!router.currentRoute.value.meta?.public) {
                router.push('/auth/login');
            }
        }
        return Promise.reject(error);
    });
}
