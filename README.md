

# Chain Reaction Game

## Overview

Chain Reaction is a real-time multiplayer game where players take turns to increment and trigger chain reactions on a grid-based board. The game is implemented using Java Spring Boot for the backend, WebSocket for real-time communication, and HTML/CSS/JavaScript for the frontend. The game logic involves handling chain reactions using depth-first search (DFS) and recursion.



https://github.com/user-attachments/assets/9e43e723-05c6-4c5d-8144-dc97672a60ea




## Technologies Used

- **Java Spring Boot**: Framework used for building the backend service. It handles game logic, WebSocket communication, and state management.
- **WebSocket**: Provides real-time, bidirectional communication between the client and server, enabling instant updates to the game board.
- **HTML/CSS/JavaScript**: Frontend technologies used to create the user interface and interact with the backend via WebSocket.
- **Depth-First Search (DFS)**: Algorithm used to handle chain reactions on the game board. It recursively propagates changes to neighboring cells.

## Features

- **Real-Time Updates**: The game board updates in real-time for all players as moves are made.
- **Multiplayer**: Supports multiple players taking turns to make moves.
- **Dynamic Board**: The board dynamically changes color based on the current player's actions and chain reactions.

## Getting Started

### Prerequisites

- **Java 17 or higher**
- **Maven** for dependency management

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/adtiio/chain-reaction.git
   cd chain-reaction

2. **Build the Backend**

   ```bash
   mvn clean install

3. **Run the Backend**

   ```bash
   mvn spring-boot:run
4. The server will start on http://localhost:8081. 
  



