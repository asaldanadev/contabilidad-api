# Sistema Contable API

API REST para gestión contable con soporte de partida doble.

## Stack

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.5.14 |
| PostgreSQL | 16+ |
| SpringDoc OpenAPI | 2.8.9 |

## Modelo de dominio

```
Cuenta (ACTIVO | PASIVO | PATRIMONIO | INGRESO | GASTO)
Asiento (BORRADOR → CONFIRMADO | ANULADO)
└── Líneas (DÉBITO / CRÉDITO) — suma DÉBITOS == suma CRÉDITOS
```

## Endpoints

### Cuentas `/api/cuentas`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Listar todas |
| GET | `/activas` | Solo activas |
| GET | `/tipo/{tipo}` | Filtrar por tipo |
| GET | `/{id}` | Buscar por ID |
| POST | `/` | Crear |
| PUT | `/{id}` | Actualizar |
| DELETE | `/{id}` | Desactivar |

### Asientos `/api/asientos`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Listar todos |
| GET | `/{id}` | Buscar por ID |
| POST | `/` | Crear (valida partida doble) |
| PATCH | `/{id}/confirmar` | Confirmar |
| PATCH | `/{id}/anular` | Anular |

## Ejecución local

**Requisitos:** Java 17+, PostgreSQL 16+

1. Crear base de datos `contabilidad_db` en PostgreSQL
2. Configurar variables de entorno:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/contabilidad_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=tu_password
```

3. Ejecutar:

```bash
./mvnw spring-boot:run
```

Swagger UI disponible en `http://localhost:8080/swagger-ui.html`

## Demo

API desplegada en Render (free tier — primer request puede tardar ~50s):

- Swagger UI: `https://contabilidad-api-5hi2.onrender.com/swagger-ui/index.html`

## Ejemplo de uso

```json
POST /api/asientos
{
  "fecha": "2026-05-12",
  "descripcion": "Venta al contado",
  "referencia": "FAC-001",
  "lineas": [
    { "cuentaId": 1, "tipo": "DEBITO",  "monto": 1000.00 },
    { "cuentaId": 2, "tipo": "CREDITO", "monto": 1000.00 }
  ]
}
```

## Autor

[asaldanadev](https://github.com/asaldanadev)