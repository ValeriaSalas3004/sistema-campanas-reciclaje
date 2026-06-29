# Sistema de Gestión de Campañas de Reciclaje

## Descripción
Este proyecto consiste en el desarrollo de un sistema para la gestión de campañas de reciclaje, orientado a municipalidades y comunidades.

El sistema permitirá medir el impacto real de las campañas de recolección y facilitar a la población información clara sobre campañas activas y finalizadas, evitando desplazamientos innecesarios y problemas ambientales.

---

## Objetivos
- Facilitar la gestión de campañas de reciclaje
- Permitir el registro y consulta de campañas activas
- Generar reportes y estadísticas sobre recolección
- Informar a la comunidad sobre puntos de reciclaje

---

Tipos de usuario
- Gestor: administra campañas, zonas, residuos, usuarios y reportes
- Voluntario: participa en campañas y registra reportes
- Invitado: consulta información pública del sistema

---

## Módulos del sistema

### Módulo de Usuarios
- Registro de usuarios
- Autenticación (login)
- Gestión de roles:
 - Gestor
 -Voluntario
- Edición de perfil

### Módulo de Campañas
- CRUD de campañas
- Validaciones:
  - La fecha de finalización no puede ser menor a la fecha de inicio
  - No se pueden registrar materiales en campañas finalizadas

### Módulo de Inscripciones a Campañas
- Registro de usuarios en campañas
- Cancelación de inscripción
 -Validaciones:
  -No se pueden duplicar inscripciones
  -Solo se permite inscripción en campañas próximas

### Módulo de Zonas de Recolección
- CRUD de zonas de recolección
  
###Módulo de Tipos de Residuo
- CRUD de tipos de residuos
- Clasificación de materiales recolectados

### Módulo de Reportes
- Comparación de campañas
- Estadísticas:
  - Kilogramos totales por campaña
  - Porcentaje de recolección alcanzado
  - Kilogramos por tipo de material

---

## Seguridad
- Autenticación mediante Spring Security
- Uso de Basic Auth
- Control de acceso por roles:
 - Gestor: acceso completo al sistema
 -Voluntario: acceso a reportes y campañas
 -Invitado: solo consulta de información pública

## Integrantes
- Valeria Salas 
- Enier Aragón 
- Eduardo Arias

---

## Tecnologías

- Backend: Java + Spring Boot
- Arquitectura: API REST
- Base de datos: PostgreSQL (JPA/Hibernate)
- Frontend: HTML, CSS y JavaScript
- Seguridad: Spring Security (Basic Auth)
- Pruebas: Postman
- Despliegue: Render


---

## URL del sistema
- Backend: https://sistema-campanas-reciclaje.onrender.com
- Frontend: https://sistema-campanas-reciclaje-1.onrender.com

---

## Estructura del Proyecto
El proyecto sigue una arquitectura cliente-servidor y una organización en capas según lo establecido en las instrucciones del curso.


## Estado del proyecto
En desarrollo

