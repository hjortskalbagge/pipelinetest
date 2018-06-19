#!/usr/bin/env groovy

pipeline {
    agent none

    parameters {
        run description: '', 
        filter: 'SUCCESSFUL', 
        name: 'BUILD', 
        projectName: 'Test Pipeline'
        slackNotifier: new Slack(this)
    }

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
				def input = new Input(this,currentBuild)
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
					slacknotifyer.send(true, 'deployment successful')
				}
				unstable {
					slacknotifyer.send(false, 'deployment unstable')
				}
				failure {
					slacknotifyer.send(false, 'deployment failed')
				}
			}
		}
	}
}
