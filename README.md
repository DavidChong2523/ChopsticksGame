# ChopsticksGame
An implementation of the game chopsticks in Java. It features a command line interface with the option to play against a computer player or another person.

This project began as my introduction to computer science. I had attempted several forays into the field beforehand, but always found myself lost in the unfamiliar lands of code syntax and object oriented design. In order to maintain motivation, I decided I would make something tangible--a game, and in doing so learn the skills I had previously struggled with. At the same time, I envisioned that the construction of the game's computer player would pave the way for the future creation of a chess engine, something that had always fascinated me. Now, at the end of the project, I am pleased to find both these goals satisfied. The introductory skills I began with have since grown to cover concepts from machine learning to cryptography, and I have indeed successfully implemented a chess engine. You can find it [here](https://github.com/ColinAChen/cylinderChess). 

## Computer Player Implementation Details
The computer player selects moves using minimax enhanced by alpha-beta pruning. From each position, it generates future game positions based on the possible moves itself or its opponent can play. It then evaluates each line of play and chooses the move which will cast it towards the most favorable outcome. During this process, sub-optimal lines are pruned from the game tree, which allows the computer player to evaluate deeper into the future. For evaluation, the computer player uses a q-table which stores the desirability of each game position. These desirability values were acheived through q-learning and 10,000,000 games of self play. 
