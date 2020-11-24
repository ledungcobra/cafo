const siteRouter = require("./site");
const restaurantsRouter = require("./restaurants");
const citiesRouter = require("./cities");
const menuRouter = require('./menus');

function route(app) {
    app.use("/restaurants", restaurantsRouter);
    app.use("/menus", menuRouter);
    app.use("/cities", citiesRouter);
    app.use("/", siteRouter);
}

module.exports = route;