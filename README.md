# MetaMapa (Backend) — Sistema de mapeo colaborativo (TPA DDSI UTN FRBA 2025)

Backend del proyecto **MetaMapa**, una plataforma colaborativa para la **recopilación, gestión y visualización de hechos geolocalizados** (con componente temporal), provenientes de múltiples fuentes (carga manual, datasets/CSV y fuentes proxy), organizados en **colecciones**.

Frontend (proyecto separado): https://github.com/totopedroo/metamapa-tpa-ui

---

## Funcionalidades principales
- **Colecciones públicas**: listado, búsqueda y filtros de hechos (categoría, fecha, ubicación, fuente).
- Modos de navegación: **Irrestricto** (todos los hechos) y **Curado** (solo hechos con consenso).
- **Consenso configurable por colección** (mayoría simple / absoluta / múltiples menciones, según configuración).
- **API/servicios** para gestión (colecciones, fuentes, solicitudes, configuración).
- **Contribuciones**: alta de hechos y solicitudes de eliminación/modificación (incluye detección de spam).
- **Persistencia relacional** con ORM (JPA/Hibernate).
- **Importación masiva desde CSV** (+10.000 registros, según dataset usado en la cursada).
- **Exportación CSV** (hechos / estadísticas).
- **Servicio de estadísticas** (por provincia/categoría/hora, entre otras).

---

## Tech stack
- Java
- Spring Boot
- Maven
- JPA / Hibernate
- MySQL
- JWT (autenticación)

---

## Branches
- `main`: **versión final** del proyecto.
- Ramas `ENTREGA*`, `entrega*` y feature-branches: hitos/checkpoints de la cursada.

---

## Ejecutar en local

### Requisitos
- Java (JDK)
- Maven
- MySQL (local)

### 1) Clonar
```bash
git clone https://github.com/totopedroo/metamapa-tpa.git
cd metamapa-tpa
```

### 2) Configurar variables (DB / JWT / Proxy)
Este proyecto usa placeholders en `application.properties` y toma configuración desde variables de entorno.

Variables recomendadas:
```bash
# Base de datos
export DB_URL="jdbc:mysql://localhost:3306/metamapa_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true"
export DB_USER="root"
export DB_PASS=""

# JWT
export JWT_SECRET="cambia_esto_por_una_clave_larga"

# Proxy (si aplica en tu entorno)
export PROXY_DESASTRES_EMAIL=""
export PROXY_DESASTRES_PASSWORD=""
```

### 3) Ejecutar
```bash
mvn clean install
mvn spring-boot:run
```
El backend levanta por defecto en `http://localhost:8080`.

### Windows (Powershell)
Ejemplo:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/metamapa_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true"
$env:DB_USER="root"
$env:DB_PASS=""
$env:JWT_SECRET="cambia_esto_por_una_clave_larga"

mvn spring-boot:run
```

## Test y validación
### Ejecutar tests
```bash
mvn test
```

### Validar el proyecto (más exhaustivo)
```bash
mvn clean verify
```
Este comando puede incluir:
- tests
- validaciones de formato (checkstyle)
- chequeos de calidad y cobertura (según configuración del proyecto)

## Créditos
### Trabajo Práctico Anual — Diseño y Desarrollo de Sistemas de Información (DDSI) — UTN FRBA — 2025.
