# Medical Appointment API

## Descripción

Medical Appointment API es una API REST desarrollada con Spring Boot para la gestión de citas médicas. Permite registrar médicos y pacientes, programar, cancelar y reprogramar citas, consultar disponibilidad y listar citas mediante filtros.

El proyecto fue desarrollado siguiendo una arquitectura por capas, aplicando principios SOLID y buenas prácticas de desarrollo.

---

# Tecnologías

* Java 21
* Spring Boot 3.5.16
* Spring Data JPA
* Spring Validation
* H2 Database
* Maven
* Lombok
* SpringDoc OpenAPI (Swagger)

---

# Arquitectura

El proyecto utiliza una arquitectura por capas organizada de la siguiente manera:

```text
controller
dto
entity
enums
exception
mapper
repository
service
validation
config
```

Cada capa tiene una única responsabilidad:

* **Controller:** exposición de endpoints REST.
* **Service:** implementación de la lógica de negocio.
* **Repository:** acceso a la base de datos.
* **Entity:** entidades JPA.
* **DTO:** objetos de transferencia de datos.
* **Mapper:** conversión entre entidades y DTO.
* **Validation:** validaciones de negocio.
* **Exception:** manejo global de excepciones.
* **Config:** configuración e inicialización de datos.

---

# Requisitos

* Java 21
* Maven 3.9 o superior

---

# Ejecución

Clonar el proyecto:

```bash
git clone <URL_DEL_REPOSITORIO>
```

Ingresar al proyecto:

```bash
cd medical-appointment-api
```

Ejecutar:

```bash
mvn spring-boot:run
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

---

# Swagger

Documentación de la API:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Consola H2

```text
http://localhost:8080/h2-console
```

Configuración:

```text
JDBC URL:
jdbc:h2:mem:medical_appointment_db

User:
sa

Password:
```

---

# Funcionalidades implementadas

## Médicos

* Registrar médico.
* Consultar todos los médicos.
* Consultar médico por identificador.

## Pacientes

* Registrar paciente.
* Consultar todos los pacientes.
* Consultar paciente por identificador.

## Citas

* Registrar cita médica.
* Consultar disponibilidad de un médico.
* Cancelar cita.
* Reprogramar cita.
* Listar citas con filtros.

---

# Reglas de negocio implementadas

* Horario de atención:

    * Lunes a viernes de 08:00 a 18:00.
    * Sábados de 08:00 a 13:00.
    * No se permiten citas los domingos.

* Las citas tienen una duración de 30 minutos.

* Un médico no puede tener dos citas en el mismo horario.

* Un paciente no puede tener dos citas con el mismo médico en el mismo horario.

* Una cancelación realizada con menos de dos horas de anticipación genera una penalización.

* Un paciente con tres o más penalizaciones en los últimos treinta días no puede agendar nuevas citas.

---

# Datos iniciales

Al iniciar la aplicación se cargan automáticamente:

* Médicos de ejemplo.
* Pacientes de ejemplo.

Esto facilita la ejecución de pruebas desde Swagger o Postman.

---

# Manejo de errores

La API cuenta con un manejo global de excepciones mediante:

* ResourceNotFoundException
* BusinessException
* GlobalExceptionHandler

Las respuestas de error mantienen un formato uniforme para facilitar el consumo de la API.

---

# Pruebas

Las pruebas unitarias fueron desarrolladas utilizando:

* JUnit 5
* Mockito
* JaCoCo

Para ejecutar las pruebas:

```bash
mvn test
```

Para generar el reporte de cobertura:

```bash
mvn clean verify
```

---

# Autor

Jefferson Cáceres
