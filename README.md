# interlok-fx-installer
Java FX installer for Interlok

## Pre-Requisites

This requires a JavaFX enabled JDK which means either OracleJDK or OpenJDK with OpenJFX with your JAVA_HOME environment variable set to the correct location. Azul systems has a Java 8 bundle with OpenJFX; one of the easiest ways is to install it via scoop. JavaFX will also be required at runtime.

```
scoop install zulufx8
```

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

## Additional configuration

All the base configuration is stored in `src/main/resources/installer.properties` which can be overriden either at build time; or via system properties on startup.
```
java -Dinterlok.version=3.10.0-RELEASE ./build/libs/interlok-fx-installer.jar
```

If you want to include a different maven compatible nexus instance (either you're mirroring our public repo, or private dependencies are stored there) then you can also do this either as a system property or do it when the installer window presents itself.

```
java -DadditionalNexusBaseUrl=http://your-nexus.com ./build/libs/interlok-fx-installer.jar
```


