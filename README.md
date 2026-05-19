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

## Módulos del sistema

### Módulo de Campañas
- CRUD de campañas
- Validaciones:
  - La fecha de finalización no puede ser menor a la fecha de inicio
  - No se pueden registrar materiales en campañas finalizadas

### Módulo de Zonas de Recolección
- CRUD de zonas de recolección

### Módulo de Reportes
- Comparación de campañas
- Estadísticas:
  - Kilogramos totales por campaña
  - Porcentaje de recolección alcanzado
  - Kilogramos por tipo de material

---

## Integrantes
- Valeria Salas 
- Enier Aragón 
- Eduardo Arias

---

## Tecnologías
- Backend: Java con Spring Boot
- API: REST
- Base de datos: MySQL (JPA/Hibernate)
- Frontend: HTML, CSS y JavaScript
- Herramientas de prueba: Postman


---

## Estructura del Proyecto
El proyecto sigue una arquitectura cliente-servidor y una organización en capas según lo establecido en las instrucciones del curso.


## Estado del proyecto
En desarrollo

