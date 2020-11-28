const City = require('../model/City');
//const Restaurant = require('../model/Restaurant');
const { mongooseToObject } = require('../../utils/mongoose')
const { multipleMongooseToObject } = require('../../utils/mongoose')

const getMessageForClient = require('../../utils/message')

class CityController {
    //[GET] /cities/id/:id
    showOneByID(req, res, next) {
        City.findOne({ _id: req.params.id }, 'name city_url')
            .then(city => {
                city = mongooseToObject(city);
                res.send(city);
            })
            .catch(err => {
                res.send(getMessageForClient('null'));
            });
    }

    //[GET] /cities/:city_url
    showOneByURL(req, res, next) {
        City.findOne({ city_url: req.params.city_url }, 'name city_url')
            .then(city => {
                city = mongooseToObject(city);
                res.send(city);
            })
            .catch(err => {
                res.send(getMessageForClient('null'));
            });
    }

    //[GET] /cities
    showFull(req, res, next) {
        City.find()
            .then(cities => {
                cities = multipleMongooseToObject(cities);
                console.log(cities)
                res.send(cities);
            })
            .catch(err => {
                res.send(getMessageForClient('null'));
            });
    }
}

module.exports = new CityController();