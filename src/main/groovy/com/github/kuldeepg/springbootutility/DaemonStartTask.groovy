package com.github.kuldeepg.springbootutility

import groovyx.net.http.RESTClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by Kuldeep on 9/21/15.
 */
class DaemonStartTask extends DefaultTask {
    ApplicationSetting settings = new ApplicationSetting()

    DaemonStartTask configure(Closure config){
        config.resolveStrategy = Closure.DELEGATE_ONLY
        config.delegate = this
        config()
        this
    }

    @TaskAction
    def startDaemon(){
        DaemonEnvironment environmentSettings = getProject().getExtensions().findByName("springBootDaemonize")

        File logFile = project.file(settings.logfile)

        def processArgs = ([
                environmentSettings.javaExec,
                new String("-Dserver.port=${settings.serverPort}"),
                new String("-Dspring.profiles.active=${settings.serverProfile}"),
                new String("-Dspring.config.location=${settings.propertiesFile}"),
                "-Dcom.sun.management.jmxremote",
                new String("-Dcom.sun.management.jmxremote.port=${settings.jmxPort}"),
                "-Dcom.sun.management.jmxremote.authenticate=false",
                "-Dcom.sun.management.jmxremote.ssl=false",
                new String("-Djava.rmi.server.hostname=${settings.ipAddress}"),
                settings.jvmArgs,
                "-jar",
                settings.warFile,
        ]).flatten()

        ProcessBuilder processBuilder = new ProcessBuilder(processArgs)

        processBuilder.directory(project.file('.'))
        processBuilder.redirectErrorStream(true)
        processBuilder.redirectOutput(logFile)

        Process process = processBuilder.start()
        Integer waitInSecs = settings.maxWait
        String url = new String("http://${settings.ipAddress}:${settings.serverPort}/")
        def urlStringBuilder = new StringBuilder()
        urlStringBuilder.append(url)
        if(${settings.contextRoot}?.trim()){
            urlStringBuilder.append( ${settings.contextRoot}).append( "/")
        }
        RESTClient client = new RESTClient(url.toString())
        client.getClient().getParams().setParameter("http.connection.timeout", 5000)
        client.getClient().getParams().setParameter("http.socket.timeout", 5000)

        while(waitInSecs > 0) {
            try {
                client.options path: settings.serverValidationPath
                break
            } catch(ex) {
                if(ex instanceof java.net.SocketTimeoutException)
                    waitInSecs -= 5
                else
                    waitInSecs -= 1

                sleep 1
            }
        }
        if(waitInSecs == 0) {
            process.destroy()
            assert false, "${settings.name} daemon couldn't be started within ${settings.maxWait} seconds. Check the ${settings.logfile} to see possible errors, or increase the maxWait time."
        }

        println "${settings.name} daemon successfully started on port: ${settings.serverPort}."
        println "========================================================================================================"
        println "Daemon name            : ${settings.name}"
        println "Server port            : ${settings.serverPort}"
        println "Spring profile         : ${settings.serverProfile}"
        println "JMX port               : ${settings.jmxPort}"
        println "IP address             : ${settings.ipAddress}"
        println "Logfile                : ${settings.logfile}"
        println "Additional JVM args    : ${settings.jvmArgs}"
        println "========================================================================================================"

        assert true
    }
}
