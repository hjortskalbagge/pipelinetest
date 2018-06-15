#!/usr/bin/env groovy

void NotifySlack(String release = null, String messageIn = null, String success = false) {

	String color = '#ff0000'
	String message = 'pipeline initialized for '+release

	if(success) {
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
			ok: 'Yes',
			//parameters: [[
			//	$class: 'BooleanParameterDefinition',
			//	defaultValue: true,
			//	description: '',
			//	name: 'Please confirm you agree with this'
			//]]
		)
	} catch(err) { // input false
		def user = err.getCauses()[0].getUser()
		userInput = false
		message = "Aborted by: [${user}]"
		currentBuild.result = 'FAILURE'
	}

	return message
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
			    NotifySlack("${env.RELEASE}")
			}
		}

		stage('confirm staging stability') {
			steps {
				NotifySlack("${env.RELEASE}", UserInput(currentBuild))
			}
		}
	}

}
