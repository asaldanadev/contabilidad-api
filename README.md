# 📊 API Sistema Contable

API REST corporativa para gestión contable con soporte de **partida doble**, desarrollada como proyecto independiente de portafolio.

## 🛠️ Tecnologías

| Tecnología | Versión |
|-----------|---------|
| Java | 17 (LTS) |
| Spring Boot | 3.5.14 |
| Spring Data JPA | En pom |
| PostgreSQL | 16+ |
| Lombok | En pom |
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
- PostgreSQL corriendo en `localhost:5432`

### Pasos de Configuración

1. Crear la base de datos vacía en PostgreSQL mediante pgAdmin 4 con el nombre exacto: `contabilidad_db`.
2. Configurar sus credenciales locales en el archivo `src/main/resources/application.properties`.

### Inicialización en Visual Studio Code
Para arrancar el sistema de forma visual y gratuita sin escribir comandos de consola:
1. Instale la extensión **Spring Boot Extension Pack** desde el mercado de VS Code.
2. Diríjase a la barra lateral izquierda y haga clic en la herramienta **SPRING BOOT DASHBOARD** (icono de enchufe/hoja).
3. En la sección superior `APPS`, localice el proyecto `contabilidad-api`.
4. Haga clic izquierdo en el **icono de reproducción (triángulo verde hacia la derecha)** para encender el servidor.

La API estará disponible en de inmediato en: `http://localhost:8080`

**Swagger UI:** `http://localhost:8080/swagger-ui.html`

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
  "fecha": "2026-05-12",
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
  "timestamp": "2026-05-12T10:30:00"
}
```

---

## 👤 Autor

Desarrollado de forma independiente por **asaldanadev**.
