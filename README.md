# Chess Engine Project

This project is aimed at building a fully functional chess engine and a corresponding web-based interface to allow users to play chess against the engine. The project is built using **Java**, **Spring Boot**, and **Maven**. The engine will use **bitboards** for representing the chessboard and pieces, allowing for efficient move generation and game logic.

## Project Roadmap

### 1. **Build the Chess Engine (In Progress)**

The first step is to build the core chess engine:
- Use **bitboards** to represent the chessboard and pieces.
- Implement move generation and game logic.
- Handle special rules such as castling, pawn promotion, and checkmate.

### 2. **Create Working Frontend with Spring Boot**

Next, a web-based front-end will be developed using **Spring Boot**:
- Develop a web interface that allows users to play chess against the engine.
- Build an API for interacting with the chess engine backend.
- Implement a visual chessboard and handle user inputs.

### 3. **Deploy the Application**

Deploy the project to a cloud platform such as **Heroku** or **AWS**:
- Ensure the application is accessible online for users to play.
- Set up continuous integration and deployment pipelines.

### 4. **Build a Chess Bot**

After deployment, enhance the chess engine by building an AI-based chess bot:
- Implement AI algorithms to allow the engine to play against users at various difficulty levels.
- Optimize the bot for performance and accuracy.

---

## How to Build and Run the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/MiguelRyan/ChessEngine
   ```
   
2. Navigate to the project directory:
    ```bash
    cd ChessEngine
    ```

3. Build the project with Maven:
    ```bash
    ./mvnw clean install
    ```
4. Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```
## Technologies Used
- **Java**
- **Spring Boot**
- **Maven**
- **Bitboards** for chess engine implementation
- **HTML/CSS/JavaScript** for front-end (planned)
