
# 🎬 Movierama

A full-stack web application to manage movies and ratings, built with:

- **Backend**: Spring Boot (Java 17), JPA, Redis, PostgreSQL
- **Frontend**: Angular 19, PrimeNG
- **Authentication**: Keycloak
- **Observability**: Loki, Promtail, Grafana, Prometheus

In adition to the required functional features, the application includes:
- **Multilanguage support**: English and Greek both labels and lookup data
- **Dark mode**: Toggle between light and dark theme
- **Responsive design**: Optimized for desktop and mobile devices
- **Api Documentation**: Swagger UI for backend API under /movierama/api/swagger-ui/index.html
- **Dockerized**: All components are containerized for easy deployment
- **Monitoring and Logging**: Integrated with Grafana, Loki, and Prometheus for observability
- **Keycloak**: For secure authentication, authorization and user management.
- **Cache**: Redis for caching frequently accessed data

---

## 📂 Project Structure

```
backend/                # Spring Boot application
backend/configuration/  # Configuration files for externally provisioning confuguration files to Prometheus, Promtail, Loki and Keycloak
frontend/               # Angular frontend
```

## &#x1F6E2; Database

For better separation of concerns, security and maintenance the application database includes different schemas to support application generated data and data used by supporting service e.g Keycloak

- **movierama**: Schema for application data.
- **keycloak**: schema for keycloak generated data.

In the dockerized deloyment, all data are persisted into the host machine, using mounted volume db-data.

---

## 🚀 Quick Start (Docker Compose)

Make sure you have **Docker** and **Docker Compose** installed.

1️⃣ Clone the repository:

```bash

git clone https://github.com/your-username/movierama.git
cd backend
```

3️⃣ Start all services:

```bash

docker-compose -f docker-compose-demo up -d
```

4️⃣ Access the application:

- **Frontend UI:** [http://localhost](http://localhost)
- **Backend API:** [http://localhost:8082](http://localhost:8082)
- **Keycloak Admin:** [http://localhost:8081](http://localhost:8081)
- **Grafana:** [http://localhost:3000](http://localhost:3000)

---


## 🚀 Building the Application Images

1️⃣ Build the frontned Angular application:

```bash

cd frontend
docker build -t ekavakakis/movierama-fe:latest --build-arg env=production .
docker push ekavakakis/movierama-fe:latest
```

2️⃣ Build the backend and frontend Docker images (or use prebuilt images):

```bash

cd backend
docker build -t ekavakakis/movierama-be:latest .
docker push ekavakakis/movierama-be:latest
```

## 🔑 Keycloak Credentials

Default admin credentials:

```
Username: keycloak
Password: keycloak
```

No additional actions are required to set up Keycloak, since realm configuration is included in the Docker Compose setup.

---

## 🐳 Docker Services Overview

| Service    | Description                   | Port        |
|------------|-------------------------------|-------------|
| postgres   | Database                      | 5432        |
| redis      | Cache                         | 6379        |
| keycloak   | Identity Provider             | 8081 (host) |
| backend    | Spring Boot API               | 8082        |
| frontend   | Angular Application           | 80          |
| grafana    | Monitoring Dashboard          | 3000        |
| loki       | Log aggregation               | 3100        |
| promtail   | Log shipping                  | -           |

---

## 🧑‍💻 Development


## Backend

### ⚙️ Environment Configuration

Backend expects these environment variables (configured in `docker-compose.yml`):

- `DATASOURCE_URL` : Database connection URL
- `DATASOURCE_USER` : Database username
- `DATASOURCE_PWD` : Database password
- `CACHE_URL` : Redis cache URL
- `CACHE_PWD` : Redis cache password
- `KEYCLOAK_URL` : Keycloak URL for token validation


Run Spring Boot app locally:

```bash
cd backend
./mvnw clean package
./mvnw spring-boot:run
```

### Frontend

Install dependencies and start dev server:

```bash

cd frontend
npm install
ng serve --proxy-config proxy.conf.json
```

---

## 📊 Monitoring and Logs

- **Grafana**: Visualize logs and metrics
- **Loki**: Stores application logs
- **Promtail**: Collects logs from Docker containers, by scrapping externally mounted directory ``./logs``
- **Prometheus**: Scrapes metrics from backend and other services
- **Backend**: Exposes metrics at `/actuator/prometheus`, writes logs to `/logs` directory for Promtail to collect
- **Frontend**: No metrics, but forwards application logs to the backend which are collected by Promtail using the same mechanism as above.

Grafana default credentials:

```
Username: admin
Password: admin
```

---