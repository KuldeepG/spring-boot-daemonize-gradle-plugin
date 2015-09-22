package com.github.kuldeepg.springbootutility

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by Kuldeep on 9/21/15.
 */
class SpringBootDaemonizePlugin implements Plugin<Project> {
    void apply(Project project) {
        DaemonEnvironment env = project.container(DaemonEnvironment)
        project.extensions.springBootDaemonize = env

        project.afterEvaluate {
            for(app in env.apps) {
                def startTaskName = "${app.key}StartDaemon"
                def stopTaskName = "${app.key}StopDaemon"

                project.task(type: DaemonStartTask, startTaskName, {settings = app.value})
                project.task(type: DaemonStopTask, stopTaskName, {settings = app.value})
            }
        }
    }
}
