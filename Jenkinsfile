#!/usr/bin/env groovy

pipeline {
    agent none

	def NotifySlack(messageIn, success) {

		String color = '#ff0000'
		String message = 'pipeline initialized for ${env.RELEASE}'

		if(success == "ok") {
			color = '#00ff00'
		}

		if(messageIn) {
			message = step
		}

		slackSend channel: "#botlog", message: message,  color: color
	}


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

				NotifySlack('staging confirmed')
			}
		}
	}

}
