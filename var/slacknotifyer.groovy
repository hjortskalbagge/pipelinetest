def NotifySlack(boolean success = false, String messageIn = null) {

	String color = '#ff0000'
	String message = 'pipeline initialized for ' + "${env.RELEASE}"

	if(success == true) {
		color = '#00ff00'
	}

	if(messageIn) {
		message = messageIn
	}

	slackSend channel: "#botlog", message: "${env.JOB_NAME}"  + ': ' + message,  color: color
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

void UserInputStep() {
	String message = UserInput(currentBuild)
	boolean confirmed = false
	if(message.indexOf('confirmed') > -1) {
		confirmed = true
	}

	NotifySlack(confirmed, message)
}