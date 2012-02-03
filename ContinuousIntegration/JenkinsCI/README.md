# JenkinsCI

This component provides functionality for accessing Jenkins CI and adding a build
job to it.

## System requirements
This component was tested using Jenkins version 1.450.

## Tests

In order to run the tests, a JIRA instance needs to be running locally under
port 8080. A user with credentials test:12345 should be created. Also, be make
sure that no build job called "Janus" exists.

## TODO
 - support build custimization, i.e., how the source code is retrieved and which
   build steps are involved.
 - verify that the job does not exist (use http://localhost:8080/api/xml)
 -