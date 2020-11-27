const siteRouter = require("./site");
const restaurantsRouter = require("./restaurants");
const citiesRouter = require("./cities");
const menuRouter = require('./menus');
const authRouter = require('./auth');
const userRouter = require('./user');
const orderRouter = require('./order');
const shipperRouter = require('./shipper');


function route(app) {
    app.use("/restaurants", restaurantsRouter);
    app.use("/menus", menuRouter);
    app.use("/cities", citiesRouter);
    app.use("/auth", authRouter);
    app.use("/user", userRouter);
    app.use("/order", orderRouter);
    app.use("/shipper", shipperRouter);
    app.use("/", siteRouter);
}

module.exports = route;