# Steam Key Manager
Simple JavaFX based GUI tool that helps organizing your Steam/Origin/HB keys (WIP)

# Intro & Motivation
Hi all! Originally this GUI based tool was made for my own use only, but now I decided to publish it to Github as my "early Java projects". Nothing quite special, just a handy tool to deal with my ever-increasing pool of unused Steam keys.

If you are kind of person like me, this tool might come in handy for you, too. 

Though various improvements was made to this project, I still consider this WIP and some features are to be added when I'm free

# Functions
As of now, the Steam Key Manager supports:
- Add/Remove entries to the table
- Add notes to entries
- Sort by game or notes (or keys, if you prefer)
- Edit an entry

Here's a list of planned updates to the manager (priority ones marked in **bold**):
- **Search** (coming in 0.1.0)
- Copy/paste keys from the list (coming in 0.1.0)
- ***File picker / Data parser*** (coming in 0.1.0)
- Remark entries (e.g. important, for trade, redeemed)
- Warning dialog popup (coming in 0.1.0)
- Bug fixes

# Issues? 
Should you encounter bugs and other issues, feel free to post them in the [Issues] tab.
I know for sure that my codes are still not perfect. Feel free to commend upon the codes.

Due to its immature nature I will not release a Java executable version of the manager for now.

# Changelog
## Apr 13, 2017
I have reverted a major commit made on March. 

That particular commit introduced many great functions, however it overcomplicated the code and made debugging
extremely difficult. I also did not make ANY commits (only kept local saves) while implementing functions.
Absolutely BAD habit!! After failing to resolve a weird `NullPointerException` I had to make
 this difficult decision to trash the March commit and start over from Feb commits.
 I am working on restoring the functionality introduced in Version 0.1.0 at this moment.
## Mar 17, 2017
Version 0.1.0

(This commit was reverted)
## Feb 28, 2017
Version 0.0.2
- Layout improvements
- Made table cells editable
- Added right click context menu to remove an entry
## Feb 23, 2017
Version 0.0.1
- ***Project restarted. Rewritten with JavaFX***
- *New layout design*, now it should be more intuitive and easier to use
- Proof-of-concept file parser
- A lot of code refactoring
## Apr 9, 2016
- **Added a Management tab**
- Now supported removal and search of preexisting keys
- Introduced "Undo" finction in the management tab. *Beta*
- Reorganized layout
- Code cleanup
- Minor bugfix
- And where the heck is my HTC Vive shipment?!
## Feb 21, 2016
- **Initial commit**. Only framework of the manager
- Support addition of keys only

[Issues]: <https://github.com/l19980623/SteamKeyManager/issues>
