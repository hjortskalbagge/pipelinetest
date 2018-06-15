#!/usr/bin/env groovy

pipeline {
    agent none

    parameters {
        run description: '', 
        filter: 'SUCCESSFUL', 
        name: 'BUILD', 
        projectName: 'Test Pipeline'
    }

	stages {
		stage('Deploy') {
			agent { node { label 'master' } }
			
			environment {
				RELEASE = "${env.BRANCH_NAME}-${env.GIT_COMMIT}"
				PROJECT_NAME = 'obelisk-testing'
			}

			steps {
			    slackSend channel: "#botlog", message: "started building ${env.RELEASE}",  color: "#0e660d"
			}
		}
	}

}
