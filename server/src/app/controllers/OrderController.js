const Order = require('../model/Order');

const { mongooseToObject } = require('../../utils/mongoose')
const { multipleMongooseToObject } = require('../../utils/mongoose');
const Food = require('../model/Food');
const Restaurant = require('../model/Restaurant');
const moment = require('moment-timezone');

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
        console.log(req.body);
        const order = new Order({
            status: "WAITING",
            orderPosition: req.body.userPos,
            foods: req.body.foods,

            //todo: fix user auth
            user_id: req.userID,
            restaurant_id: req.body.resID,
            shipper_id: null,
        });

        await order.save()
        res.send({ message: "Success!" })
    }

    //[GET] /orders
    show = async(req, res, next) => {
        let orders = await Order.find({
            user_id: req.userID
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

    //[GET] /orders/id/:id
    showByID = async(req, res, next) => {
        const order_id = req.params.id;
        let order = await Order.findOne({
            _id: order_id
        })

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
    }

    cancelOrder = async(req, res, next) => {
        const order_id = req.body.order_id;
        let order = await Ordering.findOne({ _id: order_id });

        if (req.userID.toString() === String(order.user_id) && order.status === 'WAITING') {
            order.status = 'CANCEL';
            (await order).save();

            res.send({
                message: "Cancel order successfuly!"
            })
        } else {
            res.send({
                message: "Cancel order unsuccessfuly!"
            })
        }
    }

}

module.exports = new OrderController();