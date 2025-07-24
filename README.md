# Tavloo

A modern implementation of Tavloo/Backgammon using Java with Swing GUI and socket programming for multiplayer functionality. The game supports local play, online multiplayer, and AI bot opponents.

## Features

- **Local Multiplayer**: Play against another person on the same computer
- **Online Multiplayer**: Connect to remote players via network
- **AI Bot**: Play against "Shanteegan", an AI opponent using minimax algorithm
- **Full Backgammon Rules**: Complete implementation of traditional Tavloo/Backgammon gameplay
- **Modern Build System**: Maven-based project structure with dependency management

## Requirements

- Java 17 or higher
- Maven 3.6+ (for building from source)

## Building and Running

### Using Maven (Recommended)

```bash
# Compile the project
mvn compile

# Run tests
mvn test

# Build JAR files
mvn package

# Run the client
java -jar target/TavlooClient.jar

# Run the server (for online multiplayer)
java -jar target/TavlooServer.jar
```

### Using Legacy Makefile

The project still includes the original Makefile for compatibility:

```bash
# Build using Makefile
make

# Run client
java -jar TavlooClient.jar

# Run server
java -jar TavlooServer.jar

# Clean build artifacts
make clean
```

## Game Modes

1. **Local Match**: Two players on the same computer take turns
2. **Online Match**: Connect to a remote server to play against other players
3. **Bot Match**: Play against the AI bot "Shanteegan"

## Architecture

The project follows a clean modular architecture:

- `Game`: Core game logic and state management
- `Client`: Main entry point for the game client
- `Server`: Multiplayer server for online games
- `MyFrame`: Main game window and UI controller
- `MyPanel`: Game board rendering and user input handling
- `Bot`: AI player implementation using minimax algorithm
- `TreeNode`: Decision tree node for AI algorithms
- `Listener`: Network communication handler

## Configuration

Game settings can be configured in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.ip=localhost
server.port=12300

# Game Configuration
game.title=Tavloo: Shanteegan's Rath
game.window.width=800
game.window.height=800
```

## Development

### Project Structure

```
src/
├── main/
│   ├── java/com/tavloo/     # Source code
│   └── resources/           # Configuration files
└── test/
    └── java/com/tavloo/     # Unit tests
```

### Testing

The project includes JUnit 5 tests for core game logic:

```bash
mvn test
```

### Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add/update tests as needed
5. Ensure all tests pass
6. Submit a pull request

## License

This project is open source. See the repository for license details.

## Credits

Original implementation by rom2110. Modernized with Maven build system, comprehensive documentation, and improved code structure.
