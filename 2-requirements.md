**Mobile Development 2023/24 Portfolio**
# Requirements

Student ID: `22026756`

## Functional Requirements

* When app loads, Button to start game and setup pieces is shown

* When start game button pressed, a visual representation of chess pieces is shown

* When app loads, Visual representation of chess board is shown

* Chess pieces are clickable to reveal possible moves of that piece

* When a possible move is clicked, the piece moves to that location

* When a piece moves on top of a piece of opposite colour, it will capture that piece

* A move is not possible if it will put your King in check

* The game ends when you have no legal moves, your king is taken, your opponent has no legal moves, or your opponent's king is taken

* When the user moves, a prompt is sent to an OpenAI LLM

* When a prompt is sent to the LLM, it responds with a chess move

* When the LLM responds with a chess move, it is parsed and the move is performed

* If the LLM responds with anything other than a chess move, the user wins

* If the performed move is illegal, a counter will increase on the user's stats page for "Number of illegal moves"

* If the game is won, a counter will increase on the user's stats page for "Number of wins"

* If the game is lost, a counter will increase on the user's stats page for "Number of losses"

* After a win/loss counter increases, the percentage of wins out of total games will be calculated, and shown on the user's stats page for "Win percentage"

## Non-functional Requirements

* On a default Pixel 3 emulator in Android Studio, when the app is launched, the start game button is visible within 10 seconds

* On a default Pixel 3 emulator in Android Studio, when the start game button is pressed, the chess pieces are visible within 5 seconds

* On a default Pixel 3 emulator in Android Studio, when a chess piece is pressed, any valid moves are visually represented within 5 seconds

* On a default Pixel 3 emulator in Android Studio, when a possible move is pressed, the piece will be visually represented in its new location within 5 seconds

* On a default Pixel 3 emulator in Android Studio, when a user move is finished, the LLM's move will be visually represented within 10 seconds

* API Key is not uploaded publicly

* User is limited to 20,000 tokens per day ($0.01 worth of OpenAI tokens)

