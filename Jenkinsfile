#!/usr/bin/env groovy

void NotifySlack(String release = null, boolean success = false, String messageIn = null) {

	String color = '#ff0000'
	String message = 'pipeline initialized for ' + release

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

void UserInputStep(envData) {
	String message = UserInput(currentBuild)
	boolean confirmed = false
	if(message.indexOf('confirmed') > -1) {
		confirmed = true
	}

	NotifySlack(envData, confirmed, message)
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
			    NotifySlack("${env.RELEASE}", true)
			}
		}

		stage('confirm staging stability') {
			steps {
				UserInputStep("${env.RELEASE}")
			}
		}

		stage('deploy live') {
			steps {
				sh "echo 'OK'"
			}

			post {
				success {
					NotifySlack("${env.RELEASE}", true, 'deployment successful')
				}
				unstable {
					NotifySlack("${env.RELEASE}", false, 'deployment unstable')
				}
				failure {
					NotifySlack("${env.RELEASE}", false, 'deployment failed')
				}
			}
		}
	}
}
