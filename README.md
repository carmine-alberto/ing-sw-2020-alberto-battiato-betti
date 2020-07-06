![SANTORINI](https://pbs.twimg.com/media/DcMrncHWsAIr5dB?format=jpg&name=small)
# Prova Finale di Ingegneria del Software a.a. 2019-2020
For this year's final exam, the partecipants were required to implement the game [Santorini](http://www.craniocreations.it/prodotto/santorini/) using the Java programming language and the Model-View-Controller pattern.  
The project covers all the game's rules for two and three players, except for several advanced gods whose power has not been implemented.  
Using Object-Oriented Programming principles and patterns (better detailed in the JavaDoc), two different game interfaces were developed: CLI and GUI. 
- The CLI (Command Line Interface) offers a simple game mode to be used in the command line;
- The GUI (Graphical User Interface) caters to those players who like a more complete and good-looking game experience.

## UML
The UML documentation is available in two different versions: the initial one, created as soon as the game specs were made public, and the final one, created once the game was completed. There are several and interesting differences.

* [Initial UML](https://raw.githubusercontent.com/carmine-alberto/ing-sw-2020-alberto-battiato-betti/master/deliverables/UML/OldModelController.png?token=ANPYJVHN5TTZ4PWANRURLA27A5N6S)

* [Final UML](https://raw.githubusercontent.com/carmine-alberto/ing-sw-2020-alberto-battiato-betti/master/deliverables/UML/UML_general.png?token=ANPYJVGJMHA5NRHJ7MOCBTK7A5NFM)
* [Final Model UML](https://raw.githubusercontent.com/carmine-alberto/ing-sw-2020-alberto-battiato-betti/master/deliverables/UML/UML_model.png?token=ANPYJVDRYSSKQN3U7WW2ONK7A5N42)

## JavaDoc
The project is also provided with a Java documentation that covers almost every model's class. The documentation can be found in the "deliverables" folder of the project.

## Implemented functionality
| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Advanced Gods | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Undo | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

## JAR Execution
For both client and server jars is required [Java 14.0.1 JDK](https://jdk.java.net/14/) version.
The jars can be found in the deliverables folder of the project.

### Server
You can launch the server moving to the "Jar" folder in your terminal and using the command <code> java --enable-preview -jar .\AM49-Server.jar </code>
The default port number is 1200. To change it, launch the server appending <code> -port MY_PORT_NUMBER </code> to the command above.
  
### Client
You can launch the client moving to the "Jar" folder in your terminal and using the command  <code> java --enable-preview -jar .\AM49-Client.jar </code>
- CLI 
In order to use this game interface, it will be necessary to append the parameter <code>-CLI</code> to the command above.
- GUI
By default, the interface launched is the GUI.

It's possible to switch from one mode to another in some moments during the program's execution: 
- during a CLI game by typing "switch";
- in a GUI game by selecting CLI during the login.
## Gruppo AM49

- ###   10603433    Filippo Betti ([@FilippoBetti98](https://github.com/FilippoBetti98))<br>filippo.betti@mail.polimi.it
- ###   10560934    Carmine Alberto ([@carmine-alberto](https://github.com/carmine-alberto))<br>carmine.alberto@mail.polimi.it
- ###   10556595    Jacopo Battiato ([@batjacopo](https://github.com/batjacopo))<br>jacopo.battiato@mail.polimi.it
<br>
