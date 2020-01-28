/**
 * Copyright 2020 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.savvasdalkitsis.module.deps.graph

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

class ModuleDependencyGraphPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('graphModules') {
            doLast {
                checkGraphVizIsInstalled(project)
                def graph = new StringBuilder()
                project.subprojects.each { module ->
                    def impl = getDependencies(module, "implementation")
                    def api = getDependencies(module, "api")
                    def compile = getDependencies(module, "compile")
                    if (impl.empty && api.empty && compile.empty) {
                        graph.append("\t\t${strip(module)};").append("\n")
                    }
                    graphDeps(impl, module, "black", graph)
                    graphDeps(api, module, "red", graph)
                    graphDeps(compile, module, "green", graph)
                }
                def dot = """
                digraph modules {
                    rankdir = "TB"
                    label = "Module dependencies"
                
                    ${graph.toString()}
                    
                    subgraph cluster_01 { 
                        label = "Legend"
                        graph [fontsize = 10]
                        node [fontsize = 10]
                        edge [fontsize = 10]
                        module -> dependency  [color=red, label="api"]
                        module -> dependency [color=black, label="implementation"]
                        module -> dependency [color=green, label="compile"]
                    }
                }
                """.stripIndent()
                def dotFile = File.createTempFile("module_graph", ".dot")
                def outputFile = File.createTempFile("module_graph", ".png")
                dotFile.write(dot)
                project.exec {
                    executable = "dot"
                    args("-o", outputFile.absolutePath, "-Tpng", dotFile.absolutePath)
                }
                project.exec {
                    executable = "open"
                    args(outputFile.absolutePath)
                }
            }
        }
    }

    private static checkGraphVizIsInstalled(Project project) {
        def message = "You need GraphViz installed on your system to run this task. Please visit http://www.graphviz.org/ for more information on how to install it"
        try {
            def result = project.exec {
                executable = "dot"
                args = ["-V"]
                ignoreExitValue = true
            }
            if (result.exitValue != 0) {
                throw new IllegalStateException(message)
            }
        } catch (Exception e) {
            throw new IllegalStateException(message, e)
        }
    }

    private static Set<Dependency> graphDeps(Set<Dependency> dependencies, Object module, String color, StringBuilder graph) {
        dependencies.each { dep ->
            graph.append("\t\t${strip(module)} -> ${strip(dep.dependencyProject)} [color = $color]").append("\n")
        }
    }

    private static Set<Dependency> getDependencies(Project module, String configurationName) {
        module.configurations.getByName(configurationName).dependencies.findAll {
            it.hasProperty("dependencyProject")
        }
    }

    private static def strip(Object o) {
        o.toString().replaceAll("project ':", "\"").replaceAll("'", "\"")
    }
}

