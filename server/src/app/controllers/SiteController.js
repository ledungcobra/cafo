const { multipleMongooseToObject } = require("../../utils/mongoose");
const Restaurant = require("../model/Restaurant");

class SiteController {
    //[GET] /
    index(req, res, next) {
        let resJSON = {
            api_name: "cafo",
            version: "0.2"
        }
        res.send(resJSON);
    }

    /* async search(req, res, next) {
        let keyword = String(req.body.keyword);
        let keyword1 = String(req.body.keyword);
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
        })

        let rest2 = await Restaurant.find({
            $text: { $search: keyword1 },
            //address: { $regex: keyword },
            city_id: city_id
        }).skip(10).limit(4);


        let difference = rest2.filter(x => !rest1.some(y => y._id.toString() === x._id.toString()))

        result.push(rest1);
        result.push(difference);

        res.send(rests);
    } */

    async search(req, res, next) {
        let keyword = String(req.body.keyword);
        let keyword1 = String(req.body.keyword);
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
            $text: { $search: keyword1 },
            //address: { $regex: keyword },
            city_id: city_id
        }, '_id').skip(0).limit(100);


        rest1 = rest1.map(item => item._id.toString());
        rest2 = rest2.map(item => item._id.toString());
        console.log(rest2);
        let difference = rest2.filter(x => !rest1.includes(x));

        result = rest1.concat(difference);
        //result.push(difference);

        res.send(result);
    }

}

module.exports = new SiteController;