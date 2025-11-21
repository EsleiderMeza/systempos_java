# ============================
#   STAGE 1 — Build con Maven
# ============================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Crear jar sin tests
RUN mvn -B -DskipTests clean package


# ============================
#   STAGE 2 — Imagen final
# ============================
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar el jar desde el build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
