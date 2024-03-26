FROM node:16.17.0-alpine as tcapp

WORKDIR /app

COPY package.json .

RUN npm install

EXPOSE 3000

COPY . .

RUN npm run build

# nginx

FROM nginx:1.25-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

WORKDIR /usr/share/nginx/html

RUN rm -f ./*

COPY --from=tcapp /app/build .

EXPOSE 80

ENTRYPOINT [ "nginx", "-g", "daemon off;" ]