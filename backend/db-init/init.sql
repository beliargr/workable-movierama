CREATE USER movierama_app_user WITH PASSWORD 'movieramapassword';
CREATE SCHEMA movierama AUTHORIZATION movierama_app_user;
GRANT ALL ON SCHEMA movierama TO movierama_app_user;

CREATE USER keycloak_user WITH PASSWORD 'keycloak';
CREATE SCHEMA keycloak AUTHORIZATION keycloak_user;
GRANT ALL ON SCHEMA keycloak TO keycloak_user;

-- Optionally grant privileges to the mainadmin on all schemas
GRANT ALL PRIVILEGES ON SCHEMA movierama TO dbadmin;
GRANT ALL PRIVILEGES ON SCHEMA keycloak TO dbadmin;