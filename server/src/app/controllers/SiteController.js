class SiteController {
    //[GET] /
    index(req, res, next) {
        let resJSON = {
            API_NAME: "CAFO",
            VER: "0.1"
        }
        res.send(resJSON);
    }
}

module.exports = new SiteController;