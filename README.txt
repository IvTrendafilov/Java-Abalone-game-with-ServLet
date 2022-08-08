First you need to have ansii escape console downloaded if you are using eclipse.
Do that via the marketplace. We suggest using Eclipse with dark mode on with default ansi escape settings.
If you do not want to use the dark mode, you can open Window-Preferences-Ansi console and tinker with the color settings.
In order to import the project create a project named PP and then import the archive with the src folder.
After this you run AbaloneServer,then AbaloneClient.
Commands are given for example
create:room:som(password):2(number of players)
for a player to join you need to write
join:room:som
after this 2/3/4 must write ready fro the game to start
after this the command is given for example move:24:31:R
if you want to leave when it is your turn you write quite
if you want to disconnect from server you write d
if you want to see the list of rooms you write list
if you want to see the players when you have created/joined a room you write getplayers
if you want a hint you just type hint
If you want to play with a bot you have to run the ComputerAbalonePlayerClient join a room/write ready
and after that the bot starts moving on his own