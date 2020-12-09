const createError = require('http-errors');
const Order = require('../model/Order');
const { mongooseToObject } = require('../../utils/mongoose')
const { multipleMongooseToObject } = require('../../utils/mongoose');
const Food = require('../model/Food');
const Restaurant = require('../model/Restaurant');
const moment = require('moment-timezone');
const getMessageForClient = require('../../utils/message')

/*
status
+ WAITING
+ SHIPPING
+ DONE
+ CANCEL
*/

class OrderController {
    //[POST] /orders
    saveOrder = async(req, res, next) => {
        try {
            const order = new Order({
                status: "WAITING",
                orderPosition: req.body.userPos,
                foods: req.body.foods,

                user_id: req.userID,
                restaurant_id: req.body.resID,
                shipper_id: null,
            });

            await order.save()
            res.send(getMessageForClient(res.statusCode, 'Order successfuly!'));

        } catch (error) {
            next(createError(error));
        }
    }

    //[GET] /orders
    show = async(req, res, next) => {
        try {
            let orders = await Order.find({
                user_id: req.userID
            }, '-__v -updatedAt -createdAt');
            orders = multipleMongooseToObject(orders);
            for (let i = 0; i < orders.length; i++) {
                if (orders[i].restaurant_id) {
                    let rest = await Restaurant.findOne({ _id: orders[i].restaurant_id }, 'name address image');
                    if (rest) {
                        rest = mongooseToObject(rest);
                        orders[i].restaurant = rest;
                    }
                }
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

        } catch (error) {
            next(createError(error));
        }
    }

    //[GET] /orders/id/:id
    showByID = async(req, res, next) => {
        try {
            const order_id = req.params.id;
            let order = await Order.findOne({
                _id: order_id
            })
            if (order) {
                order = mongooseToObject(order);
                let rest = await Restaurant.findOne({ _id: order.restaurant_id }, '-_id name address operating phones image');
                rest = mongooseToObject(rest);
                let foods = new Array();

                for (let i = 0; i < order.foods.length; i++) {
                    let food = await Food.findOne({ _id: order.foods[i].foodID }, '-_id -__v -category_id -createdAt -updatedAt');
                    food = mongooseToObject(food);
                    foods.push(food);
                    foods[i].amount = order.foods[i].count;
                }
                //order.order_time = moment.tz(order.order_time, 'Asia/Ho_Chi_Minh');
                order.foods = foods;
                res.send(order);
            } else {
                next(createError(404));
            }
        } catch (error) {
            next(createError(400, error));
        }
    }

    cancelOrder = async(req, res, next) => {
        try {
            const order_id = req.body.order_id;
            let order = await Order.findOne({ _id: order_id });

            if (req.userID.toString() === String(order.user_id) && order.status === 'WAITING') {
                order.status = 'CANCEL';
                (await order).save();

                res.send(getMessageForClient(res.statusCode, 'Cancel order successfuly!'))
            } else {
                res.status(400).send(getMessageForClient(res.statusCode, 'Cancel order unsuccessfuly!'))
            }
        } catch (error) {
            next(createError(error));
        }
    }

}

module.exports = new OrderController();