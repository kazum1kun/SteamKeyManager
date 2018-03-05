# Changelog

### Version 0.2.1-alpha
Released Mar 4, 2018
- ~(minor) Fixed occasional NPE issue with "Show in Steam..." context menu

### Version 0.2.0-alpha
Released Jul 12, 2017
- +(major) Search functionality added
- ~(minor) Table cells now commit changes (rather than discard them) on focus lost
- ~(QoL) Language selector now always indicate correct "selected language"
- -(minor) Changelog is now separate from README

### Version 0.1.0-alpha
**"The internationalization update"**, released Jul 4, 2017

Happy independence day!!

After days of coding and testing I am glad to announce this major feature update to SKM. 
This update in fact included more features than original 0.1.0. Enjoy!

- +(major) Added localization for Simp Chinese and Japanese
- +(major) Implemented functionality to write to files. Now the read/write should be fully functional
- +(minor) Added more dialogs to make SKM more responsive
- ~(minor) Tweaked the layout of the key table and other areas
- ~(minor) QoL changes and bug fixes


### Version 0.0.5-1-alpha
Released on Jun 26, 2017
- +(minor) Added "Copy Model.Key" and "Copy Model.Key and Remove" to the context menu
- ~(minor) Tweaked changelog format

### Version 0.0.5-alpha
Released on Jun 25, 2017

- **You can now import a existing text file and let SKM parse it for keys**
- Warning / info dialog added
- Imported Gradle bundle to facilitate development

### Apr 13, 2017
I have reverted a major commit made on March. 

That particular commit introduced many great functions, however it overcomplicated the code and made debugging
extremely difficult. I also did not make ANY commits (only kept local saves) while implementing functions.
Absolutely BAD habit!! After failing to resolve a weird `NullPointerException` I had to make
 this difficult decision to trash the March commit and start over from Feb commits.
 I am working on restoring the functionality introduced in Version 0.1.0 at this moment.

### Mar 17, 2017
Version 0.1.0

(This commit was reverted)

### Version 0.0.2
Released on Feb 28, 2017
- Layout improvements
- Made table cells editable
- Added right click context menu to remove an entry
### Version 0.0.1
Released on Feb 23, 2017
- ***Project restarted. Rewritten with JavaFX***
- *New layout design*, now it should be more intuitive and easier to use
- Proof-of-concept file parser
- A lot of code refactoring
### Apr 9, 2016
- **Added a Management tab**
- Now supported removal and search of preexisting keys
- Introduced "Undo" finction in the management tab. *Beta*
- Reorganized layout
- Code cleanup
- Minor bugfix
- And where the heck is my HTC Vive shipment?!
### Feb 21, 2016
- **Initial commit**. Only framework of the manager
- Support addition of keys only