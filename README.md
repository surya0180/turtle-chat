# 🐢 Turtle Chat

Welcome to **Turtle Chat**, a modern and secure chat application that simplifies the way users connect and communicate. With OTP-based login, chatroom creation, real-time messaging, and scalable architecture, Turtle Chat provides a seamless and efficient chatting experience.

## 📝 Table of Contents

- [Features](#-features)
- [Architecture Overview](#-architecture-overview)
- [Frontend](#-frontend)
- [Backend](#-backend)
  - [Main Server](#main-server)
  - [Socket Application](#socket-application)
  - [Batcher Service](#batcher-service)
- [Technology Stack](#-technology-stack)
- [Installation](#-installation)
- [Usage](#-usage)
- [Screenshots & GIFs](#-screenshots--gifs)
- [Contributing](#-contributing)
- [License](#-license)

## 🌟 Features

- **OTP-Based Login:** Secure login via email OTP; no passwords needed.
- **Chatrooms:** Create and manage chatrooms, join others with invitations.
- **Real-time Messaging:** Live chat using WebSockets with support for multiple clients.
- **Invitations:** Invite others to join your chatrooms.
- **Chatroom Management:** Delete chatrooms when they are no longer needed.
- **Scalable Architecture:** Built to handle concurrent users and messages efficiently.

## 🏛️ Architecture Overview

Turtle Chat is designed with scalability and efficiency in mind, utilizing a microservices architecture. Here's a high-level overview of the system:

1. **Main Server (REST API):**
   - Handles authentication, chatroom CRUD operations, and invitations.
   - Built with Java Spring Boot.

2. **Socket Application (WebSockets):**
   - Manages real-time message exchange between clients in chatrooms.
   - Consists of three nodes running in Docker containers, each connected to a Redis PubSub for message broadcasting.
   - Publishes messages to a Kafka pipeline for further processing.

3. **Batcher Service:**
   - Consumes messages from the Kafka pipeline in batches.
   - Efficiently stores messages in the database, ensuring persistence and scalability.

![Architecture Diagram Placeholder](#) *(Add an architecture diagram here if available)*

## 🎨 Frontend

The frontend of Turtle Chat is developed using **React JS**, providing a responsive and user-friendly interface for all devices. Users can log in, create or join chatrooms, and interact with others in real-time.

## 💻 Backend

### Main Server

The main server is a **Java Spring Boot** application responsible for:

- **User Authentication:** Handles OTP verification via email.
- **Chatroom Management:** Supports CRUD operations for chatrooms and managing invitations.
- **REST API:** Exposes endpoints for the frontend to interact with the backend.

### Socket Application

The Socket application handles the core functionality of real-time messaging:

- **WebSocket Connection:** Facilitates live communication between users.
- **Redis PubSub Integration:** Ensures all Socket nodes receive and broadcast messages to clients in the respective chatrooms.
- **Kafka Integration:** Publishes messages to a Kafka pipeline for further processing by the Batcher service.

### Batcher Service

The Batcher service is responsible for:

- **Kafka Consumer:** Consumes messages from the Kafka pipeline.
- **Batch Processing:** Efficiently stores chat messages into the database, optimizing for high throughput and low latency.

## 🛠️ Technology Stack

- **Frontend:** React JS
- **Backend:** Java Spring Boot
- **WebSocket:** Java with Spring Boot, Docker, Redis PubSub
- **Database:** Relational Database (e.g., PostgreSQL, MySQL)
- **Messaging Queue:** Kafka
- **Containerization:** Docker & Docker Compose

## ⚙️ Installation

To run Turtle Chat locally, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/turtle-chat.git
   cd turtle-chat
2. **Configure Environment Variables:**
   Create an `.env` file in the root of your backend project directory with the following environment variables:
   ```env
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/turtlechat
   SPRING_DATASOURCE_USERNAME=your_db_username
   SPRING_DATASOURCE_PASSWORD=your_db_password
   KAFKA_BOOTSTRAP_SERVERS=localhost:9092
   REDIS_HOST=localhost
   REDIS_PORT=6379
3. **Build and Run the Docker Containers:**
   Navigate to the root of your project and run:
   ```bash
   docker-compose up --build
4. **Run the Spring Boot Application:**
   If not running via Docker, you can start the Spring Boot application directly:
   ```bash
   ./mvnw spring-boot:run
## 🚀 Usage

After setting up the application, you can:

- **Login:** Enter your email to receive an OTP and log in.
- **Create Chatrooms:** Start a new chatroom and invite others to join.
- **Join Chatrooms:** Accept invitations and join existing chatrooms.
- **Chat:** Communicate with others in real-time within the chatrooms.
- **Manage Chatrooms:** Invite others or delete chatrooms as needed.

## 📸 Screenshots & GIFs

*(Place screenshots and GIFs showcasing the application here)*

## 🤝 Contributing

Contributions are welcome! If you have suggestions or improvements, feel free to submit a pull request or open an issue.

1. Fork the repository.
2. Create a new branch: `git checkout -b feature-branch-name`.
3. Make your changes and commit them: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature-branch-name`.
5. Submit a pull request.

## 📝 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
