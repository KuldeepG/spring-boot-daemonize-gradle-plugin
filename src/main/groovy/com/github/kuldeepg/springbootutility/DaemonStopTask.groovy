package com.github.kuldeepg.springbootutility

import org.apache.commons.lang3.SystemUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.management.MBeanServerConnection
import javax.management.remote.JMXConnector
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXServiceURL
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

/**
 * Created by Kuldeep on 9/21/15.
 */

class DaemonStopTask  extends DefaultTask {
    ApplicationSetting settings = new ApplicationSetting()

    DaemonStopTask configure(Closure config){
        config.resolveStrategy = Closure.DELEGATE_ONLY
        config.delegate = this
        config()
        this
    }

    @TaskAction
    def stopDaemon(){
        try {
            JMXServiceURL jmxUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://${settings.ipAddress}:${settings.jmxPort}/jmxrmi")
            final JMXConnector connector = JMXConnectorFactory.connect(jmxUrl)

            final MBeanServerConnection remoteConnection = connector.getMBeanServerConnection()

            final RuntimeMXBean remoteRuntime = ManagementFactory.newPlatformMXBeanProxy(
                    remoteConnection,
                    ManagementFactory.RUNTIME_MXBEAN_NAME,
                    RuntimeMXBean.class
            )

            Integer processId = Integer.parseInt(remoteRuntime.getName().split('@')[0])

            String killCommand = "kill -9 ${processId}"
            if(SystemUtils.IS_OS_WINDOWS)
                killCommand = "taskkill /F /PID ${processId}"

            Runtime.getRuntime().exec(killCommand)
        } catch(ex) {
            println "Couldn't stop ${settings.name} daemon at port ${settings.serverPort}!"
        }
        println "Successfully stopped ${settings.name} daemon at port ${settings.serverPort}!"
        assert true
    }
}
