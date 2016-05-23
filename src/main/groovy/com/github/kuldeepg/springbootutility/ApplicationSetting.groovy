package com.github.kuldeepg.springbootutility

/**
 * Created by Kuldeep on 9/21/15.
 */
class ApplicationSetting {
    //Application properties
    Integer serverPort = 3005
    Integer jmxPort = -1
    String ipAddress
    String serverProfile
    String propertiesFile = 'classpath:/application.yml'
    String logfile = 'application-server.log'
    String[] jvmArgs = []
    String name
    String warFile
    String contextPath

    //Application validation properties
    String serverValidationPath 
    Integer maxWait = 120 //in secs

    ApplicationSetting configure(Closure cfg) {
        cfg.delegate = this
        cfg()
        if(jmxPort == -1)
            jmxPort = serverPort + 1
        serverValidationPath = contextPath + '/health'
        ipAddress = InetAddress.getLocalHost().getHostAddress()
        validate(this)
    }

    ApplicationSetting validate(ApplicationSetting self) {
        //Check if the mandatory parameters are provided
        if(!self.warFile?.trim() || !self.serverProfile?.trim())
            assert false, "warFile and serverProfile are required parameters!!"

        return self
    }
}
