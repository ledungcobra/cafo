const Ordering = require('../model/Ordering');
const { mongooseToObject, multipleMongooseToObject } = require('../../utils/mongoose');
const Restaurant = require('../model/Restaurant');
const Food = require('../model/Food');

const getDistanceFromLatLonInKm = require('../../utils/distance');
const Mongoose = require('mongoose');

class ShipperController {
    //[GET] /search?long=?&lat=?
    searchOrdering = async(req, res, next) => {
        let long = parseFloat(req.query.long);
        let lat = parseFloat(req.query.lat);

        let orderings = await Ordering.find({
            status: "WAITING",
        }, 'restaurant_id orderPosition');

        let distanceArr = new Array();

        for (let i = 0; i < orderings.length; i++) {
            let rest = await Restaurant.findOne({ _id: orderings[i].restaurant_id }, 'position');

            let distanceToCus = getDistanceFromLatLonInKm(lat, long, orderings[i].orderPosition.latitude, orderings[i].orderPosition.longitude);
            let distanceToRes = getDistanceFromLatLonInKm(lat, long, rest.position.latitude, rest.position.longitude);

            let ordering = new Object();
            ordering._id = orderings[i]._id;
            ordering.distanceToCus = distanceToCus;
            ordering.distanceToRes = distanceToRes;

            distanceArr.push(ordering);
        }

        //selection sort distance
        for (let i = 0; i < distanceArr.length; i++) {
            // Finding the smallest number in the subarray
            let min = i;
            for (let j = i + 1; j < distanceArr.length; j++) {
                if (distanceArr[j].distanceToCus + distanceArr[j].distanceToRes < distanceArr[min].distanceToCus + distanceArr[min].distanceToRes) {
                    min = j;
                }
            }
            if (min != i) {
                // Swapping the elements
                let tmp = distanceArr[i];
                distanceArr[i] = distanceArr[min];
                distanceArr[min] = tmp;
            }

            if (i === 5) {
                break;
            }
        }

        let n = 5;
        if (n > distanceArr.length) {
            n = distanceArr.length;
        }

        let results = new Array();
        for (let i = 0; i < n; i++) {
            let ordering = await Ordering.findOne({ _id: distanceArr[i]._id }, '-__v -updatedAt -createdAt')
            ordering = mongooseToObject(ordering);
            ordering.distanceToCus = distanceArr[i].distanceToCus;
            ordering.distanceToRes = distanceArr[i].distanceToRes;
            let rest = await Restaurant.findOne({ _id: ordering.restaurant_id }, '-_id name address operating phones image');
            rest = mongooseToObject(rest);
            let foods = new Array();

            for (let i = 0; i < ordering.foods.length; i++) {
                let food = await Food.findOne({ _id: ordering.foods[i].foodID }, '-_id -__v -category_id -createdAt -updatedAt');
                food = mongooseToObject(food);
                foods.push(food);
                foods[i].amount = ordering.foods[i].count;
            }

            ordering.foods = foods;
            ordering.restaurant = rest;
            results.push(ordering);
        }

        res.send(results);
    }

    //[POST] /get-order?order_id=?
    getOrder = async(req, res, next) => {
        const order_id = req.body.order_id;

        Ordering.findOne({ _id: order_id })
            .then(ordering => {
                if (ordering.shipper_id === null && ordering.status === 'WAITING') {
                    ordering.shipper_id = (req.userID);
                    ordering.status = "SHIPPING";
                    ordering.save();
                    res.send({ message: "Get order successfuly!" });
                } else {
                    res.send({ message: "Get order unsuccessfuly!" });
                }
            })
            .catch(next)
    }

    cancelOrder = async(req, res, next) => {
        const order_id = req.body.order_id;
        let ordering = await Ordering.findOne({ _id: order_id });

        if (req.userID.toString() === String(ordering.shipper_id) && ordering.status === 'SHIPPING') {
            ordering.status = 'WAITING';
            console.log('lololo')
            ordering.shipper_id = null;
            (await ordering).save();

            res.send({
                message: "Cancel ordering successfuly!"
            })
        } else {
            res.send({
                message: "Cancel ordering unsuccessfuly!"
            })
        }
    }

}

module.exports = new ShipperController();