# interlok-fx-installer

[![GitHub tag](https://img.shields.io/github/tag/sebastien-belin-adp/interlok-fx-installer.svg)](https://github.com/sebastien-belin-adp/interlok-fx-installer/tags) ![license](https://img.shields.io/github/license/sebastien-belin-adp/interlok-fx-installer.svg) [![Build Status](https://travis-ci.org/sebastien-belin-adp/interlok-fx-installer.svg?branch=develop)](https://travis-ci.org/sebastien-belin-adp/interlok-fx-installer) [![codecov](https://codecov.io/gh/sebastien-belin-adp/interlok-fx-installer/branch/develop/graph/badge.svg)](https://codecov.io/gh/sebastien-belin-adp/interlok-fx-installer) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/sebastien-belin-adp/interlok-fx-installer.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/sebastien-belin-adp/interlok-fx-installer/context:java) [![Total alerts](https://img.shields.io/lgtm/alerts/g/sebastien-belin-adp/interlok-fx-installer.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/adaptris/interlok/alerts/)

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


