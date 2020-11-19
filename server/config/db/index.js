const mongoose = require('mongoose')

async function connect() {
    try {
        await mongoose.connect('mongodb+srv://anhhuu:maiy3uem@cluster0.16sjn.mongodb.net/cafo_api_app?retryWrites=true&w=majority', {
            useNewUrlParser: true,
            useUnifiedTopology: true,
            useFindAndModify: false,
            useCreateIndex: true
        });
        console.log('Connect cafo_dev database successfully!')
    } catch (error) {
        console.log('Connect cafo_dev database failure!')
    }
}

module.exports = { connect };