#!/usr/bin/env groovy

void NotifySlack(boolean success = false, String messageIn = null) {

	String color = '#ff0000'
	String message = 'pipeline initialized for ' + "${env.RELEASE}"

	if(success == true) {
		color = '#00ff00'
	}

	if(messageIn) {
		message = messageIn
	}

	slackSend channel: "#botlog", message: "${env.JOB_NAME}"  + ': ' + message,  color: color
}

def UserInput(currentBuild) {
	String message = 'staging confirmed'
	try {
		userInput = input(
			id: 'staging',
			message: 'staging ok?',
			ok: 'Yes'
		)
	} catch(err) { // input false
		def user = err.getCauses()[0].getUser()
		userInput = false
		userName = "${user}".toLowerCase().replaceAll(' ','.')
		message = "Aborted by: @"+userName
		currentBuild.result = 'FAILURE'
	}

	return message
}

void UserInputStep() {
	String message = UserInput(currentBuild)
	boolean confirmed = false
	if(message.indexOf('confirmed') > -1) {
		confirmed = true
	}

	NotifySlack(confirmed, message)
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
				RELEASE = "${env.GIT_BRANCH}-${env.GIT_COMMIT}"
				PROJECT_NAME = 'some project'
			}

			steps {
			    NotifySlack(true)
			}
		}

		stage('confirm staging stability') {
			steps {
				UserInputStep()
			}
		}

		stage('deploy live') {

			agent { node { label 'master' } }

			steps {
				sh "echo 'OK'"
			}

			post {
				success {
					NotifySlack(true, 'deployment successful')
				}
				unstable {
					NotifySlack(false, 'deployment unstable')
				}
				failure {
					NotifySlack(false, 'deployment failed')
				}
			}
		}
	}
}
