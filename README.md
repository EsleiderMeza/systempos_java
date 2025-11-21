# 🛒 SystemPOS - Java Spring Boot

Sistema de punto de venta desarrollado en **Java Spring Boot** con arquitectura moderna y contenerización Docker.

## 🚀 Características

- ✅ **Spring Boot 3.2** - Framework moderno
- ✅ **Spring Security** - Autenticación y autorización
- ✅ **Spring Data JPA** - Persistencia con MySQL
- ✅ **Thymeleaf** - Motor de plantillas
- ✅ **Docker** - Contenerización completa
- ✅ **Maven** - Gestión de dependencias

## 🏗️ Arquitectura
com.systempos/
├── controller/ # Controladores MVC
├── service/ # Lógica de negocio
├── repository/ # Acceso a datos
├── model/ # Entidades JPA
├── config/ # Configuraciones
└── api/ # Endpoints REST


## 📋 Prerrequisitos

- **Java 17**
- **Maven 3.9+**
- **MySQL 8.0+**
- **Docker** (opcional)

## 🐳 Ejecución con Docker

```bash
# Clonar repositorio
git clone https://github.com/EsleiderMeza/systempos_java.git
cd systempos_java

# Ejecutar con Docker Compose
docker compose up -d

# La aplicación estará en: http://localhost:8081


# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar aplicación
mvn spring-boot:run

# Empaquetar
mvn clean package



spring.datasource.url=jdbc:mysql://localhost:3306/systempos_db
spring.datasource.username=root
spring.datasource.password=password
server.port=8081

package
🌐 Acceso
Aplicación: http://localhost:8081

Base de datos: MySQL en localhost:3306

Usuario por defecto: admin

Contraseña: admin123




