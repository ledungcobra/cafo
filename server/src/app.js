const express = require('express');
const route = require('./routes')
const db = require('./config/db');
const cors = require('cors')
const logger = require('morgan')
const app = express();
const port = process.env.PORT || 3000;

//Connect DB
db.connect();
app.use(cors());
app.use(logger('dev'))

app.use(express.urlencoded({
    extended: true
}));
app.use(express.json());

//Routes init
route(app);

app.listen(port, () => {
    console.info(`cafo-api is listening at http://localhost:${port}`);
})