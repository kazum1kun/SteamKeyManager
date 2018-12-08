# An Important Update to the Project
As of December 2018, the development has moved to [QSteamKeyManager](https://github.com/l19980623/QSteamKeyManager), a Python/Qt rewrite of the project. The current project will no longer receive any update from me. Thank you for your support, and do check out the new project!

# Steam Key Manager
Simple JavaFX based GUI tool that helps organizing your Steam keys and redemption URLs (WIP)

Latest release: [0.2.1-beta] at Mar 3, 2018

## Intro & Motivation
Hi all! Originally this GUI based tool was made for my own use only, but now I decided to publish it to Github as my "early Java projects". Nothing quite special, just a handy tool to deal with my ever-increasing pool of unused Steam keys.

If you are kind of person like me, this tool might come in handy for you, too. 

Though various improvements was made to this project, I still consider this WIP and some features are to be added when I'm free

## How to Use
Download & run the latest executable from the [Releases] tab. You need Java RE installed on your computer. You can 
download a free copy [here].

Currently the file parser is very immature and can only recognize a very specific pattern of data:

`AAAAA-BBBBB-CCCCC; Pseudo Game` or `Pseudo Game; AAAAA-BBBBB-CCCCC`

I did attempt to include more delimiters into the SKM, but it ended up taking URLs apart, so I will come back to this later.

## Functions
As of now, the Steam Model.Key Manager supports:
- Add/Remove entries to the table
- Add notes to entries
- Sort by game or notes (or keys, if you prefer)
- Search for game/notes
- See a game in Steam
- Edit an entry
- Copy key to the system clipboard
- Import keys from an existing text file
- Save the table to a text file
- Multi-language support (English, Simp. Chinese and Japanese)
- Intuitive, minimal and modern UI design

Here's a list of planned updates to the manager (priority ones marked in **bold**):
- Improved file parser (for Non-SKM formats)
- Remark entries (e.g. important, for trade, redeemed)
- Support for more formats (being worked on)
    - SQL
    - MS Excel (.xlsx)
- Encrypt local storage
- Bug fixes

## Issues? 
Should you encounter bugs and other issues, feel free to post them in the [Issues] tab.
I know for sure that my codes are still not perfect. Feel free to commend upon the codes.

## Changelog
[See here]

[0.2.1-alpha]: <https://github.com/l19980623/SteamKeyManager/releases/tag/v0.2.1>
[Issues]: <https://github.com/l19980623/SteamKeyManager/issues>
[Releases]: <https://github.com/l19980623/SteamKeyManager/releases>
[here]: <https://java.com/download>
[See here]: <https://github.com/l19980623/SteamKeyManager/blob/master/CHANGELOG.md>
[PyQt]: <https://riverbankcomputing.com/software/pyqt/intro>
