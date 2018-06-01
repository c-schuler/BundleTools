# Bundle Tools

This tooling is provided as an example for consolidating a directory of transaction bundles into a single "master" transaction bundle.

## Usage
This is a Maven project and will need to be installed on the user machine.

There are 2 arguments the user must provide when running this program:
* inputDirectoryPath: this is where all the bundles are located - currently not recursive
* outputDirectoryPath: this is where the master bundle file will be printed

Command Line:
      
      mvn exec:java -Dexec.args="inputDirectoryPath outputDirectoryPath"
  
Example (running program on a Mac):

      mvn exec:java -Dexec.args="/Users/christopherschuler/Documents/workspace/bundles /Users/christopherschuler/Desktop"
      
  Once the program has finished, look in the outputDirectory for a json or xml file named "master".