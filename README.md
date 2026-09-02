# LogiTrack Inventory System

Sistema web para la gestión de bodegas, productos, inventario y movimientos de una empresa.

El proyecto está desarrollado con **Java + Spring Boot**, **MySQL** y un frontend web con **HTML, CSS y JavaScript**.

---

## 📌 Descripción del proyecto

**LogiTrack S.A.** administra varias bodegas distribuidas en distintas ciudades. El sistema permite gestionar los productos almacenados, controlar el inventario y registrar los movimientos realizados dentro de las bodegas.

El sistema cuenta con autenticación mediante **JWT**, control de acceso por roles y almacenamiento seguro de contraseñas mediante **BCrypt**.

---

## 🏗️ Arquitectura

```text
LogiTrack
│
├── logitrack-backend
│   └── Spring Boot / Java
│
└── logitrack-frontend
    └── HTML / CSS / JavaScript
```

### Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Hibernate
- BCrypt
- MySQL

### Frontend

- HTML5
- CSS3
- JavaScript
- Fetch API
- Live Server

---

# 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java | Lenguaje principal del backend |
| Spring Boot | Desarrollo de la API REST |
| Spring Web | Creación de controladores y endpoints |
| Spring Data JPA | Persistencia y acceso a datos |
| Hibernate | ORM |
| Spring Security | Seguridad y autorización |
| JWT | Autenticación mediante tokens |
| BCrypt | Hash seguro de contraseñas |
| MySQL | Base de datos |
| HTML5 | Estructura del frontend |
| CSS3 | Diseño y estilos |
| JavaScript | Lógica del frontend |
| Live Server | Ejecución del frontend |

---

# 🔐 Seguridad

El sistema implementa autenticación y autorización mediante **JWT**.

Flujo:

```text
Usuario
   │
   ▼
Login
   │
   ▼
Backend
   │
   ├── Verifica email
   ├── Verifica contraseña
   └── Genera JWT
          │
          ▼
       Frontend
          │
          ▼
Authorization: Bearer TOKEN
          │
          ▼
       Endpoints protegidos
```

Las contraseñas se almacenan utilizando **BCrypt**.

### Roles

- `ADMIN`
- `EMPLEADO`

### Permisos

| Funcionalidad | ADMIN | EMPLEADO |
|---|:---:|:---:|
| Bodegas | ✅ | ✅ |
| Productos | ✅ | ✅ |
| Inventario | ✅ | ✅ |
| Movimientos | ✅ | ✅ |
| Reportes | ✅ | ✅ |
| Gestión de usuarios | ✅ | ❌ |
| Auditorías | ✅ | ❌ |

La creación de usuarios se realiza desde el sistema y está restringida al rol **ADMIN**.

---

# 🗄️ Base de datos

Base de datos:

```text
LogiTrack_DB
```

Usuario local:

```text
logiTrack
```

Contraseña local:

```text
logiTrack123!
```

> Estas credenciales corresponden al entorno de desarrollo local.

---

# 📊 Modelo de datos

El sistema cuenta con las siguientes tablas:

```text
usuarios
   │
   ├───────────────┐
   │               │
   ▼               ▼
bodegas        movimientos
                   │
                   ▼
          detalle_movimiento
                   │
                   ▼
               productos
                   │
                   ▼
              categorias

bodegas ─────── inventario ─────── productos

usuarios ─────── auditorias
```

---

## 👤 Tabla `usuarios`

| Campo | Descripción |
|---|---|
| `id_usuario` | Identificador del usuario |
| `nombre` | Nombre |
| `apellido` | Apellido |
| `email` | Correo electrónico |
| `password` | Contraseña almacenada mediante BCrypt |
| `rol` | `ADMIN` o `EMPLEADO` |
| `activo` | Estado del usuario |
| `fecha_creacion` | Fecha de creación |

---

## 🏢 Tabla `bodegas`

| Campo | Descripción |
|---|---|
| `id_bodega` | Identificador de la bodega |
| `nombre` | Nombre de la bodega |
| `ubicacion` | Ubicación |
| `capacidad` | Capacidad de almacenamiento |
| `encargado_id` | Usuario encargado |
| `activo` | Estado de la bodega |

`encargado_id` tiene relación con `usuarios`.

---

## 📦 Tabla `categorias`

| Campo | Descripción |
|---|---|
| `id_categoria` | Identificador |
| `nombre` | Nombre de la categoría |

El nombre de la categoría es único.

---

## 🛒 Tabla `productos`

| Campo | Descripción |
|---|---|
| `id_producto` | Identificador |
| `nombre` | Nombre del producto |
| `id_categoria` | Categoría |
| `precio` | Precio |
| `descripcion` | Descripción |
| `activo` | Estado del producto |

El precio se maneja con `DECIMAL(10,2)`.

---

## 📈 Tabla `inventario`

| Campo | Descripción |
|---|---|
| `id_inventario` | Identificador |
| `id_bodega` | Bodega |
| `id_producto` | Producto |
| `stock` | Cantidad disponible |
| `fecha_actualizacion` | Última actualización |

Existe una restricción única:

```sql
UNIQUE(id_bodega, id_producto)
```

Esto evita duplicar el mismo producto dentro de una misma bodega.

---

## 🔄 Tabla `movimientos`

Tipos de movimiento:

```text
ENTRADA
SALIDA
TRANSFERENCIA
```

| Campo | Descripción |
|---|---|
| `id_movimiento` | Identificador |
| `fecha` | Fecha del movimiento |
| `tipo` | Tipo de movimiento |
| `usuario_id` | Usuario que realiza la operación |
| `bodega_origen_id` | Bodega de origen |
| `bodega_destino_id` | Bodega destino |
| `observacion` | Observación |

---

## 📋 Tabla `detalle_movimiento`

| Campo | Descripción |
|---|---|
| `id_detalle` | Identificador |
| `movimiento_id` | Movimiento |
| `producto_id` | Producto |
| `cantidad` | Cantidad |
| `precio_unitario` | Precio unitario |

---

## 📝 Tabla `auditorias`

Tipos de operación:

```text
INSERT
UPDATE
DELETE
```

| Campo | Descripción |
|---|---|
| `id_auditoria` | Identificador |
| `tipo_operacion` | Tipo de operación |
| `fecha_hora` | Fecha y hora |
| `usuario_id` | Usuario responsable |
| `entidad` | Entidad afectada |
| `entidad_id` | ID de la entidad |
| `valores_anteriores` | Valores anteriores |
| `valores_nuevos` | Valores nuevos |

Los valores anteriores y nuevos pueden almacenarse en formato JSON.

---

# 🔄 Gestión de movimientos

## Entrada

Aumenta el stock:

```text
Stock actual + cantidad
```

## Salida

Disminuye el stock:

```text
Stock actual - cantidad
```

Antes de realizar la operación se valida que exista suficiente inventario.

## Transferencia

Mueve productos entre dos bodegas:

```text
Bodega origen
      │
      │ - cantidad
      ▼
Producto
      │
      │ + cantidad
      ▼
Bodega destino
```

El sistema valida que:

- La cantidad sea mayor que cero.
- Exista suficiente stock en la bodega de origen.
- Se actualice el inventario de origen.
- Se actualice o cree el inventario de destino.

---

# 📊 Reportes

Endpoint:

```http
GET /reportes/resumen
```

Incluye:

- Total de bodegas.
- Total de productos.
- Total de registros de inventario.
- Total de movimientos.
- Total de entradas.
- Total de salidas.
- Total de transferencias.

---

# 🌐 Endpoints principales

## 🔑 Autenticación

### Login

```http
POST /auth/login
```

Ejemplo:

```json
{
  "email": "usuario@correo.com",
  "password": "123456"
}
```

El backend devuelve un JWT.

---

## 👤 Usuarios

### Listar usuarios

```http
GET /usuarios
```

Requiere rol `ADMIN`.

### Crear usuario

```http
POST /usuarios
```

Requiere rol `ADMIN`.

Ejemplo:

```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "password": "123456",
  "rol": "EMPLEADO",
  "activo": true
}
```

La contraseña se procesa mediante BCrypt antes de almacenarse.

---

## 🏢 Bodegas

Ruta principal:

```http
/bodegas
```

Acceso:

```text
ADMIN
EMPLEADO
```

---

## 🛒 Productos

Ruta principal:

```http
/productos
```

Acceso:

```text
ADMIN
EMPLEADO
```

---

## 📦 Inventario

Ruta principal:

```http
/inventario
```

---

## 🔄 Movimientos

Ruta principal:

```http
/movimientos
```

Registro:

```http
POST /movimientos/registrar
```

Tipos:

```text
ENTRADA
SALIDA
TRANSFERENCIA
```

---

## 📊 Reportes

```http
GET /reportes/resumen
```

---

## 📝 Auditorías

Ruta:

```http
/auditorias
```

Acceso restringido a `ADMIN`.

---

# 🔑 Uso del JWT

Los endpoints protegidos reciben el token mediante:

```http
Authorization: Bearer TOKEN
```

Ejemplo:

```javascript
fetch("http://localhost:8080/productos", {
    headers: {
        "Authorization": `Bearer ${token}`
    }
});
```

El frontend guarda el token después del login y lo utiliza para realizar las solicitudes al backend.

---

# ⚙️ Configuración

Backend:

```text
http://localhost:8080
```

Frontend:

```text
http://localhost:5500
```

La aplicación utiliza MySQL como sistema de gestión de base de datos.

---

# 🖥️ Ejecución del proyecto

## 1. Crear la base de datos

```sql
CREATE DATABASE LogiTrack_DB;
```

Después ejecutar el script SQL de creación de tablas.

## 2. Iniciar MySQL

Verificar que el servidor MySQL esté funcionando.

## 3. Ejecutar el backend

Abrir:

```text
logitrack-backend
```

Ejecutar la clase principal:

```text
LogitrackBackendApplication
```

El backend estará disponible en:

```text
http://localhost:8080
```

## 4. Ejecutar el frontend

Abrir el frontend utilizando **Live Server**.

Normalmente estará disponible en:

```text
http://localhost:5500
```

---

# 🔗 Comunicación Frontend ↔ Backend

El frontend realiza solicitudes HTTP mediante `fetch()`.

Ejemplo:

```javascript
const response = await fetch(`${API_BASE}/usuarios`, {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify(usuario)
});
```

El backend valida la autenticación y autorización y posteriormente realiza las operaciones correspondientes en MySQL.

---

# 🌍 CORS

El backend permite solicitudes provenientes de:

```text
http://localhost:5500
```

y:

```text
http://127.0.0.1:5500
```

Esto permite la comunicación entre el frontend ejecutado mediante Live Server y el backend Spring Boot.

---

# 📁 Estructura general del backend

```text
src
└── main
    └── java
        └── com.logitrack.logitrack_backend
            │
            ├── controller
            ├── model
            ├── repository
            ├── service
            └── security
```

### Controller

Contiene los endpoints REST.

### Model

Contiene las entidades que representan las tablas de la base de datos.

### Repository

Permite realizar operaciones de persistencia mediante Spring Data JPA.

### Service

Contiene la lógica de negocio, especialmente la relacionada con inventario y movimientos.

### Security

Contiene la configuración de Spring Security, JWT y autenticación.

---

# 🧩 Funcionalidades implementadas

- [x] Login de usuarios.
- [x] Autenticación mediante JWT.
- [x] Autorización mediante roles.
- [x] Roles ADMIN y EMPLEADO.
- [x] Contraseñas almacenadas mediante BCrypt.
- [x] Creación de usuarios desde el sistema.
- [x] Restricción de gestión de usuarios para ADMIN.
- [x] Gestión de bodegas.
- [x] Gestión de productos.
- [x] Gestión de categorías.
- [x] Gestión de inventario.
- [x] Registro de entradas.
- [x] Registro de salidas.
- [x] Registro de transferencias.
- [x] Validación de cantidades.
- [x] Validación de stock insuficiente.
- [x] Actualización del inventario.
- [x] Reporte general.
- [x] Gestión de auditorías.
- [x] Protección de endpoints.
- [x] Comunicación frontend/backend.
- [x] Configuración CORS.

---

# 🚧 Mejoras futuras

- [ ] Bean Validation (`@Valid`, `@NotNull`, `@Size`, etc.).
- [ ] Manejo global de excepciones.
- [ ] Swagger / OpenAPI.
- [ ] Consultas avanzadas de inventario.
- [ ] Filtros de movimientos por fechas.
- [ ] Consultas avanzadas de auditoría.
- [ ] Reportes más detallados.
- [ ] Alertas de productos con stock bajo.
- [ ] Pruebas unitarias e integración.
- [ ] Despliegue en un servidor.

---

# 👨‍💻 Estado actual

El proyecto se encuentra funcional para el entorno local.

Flujo principal:

```text
Frontend
    │
    ▼
Login
    │
    ▼
JWT
    │
    ▼
Spring Security
    │
    ▼
Controllers
    │
    ▼
Services
    │
    ▼
Repositories / JPA
    │
    ▼
Hibernate
    │
    ▼
MySQL
```

El flujo principal de autenticación, gestión de usuarios, inventario y movimientos se encuentra implementado.

---

# 📌 Información del proyecto

| Elemento | Información |
|---|---|
| Proyecto | LogiTrack Inventory System |
| Backend | Spring Boot |
| Lenguaje | Java |
| Base de datos | MySQL |
| Frontend | HTML, CSS y JavaScript |
| Autenticación | JWT |
| Hash de contraseñas | BCrypt |
| ORM | Hibernate / JPA |
| Puerto backend | `8080` |
| Puerto frontend | `5500` |

---

# 📄 Licencia

Proyecto académico desarrollado para fines educativos y de formación en desarrollo de aplicaciones web.
