void NotifySlack(messageIn = null, success = false) {

	String color = '#ff0000'
	String message = 'pipeline initialized for ${env.RELEASE}'

	if(success) {
		color = '#00ff00'
	}

	if(messageIn) {
		message = step
	}

	slackSend channel: "#botlog", message: message,  color: color
}