# ISACEBOY - Gestor de Reservas de Habitaciones

![Logo](assets/IASCEBOY.jpg)

**ISACEBOY** es una aplicación de escritorio desarrollada en Java, diseñada para facilitar la gestión de reservas de habitaciones de forma eficiente, rápida y organizada. Está pensada para instituciones que necesitan controlar el uso de salas o espacios mediante un sistema de agendamiento centralizado.

---

## 🚀 Características

- Gestión de usuarios (clientes)
- Registro y modificación de habitaciones
- Calendario de reservas con interfaz visual
- Control de disponibilidad
- Interfaz gráfica intuitiva con JavaFX
- Persistencia de datos usando JPA (EclipseLink) y base de datos local (MySQL o SQLite)

---

## 🧠 Tecnologías utilizadas

- **Java 17**
- **JavaFX 19**
- **JPA (EclipseLink 2.7.12)**
- **Base de datos MySQL** _(opcionalmente SQLite para distribución portable)_
- **Maven**
- **CalendarFX** (gestión de calendarios)
- **ControlsFX** (componentes UI avanzados)

---

## 🗂 Estructura del Proyecto

```plaintext
src/
 ├── co.edu.uptc.model           # Entidades JPA
 ├── co.edu.uptc.persistence     # Controladores de persistencia
 ├── co.edu.uptc.controller      # Lógica de negocio
 ├── co.edu.uptc.view            # Interfaces gráficas JavaFX
 └── co.edu.uptc.Main.java       # Clase principal
```
