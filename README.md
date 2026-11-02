# Mobile Phone Store

An Android mobile phone store application developed for the **Mobile Device Technology and Applications** course at the **Islamic University of Madinah**, First Semester 2024–2025.

## Table of Contents
- [Abstract](#abstract)
- [Objectives](#objectives)
- [Application Design](#application-design)
- [Main Features](#main-features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)
- [Conclusion and Future Enhancements](#conclusion-and-future-enhancements)

## Abstract

The **Mobile Phone Store** application simulates a digital storefront for mobile phones. It provides two user roles: **Admin** and **Customer**.

- **Customers** can browse available products, add items to their shopping cart, and proceed to checkout.
- **Admins** can manage the product inventory and user accounts through CRUD operations (Create, Read, Update, Delete).

## Objectives

- Provide a simple and user-friendly mobile store application.
- Enable convenient shopping and inventory management.
- Demonstrate Android application development and database operations.
- Provide separate functionality for customers and administrators.

## Application Design

### Customer Workflow
1. Sign up or log in.
2. Browse available products.
3. Select products and add them to the shopping cart.
4. Proceed to checkout.
5. View the total price and generated bill.

### Admin Workflow
1. Log in using admin credentials.
2. Access the admin dashboard.
3. Manage products (add, update, and delete).
4. Manage users (add, update, and delete).
5. Log out of the application.

## Main Features

### Customer Features
- User registration and login.
- Browse available mobile products.
- Select products and manage quantities.
- Add products to the shopping cart.
- Calculate the total purchase price.
- Checkout and simulate bill printing.

### Admin Features
- Admin dashboard.
- Product inventory management.
- Add, update, and delete products.
- User account management.
- Add, update, and delete user records.
- Manage user roles.

## Technologies

- **Programming Language:** Java
- **Development Environment:** Android Studio
- **User Interface:** XML
- **Database:** SQLite
- **Platform:** Android

## Project Structure

The application is organized into the following main Java classes:

| Class | Description |
|---|---|
| `MainActivity.java` | Handles login, verifies credentials, and directs users to the appropriate dashboard. |
| `Signup.java` | Handles user registration, input validation, and saving user information. |
| `AdminPage.java` | Provides the admin dashboard and navigation to management screens. |
| `ManageProductsActivity.java` | Handles product inventory CRUD operations. |
| `ManageUsersActivity.java` | Handles user account management and role editing. |
| `UserPage.java` | Displays available products and manages product selection and cart operations. |
| `CheckoutPage.java` | Displays cart details, calculates the total price, and simulates bill printing. |
| `Product.java` | Represents product information and supports passing product data between activities. |

## Screenshots

### Login and Registration

**Login Page**

![Login Page](Login%20Page.png)

**Signup Page**

![Signup Page](Signup%20Page.png)

### Admin Dashboard

![Admin Dashboard](Admin%20Dashboard.png)

### Product Management

**Manage Products**

![Manage Products](Manage%20Products.png)

**Available Products**

![Available Products](Available%20Products.png)

### User Management

![Manage Users](Manage%20Users.png)

## Conclusion and Future Enhancements

The Mobile Phone Store application provides a user-friendly shopping interface for customers and practical management tools for administrators. It demonstrates the use of Java, XML, SQLite, and Android development concepts in building a mobile shopping application.

### Future Enhancements
- Integrate online payment functionality.
- Add notifications for administrators and customers.
- Improve application security.
- Expand product and shopping management features.
