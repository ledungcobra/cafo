const Order = require('../model/Order');
const { mongooseToObject, multipleMongooseToObject } = require('../../utils/mongoose');
const Restaurant = require('../model/Restaurant');
const Food = require('../model/Food');

const getDistanceFromLatLonInKm = require('../../utils/distance');
const Mongoose = require('mongoose');

const getMessageForClient = require('../../utils/message');

class ShipperController {
    //[GET] /orders
    show = async(req, res, next) => {
        let orders = await Order.find({
            shipper_id: req.userID
        }, '-__v -updatedAt -createdAt');
        orders = multipleMongooseToObject(orders);
        for (let i = 0; i < orders.length; i++) {
            let rest = await Restaurant.find({ _id: orders[i].restaurant_id }, 'name address image');
            let total = 0;
            let count = 0;
            let foodsOrder = new Array();
            for (let j = 0; j < orders[i].foods.length; j++) {
                let food = await Food.findOne({ _id: orders[i].foods[j].foodID }, '-category_id -createdAt -updatedAt -__v');
                count += parseInt(orders[i].foods[j].count);
                total += food.price.value * parseInt(orders[i].foods[j].count);
                food = mongooseToObject(food);
                food.count = orders[i].foods[j].count;
                foodsOrder.push(food);
            }

            orders[i].total = total;
            orders[i].count = count;
            orders[i].foods = foodsOrder;
        }

        res.send(orders);
    }

    //[GET] /search?long=?&lat=?
    searchOrdering = async(req, res, next) => {
        let long = parseFloat(req.query.long);
        let lat = parseFloat(req.query.lat);

        let orders = await Order.find({
            status: "WAITING",
        }, 'restaurant_id orderPosition');

        console.log(orders);

        let distanceArr = new Array();

        for (let i = 0; i < orders.length; i++) {
            let rest = await Restaurant.findOne({ _id: orders[i].restaurant_id }, 'position');
            console.log(rest);
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
            let order = await Order.findOne({ _id: distanceArr[i]._id }, '-__v -updatedAt -createdAt')
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

        Order.findOne({ _id: order_id })
            .then(order => {
                if (order.shipper_id === null && order.status === 'WAITING') {
                    order.shipper_id = (req.userID);
                    order.status = 'SHIPPING';
                    order.save();
                    res.send(getMessageForClient('Get order successfuly!'));
                } else {
                    res.send(getMessageForClient('Get order unsuccessfuly!'));
                }
            })
            .catch(next)
    }

    //[POST] /cancel-order?order_id=?
    cancelOrder = async(req, res, next) => {
        const order_id = req.body.order_id;
        let order = await Order.findOne({ _id: order_id });

        if (req.userID.toString() === String(order.shipper_id) && order.status === 'SHIPPING') {
            order.status = 'WAITING';
            order.shipper_id = null;
            (await order).save();

            res.send(getMessageForClient('Cancel order successfuly!'))
        } else {
            res.send(getMessageForClient('Get order unsuccessfuly!'))
        }
    }

    //[POST] /finish-order?order_id=?
    finishOrder = async(req, res, next) => {
        const order_id = req.body.order_id;
        let order = await Order.findOne({ _id: order_id });

        if (req.userID.toString() === String(order.shipper_id) && order.status === 'SHIPPING') {
            order.status = 'DONE';
            order.shipper_id = null;
            await order.save();

            res.send(getMessageForClient('Finish order successfuly!'))
        } else {
            res.send(getMessageForClient('Finish order unsuccessfuly!'))
        }
    }
}

module.exports = new ShipperController();