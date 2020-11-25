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
    extended: false
}));
app.use(express.json());

app.use(function(req, res, next) {
    res.header(
        "Access-Control-Allow-Headers",
        "x-cafo-client-access-token"
    );
    next();
});

//Routes init
route(app);

app.listen(port, () => {
    console.info(`cafo-api is listening at http://localhost:${port}`);
})