# interlok-fx-installer
Java FX installer for Interlok

## Build

```
$ ./gradlew clean jar

BUILD SUCCESSFUL in ...s
4 actionable tasks: 4 executed
```

## Execute

```
java -jar ./build/libs/interlok-fx-installer.jar
```

## Extra Property

In case a different nexus server is used to host the Interlok dependencies or some private dependencies.

```
java -DadditionalNexusBaseUrl=http://your-nexus.com ./build/libs/interlok-fx-installer.jar

```


