//Insert db
const db = require('../config/db');
const { Mongoose } = require('mongoose');

const saveData = require('./saveData')

db.connect();

saveData();