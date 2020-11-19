const express = require('express');
const route = require('./routes/index')
const db = require('./config/db');
const cors = require('cors')
const app = express();
const port = process.env.PORT || 5000;

//Connect DB
db.connect();
console.log(1);
app.use(cors());

app.use(express.urlencoded({
    extended: true
}));
app.use(express.json());

//Routes init
route(app);
console.log(2);

app.listen(port, () => {
    console.info(`cafo-api is listening at http://localhost:${port}`);
})


/* const insertData = require('./utils/insertRawData');
insertData(); */
console.log(3);

/* 
const Address = require('./app/model/Address')

const ad = new Address({
    district: "5",
    city: "Hồ Chí Minh",
    city_url: "ho-chi-minh",
})

//console.log(menu);

ad.save(function(err) {
    if (err) return handleError(err);
}); */