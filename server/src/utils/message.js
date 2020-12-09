const VERSION = 0.2;

getMessageForClient = (status_code, message) => {
    return {
        api_name: "cafo",
        version: VERSION,
        status_code: status_code,
        message: message
    }
}

module.exports = getMessageForClient;