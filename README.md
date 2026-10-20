# eng1-project

## Running the project
FYI im using Intellij Idea Community Edition. \
It can be downloaded from https://github.com/jetbrains/intellij-community in the **Releases** section.

To run the project, open
```text
lwjgl3/src/main/java/io.team0xB.lwjgl3/Lwjgl3Launcher
```
and hit Run.

## Project Structure

```text
Entry Point -> Main
```

```text
Screens
    |
    |- MenuScreen
    |
    |- ControlsScreen
    |
    |- GameScreen
```
## Screens
`MenuScreen` - first screen shown when the game is launched. 
Features PLAY button.

`ControlsScreen` - immediately shown after hitting PLAY. Explains controls. 
Pressing some button proceeds to GameScreen.

`GameScreen` - main gameplay happens here.

## Utility
UiSkin

A Skin is a **named lookup table** of UI resources and styles. 
Instead of configuring every button’s font, colours and backgrounds by hand each time, 
you register them once under names, and actors fetch what they need.
The problem it solves:

A `TextButton` needs a style: a font, a background for each state (normal, hovered, pressed), 
and a font colour. Without a skin you’d build that style object for every button. 
With a skin, you build it once and write:

```java
new TextButton("Play", skin);
```

How it’s organised: \
A skin stores objects by type + name. That’s why it is possible to register 
two different things both called "default", since one is 
a `TextButtonStyle` and the other is a `LabelStyle`:

```java
skin.add("default", buttonStyle);
skin.add("default", labelStyle);
```

When an actor looks up a style, it asks for its own type with 
the name "default", so there’s no clash. \
Naming a style "default" means `new TextButton("Play", skin)` and 
`new Label("Hi", skin)` find it automatically.

## Team 0xB

Team 0xB's repository for ENG1

LINKS

Group Drive
https://drive.google.com/drive/folders/1UQP9r5Sy3fwqV7JlfrrqpQL1qGRqgC_3?usp=drive_link
Meeting Minutes
https://drive.google.com/drive/folders/14dwLzBPo0pqTl2RkaYmpeRrKNOyn6NoW?usp=drive_link
Trello Board:
https://trello.com/b/wc4Rn9f3/development-board
