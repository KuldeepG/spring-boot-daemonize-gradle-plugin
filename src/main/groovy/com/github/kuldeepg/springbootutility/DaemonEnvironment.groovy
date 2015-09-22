package com.github.kuldeepg.springbootutility

/**
 * Created by Kuldeep on 9/21/15.
 */
class DaemonEnvironment {
    //JAVA Environment properties
    String javaHome = System.getenv('JAVA_HOME')
    String javaExec = javaHome != null ? "${javaHome}/bin/java" : "java"
    Map<String, ApplicationSetting> apps = [:]

    def methodMissing(String name, args) {
        ApplicationSetting app = new ApplicationSetting()
        app.name = name
        apps[name] = app
        app.configure(args[0] as Closure)
    }
}
