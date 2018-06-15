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

	def json = new groovy.json.JsonBuilder()
	json {
           text  "New comic book alert!"
           attachments ([
             {
               title  "The Further Adventures of Slackbot"
               fields  ([
                 {
                   title  "Volume"
                   value  "1"
                   short  true
                 }
                 {
                   title  "Issue"
                   value  "3"
                   short  true
                 }
               ])
               author_name  "Stanford S. Strickland"
               author_icon  "http //a.slack-edge.com/7f18https //a.slack-edge.com/bfaba/img/api/homepage_custom_integrations-2x.png"
               image_url  "http //i.imgur.com/OJkaVOI.jpg?1"
             }
             {
               title  "Synopsis"
               text  "After @episod pushed exciting changes to a devious new branch back in Issue 1  Slackbot notifies @don about an unexpected deploy..."
             }
             {
               fallback  "Would you recommend it to customers?"
               title  "Would you recommend it to customers?"
               callback_id  "comic_1234_xyz"
               color  "#3AA3E3"
               attachment_type  "default"
               actions  ([
                 {
                   name  "recommend"
                   text  "Recommend"
                   type  "button"
                   value  "recommend"
                 }
                 {
                   name  "no"
                   text  "No"
                   type  "button"
                   value  "bad"
                 }
               ])
             }
           ])
         }
    String attachment = json.toPrettyString()

	//slackSend channel: "#botlog", message: message,  color: color
	slackSend(color: color, channel: '#botlog', attachments: attachment)


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
	}

}
