#!/usr/bin/env groovy

void NotifySlack(String messageIn = null, String success = false) {

	String color = '#ff0000'
	String message = 'pipeline initialized for ${env.RELEASE}'

	if(success) {
		color = '#00ff00'
	}

	if(messageIn) {
		message = step
	}

	slackSend channel: "#botlog", message: message,  color: color
}

pipeline {
    agent none

    parameters {
        run description: '', 
        filter: 'SUCCESSFUL', 
        name: 'BUILD', 
        projectName: 'Test Pipeline'
    }

	stages {
		stage('stage') {
			agent { node { label 'master' } }
			
			environment {
				RELEASE = "${env.BRANCH_NAME}-${env.GIT_COMMIT}"
				PROJECT_NAME = 'some project'
			}

			steps {
			    NotifySlack()
			}
		}

		stage('confirm staging stability') {
			steps {
				input(
					id: 'stage',
					message: 'Proceed?',
					ok: 'Yes'
				)

				String message = 'staging confirmed'
				try {
                    userInput = input(
                        id: 'Proceed1',
                        message: 'staging ok?',
                        ok: 'Yes'
                        parameters: [
                        	[$class: 'BooleanParameterDefinition', defaultValue: true, description: '', name: 'Please confirm you agree with this']
                        ])
                } catch(err) { // input false
                    def user = err.getCauses()[0].getUser()
                    userInput = false
                    message = "Aborted by: [${user}]"
                }

				NotifySlack(message )
			}
		}
	}

}
