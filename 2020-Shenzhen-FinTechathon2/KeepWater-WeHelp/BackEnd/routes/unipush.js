const axios = require("axios");
var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {

    //进行unipush的api调用
  res.send('respond with a resource');
});

module.exports = router;
