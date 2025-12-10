# Étape 1 : On construit l'application avec Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# On compile et on crée le fichier .jar (en sautant les tests pour aller plus vite)
RUN mvn clean package -DskipTests

# Étape 2 : On prépare l'image finale légère pour lancer l'appli
FROM openjdk:17-jdk-slim
WORKDIR /app
# On récupère le .jar créé à l'étape 1
COPY --from=build /app/target/coach-assistant-back-0.0.1-SNAPSHOT.jar app.jar

# On ouvre le port 8080
EXPOSE 8080

# La commande de démarrage
ENTRYPOINT ["java","-jar","app.jar"]