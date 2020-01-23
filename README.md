# ChopsticksGame
An implementation of the game chopsticks in Java. It features a command line interface with the option to play against a computer player or another person.

## Computer Player Implementation Details
The computer player selects moves using minimax enhanced by alpha-beta pruning. From each position, it generates future game positions based on the moves itself or its opponent are able to play. It then evaluates board positions and chooses the best move. For evaluation, the computer player uses a q-table updated through 10,000,000 games of self play. 
