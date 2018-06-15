#!/usr/bin/env groovy

void NotifySlack(String release = null, boolean success = false, String messageIn = null) {

	String color = '#ff0000'
	String message = 'pipeline initialized for '+release

	if(success == true) {
		color = '#00ff00'
	}

	if(messageIn) {
		message = messageIn
	}

	slackSend channel: "#botlog", message: message,  color: color
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
		message = "Aborted by: [${user}]"
		currentBuild.result = 'FAILURE'
	}

	return message
}

//void UserInputStep(envData) {
//	String message = UserInput(currentBuild)
//	boolean confirmed = false
//	if(indexOf('confirmed', message) > -1) {
//		confirmed = true
//	}
//
//	NotifySlack(envData, confirmed, message)
//}

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
			    NotifySlack("${env.RELEASE}", true)
			}
		}

		//stage('confirm staging stability') {
		//	steps {
		//		UserInputStep("${env.RELEASE}")
		//	}
		//}
	}

}
