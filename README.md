<a id="readme-top"></a>

<br />
<div align="center">
  <img src="/client/public/game-icon.png" alt="game-icon" width="80" height="80">

  <h3 align="center">Super Mario–Style Platformer Game</h3>

  <p align="center">
    A full-stack 2D platformer game built with React + Vite (frontend) and Spring Boot 3 + MongoDB (backend).
    <br />
    <a href="https://github.com/tarsimelinda/freestyle-mern-project-react">View Repository</a>
  </p>
</div>


---

## Table of Contents
- [About The Project](#about-the-project)
  - [Built With](#built-with)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Installation](#installation)
- [Running the Project](#running-the-project)
- [Usage](#usage)
- [Roadmap](#roadmap)
- [Commands](#commands)
- [Future Improvements](#future-improvements)
- [Contact](#contact)



---

## About The Project

This project is a small 2D platformer game created as a personal CV project, showcasing complete **full-stack development** with:

- real-time rendering in React via **HTML Canvas**
- reusable custom hooks for physics and game loop
- modular CSS for a clean UI structure
- a full **REST API** built with Spring Boot 3
- MongoDB persistence for players and game data
- DTOs, validation, global exception handling
- Unit tests for the service layer using **JUnit 5 + Mockito**
- server-side rate limiting for security

The goal is to demonstrate a clean, maintainable, testable application with modern tooling across both the frontend and backend.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



---

## Built With

### Frontend

- **React + Vite**
- **CSS Modules**
- **HTML Canvas rendering**
- **Custom hooks** (keyboard controls, physics, timing)
- **ESLint + Prettier**

### Backend

- **Spring Boot 3**
- **Layered architecture (Controller → Service → Repository)**
- **Spring Web + Spring Data MongoDB**
- **DTOs + Jakarta Validation**
- **Lombok**
- **Custom GlobalExceptionHandler**
- **Rate limiting using Bucket4J**
- **JUnit 5 + Mockito (service unit tests)** 


<p align="right">(<a href="#readme-top">back to top</a>)</p>



---

## Getting Started

Follow these steps to run both the frontend and backend locally.

### Prerequisites

Install the following:

- [Node.js 18+]
- [npm]
- [Java 21+]
- [Maven 3.9+]
- [MongoDB Atlas or local MongoDB]

---

## Environment Variables

### 🔵 Backend Required Variables

Set these **before running Spring Boot**:

#### Windows PowerShell
```sh
setx MONGO_URI "mongodb+srv://<user>:<password>@cluster.mongodb.net/supermario"
setx SERVER_PORT "7070"
```

Used in application.properties:
```sh
spring.data.mongodb.uri=${MONGO_URI}
spring.data.mongodb.database=supermario
server.port=${SERVER_PORT:7070}
```
Backend protection relies on rate limiting.

### Installation

#### Clone the repository
```sh
   git clone https://github.com/tarsimelinda/freestyle-mern-project-react.git
   ```
   
### Run the Game

#### Frontend (React)

   ```js
    cd client
    npm install
    npm run dev
   ```

Then open your browser at http://localhost:5173.

#### Backend (Spring Boot)

```sh
cd backend
mvn spring-boot:run
```
The backend runs at http://localhost:7070
 and exposes REST endpoints under /api.

 The backend is protected with rate limiting
→ excessive requests receive HTTP 429 Too Many Requests.


<p align="right">(<a href="#readme-top">back to top</a>)</p>



## Usage

1. Start both frontend and backend.

2. In your browser, open http://localhost:5173
.

3. Select 1 or 2 players.

4. Register players (name + character).

5. Start the game and collect coins while avoiding enemies.

6. When game over, restart or return to the main menu.

The backend automatically stores player and user data in MongoDB.
Enemies and coins can be accessed or managed via the REST API.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



## Roadmap

| Feature                                      |   Status  |
| -------------------------------------------- | :-------: |
| Core 2D platformer (movement, jump, gravity) |    <span style="color:green">Done</span>  |
| Coins, enemies, scoreboard, game over        |    <span style="color:green">Done</span>  |
| Player registration via frontend form        |    <span style="color:green">Done</span>  |
| REST API (players, enemies, users)           |    <span style="color:green">Done</span>  |
| DTOs, validation, exception handling         |    <span style="color:green">Done</span>  |
| Unit tests for the service layer using JUnit 5 + Mockito                  |    <span style="color:green">Done</span>  |
| Rate limiting on backend                    |    <span style="color:green">Done</span>  |
| Responsive layout + modular CSS              |    <span style="color:green">Done</span>  |
| **User authentication (JWT)**                |  Planned |
| **Leaderboard via backend**                  |  Planned |
| **Saving progress (checkpoints)**            |  Planned |
| **Character selection with sprites**         |  Planned |
| **Deploy to Render / Vercel**                |  Planned |


<p align="right">(<a href="#readme-top">back to top</a>)</p>

| Command               | Description              |
| --------------------- | ------------------------ |
| `npm run dev`         | Start frontend           |
| `npm run lint`        | Lint frontend files      |
| `npm run format`      | Format frontend code     |
| `mvn test`            | Run backend tests        |
| `mvn spotless:apply`  | Format backend Java code |
| `mvn spring-boot:run` | Run backend server       |

<p align="right">(<a href="#readme-top">back to top</a>)</p>


##  Future Improvements

* Code cleanup and further modularization

* JWT authentication

* Leaderboard stored in MongoDB

* More dynamic enemies and power-ups

* Deployment to cloud (Render + Vercel + Mongo Atlas)

* Custom sprites and animations

<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Contact

Társi Melinda - melindatarsi9@gmail.com

Project Link: https://github.com/tarsimelinda/freestyle-mern-project-react

<p align="right">(<a href="#readme-top">back to top</a>)</p>



[Node.js 18+]: https://nodejs.org/en
[npm]: https://www.npmjs.com/
[Java 21+]: https://adoptium.net/en-GB
[Maven 3.9+]: https://maven.apache.org/
[MongoDB]: https://www.mongodb.com/