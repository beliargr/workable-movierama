
# 🎬 Movierama

A full-stack web application to manage movies and ratings, built with:

- **Backend**: Spring Boot (Java 17), JPA, Redis, PostgreSQL
- **Frontend**: Angular 19, PrimeNG
- **Authentication**: Keycloak
- **Observability**: Loki, Promtail, Grafana, Prometheus

---

## 📂 Project Structure

```
backend/           # Spring Boot application
frontend/          # Angular frontend
docker-compose.yml # Multi-container stack
loki-config.yml    # Loki configuration
promtail-config.yml# Promtail configuration
```

---

## 🚀 Quick Start (Docker Compose)

Make sure you have **Docker** and **Docker Compose** installed.

1️⃣ Clone the repository:

```bash
git clone https://github.com/your-username/movierama.git
cd movierama
```

2️⃣ Build the backend and frontend Docker images (or use prebuilt images):

```bash
docker build -t movierama-be ./backend
docker build -t movierama-fe ./frontend
```

3️⃣ Start all services:

```bash
docker-compose up -d
```

4️⃣ Access the application:

- **Frontend UI:** [http://localhost](http://localhost)
- **Backend API:** [http://localhost:8082](http://localhost:8082)
- **Keycloak Admin:** [http://localhost:8081](http://localhost:8081)
- **Grafana:** [http://localhost:3000](http://localhost:3000)

---

## 🔑 Keycloak Credentials

Default admin credentials:

```
Username: keycloak
Password: keycloak
```

Make sure to:

- Log in to Keycloak
- Create the `movierama` realm
- Create clients and roles as needed

---

## ⚙️ Environment Configuration

The backend expects these environment variables (configured in `docker-compose.yml`):

- `DATASOURCE_URL`
- `DATASOURCE_USER`
- `DATASOURCE_PWD`
- `CACHE_URL`
- `CACHE_PWD`
- `KEYCLOAK_URL`

Example:

```yaml
environment:
  DATASOURCE_URL: jdbc:postgresql://postgres:5432/movierama?currentSchema=movierama
  DATASOURCE_USER: movierama_app_user
  DATASOURCE_PWD: movieramapassword
  CACHE_URL: redis
  CACHE_PWD: movierama_password
  KEYCLOAK_URL: http://keycloak:8080/realms/movierama
```

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

### Backend

Run Spring Boot app locally:

```bash
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
- **Promtail**: Collects logs from Docker containers

Grafana default credentials:

```
Username: admin
Password: admin
```

---

## 🛡️ Security Notes

- Change default passwords before deploying to production.
- Secure Docker and restrict network access.
- Configure HTTPS termination if exposing externally.

---

## 📄 License

This project is licensed under the MIT License.
