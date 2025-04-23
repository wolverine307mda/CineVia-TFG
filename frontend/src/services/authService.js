// src/services/auth.js
import axios from 'axios'

const API_URL = 'http://localhost:8080/api/v1/auth'

// Iniciar sesión
export async function signin(email, password) {
    const response = await axios.post(`${API_URL}/signin`, {
        email,
        password
    })
    const token = response.data.token
    localStorage.setItem('token', token)
    return token
}

// Registrar nuevo usuario
export async function signup(nombre, email, password) {
    const response = await axios.post(`${API_URL}/signup`, {
        nombre,
        email,
        password
    })
    const token = response.data.token
    localStorage.setItem('token', token)
    return token
}

// Cerrar sesión
export function logout() {
    localStorage.removeItem('token')
}
