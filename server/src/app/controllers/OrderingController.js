const Ordering = require('../model/Ordering');

const { mongooseToObject } = require('../../utils/mongoose')
const { multipleMongooseToObject } = require('../../utils/mongoose');
const Food = require('../model/Food');
const Restaurant = require('../model/Restaurant');

/*
status
+ WAITING
+ SHIPPING
+ DONE
+ CANCEL
*/

class OrderingController {
    //[POST] /order
    saveOrder = async(req, res, next) => {
        console.log(req.body);
        const ordering = new Ordering({
            status: "WAITING",
            orderPosition: req.body.userPos,
            foods: req.body.foods,

            //todo: fix user auth
            user_id: req.userID,
            restaurant_id: req.body.resID,
            shipper_id: null,
        });

        await ordering.save()
        res.send({ message: "Success!" })
    }

    //[GET] /order/get
    show = async(req, res, next) => {
        let orderings = await Ordering.find({
            user_id: req.userID
        }, '-__v -updatedAt -createdAt');
        orderings = multipleMongooseToObject(orderings);
        for (let i = 0; i < orderings.length; i++) {
            let rest = await Restaurant.find({ _id: orderings[i].restaurant_id }, 'name address image');
            let total = 0;
            let count = 0;
            for (let j = 0; j < orderings[i].foods.length; j++) {
                let food = await Food.findOne({ _id: orderings[i].foods[j].foodID }, 'price');
                count += parseInt(orderings[i].foods[j].count);
                total += food.price.value * parseInt(orderings[i].foods[j].count);
            }

            orderings[i].total = total;
            orderings[i].count = count;
        }

        res.send(orderings);
    }

    //[GET] /order/id/:id
    showByID = async(req, res, next) => {
        const order_id = req.params.id;
        let ordering = await Ordering.findOne({
            _id: order_id
        })

        ordering = mongooseToObject(ordering);
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
        res.send(ordering);
    }

    cancelOrder = async(req, res, next) => {
        res.send({ message: "HẾT PIN CHƯA LÀM :))))))" })
    }

}

module.exports = new OrderingController();