

# 🧾 Smart Invoice Generator – Spring Boot Backend

![License](https://img.shields.io/badge/License-MIT-green.svg)
![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)

---

## 📌 Project Overview

**Smart Invoice Generator** is a backend-driven invoice management system built using **Spring Boot** that allows users to:

* Create professional invoices
* Apply dynamic HTML templates
* Generate PDF invoices
* Send invoices via email
* Manage multiple invoice templates
* Support GST (CGST / SGST / IGST)
* Store invoice history securely

The system is designed with **scalability, modularity, and real-world business usage** in mind.

---

## ✨ Features

### 🧾 Invoice Management

* Create invoices using JSON input
* Auto-generate invoice numbers
* Supports multiple line items
* Auto calculation of:

  * Subtotal
  * CGST / SGST / IGST
  * Grand Total

### 🎨 Template-Based Invoices

* Multiple pre-designed HTML templates
* Admin can:

  * Create templates
  * Update templates
  * Delete templates (Hard Delete)
* Invoice stores a **snapshot of template HTML**, so old invoices remain intact

### 📄 PDF Generation

* Converts invoice HTML → PDF
* Uses **OpenHTMLtoPDF**
* Supports:

  * Logo rendering
  * QR code
  * GST breakdown
  * Professional layouts

### 📧 Email Integration

* Send invoice PDFs via email
* Automatic attachment generation
* SMTP-based email sending

### 🔐 Secure Architecture

* JWT-based authentication
* User-based invoice access
* Admin-only template management

### 🧠 Smart Design Decisions

* Template deletion does NOT affect old invoices
* HTML stored inside invoice for historical accuracy
* Clean separation of concerns

---

## 🛠 Tech Stack

### 🔹 Backend

* Java 17
* Spring Boot 3.x
* Spring Data JPA
* Hibernate
* MySQL
* Lombok

### 🔹 PDF & Utilities

* OpenHTMLToPDF
* QR Code Generator
* Jackson (JSON Parsing)

### 🔹 Security

* Spring Security
* JWT Authentication

### 🔹 Tools

* Maven
* IntelliJ IDEA
* Postman
* Git & GitHub

---

## 🏗 Architecture Overview

```
Controller → Service → Repository → Database
                 ↓
             PDF Engine
                 ↓
             Email Service
```

### Key Modules

* Invoice Module
* Template Module
* User Module
* PDF Generator
* Email Sender

---

## ⚙️ How It Works

### 1️⃣ Create Invoice

User sends invoice data (JSON + optional logo + QR):

```json
{
  "invoice": {
    "date": "2026-01-26",
    "dueDate": "2026-02-05"
  },
  "company": {
    "name": "Nihal Tech",
    "address": "Bhopal"
  },
  "billing": {
    "name": "Rahul Sharma",
    "address": "Indore"
  },
  "items": [
    { "name": "Website Development", "qty": 1, "amount": 25000 }
  ],
  "template": {
    "id": 3
  }
}
```

---

### 2️⃣ Template Handling

* Template is fetched from DB
* HTML snapshot is saved in invoice
* Template can be deleted later safely

---

### 3️⃣ PDF Generation

* HTML + data merged
* QR code generated
* PDF rendered
* Stored or emailed

---

### 4️⃣ Email Invoice

* Invoice PDF sent as attachment
* SMTP based email service

---

## 🚀 How to Run the Project

### ✅ Prerequisites

* Java 17+
* MySQL
* Maven
* SMTP credentials

---

### 🔹 Step 1: Clone Repository

```bash
git clone https://github.com/yourusername/invoice-generator.git
cd invoice-generator
```

---

### 🔹 Step 2: Configure Database

`application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/invoicedb
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
```

---

### 🔹 Step 3: Configure Mail

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

---

### 🔹 Step 4: Run Application

```bash
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

---

## 📡 API Overview

### 🔹 Create Invoice

```
POST /api/invoices
```

### 🔹 Get All Invoices

```
GET /api/invoices
```

### 🔹 Preview Invoice

```
GET /api/invoices/{id}/preview
```

### 🔹 Send Invoice

```
POST /api/invoices/{id}/send
```

### 🔹 Admin Templates

```
GET    /api/admin/templates
POST   /api/admin/templates
PUT    /api/admin/templates/{id}
DELETE /api/admin/templates/{id}
```

---

## 🔐 Security Rules

| Role   | Access                      |
| ------ | --------------------------- |
| USER   | Create, View, Send Invoices |
| ADMIN  | Manage Templates            |
| PUBLIC | No Access                   |

---

## 📸 Screenshots (Optional)

Add:

* Invoice PDF preview
* Template list
* Admin panel

---

## 👨‍💻 Author

**Nihal Singh**
Backend Developer | Java | Spring Boot
Built as a production-grade backend system.

---

## 📄 License

This project is licensed under the **MIT License**
© 2026 Nihal Singh. All rights reserved.


