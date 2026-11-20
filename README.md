# Electromechanics Backend

Backend del sistema de gestión de mantención industrial de **Electromechanics Ingeniería SpA**. Permite registrar la jornada de los técnicos en terreno, centralizar los reportes de mantención y dar seguimiento a su estado, con control de acceso por rol.

Proyecto académico desarrollado para la asignatura **DSY1105 – Desarrollo de Aplicaciones Móviles** (Duoc UC). Es el backend de una aplicación Android en Kotlin.

---

## Arquitectura

El sistema está compuesto por tres microservicios independientes y una base de datos PostgreSQL compartida, con un schema por servicio.

```
                    ┌─────────────────────┐
  Cliente (Android) │   usuario-service   │ :8081
  ───────────────►  │  registro · login   │
                    │  usuarios · roles   │
                    └──────────┬──────────┘
                               │ emite JWT
                               ▼
              ┌────────────────────────────────┐
              │  jornada-service │ reporte-service
              │      :8082       │     :8083
              │  valida JWT      │ valida JWT
              └────────┬─────────┴────────┬────┘
                       ▼                  ▼
                 ┌──────────────────────────────┐
                 │ PostgreSQL 18                │ :5433 (host)
                 │  schema usuario · jornada ·  │
                 │  reporte                     │
                 └──────────────────────────────┘
```

Cada servicio valida los tokens por su cuenta, con la misma clave secreta. No hay llamadas entre servicios para autenticar una petición.

### Microservicios

| Servicio | Puerto | Responsabilidad |
|---|---|---|
| **usuario-service** | 8081 | Registro, autenticación (login con JWT), administración de usuarios y roles |
| **jornada-service** | 8082 | Inicio y término de jornadas de trabajo, con geolocalización opcional |
| **reporte-service** | 8083 | Creación de reportes de mantención, seguimiento de estado y consulta |

---

## Tecnologías

| Área | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.1.2 |
| Web | Spring Web MVC (Tomcat 11) |
| Seguridad | Spring Security, JWT (jjwt 0.12.6), BCrypt |
| Persistencia | Spring Data JPA, Hibernate 7 |
| Base de datos | PostgreSQL 18 (producción y Docker), H2 (pruebas) |
| Validación | Jakarta Bean Validation |
| Documentación API | springdoc-openapi 3.0.0 (Swagger UI) |
| Pruebas | JUnit 5, Mockito, AssertJ, MockMvc |
| Contenedores | Docker, Docker Compose |
| Build | Maven (wrapper incluido) |

---

## Roles y permisos

| Acción | Trabajador | Supervisor |
|---|:---:|:---:|
| Registrarse e iniciar sesión | ✔ | ✔ |
| Iniciar y terminar su jornada | ✔ | |
| Ver sus propias jornadas | ✔ | |
| Ver todas las jornadas (o filtrar por id) | | ✔ |
| Crear reportes | ✔ | |
| Ver sus propios reportes | ✔ | |
| Ver todos los reportes (o filtrar por id) | | ✔ |
| Cambiar el estado de un reporte | | ✔ |
| Listar trabajadores (o filtrar por id) | | ✔ |
| Cambiar el rol de un trabajador | | ✔ (no el suyo) |

Los usuarios nuevos se crean siempre como `TRABAJADOR`. El rol `SUPERVISOR` se asigna directamente en la base de datos.

---

## Endpoints principales

**usuario-service** (`:8081`)

| Método | Ruta | Acceso |
|---|---|---|
| POST | `/api/v1/usuarios/registro` | Público |
| POST | `/api/v1/usuarios/login` | Público |
| GET | `/api/v1/usuarios` | Supervisor |
| GET | `/api/v1/usuarios/{id}` | Supervisor |
| GET | `/api/v1/usuarios/rut/{rut}` | Supervisor |
| PATCH | `/api/v1/usuarios/{id}/rol` | Supervisor |

**jornada-service** (`:8082`)

| Método | Ruta | Acceso |
|---|---|---|
| POST | `/api/v1/jornadas/iniciar` | Trabajador |
| PUT | `/api/v1/jornadas/finalizar` | Trabajador |
| GET | `/api/v1/jornadas/mias` | Trabajador |
| GET | `/api/v1/jornadas` | Supervisor |
| GET | `/api/v1/jornadas/{id}` | Supervisor |
| GET | `/api/v1/jornadas/usuario/{usuarioId}` | Supervisor |

**reporte-service** (`:8083`)

| Método | Ruta | Acceso |
|---|---|---|
| POST | `/api/v1/reportes` | Trabajador |
| GET | `/api/v1/reportes/mios` | Trabajador |
| GET | `/api/v1/reportes` | Supervisor |
| GET | `/api/v1/reportes/{id}` | Supervisor |
| GET | `/api/v1/reportes/usuario/{usuarioId}` | Supervisor |
| GET | `/api/v1/reportes/estado/{estado}` | Supervisor |
| PATCH | `/api/v1/reportes/{id}/estado` | Supervisor |

Cada servicio tiene su documentación interactiva en `/swagger-ui.html`.

---

## Ejecución con Docker

Requisitos: Docker Desktop, Git.

```powershell
git clone https://github.com/BrandonLisboa/Electromechanics_backend.git
cd Electromechanics_backend
Copy-Item .env.example .env
```

Edita `.env` con tu contraseña de PostgreSQL y tu `JWT_SECRET` (al menos 32 caracteres). Luego:

```powershell
docker compose up --build -d
docker compose ps
```

Para la guía completa de instalación, creación del supervisor y pruebas, revisa [`despliegue.txt`](despliegue.txt).

---

## Ejecución sin Docker

Para desarrollo, cada servicio puede correr por separado con Maven. Define las variables de entorno antes de iniciar:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/electromechanics"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="tu_contraseña"
$env:JWT_SECRET="tu_clave_de_al_menos_32_caracteres"
./mvnw spring-boot:run
```

Las pruebas unitarias y de integración se ejecutan con:

```powershell
./mvnw clean test
```

Usan H2 en memoria, así que no necesitan PostgreSQL.

---

## Estructura del repositorio

```
Electromechanics_backend/
├── docker-compose.yml        Orquestación de los cuatro contenedores
├── .env.example              Plantilla de variables de entorno
├── db/init/
│   └── 01-schemas.sql        Creación de schemas al primer arranque
├── usuario-service/          Microservicio de usuarios y autenticación
├── jornada-service/          Microservicio de jornadas
├── reporte-service/          Microservicio de reportes
├── despliegue.txt            Guía paso a paso de despliegue
└── README.md
```

Cada microservicio sigue la misma organización: `controller`, `service`, `repository`, `model`, `dto`, `config` (seguridad, JWT y Swagger) y `exception`.

---

## Seguridad

- Las contraseñas se almacenan cifradas con **BCrypt**. Nunca se guardan en texto plano.
- Los tokens JWT se firman con HMAC-SHA256 y expiran a las 8 horas.
- Las credenciales y la clave JWT se leen de variables de entorno. No están en el código.
- Las respuestas nunca incluyen el hash de la contraseña.
- El login responde igual si el RUT no existe o la contraseña es incorrecta, para no revelar qué RUT está registrado.

---

## Autores

Brandon Lisboa y Camilo Pedraza – Duoc UC, sede Maipú.