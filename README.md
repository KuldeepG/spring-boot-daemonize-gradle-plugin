[![Build Status](https://travis-ci.org/KuldeepG/spring-boot-daemonize-gradle-plugin.svg?branch=master)](https://travis-ci.org/KuldeepG/spring-boot-daemonize-gradle-plugin)
# spring-boot-daemonize-gradle-plugin

This gradle plugin helps you to daemonize your spring boot applications.

###Pre-requisites

- Your project should have a standalone war file generated

###Usage

In your build.gradle file just add following lines

```
springBootDaemonize {
  <daemon name> {
    warFile = "<war file path>"
    serverProfile = "<spring boot profile name to use>"
  }
}
```

###Other configurable options

|Option|Description|
|------|-----------|
|warFile|The relative path of the spring boot standalone war file. It can be the build folder where the war is generated or can be any other location relative to the root folder of project. Eg. warfile = 'build/libs/example.war'|
|serverProfile|Spring boot profile that the daemon would be using. Eg. serverProfile = 'test'|
|serverPort|Application port that would be used. Default is 3005|
|jmxPort|JMX management connection port, if not defined, this will be the server port + 1|
|propertiesFile|Spring boot configuration properties file. Default is classpath:/application.yml|
|logfile|Relative path for the spring boot log file. Default is application.log|
|jvmArgs|Array of any additional jvm args that would be needed by the daemon.|
|serverValidationPath|Can be any valid path, that responds to the REST Options method. This is used to check if the server is up and running.|
|maxWait|The maximum number of seconds the task should wait for the application to start. Default is 120 secs.|
