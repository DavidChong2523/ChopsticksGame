# ChopsticksGame
An implementation of the game chopsticks in Java. It features a command line interface with the option to play against a computer player or another person.

## Computer Player Implementation Details
The computer player selects moves using minimax enhanced by alpha-beta pruning. From each position, it generates future game positions based on the possible moves itself or its opponent can play. It then evaluates each line of play and chooses the move which will cast it towards the most favorable outcome. During this process, sub-optimal lines are pruned from the game tree, which allows the computer player to evaluate deeper into the future. For evaluation, the computer player uses a q-table which stores the desirability of each game position. These desirability values were acheived through q-learning and 10,000,000 games of self play. 
