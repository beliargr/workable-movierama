### STAGE 1: Build ###
FROM node:22.17-alpine AS build
ARG env
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
RUN npm config delete proxy
RUN npm config delete http-proxy
RUN npm config delete https-proxy
COPY . .
RUN npm run build:$env

### STAGE 2: Run ###
FROM nginx:latest
RUN apt update && apt dist-upgrade -y && apt install -y libcap2-bin procps
COPY --from=build /usr/src/app/dist/browser /usr/share/nginx/html

COPY ./nginx.conf /etc/nginx/nginx.conf

COPY ./mime.types /etc/nginx/conf/mime.types
# COPY ./proxy.conf /etc/nginx/proxy.conf
# COPY ./fastcgi.conf /etc/nginx/fastcgi.conf

