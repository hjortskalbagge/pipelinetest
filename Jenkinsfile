#!/usr/bin/env groovy

pipeline {
    agent none

    parameters {
        run description: '', 
        filter: 'SUCCESSFUL', 
        name: 'BUILD', 
        projectName: 'Test Pipeline'
    }

    Slack slackNotifier: new main.notifiers.Slack(this)

	stages {
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
				Input input = new  main.choices.Input(this,currentBuild)
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
