#!/usr/bin/env groovy

@Library('./lib.groovy')_
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
			    NotifySlack
			}
		}

		stage('confirm staging stability') {
			steps {
				input(
					id: 'stage',
					message: 'Proceed?',
					ok: 'Yes'
				)

				// NotifySlack 'staging confirmed'
			}
		}
	}

}
