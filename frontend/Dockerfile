FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Copia el frontend ya compilado (Vue → dist/)
COPY dist/ .

# Configuración personalizada de nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
