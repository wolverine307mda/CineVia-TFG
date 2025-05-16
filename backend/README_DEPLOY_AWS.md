# 📦 Despliegue de GeoFilm en AWS Elastic Beanstalk

Este proyecto utiliza múltiples contenedores (Spring Boot backend, Vue frontend, Redis, PostgreSQL) y se despliega mediante Elastic Beanstalk con soporte de ECS (`Dockerrun.aws.json`).

---

## ✅ Pasos previos

1. Instala la EB CLI: `pip install awsebcli --user`
2. Configura AWS CLI: `aws configure` con tu access key
3. Asegúrate de estar en la región `us-west-2`

---

## 📤 Subir imágenes a ECR

### 1. Crear repositorios

```bash
aws ecr create-repository --repository-name geofilm-app
aws ecr create-repository --repository-name geofilm-frontend
```

### 2. Login en ECR

```bash
aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 869446329554.dkr.ecr.us-west-2.amazonaws.com
```

### 3. Construir y subir imágenes

```bash
# Backend
docker build -t geofilm-app .
docker tag geofilm-app:latest 869446329554.dkr.ecr.us-west-2.amazonaws.com/geofilm-app:latest
docker push 869446329554.dkr.ecr.us-west-2.amazonaws.com/geofilm-app:latest

# Frontend
cd ../frontend
docker build -t geofilm-frontend .
docker tag geofilm-frontend:latest 869446329554.dkr.ecr.us-west-2.amazonaws.com/geofilm-frontend:latest
docker push 869446329554.dkr.ecr.us-west-2.amazonaws.com/geofilm-frontend:latest
```

---

## 🚀 Desplegar con EB CLI

```bash
eb init --platform "Docker running on 64bit Amazon Linux 2023" --region us-west-2
eb create geofilm-env --profile default
```

🟢 Tu app estará accesible en la URL pública que te dará AWS EB.

---

## ⚠️ Notas

- Los datos de Postgres y Redis **no son persistentes**. Para producción, usa RDS y ElastiCache.
- Puedes ver logs con `eb logs --profile default`
- Re-deploy con `eb deploy --profile default`