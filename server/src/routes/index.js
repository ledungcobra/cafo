const siteRouter = require("./site");
const restaurantsRouter = require("./restaurants");
const citiesRouter = require("./cities");
const menusRouter = require('./menus');
const authRouter = require('./auth');
const usersRouter = require('./users');
const ordersRouter = require('./orders');
const shippersRouter = require('./shippers');


function route(app) {
    app.use("/restaurants", restaurantsRouter);
    app.use("/menus", menusRouter);
    app.use("/cities", citiesRouter);

    app.use("/auth", authRouter);
    //v1
    app.use("/user", usersRouter);
    //v2
    app.use("/users", usersRouter);

    //v1
    app.use("/order", ordersRouter);
    //v2
    app.use("/orders", ordersRouter);

    //v1
    app.use("/shipper", shippersRouter);
    //v2
    app.use("/shippers", shippersRouter);

    app.use("/", siteRouter);
}

module.exports = route;