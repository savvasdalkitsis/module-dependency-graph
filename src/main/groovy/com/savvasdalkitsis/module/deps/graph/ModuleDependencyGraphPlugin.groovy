package com.savvasdalkitsis.module.deps.graph

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.StopExecutionException

import java.util.concurrent.TimeUnit

class ModuleDependencyGraphPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('graphModules') {
            doLast {
                if (!hasGraphVizInstalled(project)) {
                    throw new IllegalStateException("You need GraphViz installed on your system to run this task. Please visit http://www.graphviz.org/ for more information on how to install it")
                }
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

    private static boolean hasGraphVizInstalled(Project project) {
        try {
            def result = project.exec {
                executable = "dot"
                args "-V"
                ignoreExitValue = true
            }
            return result.exitValue == 0
        } catch (Exception e) {
            return false
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

