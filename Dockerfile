FROM eclipse-temurin:17-jdk

WORKDIR /app

# Crear aplicación Java
RUN echo 'public class App { public static void main(String[] args) { System.out.println("🎉 ¡SYSTEMPOS EN DOCKER FUNCIONANDO!"); } }' > App.java

# Compilar y crear JAR en UN SOLO LAYER
RUN javac App.java && \
    echo "Main-Class: App" > manifest.txt && \
    jar cfm app.jar manifest.txt App.class && \
    ls -la && \
    java -jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
