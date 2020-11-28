const VERSION = 0.2;

getMessageForClient = (message) => {
    return {
        api_name: "cafo",
        version: VERSION,
        message: message
    }
}

module.exports = getMessageForClient;