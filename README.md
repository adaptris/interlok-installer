# interlok-installer

[![GitHub tag](https://img.shields.io/github/tag/adaptris/interlok-installer.svg)](https://github.com/adaptris/interlok-installer/tags) [![license](https://img.shields.io/github/license/adaptris/interlok-installer.svg)](https://github.com/adaptris/interlok-installer/blob/develop/LICENSE) [![Build Status](https://travis-ci.org/adaptris/interlok-installer.svg?branch=develop)](https://travis-ci.org/adaptris/interlok-installer) [![codecov](https://codecov.io/gh/adaptris/interlok-installer/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-installer) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/adaptris/interlok-installer.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/adaptris/interlok-installer/context:java) [![Total alerts](https://img.shields.io/lgtm/alerts/g/adaptris/interlok-installer.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/adaptris/interlok-installer/alerts/)

Java FX installer for Interlok. This automatically downloads the selected optional components along with the base system to build an Interlok installation in your preferred location.

* It uses gradle behind the scenes.
* It makes use our parent build.gradle [https://github.com/adaptris-labs/interlok-build-parent](https://github.com/adaptris-labs/interlok-build-parent)
* You can optionally download the generated build.gradle to ease your transition to using build.gradle since this is our preferred project management tool.

## Build

### Simple Jar

```
$ ./gradlew clean jar
```

### Uber Jar

```
$ ./gradlew clean uberJar -PtargetPlatform=(win|linux|mac)
```

### Zip or Tar (Jar + Script)

```
$ ./gradlew clean assemble -PtargetPlatform=(win|linux|mac)
```

## Execute

### Uber Jar

```
java -jar ./build/libs/interlok-installer-(version)-(win|linux|mac).jar
```

### Zip or Tar (Jar + Script)

First unzip or untar the archive file, then launch bin/interlok-installer.bat or bin/interlok-installer depending if you are on windows or linux/mac.

## Publish

The simple jar, the uber jar and the targeted platform archive will be published (zip file on windows, tar file on linux or mac).

```
$ ./gradlew clean publish -PtargetPlatform=(win|linux|mac)
```

## Test

```
$ ./gradlew clean check
```

## Additional configuration

All the base configuration is stored in `src/main/resources/installer.properties` which can be overriden either at build time; or via system properties on startup.

```
java -Dinterlok.version=3.12.0-RELEASE ./build/libs/interlok-installer-(version)-(win|linux|mac).jar
```

If you want to include a different maven compatible nexus instance (either you're mirroring our public repo, or private dependencies are stored there) then you can also do this either as a system property or do it when the installer window presents itself.

```
java -DadditionalNexusBaseUrl=http://your-nexus.com/path/to/content ./build/libs/interlok-installer-(version)-(win|linux|mac).jar
```

## Notes

* To start the installed Interlok you can either executable the bat or bash file in the bin directory or run `java -jar lib/interlok-boot.jar`. Please note that on linux and mac you may need to make the bash file executable `chmod +x ./start-interlok`
* If you want to "run as Windows services" then we suggest using something like [https://github.com/winsw/winsw](https://github.com/winsw/winsw) to wrap the java process.
* You will have to explicitly choose all the optional components you want
* You will need internet access

