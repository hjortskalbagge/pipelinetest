#!/usr/bin/env groovy

library identifier: 'slacknotifyer@master', retriever: modernSCM(
  [$class: 'GitSCMSource',
   remote: 'https://github.com/hjortskalbagge/pipelinetest.git',
   credentialsId: ''])



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
			    slacknotifyer.NotifySlack(true)
			}
		}

		stage('confirm staging stability') {
			steps {
				slacknotifyer.UserInputStep()
			}
		}

		stage('deploy live') {

			agent { node { label 'master' } }

			steps {
				sh "echo 'OK'"
			}

			post {
				success {
					slacknotifyer.NotifySlack(true, 'deployment successful')
				}
				unstable {
					slacknotifyer.NotifySlack(false, 'deployment unstable')
				}
				failure {
					slacknotifyer.NotifySlack(false, 'deployment failed')
				}
			}
		}
	}
}
