const mongoose = require('mongoose')

async function connect() {
    try {
        //await mongoose.connect('mongodb+srv://anhhuu:maiy3uem@cluster0.16sjn.mongodb.net/cafo_api_app?retryWrites=true&w=majority', {
        await mongoose.connect('mongodb://localhost:27017/cafo_api_dev', {
            useNewUrlParser: true,
            useUnifiedTopology: true,
            useFindAndModify: false,
            useCreateIndex: true
        });
        console.log('Connect cafo_api_dev database successfully!')
    } catch (error) {
        console.log('Connect cafo_api_dev database failure!')
    }
}

module.exports = { connect };