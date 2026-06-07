# Music Downloader Platform - Arquitectura y Plan Maestro v1.0

## 1. Visión del Proyecto

Music Downloader Platform es una plataforma SaaS multiusuario que permite descargar música y videos desde URLs compatibles con yt-dlp y almacenarlos automáticamente en Google Drive.

El objetivo del proyecto es:

* Aprender arquitectura de software profesional.
* Aplicar Hexagonal Architecture.
* Aplicar Event Driven Architecture.
* Implementar patrones de diseño.
* Utilizar CI/CD profesional.
* Construir un portfolio sólido.
* Documentar todo el proceso mediante SDD (Specification Driven Development).
* Generar contenido técnico para LinkedIn.

La plataforma será utilizada inicialmente por el autor del proyecto, pero será diseñada desde el principio como una solución multiusuario.

---

# 2. Objetivos Funcionales

## Usuarios

Los usuarios podrán:

* Registrarse.
* Iniciar sesión.
* Vincular una cuenta de Google.
* Descargar música.
* Descargar videos.
* Descargar playlists.
* Visualizar progreso en tiempo real.
* Consultar historial de descargas.
* Consultar metadatos.
* Gestionar sus descargas.

---

## Administradores

Los administradores podrán:

* Ver todos los usuarios.
* Ver todas las descargas.
* Bloquear usuarios.
* Habilitar usuarios.
* Consultar métricas globales.
* Consultar errores del sistema.
* Consultar actividad general.
* Acceder a dashboard SaaS.

---

# 3. Tecnologías Seleccionadas

## Backend

* Java 21
* Spring Boot
* Maven

## Persistencia

* PostgreSQL
* Flyway

## Mensajería

* Apache Kafka

## Frontend

* Angular

## Mobile

* Flutter (fase futura)

## Worker

* Python
* FastAPI o DJango
* yt-dlp
* FFmpeg

## Almacenamiento

* Google Drive

## Base de Datos

* Supabase PostgreSQL

## Infraestructura

* Docker
* Render

## Testing

* JUnit 5
* Mockito
* AssertJ
* Testcontainers
* Jacoco

## CI/CD

* GitHub Actions

---

# 4. Arquitectura General

Frontend Angular
↓
Backend Spring Boot
↓
Kafka
↓
Python Worker
↓
Google Drive

Componentes:

* Angular
* Spring Boot
* PostgreSQL
* Kafka
* FastAPI o DJango
* yt-dlp
* FFmpeg
* Google OAuth
* Google Drive API

---

# 5. Arquitectura Elegida

Hexagonal Architecture (Ports & Adapters)

Estructura:

domain/
application/
infrastructure/
presentation/

---

## Domain

Contiene:

* Entidades
* Value Objects
* Reglas de negocio
* Interfaces (Ports)
* Enums

El dominio NO debe conocer:

* Spring
* PostgreSQL
* Kafka
* Google
* FastAPI o DJango
* yt-dlp
* Docker

---

## Application

Contiene:

* Use Cases
* Commands
* Queries
* DTOs internos

Ejemplos:

* RegisterUserUseCase
* LoginUserUseCase
* CreateDownloadJobUseCase
* ConnectGoogleDriveUseCase
* GetDownloadsUseCase

---

## Infrastructure

Contiene implementaciones concretas:

* JPA
* PostgreSQL
* Kafka
* Google Drive
* OAuth
* Python Worker

---

## Presentation

Contiene:

* Controllers
* Request DTOs
* Response DTOs
* WebSocket Controllers

---

# 6. Patrones de Diseño

## Strategy

Responsabilidad:

Seleccionar comportamiento de descarga.

Implementaciones:

* AudioDownloadStrategy
* VideoDownloadStrategy
* PlaylistDownloadStrategy

---

## Factory

Responsabilidad:

Seleccionar Strategy.

Clase:

DownloadStrategyFactory

---

## Adapter

Responsabilidad:

Integrar servicios externos.

Implementaciones:

* GoogleDriveAdapter
* KafkaAdapter
* OAuthAdapter
* PostgresAdapter
* PythonWorkerAdapter

---

## Facade

Responsabilidad:

Orquestar procesos complejos.

Clase:

DownloadFacade

Ejemplo:

* Crear Job
* Publicar Evento
* Guardar Estado
* Notificar WebSocket

---

## Observer

Implementado mediante eventos Kafka.

Eventos:

* DownloadRequested
* DownloadStarted
* DownloadProgress
* DownloadCompleted
* DownloadFailed

---

## Builder

Implementado mediante Lombok.

Utilizado en:

* User
* DownloadJob
* Playlist
* MediaFile

---

# 7. Event Driven Architecture

Se utilizará Apache Kafka.

Motivación:

* Aprendizaje.
* Portfolio.
* Arquitectura distribuida.
* Desacoplamiento entre Java y Python.

No se seleccionó Kafka por volumen de tráfico sino por objetivos de aprendizaje y diseño.

---

# 8. Topics Kafka

## download.requested

Productor:

Backend Java

Consumidor:

Python Worker

---

## download.started

Productor:

Python Worker

Consumidor:

Backend Java

---

## download.progress

Productor:

Python Worker

Consumidor:

Backend Java

---

## download.completed

Productor:

Python Worker

Consumidor:

Backend Java

---

## download.failed

Productor:

Python Worker

Consumidor:

Backend Java

---

## upload.started

Productor:

Backend Java

---

## upload.completed

Productor:

Backend Java

---

# 9. Flujo Completo

Usuario
↓
Pega URL
↓
POST /downloads
↓
DownloadFacade
↓
CreateDownloadJobUseCase
↓
Guardar Job
↓
Kafka Producer
↓
download.requested
↓
Python Worker
↓
yt-dlp
↓
FFmpeg
↓
download.progress
↓
Kafka
↓
Backend
↓
WebSocket
↓
Frontend
↓
Usuario ve porcentaje
↓
Descarga terminada
↓
Subida a Google Drive
↓
download.completed
↓
Estado COMPLETED

---

# 10. Google Drive

Cada usuario tendrá su propia cuenta de Google vinculada.

Durante la primera vinculación:

Se crearán automáticamente:

Music Downloader/
├── Music/
└── Videos/

Los archivos se almacenarán automáticamente según su tipo.

Audio → Music

Video → Videos

Posteriormente el usuario podrá reorganizar sus archivos manualmente desde Google Drive.

---

# 11. Metadatos

Para archivos de audio se almacenará toda la información disponible.

Campos:

* Title
* Artist
* Album
* Duration
* Thumbnail
* Upload Date
* Channel
* Genre (si existe)

Los metadatos se guardarán en:

1. PostgreSQL
2. Etiquetas ID3 dentro del archivo

Esto permite compatibilidad con:

* Samsung Music
* Apple Music
* PowerAmp
* Spotify Local Files

---

# 12. Playlists

Las playlists estarán soportadas desde la V1.

Modelo:

Playlist
├── Song 1
├── Song 2
├── Song 3

Cada canción genera su propio DownloadJob.

Ventajas:

* Reintentos individuales.
* Tolerancia a fallos.
* Mejor seguimiento.
* Mejor escalabilidad.

---

# 13. WebSockets

Se utilizarán WebSockets para mostrar progreso en tiempo real.

Ejemplo:

{
"jobId": "123",
"status": "DOWNLOADING",
"progress": 67
}

El usuario podrá visualizar el avance sin refrescar la página.

---

# 14. Modelo Inicial de Dominio

## User

* id
* email
* password
* role
* status
* createdAt

---

## GoogleAccount

* id
* userId
* googleId
* accessToken
* refreshToken
* expiresAt

---

## DownloadJob

* id
* userId
* status
* type
* progress
* sourceUrl
* createdAt

---

## Playlist

* id
* ownerId
* title
* url

---

## MediaFile

* id
* jobId
* title
* artist
* album
* duration
* thumbnailUrl
* genre
* channel
* uploadDate
* googleDriveFileId

---

# 15. Estados del Job

* PENDING
* QUEUED
* DOWNLOADING
* PROCESSING
* UPLOADING
* COMPLETED
* FAILED

---

# 16. Seguridad

Autenticación:

* JWT

Autorización:

* ROLE_USER
* ROLE_ADMIN

Google:

* OAuth2

Persistencia:

* PostgreSQL

---

# 17. Testing

Cobertura mínima objetivo:

80%

---

## Unit Tests

Herramientas:

* JUnit 5
* Mockito
* AssertJ

---

## Integration Tests

Herramientas:

* Spring Boot Test

---

## Testcontainers

Servicios:

* PostgreSQL
* Kafka

---

## Cobertura

* Jacoco

---

# 18. CI/CD

GitHub Actions

Pipeline Pull Request:

* Checkout
* Compile
* Unit Tests
* Integration Tests
* Testcontainers
* Coverage
* Build Docker

Pipeline Main:

* Build Docker
* Push Image
* Deploy Render

---

# 19. Docker

Todo el proyecto será dockerizado.

Servicios previstos:

* backend-java
* worker-python
* kafka
* postgres (local)

Producción:

* Backend en Render
* PostgreSQL en Supabase

---

# 20. Roadmap

## Fase 0

Fundaciones

* Hexagonal Architecture
* Docker
* PostgreSQL
* Flyway
* GitHub Actions
* Jacoco
* Convenciones
* Estructura base

---

## Fase 1

Usuarios y Seguridad

* Registro
* Login
* JWT
* Roles
* Refresh Tokens

---

## Fase 2

Google OAuth

* Vincular cuenta Google
* Persistir Tokens
* Crear carpetas

---

## Fase 3

Dominio de Descargas

* DownloadJob
* Playlist
* MediaFile

---

## Fase 4

Kafka

* Topics
* Producers
* Consumers
* Eventos

---

## Fase 5

Python Worker

* FastAPI
* Kafka Consumer
* yt-dlp
* FFmpeg

---

## Fase 6

WebSockets

* Tiempo real
* Notificaciones

---

## Fase 7

Metadatos

* PostgreSQL
* ID3 Tags
* Portadas
* Géneros
* Álbumes

---

## Fase 8

Administración SaaS

* Dashboard
* Métricas
* Auditoría

---

## Fase 9

Hardening

* Rate Limiting
* Monitoring
* Logging
* Alertas
* Observabilidad

---

# 21. Filosofía del Proyecto

Este proyecto prioriza:

* Buen diseño antes que velocidad.
* Arquitectura profesional.
* Documentación continua.
* Testing desde el inicio.
* Automatización.
* Aprendizaje profundo.
* Construcción de portfolio.
* Evolución guiada mediante SDD.

Todas las decisiones futuras deberán respetar la arquitectura hexagonal, los principios SOLID y los patrones definidos en este documento.
