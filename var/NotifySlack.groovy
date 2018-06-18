def call(boolean success = false, String messageIn = null) {

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
