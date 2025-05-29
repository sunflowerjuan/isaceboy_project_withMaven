# IASCEBOY - Room Booking Manager

![Logo](assets/IASCEBOY.jpg)

**IASCEBOY** is a desktop application developed in Java, designed to facilitate efficient, fast, and organized room booking management. It is intended for institutions that need to control the use of rooms or spaces through a centralized scheduling system.

---

## Features

- User management (clients)
- Room registration and editing
- Visual reservation calendar
- Availability control
- Intuitive graphical interface with JavaFX
- Data persistence using JPA (EclipseLink) with local database (MySQL or SQLite)

---

## Technologies Used

- **Java 17**
- **JavaFX 19**
- **JPA (EclipseLink 2.7.12)**
- **Database: MySQL** _(or SQLite for portable distribution with custom dialect)_
- **Maven**
- **CalendarFX** (calendar management)
- **ControlsFX** (advanced UI components)

---

## 🗂 Project Structure

```plaintext
src/
 ├── co.edu.uptc.model           # JPA entities
 ├── co.edu.uptc.persistence     # Persistence controllers
 ├── co.edu.uptc.controller      # Business logic
 ├── co.edu.uptc.view            # JavaFX UI
 └── co.edu.uptc.Main.java       # Entry point
```

---

### 📝 `LICENSE` file (MIT)

You should also include a `LICENSE` file in your project root with the following content:

```text
MIT License

Copyright (c) 2025 Juan Sebastian

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

...

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT...

```
