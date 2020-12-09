const createError = require('http-errors')

const City = require('../model/City');
const { mongooseToObject } = require('../../utils/mongoose')
const { multipleMongooseToObject } = require('../../utils/mongoose')

class CityController {
    //[GET] /cities/id/:id
    async showOneByID(req, res, next) {
        try {
            let city = await City.findOne({ _id: req.params.id }, 'name city_url');
            //check exist city
            if (city) {
                city = mongooseToObject(city);
                res.send(city);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(400, err));
        }
    }

    //[GET] /cities/:city_url
    async showOneByURL(req, res, next) {
        try {
            let city = await City.findOne({ city_url: req.params.city_url }, 'name city_url');

            //check exist city
            if (city) {
                city = mongooseToObject(city);
                res.send(city);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(400, err));
        }
    }

    //[GET] /cities
    async showFull(req, res, next) {
        try {
            let cities = await City.find();
            if (cities.length) {
                cities = multipleMongooseToObject(cities);
                res.send(cities);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(err));
        }
    }
}

module.exports = new CityController();