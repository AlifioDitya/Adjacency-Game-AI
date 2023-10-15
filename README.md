# Adversarial Adjacency-Strategy-Game
<kbd>
  <img src="https://github.com/ahnjedid/Adjacency-Strategy-Game/blob/master/screenshots/gamePlay.png">
</kbd>

<hr>

### Introduction
The “Adjacency” program was designed as an interactive and strategic board game that is played between two players. Using X’s and O’s on an 8x8 board outputted on a computer screen, the objective of the game is for a player to get more X’s than O’s in total or vice versa after a specific number of rounds.

The initial game starts with 4 X’s in the bottom left corner and 4 O’s in the top right corner.  The first player will begin by placing an X on an empty square (through clicking).  In turn, all O’s that are on adjacent squares are then replaced with X’s.  Play will continue with the second player taking his turn by placing an O on an empty square.  In turn, all X’s that are on adjacent squares are then replaced with O’s.

<hr>

### Installation (IntelliJ)
To get started, please install the latest version of the Java Development Kit (JDK) and please install a Java IDE such as <a href="https://www.jetbrains.com/idea/">IntelliJ</a>. Please note that the deployment instructions below use IntelliJ as the IDE.

<hr>

### Installation
0. Make sure you have JDK 11 installed
1. Clone the repo and cd to the folder
``` bash
$ git clone https://github.com/AlifioDitya/Adjacency-Game-AI.git
$ cd adjacency-game-ai
```
2. Run the program
For Linux/MacOS
``` bash
$ ./run.sh
```
For Windows
``` bash
$ ./run.bat
```

<hr>

### Program Instructions
1. Run the Main class to load the program, and the input window below will pop up. Input the names of Player (X) and Bot (O) into their respective text fields.
Then, select the number of rounds (a number between 2 and 28) to play using the dropdown menu.
Also select the algorithm of the bot you want to play against.
You can make the Bot start first.
<br><br><kbd>
<img src="https://github.com/ahnjedid/Adjacency-Strategy-Game/blob/master/screenshots/inputScreen.png"></kbd>
<br><br>
2. Click Play, and the gameboard and scoreboard window will load. Player (X) starts the game by clicking on an empty button. Any adjacent O’s will change to X's as a result. 
3. Then, Bot (O) has their turn by also clicking on an empty button. Any adjacent X’s will change to O's as a result. NOTE: This process is counted as 1 round (Player and Bot both taking their turns).
4. The game will continue until there are no more rounds left to play. In the end, the player with the greater number of letters is the winner of the game.
<kbd>
  <img src="https://github.com/ahnjedid/Adjacency-Strategy-Game/blob/master/screenshots/endOfGame.png">
</kbd>

<hr>

### Notes
<ul>
  <li>Built with <a href="https://openjfx.io/">JavaFX</a></li>
  <li>Modified by ITB Graphics and AI Lab Assistant 2020</li>
  <li>Finalized by Group 13 & 57 of IF3170 Artificial Intelligence course project</li>
</ul>

<hr>

### Contributors
1. Enrique Alifio Ditya (13521142)
2. Ariel Jovananda (13421086)
3. M. Zulfiansyah Bayu Pratama (13521028)
4. Jauza Lathifah Annassalafi (13521030)

<hr>

### Task Distribution
| Task | Type | Contributors |
| --- | --- | --- |
| Environment | Code | Enrique Alifio Ditya |
| Minimax | Code | Enrique Alifio Ditya |
| Local Search | Code | Enrique Alifio Ditya |
| Genetic Algorithm | Code | Ariel Jovananda |
| Introduction | Report | Jauza Lathifah Annassalafi |
| Minimax | Report | M. Zulfiansyah Bayu Pratama |
| Local Search | Report | Jauza Lathifah Annassalafi |
| Genetic Algorithm | Report | Ariel Jovananda, M. Zulfiansyah Bayu Pratama |