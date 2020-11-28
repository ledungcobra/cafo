const Ordering = require('../model/Order');
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

        let orders = await Ordering.find({
            status: "WAITING",
        }, 'restaurant_id orderPosition');

        let distanceArr = new Array();

        for (let i = 0; i < orders.length; i++) {
            let rest = await Restaurant.findOne({ _id: orders[i].restaurant_id }, 'position');

            let distanceToCus = getDistanceFromLatLonInKm(lat, long, orders[i].orderPosition.latitude, orders[i].orderPosition.longitude);
            let distanceToRes = getDistanceFromLatLonInKm(lat, long, rest.position.latitude, rest.position.longitude);

            let order = new Object();
            order._id = orders[i]._id;
            order.distanceToCus = distanceToCus;
            order.distanceToRes = distanceToRes;

            distanceArr.push(order);
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
            let order = await Ordering.findOne({ _id: distanceArr[i]._id }, '-__v -updatedAt -createdAt')
            order = mongooseToObject(order);
            order.distanceToCus = distanceArr[i].distanceToCus;
            order.distanceToRes = distanceArr[i].distanceToRes;
            let rest = await Restaurant.findOne({ _id: order.restaurant_id }, '-_id name address operating phones image');
            rest = mongooseToObject(rest);
            let foods = new Array();

            for (let i = 0; i < order.foods.length; i++) {
                let food = await Food.findOne({ _id: order.foods[i].foodID }, '-_id -__v -category_id -createdAt -updatedAt');
                food = mongooseToObject(food);
                foods.push(food);
                foods[i].amount = order.foods[i].count;
            }

            order.foods = foods;
            order.restaurant = rest;
            results.push(order);
        }

        res.send(results);
    }

    //[POST] /get-order?order_id=?
    getOrder = async(req, res, next) => {
        const order_id = req.body.order_id;

        Ordering.findOne({ _id: order_id })
            .then(order => {
                if (order.shipper_id === null && order.status === 'WAITING') {
                    order.shipper_id = (req.userID);
                    order.status = 'SHIPPING';
                    order.save();
                    res.send(getDistanceFromLatLonInKm('Get order successfuly!'));
                } else {
                    res.send(getDistanceFromLatLonInKm('Get order unsuccessfuly!'));
                }
            })
            .catch(next)
    }

    cancelOrder = async(req, res, next) => {
        const order_id = req.body.order_id;
        let order = await Ordering.findOne({ _id: order_id });

        if (req.userID.toString() === String(order.shipper_id) && order.status === 'SHIPPING') {
            order.status = 'WAITING';
            order.shipper_id = null;
            (await order).save();

            res.send(getDistanceFromLatLonInKm('Cancel order successfuly!'))
        } else {
            res.send(getDistanceFromLatLonInKm('Get order unsuccessfuly!'))
        }
    }

}

module.exports = new ShipperController();