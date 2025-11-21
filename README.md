# SystemPOS – Sistema de Punto de Venta en Java + Spring Boot + MySQL (Docker)

SystemPOS es un sistema de punto de venta desarrollado en **Java + Spring Boot**, utilizando **MySQL 8** como base de datos. El proyecto está completamente dockerizado para facilitar su despliegue y funcionamiento en cualquier entorno.

---

## 🚀 Características principales

* Backend desarrollado en **Java 17** + **Spring Boot**
* Base de datos **MySQL 8**
* Contenedores usando **Docker Compose**
* Persistencia de datos en volúmenes
* API expuesta en `http://localhost:8081` (según configuración)

---

## 📦 Requisitos previos

Antes de ejecutar el proyecto, es necesario tener instalado:

* **Docker**
* **Docker Compose**
* **Git** (opcional, para clonar el repositorio)

---

## 🐳 Ejecución con Docker

1. Clona el repositorio:

```
git clone https://github.com/TU_USUARIO/TU_REPO.git
cd systempos
```

2. Construye e inicia los contenedores:

```
docker compose -f docker-compose.java.yml up -d --build
```

3. Verifica que los servicios estén levantados:

```
docker compose -f docker-compose.java.yml ps
```

4. Para ver logs de la aplicación Java:

```
docker compose -f docker-compose.java.yml logs -f java-app
```

---

## 🔧 Configuración de Base de Datos

Tu aplicación Spring Boot se conecta usando:

```
jdbc:mysql://mysql:3306/systempos
```

Configura `application.properties` así:

```
spring.datasource.url=jdbc:mysql://mysql:3306/systempos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456
```

> Nota: el nombre del host (`mysql`) debe ser el nombre del servicio definido en Docker Compose.

---

## 🛠 Construcción manual del backend

Si deseas ejecutar la aplicación fuera de Docker:

```
./mvnw clean package
java -jar target/systempos.jar
```

---

## 🗂 Estructura del proyecto

```
systempos/
│
├── src/main/java/...        Código fuente
├── src/main/resources       Configuración
├── docker-compose.java.yml  Servicios en Docker
├── Dockerfile               Imagen del backend
└── README.md                Este archivo
```

---

## 🧪 Pruebas

Puedes probar si el backend está funcionando con:

```
curl http://localhost:8081
```

---

## 📄 Licencia

Este proyecto es de uso personal/educativo. Ajusta la licencia según tus necesidades.

---

## 👤 Autor

Desarrollado por **Esleider Jesús Meza** (*Slade Hermesa*).

```
```
