import api from './api';
import { jwtDecode } from 'jwt-decode';
import toast from '@/utils/toast';

export async function signin(email, password) {
    try {
        const response = await api.post('/api/v1/auth/signin', { email, password });

        if (response.status === 401) {
            throw new Error('Credenciales incorrectas');
        }

        const { token, role } = response.data;

        if (!token) {
            throw new Error('No se recibió token en la respuesta');
        }

        const decoded = jwtDecode(token);
        localStorage.setItem('token', token);
        localStorage.setItem('userRole', role);
        localStorage.setItem('userEmail', email);

        // Mostrar notificación de inicio de sesión exitoso
        toast.success('Inicio de sesión exitoso 🎉');

        // Enviar evento de inicio de sesión exitoso
        const loginEvent = new CustomEvent('login-success', {
            detail: { email, role }
        });
        window.dispatchEvent(loginEvent);

        return { token, role };
    } catch (error) {
        let errorMessage = 'Error en el inicio de sesión';

        if (error.response) {
            if (error.response.status === 401) {
                errorMessage = 'Correo electrónico o contraseña incorrectos';
            } else if (error.response.data?.message) {
                errorMessage = error.response.data.message;
            }
        } else if (error.message) {
            errorMessage = error.message;
        }

        toast.error(errorMessage);
        throw new Error(errorMessage);
    }
}

export async function signup(userData) {
    try {
        // Validación de contraseña en frontend antes de enviar
        if (userData.password.length < 6) {
            throw new Error('La contraseña debe tener al menos 6 caracteres');
        }

        if (userData.password !== userData.confirmPassword) {
            throw new Error('Las contraseñas no coinciden');
        }

        const { confirmPassword, ...registrationData } = userData;
        const response = await api.post('/api/v1/auth/signup', registrationData);

        if (response.status === 400) {
            const errorData = response.data;
            if (errorData.errors) {
                const errorMessages = Object.values(errorData.errors).flat();
                throw new Error(errorMessages.join(', '));
            }
            throw new Error(errorData.message || 'Error en el registro');
        }

        const { token, role } = response.data;

        if (!token) {
            throw new Error('No se recibió token en la respuesta');
        }

        localStorage.setItem('token', token);
        localStorage.setItem('userRole', role);
        localStorage.setItem('userEmail', userData.email);

        toast.success('Registro completado ✅');
        return { token, role };
    } catch (error) {
        let errorMessage = 'Error en el registro';

        if (error.response) {
            if (error.response.status === 400 && error.response.data?.errors) {
                const errors = Object.values(error.response.data.errors).flat();
                errorMessage = errors.join(', ');
            } else if (error.response.data?.message) {
                errorMessage = error.response.data.message;
            }
        } else if (error.message) {
            errorMessage = error.message;
        }

        toast.error(errorMessage);
        throw new Error(errorMessage);
    }
}

export async function checkUsernameAvailability(username) {
    try {
        const response = await api.get('/api/users/check-username', {
            params: { username },
            validateStatus: (status) => status < 500
        });

        if (response.status === 200) {
            return response.data.available;
        }
        throw new Error('Error al verificar nombre de usuario');
    } catch (error) {
        console.error('Verificación de username:', error);
        throw new Error('No se pudo verificar el nombre de usuario. Intente nuevamente.');
    }
}

export async function checkEmailAvailability(email) {
    try {
        const response = await api.get('/api/users/check-email', {
            params: { email },
            validateStatus: (status) => status < 500
        });

        if (response.status === 200) {
            return response.data.available;
        }
        throw new Error('Error al verificar email');
    } catch (error) {
        console.error('Verificación de email:', error);
        throw new Error('No se pudo verificar el email. Intente nuevamente.');
    }
}

export function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userEmail');
    toast.info('Sesión cerrada');
}

export function isAuthenticated() {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
        const decoded = jwtDecode(token);
        return decoded.exp * 1000 > Date.now();
    } catch (error) {
        console.error('Error al decodificar token:', error);
        return false;
    }
}

export function getCurrentUserRole() {
    return localStorage.getItem('userRole');
}

export function getCurrentUserEmail() {
    return localStorage.getItem('userEmail');
}

export async function fetchCurrentUser() {
    try {
        const response = await api.get('/api/users/me');
        return response.data;
    } catch (error) {
        if (error.response?.status === 401 || error.response?.status === 403) {
            logout();
        }
        throw error;
    }
}

export async function requestPasswordReset(email) {
    try {
        const response = await api.post('/api/v1/auth/request-password-reset', {email});
        return response.data;
    } catch (error) {
        let errorMessage = 'Error al solicitar restablecimiento';

        if (error.response) {
            if (error.response.status === 404) {
                errorMessage = 'No se encontró una cuenta con ese correo electrónico';
            } else if (error.response.data?.message) {
                errorMessage = error.response.data.message;
            }
        } else if (error.message) {
            errorMessage = error.message;
        }

        throw new Error(errorMessage);
    }
}

export async function verifyResetPin(email, pin) {
    try {
        const response = await api.post('/api/v1/auth/verify-reset-pin', {email, pin});
        return response.data;
    } catch (error) {
        let errorMessage = 'Error al verificar el PIN';

        if (error.response) {
            if (error.response.status === 400) {
                errorMessage = 'PIN inválido o expirado';
            } else if (error.response.data?.message) {
                errorMessage = error.response.data.message;
            }
        } else if (error.message) {
            errorMessage = error.message;
        }

        throw new Error(errorMessage);
    }
}

export async function resetPassword({email, pin, newPassword, confirmPassword}) {
    try {
        const response = await api.post('/api/v1/auth/reset-password', {
            email,
            pin,
            newPassword,
            confirmPassword
        });
        return response.data;
    } catch (error) {
        let errorMessage = 'Error al cambiar la contraseña';

        if (error.response) {
            if (error.response.status === 400) {
                if (error.response.data?.message === 'Las contraseñas no coinciden') {
                    errorMessage = 'Las contraseñas no coinciden';
                } else if (error.response.data?.message === 'PIN inválido o expirado') {
                    errorMessage = 'PIN inválido o expirado';
                } else {
                    errorMessage = error.response.data?.message || errorMessage;
                }
            }
        } else if (error.message) {
            errorMessage = error.message;
        }

        throw new Error(errorMessage);
    }
}

export default {
    signin,
    signup,
    logout,
    isAuthenticated,
    verifyResetPin,
    requestPasswordReset,
    getCurrentUserRole,
    getCurrentUserEmail,
    fetchCurrentUser,
    resetPassword,
    checkUsernameAvailability,
    checkEmailAvailability,
};