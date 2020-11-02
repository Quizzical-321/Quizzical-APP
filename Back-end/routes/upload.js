let express = require("express");
let router = express.Router();
let fs = require("fs");
let MongoClient = require("mongodb").MongoClient;
let db;

MongoClient.connect(
  "mongodb://module6quizzical:uLBSjjivRdyY5sa3C1CQa6EsirCm2MNSQFa7oilEzad11rPNoO4zE8xKMEhibcvHiiw3xahYI4FiQs43U1WBgg==@module6quizzical.documents.azure.com:10250/mean?ssl=true",
  {useUnifiedTopology: true},
  (err, client) => {
    db = client.db("data");
    db.createCollection("class info", (err, res) => {
      if (err) {
	console.error(err);
      }
    });
    db.createCollection("user info", (err, res) => {
      if (err) {
	console.error(err);
      }
    });
    db.createCollection("notification_frequency", (err, res) => {
      if (err) {
	console.error(err);
      }
    });
  }
);

router.use(express.json());

router.post("/", (req, res, next) => {
  console.error("UID: " + req.body.uid);
  console.error("Type: " + req.body.type);
  console.error("Data: %j", req.body);
  console.error(Object.entries(req.body));

  if (req.body.type === "Profile_Image") {
    let path = "/home/site/wwwroot/images/" + req.body.uid;
    if (!fs.existsSync(path)) {
      fs.mkdirSync(path);
    }
    let filename = path + "/profile_img.jpg";
    fs.writeFileSync(filename, req.body.data, {encoding: "base64"});
  }
  else if (req.body.type === "user_info") {
    db.collection("user info").updateOne({uid: req.body.uid}, Object.assign({}, JSON.parse(req.body.data), {uid: req.body.uid}), {upsert: true}, (err, res) => {
      if (err) {
	console.error(err);
      }
    });
  }
  else if (req.body.type === "notification_frequency") {
    try {
      db.collection(req.body.type).updateOne({uid: req.body.uid}, Object.assign({}, JSON.parse(req.body.data), {uid: req.body.uid}), {upsert: true}, (err, res) => {
	if (err) {
	  console.error(err);
	}
      });
    } catch (ex) {
      db.collection(req.body.type).updateOne({uid: req.body.uid}, req.body, {upsert: true}, (err, res) => {
	if (err) {
	  console.error(err);
	}
      });
    }
  } else if (req.body.type === "Add class") {
    db.collection("class info").updateOne({uid: req.body.uid}, Object.assign({}, JSON.parse(req.body.data), {uid: req.body.uid}), {upsert: true}, (err, res) => {
      if (err) {
	console.error(err);
      }
    });
  }

  res.statusCode = 200;
  res.end();
});

module.exports = router;
