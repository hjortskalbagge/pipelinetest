#!/usr/bin/env groovy

def slackNotifier = new main.notifiers.Slack(this)
def inputElem = new main.choices.Input(this,currentBuild)

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
				script {
					slackNotifier.send(true)
				}

			}
		}

		stage('confirm staging stability') {
			steps {
				script {
					inputElem.createAndNotifyOnClick(slackNotifier)
				}
			}
		}

		stage('deploy live') {

			agent { node { label 'master' } }

			steps {
				sh "echo 'OK'"
			}

			post {
				success {
					script {
						slackNotifier.send(true, 'deployment successful')
					}
				}
				unstable {
					script {
						slackNotifier.send(false, 'deployment unstable')
					}
				}
				failure {
					script {
						slackNotifier.send(false, 'deployment failed')
					}
				}
			}
		}
	}
}
