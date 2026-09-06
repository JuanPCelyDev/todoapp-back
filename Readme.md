# Todo App - Backend

API REST para gestión de tareas personales, desarrollada en Java spring boot como parte de prueba técnica Fullstack.

## Tecnologías utilizadas

- **Java 21**
- **Spring Boot 4.1.1** (Web, Data JPA, Security, Validation)
- **PostgreSQL** como base de datos
- **Flyway** para migraciones controladas de base de datos
- **JWT** (jjwt) para autenticación
- **Swagger / OpenAPI** (springdoc-openapi) para documentación interactiva
- **Docker & Docker Compose** para el entorno de base de datos local
- **Maven** como gestor de dependencias
- **Lombok** para reducción de boilerplate

## Arquitectura

El proyecto sigue una arquitectura por capas (controller-service-repository), adecuada para el alcance de este proyecto:
com.pruebatecnica.todoapp
├── controller/ # Endpoints REST
├── service/ # Lógica de negocio
├── repository/ # Acceso a datos (Spring Data JPA)
├── entity/ # Entidades JPA y enums de dominio
├── dto/ # Objetos de transferencia (request/response)
├── security/ # JWT, filtros y configuración de autenticación
├── exception/ # Manejo centralizado de excepciones
└── config/ # Configuración general (Swagger, Security)


## Requisitos previos

- Java 21
- Docker y Docker Compose
- Maven (o usar el wrapper `mvnw` incluido, no requiere instalación)

## Configuración del entorno local

### 1. Clonar el repositorio

```bash
git clone JuanPCelyDev/todoapp-back
cd todoapp-back
```

### 2. Configurar variables de entorno

Copia el archivo de ejemplo y complétalo con tus propios valores:

```bash
cp .env.example .env
```

Variables requeridas en `.env`:


### 3. Levantar la base de datos con Docker

```bash
docker compose up -d
```

Esto levanta un contenedor de PostgreSQL en el puerto `5432`. Si el puerto no está disponible cambiarlo por otro como `5433`

### 4. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

Al arrancar, Flyway ejecuta automáticamente las migraciones y crea las tablas necesarias.

La API queda disponible en: `http://localhost:8080`

## Documentación interactiva (Swagger)

Una vez la aplicación esté corriendo, la documentación interactiva está disponible en: http://localhost:8080/swagger-ui/index.html


Desde ahí puedes probar todos los endpoints, incluyendo autenticación (botón "Authorize" con el JWT obtenido del login).

## Despliegue

La API está desplegada en Render: https://todoapp-back-e20s.onrender.com/
Swagger en producción: https://todoapp-back-e20s.onrender.com/swagger-ui/index.html

## Endpoints principales

### Autenticación

| Método | Endpoint | Descripción | Requiere token |
|---|---|---|---|
| POST | `/api/users/register` | Registro de usuario | No |
| POST | `/api/users/login` | Login (devuelve JWT) | No |

### Tareas

| Método | Endpoint | Descripción | Requiere token |
|---|---|---|---|
| POST | `/api/tasks` | Crear una tarea | Sí |
| GET | `/api/tasks` | Listar tareas del usuario (filtro opcional `?status=`) | Sí |
| PATCH | `/api/tasks/{id}` | Actualizar tarea (parcial) | Sí |
| DELETE | `/api/tasks/{id}` | Eliminar tarea | Sí |

Para los endpoints protegidos, incluir el header: Authorization: Bearer <token>


## Seguridad

- Contraseñas hasheadas con **BCrypt**.
- Autenticación stateless mediante **JWT** (expiración configurable, actualmente 24 horas).
- Validación de propiedad de recursos: un usuario solo puede ver/modificar/eliminar sus propias tareas.

## Decisiones de diseño relevantes

- **UUID como clave primaria**: se prefirió sobre IDs autoincrementales para evitar la enumeración de recursos a través de la URL del API.
- **Actualización parcial de tareas (PATCH)**: permite modificar solo los campos necesarios sin reenviar el objeto completo.
- **Records de Java para DTOs**: se aprovechan las características nativas del lenguaje (Java 21) para objetos inmutables de transferencia de datos.
- **`ddl-auto: validate` + Flyway**: el esquema de base de datos se gestiona mediante migraciones versionadas, no autogenerado por Hibernate, para mayor control y trazabilidad.

## Uso de Inteligencia Artificial

Este proyecto fue desarrollado con el apoyo de IA (Claude, de Anthropic) como asistente técnico durante el desarrollo. El uso se detalla a continuación:

- **Explicación de conceptos y patrones**: uso de arquitectura por capas, JWT, Spring Security, Bean Validation.
- **Generación de modelo de base de datos y sus scripts de migración para Flyway**: Se uso el siguiente prompt: 
    ```text
        Actúa como un Arquitecto de Bases de Datos experto en PostgreSQL y Flyway.
        Necesito diseñar el modelo de base de datos para una API REST de una To-Do App y generar sus scripts de migración para Flyway.

        Especificaciones del Modelo:
        * Tabla users:
        - id: Clave primaria UUID (usando gen_random_uuid()).
        - name: VARCHAR NOT NULL.
        - email: VARCHAR NOT NULL UNIQUE.
        - password_hash: VARCHAR NOT NULL.

        * Tabla tasks:
        - id: Clave primaria UUID (usando gen_random_uuid()).
        - user_id: Clave foránea referenciando a users(id) con ON DELETE CASCADE.
        - title: VARCHAR(150) NOT NULL.
        - description: TEXT opcional (NULL).
        - status: VARCHAR(20) NOT NULL.
        - creation_date: TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP.
        - due_date: DATE opcional (NULL).

        Lo que debes entregar:
        1. Diagrama Entidad-Relación (ERD).
        2. Script de Migración de Flyway (V1__init_schema.sql):
        - Convención de nombres estándar SQL en minúsculas y snake_case.
        - Tipos de datos nativos adecuados para PostgreSQL.
        - Claves primarias, foráneas y restricciones (NOT NULL, UNIQUE, CHECK).
        - Índices recomendados para optimizar la búsqueda de tareas por user_id y por status.
- **Generar archivo de configuración JwtService**: Se uso el siguiente Prompt:
    ```text 
        Genera la clase JwtService.java para Spring Boot 4.1.1 usando la librería io.jsonwebtoken (jjwt) versión 0.12.6 o superior. Quiero usar firma simétrica HS256 leyendo el secreto y el tiempo de expiración desde application.yaml. Incluye métodos para generar el token inyectando roles, extraer el username, validar el token y verificar expiración.
- **Diagnóstico de errores**: apoyo en la resolución de problemas de configuración (Docker, variables de entorno, despliegue en Render).
- **Redacción de documentación**: este README.

Las decisiones de arquitectura, la validación del código generado, y los ajustes necesarios para el contexto específico del proyecto fueron realizados y revisados por el desarrollador.