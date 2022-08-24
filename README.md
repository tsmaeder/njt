# njt

A place for some experiments indexing Java sources

The basic idea is to make a complete index of resolved sources and binaries. This means reference
information can be reused in particular for immutable assets, for example a particular version of a 
jar file from maven or a particular JDK class library version. 

The impetus behind these experiments is twofold:

1. Allow for sharing of resources in the case of language services at web scale (think online-IDE's). 
For example, you could cut down on startup of a new cloud workspace if you didn't have to index the 
JDK classes upon first startup.

2. Reuse the Eclipse parser infrastructure without the burden of the Eclipse project model. The days of the "my way or high way" IDE are over: project object models are determined by build systems these days, 
not IDE's (maven, gradle, etc.)
