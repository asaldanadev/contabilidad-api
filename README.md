# 📊 API Sistema Contable

API REST para gestión contable con soporte de **partida doble**, desarrollada como proyecto de portafolio del bootcamp DIO / Santander Dev Week 2023.

## 🛠️ Tecnologías

| Tecnología | Versión |
|-----------|---------|
| Java | 17 (LTS) |
| Spring Boot | 3.2.5 |
| Spring Data JPA | - |
| PostgreSQL | - |
| Lombok | - |
| SpringDoc OpenAPI | 2.5.0 |

---

## 📐 Arquitectura del Dominio

El sistema implementa el modelo contable de **partida doble**:

```
Cuenta Contable
  └── Tipos: ACTIVO | PASIVO | PATRIMONIO | INGRESO | GASTO

Asiento Contable
  ├── Estado: BORRADOR → CONFIRMADO / ANULADO
  └── Líneas de Asiento
        ├── Tipo: DÉBITO
        └── Tipo: CRÉDITO
        ⚡ Regla: Total DÉBITOS == Total CRÉDITOS
```

---

## 🚀 Endpoints

### Cuentas Contables `/api/cuentas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/cuentas` | Listar todas las cuentas |
| GET | `/api/cuentas/activas` | Listar cuentas activas |
| GET | `/api/cuentas/tipo/{tipo}` | Filtrar por tipo |
| GET | `/api/cuentas/{id}` | Buscar por ID |
| POST | `/api/cuentas` | Crear cuenta |
| PUT | `/api/cuentas/{id}` | Actualizar cuenta |
| DELETE | `/api/cuentas/{id}` | Desactivar cuenta |

### Asientos Contables `/api/asientos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/asientos` | Listar todos los asientos |
| GET | `/api/asientos/{id}` | Buscar por ID |
| POST | `/api/asientos` | Crear asiento (valida partida doble) |
| PATCH | `/api/asientos/{id}/confirmar` | Confirmar y actualizar saldos |
| PATCH | `/api/asientos/{id}/anular` | Anular asiento |

---

## 💻 Ejecución Local

### Requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL corriendo en `localhost:5432`

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-usuario/contabilidad-api.git
cd contabilidad-api

# 2. Crear base de datos en PostgreSQL
createdb contabilidad_db

# 3. Ejecutar
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

**Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## ☁️ Despliegue en Railway

### Variables de entorno requeridas en Railway:

| Variable | Valor |
|----------|-------|
| `DATABASE_URL` | `jdbc:postgresql://<host>:<port>/<db>` |
| `DATABASE_USERNAME` | usuario de PostgreSQL |
| `DATABASE_PASSWORD` | contraseña de PostgreSQL |
| `SPRING_PROFILES_ACTIVE` | `prod` |

### Pasos Railway:
1. Crear proyecto en [railway.app](https://railway.app)
2. Agregar servicio **PostgreSQL**
3. Conectar repositorio GitHub
4. Configurar las variables de entorno arriba indicadas
5. Deploy automático 🎉

---

## 📋 Ejemplo de uso

### Crear una cuenta
```json
POST /api/cuentas
{
  "codigo": "1001",
  "nombre": "Caja",
  "descripcion": "Efectivo disponible",
  "tipo": "ACTIVO"
}
```

### Crear un asiento (con partida doble)
```json
POST /api/asientos
{
  "fecha": "2024-01-15",
  "descripcion": "Venta al contado",
  "referencia": "FAC-001",
  "lineas": [
    { "cuentaId": 1, "tipo": "DEBITO",  "monto": 1000.00, "descripcion": "Cobro en caja" },
    { "cuentaId": 2, "tipo": "CREDITO", "monto": 1000.00, "descripcion": "Ingreso por ventas" }
  ]
}
```

---

## ⚠️ Manejo de Errores

La API retorna errores estructurados:

```json
{
  "status": 400,
  "mensaje": "El asiento no está balanceado. Débitos: 1000.00 | Créditos: 800.00",
  "errores": null,
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 👤 Autor

Desarrollado como portafolio del bootcamp **DIO - Santander Dev Week 2026**
