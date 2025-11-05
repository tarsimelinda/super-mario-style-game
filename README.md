<a id="readme-top"></a>

<br />
<div align="center">
    <img src="/client/public/game-icon.png" alt="game-icon" width="80" height="80">
  </a>

  <h3 align="center">Super-Mario Style game</h3>

  <p align="center">
    A 2D platformer game designed for adventure lovers to jump, collect, and conquer challenging worlds.
    <br />
    <a href="git clone https://github.com/tarsimelinda/freestyle-mern-project-react.git
">View Demo</a>
</div>


<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



## About The Project

This is a small 2D platformer game built as a personal CV project to demonstrate React and Spring Boot full-stack development.
Players can move, jump, collect coins, and avoid enemies. The project includes a full backend REST API with MongoDB persistence, DTOs, validation, and test coverage using JUnit + MockMvc.

The goal was to create a clean, maintainable structure with reusable hooks, modular CSS, and a REST-compliant backend.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

#### Frontend

* React + Vite – modern setup for fast development

* CSS Modules + global variables/utilities – modular styling

* HTML Canvas – real-time rendering and physics

* Custom hooks – keyboard controls, physics, and game loop

* ESLint + Prettier – consistent code formatting

#### Backend

* Spring Boot 3 – REST API and application structure

* MongoDB + Spring Data – persistence layer

* DTOs + Validation – clean request handling

* JUnit + MockMvc – controller-level integration tests


<p align="right">(<a href="#readme-top">back to top</a>)</p>



## Getting Started

Follow these steps to run both the frontend and backend locally.

### Prerequisites

You’ll need the following installed:

* [Node.js 18+]

* [npm]

* [Java 17+]

* [Maven 3.9+]

* [MongoDB]

### Installation



1. Clone the repository
```sh
   git clone https://github.com/tarsimelinda/freestyle-mern-project-react.git
   ```

2. Set up environment variables.

Windows PowerShell:
```sh
$env:MONGODB_URI = "mongodb+srv://<user>:<password>@cluster.mongodb.net/supermario"
$env:SERVER_PORT = "8080"
$env:CORS_ORIGIN = "http://localhost:5173"
```
These will be automatically picked up by Spring Boot from application.properties.
   
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
cd server
mvn spring-boot:run
```
The backend runs at http://localhost:8080
 and exposes REST endpoints under /api.

   ```js
   const API_KEY = 'ENTER YOUR API';
   ```
5. Change git remote url to avoid accidental pushes to base project
   ```sh
   git remote set-url origin github_username/repo_name
   git remote -v
   ```

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
| Integration tests (MockMvc)                  |    <span style="color:green">Done</span>  |
| Global CORS + dev profile                    |    <span style="color:green">Done</span>  |
| Responsive layout + modular CSS              |    <span style="color:green">Done</span>  |
| **User authentication (JWT)**                |  Planned |
| **Leaderboard via backend**                  |  Planned |
| **Saving progress (checkpoints)**            |  Planned |
| **Character selection with sprites**         |  Planned |
| **Deploy to Render / Vercel**                |  Planned |


<p align="right">(<a href="#readme-top">back to top</a>)</p>

| Command               | Description                        |
| --------------------- | ---------------------------------- |
| `npm run dev`         | Start frontend (Vite)              |
| `npm run lint`        | Lint all frontend files            |
| `npm run format`      | Format frontend code with Prettier |
| `mvn test`            | Run backend tests                  |
| `mvn spotless:apply`  | Format backend Java code           |
| `mvn spring-boot:run` | Run backend server                 |

<p align="right">(<a href="#readme-top">back to top</a>)</p>


##  Future Improvements

* Code cleanup and modular structure

* Authentication & leaderboard

* Save game progress to MongoDB

* Character animations and power-ups

* Deployment to cloud (Render / Vercel + Mongo Atlas)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Contact

Társi Melinda - melindatarsi9@gmail.com

Project Link: https://github.com/tarsimelinda/freestyle-mern-project-react

<p align="right">(<a href="#readme-top">back to top</a>)</p>



[Node.js 18+]: https://nodejs.org/en
[npm]: https://www.npmjs.com/
[Java 17+]: https://adoptium.net/en-GB
[Maven 3.9+]: https://maven.apache.org/
[MongoDB]: https://www.mongodb.com/