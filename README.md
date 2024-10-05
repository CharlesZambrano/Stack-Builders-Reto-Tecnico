# Hacker News Web Crawler

## Descripción del Proyecto

Este proyecto es un web crawler desarrollado en Java utilizando **Spring Boot** y la **API de HackerNews** para realizar scraping [Hacker News](https://news.ycombinator.com/).Tomando las primeras 30 entradas que no existan en la Base de Datos de PostgreSQL. El sistema extrae información como el número de la entrada, el título, los puntos y el número de comentarios.

Además, permite realizar dos tipos de filtrados:

1. **Filtrar entradas con más de 5 palabras en el título**, ordenadas por el número de comentarios en orden descendente.
2. **Filtrar entradas con 5 palabras o menos en el título**, ordenadas por los puntos en orden descendente.

Se incluye un mecanismo de persistencia de datos usando **PostgreSQL** para almacenar los datos extraídos y un historial de solicitudes de filtrado, registrando la marca de tiempo de la solicitud y las entradas devueltas.

## Requisitos Previos

Antes de empezar, asegúrate de tener lo siguiente instalado:

- **Java 21** o superior
- **Docker** y **Docker Compose**
- **Gradle** (opcional si no usas los wrappers incluidos)

## Configuración del Proyecto

### 1. Compilar la aplicación

El proyecto usa **Gradle** para la gestión de dependencias. Puedes construir la aplicación con el siguiente comando:

```bash
./gradlew clean build
```

### 2. Configuración de Docker Compose

Este proyecto está diseñado para correr en un contenedor Docker. Para iniciar los servicios, incluido el contenedor de PostgreSQL y la aplicación, ejecuta:

```bash
docker-compose up
```

Esto iniciará dos contenedores:

- **postgres_hackernews**: Una base de datos PostgreSQL donde se almacenan las entradas de Hacker News y el historial de solicitudes de filtrado.
- **hacker_news**: La aplicación Java que expone una API para extraer los datos de Hacker News y aplicar los filtros solicitados.

### 3. Endpoints

La aplicación expone los siguientes endpoints para interactuar con el sistema:

- `GET /crawler/fetch`: Extrae las primeras 30 entradas de Hacker News.
- `GET /crawler/filter/more-than-five`: Filtra las entradas con más de 5 palabras en el título, ordenadas por comentarios.
- `GET /crawler/filter/less-than-five`: Filtra las entradas con 5 o menos palabras en el título, ordenadas por puntos.

Puedes acceder a la documentación Swagger para explorar y probar los endpoints en la siguiente URL:

```
http://localhost:8080/swagger-ui.html
```

O importar el archivo **api-docs.json** dentro de Postman.

### 4. Base de Datos

La base de datos se inicializa automáticamente al levantar el contenedor de PostgreSQL. El archivo SQL que se encuentra en `./db-init/hacker_news.sql` crea las siguientes tablas:

- **hacker_news_entry**: Almacena las entradas extraídas de Hacker News.
- **filter_request_history**: Almacena un registro de las solicitudes de filtrado, incluyendo el tipo de filtro aplicado y las entradas retornadas.

### 5. Propiedades de la Aplicación

Se utilizan diferentes configuraciones para los entornos de desarrollo y pruebas.

#### application-dev.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/hackernews
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

#### application-test.properties

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=create-drop
```

### 6. Pruebas

Para ejecutar las pruebas unitarias, puedes usar:

```bash
./gradlew test
```

Se ha configurado el perfil de prueba para usar una base de datos en memoria H2, por lo que las pruebas no afectan a la base de datos real.

## Desarrollo Adicional

- **Health Check**: La aplicación incluye un health check que puede ser monitoreado vía Docker para verificar la salud del servicio.
- **PostgreSQL Stored Procedures**: Se han definido procedimientos almacenados en PostgreSQL para realizar las operaciones de filtrado de manera eficiente.

## Tecnologías Utilizadas

- **Spring Boot** 3.1.6
- **Jsoup** 1.16.1
- **PostgreSQL** 14
- **Docker** y **Docker Compose**
- **MapStruct** para la conversión de DTOs
- **Lombok** para reducir el boilerplate en el código

## Ejecución Local

Para ejecutar la aplicación localmente sin Docker, sigue estos pasos:

1. Configura una instancia de PostgreSQL local o usa H2 para pruebas.
2. Actualiza las propiedades de conexión en `application-dev.properties` o crea un nuevo archivo de configuración.
3. Ejecuta la aplicación con:

```bash
./gradlew bootRun
```

## Contribuciones

Las contribuciones son bienvenidas. Si tienes sugerencias o encuentras errores, por favor abre un **issue** o envía un **pull request**.
