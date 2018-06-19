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

		def slackNotifier = new main.notifiers.Slack(this)
		def input = new main.choices.Input(this,currentBuild)

		stage('stage') {
			agent { node { label 'master' } }
			
			environment {
				RELEASE = "${env.GIT_BRANCH}-${env.GIT_COMMIT}"
				PROJECT_NAME = 'some project'
			}

			steps {
			    slackNotifier.send(true)
			}
		}

		stage('confirm staging stability') {
			steps {
				input.createAndNotifyOnClick(slackNotifier)
			}
		}

		stage('deploy live') {

			agent { node { label 'master' } }

			steps {
				sh "echo 'OK'"
			}

			post {
				success {
					slackNotifier.send(true, 'deployment successful')
				}
				unstable {
					slackNotifier.send(false, 'deployment unstable')
				}
				failure {
					slackNotifier.send(false, 'deployment failed')
				}
			}
		}
	}
}
