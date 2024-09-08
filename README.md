#+author: Balakrishna Venkatesh Narayana

* Live Football World Cup Scoreboard Library
This library was build based on the next task requirements:
#+begin_quote
You are working in a sports data company, and we would like you to develop a new Live Football
World Cup Scoreboard library (or frontend application) that shows all the ongoing matches and their
scores.
The scoreboard supports the following operations:
1. Start a new game, assuming initial score 0 – 0 and adding it the scoreboard.
   This should capture following parameters:
     a. Home team
     b. Away team
2. Update score. This should receive a pair of absolute scores: home team score and away team score.
3. Finish game currently in progress. This removes a match from the scoreboard.
4. Get a summary of games in progress ordered by their total score. The games with the same total score will be returned ordered by the most recently started match in the scoreboard
#+end_quote

** Usage
To use this library you need to perform next steps:
1. Clone the project
  #+begin_src bash
  git clone git@github.com:balakrishnavn/score-board-library-java.git
  #+end_src
2. Run maven command
  #+begin_src bash
  mvn clean install
  #+end_src
3. Add dependency to the project
  #+begin_src xml
  <dependency>
    <groupId>com.sportradar</groupId>
    <artifactId>score-board-library</artifactId>
    <version>1.1</version>
  </dependency
  #+end_src

** Notes
In the library there is an interfaces named `ScoreBoard` that has default implementation named `ScoreBoardImpl`. This was made to be able to create different implementations based on user demand.

Based on requirements I made decision to use additional parameters named `homeTeam` and `awayTeam` to be able quickly find matches and operate on them. I could also use UUID instead of team names to find Matches and use Map to store ongoing Matches but decided to not do so as guidelines say that the solution should be the simplest I can think of.

All development performed in TDD style, meaning that firstly necessary tests created and then create implementation of method to satisfy test conditions.

