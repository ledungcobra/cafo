const express = require('express');
const route = require('./routes')
const db = require('./config/db');
const cors = require('cors')
const logger = require('morgan')
const createError = require('http-errors');
const debugHttp = require('debug')('cafo-api:http')

const app = express();

const getMessageForClient = require('./utils/message')

//Connect DB
db.connect();
app.use(cors());
app.use(logger('dev', { stream: { write: msg => debugHttp(msg.trimEnd()) } }));

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

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.send(getMessageForClient(err.status, err.message));
});

module.exports = app;