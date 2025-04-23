// src/axios.js
import axios from 'axios'

const instance = axios.create({
    baseURL: 'http://localhost:8080/api/v1', // Ajusta según tu backend
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
})

instance.interceptors.request.use(config => {
    const token = localStorage.getItem('jwt') // Cambiado a 'jwt'
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

instance.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401) {
            localStorage.removeItem('jwt')
            window.location.href = '/auth/login'
        }
        return Promise.reject(error)
    }
)

export default instance