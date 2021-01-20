#!/usr/bin/groovy
// Link: http://job/CP_Regression/

properties([[$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false],
  parameters([
    choice(choices: ['LS-dev'], description: '', name: 'slave_name')
  ])
])

node(slave_name) {

    stage('checkout') {
      checkout scm
    }

    stage('test') {
            bat(/mvn clean compile test -DsuiteXmlFile=src\test\resources\test-suites\CP-main-test-suite.xml -P qa,local,CP_HE,enable_TestRail_Reporting site -e/)
    }

    stage('report') {
      allure([
        includeProperties: false,
        jdk: '',
        properties: [],
        reportBuildPolicy: 'ALWAYS',
        results: [[path: 'target/allure-results']]
      ])
    }

}