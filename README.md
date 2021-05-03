# JavaMusicHub

Welcome to our repo of ESIEA' semester 6 project. The goal was to create a Music Hub following an MVC patern: a server is able to create, save and stream songs and a client can list and listen them.
To perform this, we applied our knowledges learnd during our Software Engineering course.

## Members of the team

Our group is composed of: Elouan T, Julie A, Charles D and Anis F.

## Installation

Download the project from here or, in your favorite folder, with the command git.

```bash
git clone https://github.com/elouantoy1/JavaMusicHub
```

You need to have Java installed on your computer to execute the program.

## Execute

To launch the server execute runServer.bat or type:

```bash
java -classpath bin musichub.main.Main server
```
To launch the client execute runClient.bat or type:

```bash
java -classpath bin musichub.main.Main
```
By default, the port used is 6667. Make sure to open the port on your routeur if you need to make your application accessible from outside.

## Usage

When adding a title, make sure you copy the audio file (.wav) in the folder files/content.
After modifying some elements, enter the command "s" to write modifications on disk.

Make sure you start the server before launching the client.

## Contributing
This is a school project, so we cannot accept pull request. However, feel free to exchange with us or open an issue if you spot something strange.

## License
[MIT](https://choosealicense.com/licenses/mit/)
