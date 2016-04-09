# Steam Key Manager
Simple Java Swing based GUI tool that helps organizing your Steam/Origin/HB keys (WIP)

##Intro & Motivation
Hi all! Originally this GUI based tool was made for my own use only, but now I decided to publish it to Github as my "early Java projects". Nothing quite special, just a handy tool to deal with my ever-increasing pool of unused Steam keys.

If you are kind of person like me, this tool might come in handy for you, too. 

Though various improvements was made to this project, I still consider this WIP and some features are to be added when I'm free

##Functions
As of now, the manager has the following functions:
- Read a preexisting text file (.txt) line by line to extract keys from it
- Add/Remove keys to the list
- Search (either by game or by key)
- Undo all the changes made before you push "Commit changes" button (beta)

Here's a list of potential updates to the manager (priority ones marked in **bold**):
- **Copy/paste keys from the list**
- Edit an entry
- **Implement a table with header instead of list of strings**
- UI improvements
- More intuitive and modern file picker
- **Sort the keys by game titles**
- Separate String resouces from the source code (so they are not "hard-coded" into the classes)
- **Remark entries (e.g. important, for trade, redeemed)**
- Warning dialog popup
- (Possibly) sfx when user attempts a illegal action but not fatal to the program
- Bug fixes

##Issues? 
Should you encounter bugs and other issues, feel free to post them in the [Issues] tab.
I know for sure that my codes are still not perfect. Feel free to commend upon the codes. I am a Java learner, after all.

And, unfortunately, due to its immature nature I will not release a Java executable version of the manager for now. I apologize for this inconvinence

##Changelog
###Apr 9, 16
- **Added a Management tab**
- Now supported removal and search of preexisting keys
- Introduced "Undo" finction in the management tab. *Beta*
- Reorganized layout
- Code cleanup
- Minor bugfix
- And where the heck is my HTC Vive shipment?!

###Feb 21, 16
- **Initial commit**. Only framework of the manager
- -Supported addition of keys only

[Issues]: <https://github.com/l19980623/SteamKeyManager/issues>
