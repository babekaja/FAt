# Étape 1 : builder avec l’image officielle Gradle + JDK
FROM gradle:jdk17 AS builder

# Définit le répertoire de travail
WORKDIR /workspace

# Copie tout le projet dans /workspace
COPY . .

# Rend gradlew exécutable et lance la task de production
RUN chmod +x gradlew \
 && ./gradlew wasmJsBrowserDevelopmentRun -t --no-daemon

# Étape 2 : serveur statique Nginx
FROM nginx:alpine

# Copie le résultat du build Gradle dans le dossier que Nginx sert
COPY --from=builder \
     /workspace/build/distributions \
     /usr/share/nginx/html

# Expose le port 80 et lance Nginx
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
