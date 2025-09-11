# API Documentation - MetaMapa Entrega 4

## Resumen de Implementación

Se ha completado exitosamente la **Entrega 4: Persistencia** con todas las funcionalidades requeridas:

### ✅ **Funcionalidades Implementadas:**

1. **Base de Datos y Persistencia**

   - Entidades JPA con campos de provincia y hora
   - Repositorios JPA con consultas personalizadas
   - Configuración MySQL completa

2. **Servicio de Estadísticas**

   - Estadísticas por provincia
   - Estadísticas por categoría
   - Estadísticas por hora del día
   - Detección de solicitudes spam

3. **Normalización de Datos**

   - Mapeo de categorías (ej: "incendio forestal" → "Incendio Forestal")
   - Normalización de provincias (ej: "CABA" → "Ciudad Autónoma de Buenos Aires")
   - Validación y normalización de fechas
   - Normalización de coordenadas

4. **Exportación CSV**

   - Exportación de hechos
   - Exportación de estadísticas
   - Filtros por provincia y categoría

5. **Búsqueda por Texto Libre (Bonus)**
   - Búsqueda en títulos, descripciones, categorías y provincias
   - Búsqueda avanzada con múltiples criterios
   - Búsqueda por palabras clave

## Endpoints de la API

### 📊 **Estadísticas**

#### `GET /estadisticas/provincia-mas-hechos`

Obtiene la provincia con mayor cantidad de hechos reportados.

**Respuesta:**

```json
{
  "provincia": "Buenos Aires",
  "cantidad": 150
}
```

#### `GET /estadisticas/categoria-mas-hechos`

Obtiene la categoría con mayor cantidad de hechos reportados.

**Respuesta:**

```json
{
  "categoria": "Incendio Forestal",
  "cantidad": 75
}
```

#### `GET /estadisticas/provincia-mas-hechos-categoria/{categoria}`

Obtiene la provincia con mayor cantidad de hechos de una categoría específica.

**Ejemplo:** `/estadisticas/provincia-mas-hechos-categoria/Incendio%20Forestal`

**Respuesta:**

```json
{
  "provincia": "Córdoba",
  "categoria": "Incendio Forestal",
  "cantidad": 25
}
```

#### `GET /estadisticas/hora-mas-hechos-categoria/{categoria}`

Obtiene la hora del día con mayor cantidad de hechos de una categoría específica.

**Ejemplo:** `/estadisticas/hora-mas-hechos-categoria/Accidente`

**Respuesta:**

```json
{
  "hora": "14:30:00",
  "categoria": "Accidente",
  "cantidad": 12
}
```

#### `GET /estadisticas/solicitudes-spam`

Cuenta cuántas solicitudes de eliminación son spam.

**Respuesta:**

```json
{
  "solicitudesSpam": 5
}
```

#### `GET /estadisticas/generales`

Obtiene estadísticas generales del sistema.

**Respuesta:**

```json
{
  "provinciaConMasHechos": {
    "provincia": "Buenos Aires",
    "cantidad": 150
  },
  "categoriaConMasHechos": {
    "categoria": "Incendio Forestal",
    "cantidad": 75
  },
  "solicitudesSpam": {
    "solicitudesSpam": 5
  },
  "coleccionesConHechos": [...]
}
```

#### `GET /estadisticas/coleccion/{coleccionId}`

Obtiene estadísticas por colección específica.

### 📤 **Exportación CSV**

#### `GET /exportar/hechos`

Exporta todos los hechos a CSV.

**Respuesta:** Archivo CSV descargable

#### `GET /exportar/hechos/provincia/{provincia}`

Exporta hechos por provincia a CSV.

**Ejemplo:** `/exportar/hechos/provincia/Buenos%20Aires`

#### `GET /exportar/hechos/categoria/{categoria}`

Exporta hechos por categoría a CSV.

**Ejemplo:** `/exportar/hechos/categoria/Incendio%20Forestal`

#### `GET /exportar/estadisticas`

Exporta estadísticas generales a CSV.

#### `GET /exportar/estadisticas/coleccion/{coleccionId}`

Exporta estadísticas por colección a CSV.

### 🔍 **Búsqueda por Texto Libre (Bonus)**

#### `GET /buscar/texto-libre?texto={texto}`

Realiza búsqueda por texto libre en títulos, descripciones, categorías y provincias.

**Ejemplo:** `/buscar/texto-libre?texto=incendio`

**Respuesta:**

```json
[
  {
    "idHecho": 1,
    "titulo": "Incendio en el bosque",
    "descripcion": "Gran incendio forestal...",
    "categoria": "Incendio Forestal",
    "provincia": "Córdoba",
    "horaAcontecimiento": "14:30:00",
    ...
  }
]
```

#### `GET /buscar/avanzada?texto={texto}&categoria={categoria}&provincia={provincia}`

Búsqueda avanzada con múltiples criterios.

#### `GET /buscar/palabras-clave?texto={texto}`

Búsqueda por palabras clave (divide el texto en palabras).

### 🔧 **Normalización**

#### `POST /normalizar/categoria`

Normaliza una categoría.

**Request:**

```json
{
  "categoria": "incendio forestal"
}
```

**Respuesta:**

```json
{
  "original": "incendio forestal",
  "normalizada": "Incendio Forestal"
}
```

#### `POST /normalizar/provincia`

Normaliza una provincia.

**Request:**

```json
{
  "provincia": "caba"
}
```

**Respuesta:**

```json
{
  "original": "caba",
  "normalizada": "Ciudad Autónoma de Buenos Aires"
}
```

#### `POST /normalizar/fecha`

Normaliza una fecha.

**Request:**

```json
{
  "fecha": "15/3/2024"
}
```

**Respuesta:**

```json
{
  "original": "15/3/2024",
  "normalizada": "2024-03-15",
  "valida": true
}
```

#### `POST /normalizar/titulo`

Normaliza un título.

#### `POST /normalizar/coordenadas`

Normaliza coordenadas.

**Request:**

```json
{
  "latitud": -34.6037,
  "longitud": -58.3816
}
```

**Respuesta:**

```json
{
  "latitud_original": -34.6037,
  "latitud_normalizada": -34.6037,
  "longitud_original": -58.3816,
  "longitud_normalizada": -58.3816,
  "latitud_valida": true,
  "longitud_valida": true
}
```

## Nuevos Campos en Entidades

### Hecho

- `provincia` (String): Provincia donde ocurrió el hecho
- `horaAcontecimiento` (LocalTime): Hora del día cuando ocurrió el hecho

### SolicitudEliminacion

- `esSpam` (boolean): Indica si la solicitud es considerada spam

## Mapeo de Normalización

### Categorías

- "incendio forestal", "fuego forestal", "incendio", "fuego" → "Incendio Forestal"
- "inundacion", "inundaciones", "crecida", "desborde" → "Inundación"
- "terremoto", "sismo", "temblor" → "Terremoto"
- "accidente", "accidente automovilistico", "choque" → "Accidente"

### Provincias

- "caba" → "Ciudad Autónoma de Buenos Aires"
- "buenos aires", "bs as", "bs.as." → "Buenos Aires"
- "córdoba", "cordoba" → "Córdoba"
- "santa fe", "santafe" → "Santa Fe"
- Y muchas más...

## Configuración de Base de Datos

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/metamapa_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=${DB_PASS:}
spring.jpa.hibernate.ddl-auto=update
```

## Tecnologías Utilizadas

- **Spring Boot 3.4.5**
- **Spring Data JPA**
- **MySQL 8**
- **OpenCSV** para exportación
- **Lombok** para reducción de código
- **Jackson** para serialización JSON

## Estado de Implementación

✅ **COMPLETADO AL 100%**

- ✅ Persistencia del modelo de objetos
- ✅ Normalización de la información
- ✅ Servicio de Estadísticas
- ✅ Soporte para incorporación de videos e imágenes
- ✅ Exportación de datos en formato CSV
- ✅ Soporte para búsqueda por texto libre (Bonus)
- ✅ Repositorios JPA para cada entidad
- ✅ Endpoints actualizados con nuevos campos
- ✅ DTOs actualizados

La implementación está lista para ser utilizada y cumple con todos los requerimientos de la Entrega 4.
