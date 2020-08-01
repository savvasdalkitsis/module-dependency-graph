# module-dependency-graph

This gradle plugin adds a task to your project that will create a graph of how your gradle modules depend on each other.

For example, this is the module graph of the Android project [GameFrame](https://github.com/savvasdalkitsis/gameframe):

<img src=module_graph.png width=800 />

## Installation

Apply the gradle plugin on your root `build.gradle` file:

```
plugins {
    id 'com.savvasdalkitsis.module-dependency-graph' version '0.7'
}
```

`legacy syntax:`
```gradle
buildscript {
    repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath "com.savvasdalkitsis:module-dependency-graph:0.7"
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

### Specify file locations

You can specify where the dot file and output png get created by specifying the following properties:

```bash
./gradlew graphModules -PdotFilePath={ABSOLUTE_PATH_TO_DOT_FILE} -PgraphOutputPngPath={ABSOLUTE_PATH_TO_PNG_FILE}      
```

or in the `gradle.properties` file:

```
dotFilePath={ABSOLUTE_PATH_TO_DOT_FILE}
graphOutputPngPath={ABSOLUTE_PATH_TO_PNG_FILE}
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