const { multipleMongooseToObject } = require("../../utils/mongoose");
const Restaurant = require("../model/Restaurant");

const getMessageForClient = require('../../utils/message');

class SiteController {
    //[GET] /
    index(req, res, next) {
        res.status(200);
        res.send(getMessageForClient(res.statusCode, 'API is connected successfuly'));
    }

    //[POST] /search
    async search(req, res, next) {
        let keyword = String(req.body.keyword);
        let copyKeyword = String(req.body.keyword);
        let city_id = req.body.city_id;

        var splitStr = keyword.toLowerCase().split(' ');
        for (var i = 0; i < splitStr.length; i++) {
            // You do not need to check if i is larger than splitStr length, as your for does that for you
            // Assign it back to the array
            splitStr[i] = splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
        }
        keyword = splitStr.join('.*');
        //keyword = keyword.replace(/ /g, '.*');
        keyword = '.*' + keyword + '.*';
        let result = new Array();

        let rest1 = await Restaurant.find({
            name: { $regex: keyword },
            //address: { $regex: keyword },
            city_id: city_id
        }, '_id')

        let rest2 = await Restaurant.find({
            $text: { $search: copyKeyword },
            //address: { $regex: keyword },
            city_id: city_id
        }, '_id').skip(0).limit(100);


        rest1 = rest1.map(item => item._id.toString());
        rest2 = rest2.map(item => item._id.toString());
        //console.log(rest2);
        let difference = rest2.filter(x => !rest1.includes(x));

        result = rest1.concat(difference);
        //result.push(difference);

        res.send(result);
    }

    //[POST] /search?limit=?
    async searchDetail(req, res, next) {
        let keyword = String(req.body.keyword);
        let copyKeyword = String(req.body.keyword);
        let city_id = req.body.city_id;

        var splitStr = keyword.toLowerCase().split(' ');
        for (var i = 0; i < splitStr.length; i++) {
            // You do not need to check if i is larger than splitStr length, as your for does that for you
            // Assign it back to the array
            splitStr[i] = splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
        }
        keyword = splitStr.join('.*');
        //keyword = keyword.replace(/ /g, '.*');
        keyword = '.*' + keyword + '.*';
        let result = new Array();

        let rest1 = await Restaurant.find({
            name: { $regex: keyword },
            //address: { $regex: keyword },
            city_id: city_id
        }, '_id')

        let rest2 = await Restaurant.find({
            $text: { $search: copyKeyword },
            //address: { $regex: keyword },
            city_id: city_id
        }, '_id').skip(0).limit(100);


        rest1 = rest1.map(item => item._id.toString());
        rest2 = rest2.map(item => item._id.toString());
        //console.log(rest2);
        let difference = rest2.filter(x => !rest1.includes(x));

        result = rest1.concat(difference);
        //result.push(difference);

        let n = req.body.limit;
        if (n > result.length) {
            n = result.length;
        }

        try {
            let ids = result;
            //console.log(ids);
            let restaurants = Array();
            for (let i = 0; i < n; i++) {
                let rest = await Restaurant.find({ _id: ids[i] }, '-__v -createdAt -updatedAt');
                restaurants = restaurants.concat(rest);
            }

            if (restaurants.length) {
                res.send(restaurants);
            } else {
                next(createError(404));
            }
        } catch (err) {
            next(createError(400, err));
        }
    }

}

module.exports = new SiteController;