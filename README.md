# module-dependency-graph

This gradle plugin adds a task to your project that will create a graph of how your gradle modules depend on each other.

For example, this is the module graph of the Android project [GameFrame](https://github.com/savvasdalkitsis/gameframe):

<img src=module_graph.png width=800 />

## Installation

Apply the gradle plugin on your root `build.gradle` file:

```
plugins {
    id 'com.savvasdalkitsis.module-dependency-graph' version '<latest_version>'
}
```

`legacy syntax:`
```gradle
buildscript {
    repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath "com.savvasdalkitsis:module-dependency-graph:<latest_version>"
    }
}

apply plugin: "com.savvasdalkitsis.module-dependency-graph"
```

## Usage

From the command line, simply run:

```bash
./gradlew graphModules
```

This will create a png of the dependency graph of your modules and open the image

### Filtering

You can specify a comma separated list of modules to include in the output. If this is 
specified the graph will contain only these modules and their dependants and dependencies.
This can be helpful when trying to clean up a really complicated dependency tree
and you need to focus on only a part of it.

```bash
./gradlew graphModules -PgraphFilter=core,presentation:api,network
```

### Specify file locations

You can specify where the dot file and output file get created by specifying the following properties:

```bash
./gradlew graphModules -PdotFilePath={ABSOLUTE_PATH_TO_DOT_FILE} -PgraphOutputFilePath={ABSOLUTE_PATH_TO_OUTPUT_FILE}      
```

or in the `gradle.properties` file:

```
dotFilePath={ABSOLUTE_PATH_TO_DOT_FILE}
graphOutputFilePath={ABSOLUTE_PATH_TO_OUTPUT_FILE}
```

### Support for svg and other formats

Apart from `png`, which is the default format, this plugin has support for other formats, like `svg` and any
other format that `graphviz` supports (can be found [here](https://www.graphviz.org/doc/info/output.html)):

```bash
./gradlew graphModules -PgraphOutputFormat=svg      
```

or in the `gradle.properties` file:

```
graphOutputFormat=svg
```

### Auto open output file

By default, the generated graph will be opened using the system's default app for handling
the specified format. If you don't want this to happen, you can specify the following parameter:

```bash
./gradlew graphModules -PautoOpenGraph=false      
```

or in the `gradle.properties` file:

```
autoOpenGraph=false
```

### Transform dot file

By default, the command line tool `dot` will be used to transform an output defined in the
Gradle property `graphOutputFormat`. If you don't want this to happen, 
you can specify the following parameter:

```bash
./gradlew graphModules -PtransformDot=false      
```

or in the `gradle.properties` file:

```
transformDot=false
```

## Requirements

You must have graphviz installed on your system in order to use this plugin. For more information on how to install it visit http://www.graphviz.org/

## Issues

The plugin currently only works on macOS and Linux since it is in the very early stages (as reflected by its version number). Multi-platform support will be coming soon along with many customization options.  

License
-------

    Copyright 2020 Savvas Dalkitsis

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
