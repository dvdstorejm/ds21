# ds2jmeter
Jmeter script for the DVD Store 2 open source test / benchmark tool (https://github.com/dvdstore/ds21)

1. DS2JMeter.zip - is an eclipse project (developed using eclipse Neon) containing the custom Java samplers and beans used in the JMeter test plan.
2. JMeterClassesForDS2.jar - is the jar file created from (1).
3. DS2 Test Plan Improved Version 2.jmx - is the JMeter script
4. DS2 JMeter Test Plan 0.2.doc - contains details on the JMeter test script and supporting Java files.
5. TestRun-400VU-5Iterations2.xlsx - maps a sample test run output with the different stages of the test scripts.

How to run? 
To test, we need to copy the JMeterClassesForDS2.jar to /lib/ext of JMeter installation, load the JMX file in JMeter, modify the target with the IP address of a running DS2 JSP web application and run.

Work on ds3 JMeter scripts is in progress.
