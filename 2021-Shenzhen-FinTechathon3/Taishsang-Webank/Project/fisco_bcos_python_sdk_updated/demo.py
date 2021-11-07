from flask import Flask, jsonify, request, render_template
from carrent_contract import Car_Contract
from flask_cors import CORS
import json,requests
from flask import *
from web3 import Web3

app = Flask(__name__)
contract_address = "0xa7844eb44fd581ff9b9ffc678037fd506f111fb7"
admin_privkey = "223f0c657846634dc7c04fbbdfd7763fc4cd370fff94eedcd0a6d6f6f54c622b"
CORS(app)

@app.route("/tokenURI",methods=["POST"])
def transferFrom():

    data = request.get_json()
    tokenid = data["tokenid"]
    
    carrent = Car_Contract(contract_address)
    iscarowner = carrent.tokenURI(int(tokenid))


    send_data = json.dumps({'tokenURI':iscarowner[0]})

    print(send_data)
    return make_response(send_data, 200)


@app.route("/carowner/is_carowner/<string:address>", methods=["POST", "GET"])
def is_carowner(address):
    carrent = Car_Contract(contract_address)
    iscarowner = carrent.is_carowner(address)
    return jsonify(iscarowner), 200


@app.route("/car/<int:chainnumber>", methods=["GET", "POST"])
def getcar(chainnumber):
    carrent = Car_Contract(contract_address)
    car_msg = carrent.get_car_by_chainnumber(chainnumber)
    return jsonify(car_msg), 200


@app.route("/car/list", methods=["GET", "POST"])
def getcarlist():
    carrent = Car_Contract(contract_address)
    car_msg = carrent.get_car_list()
    return jsonify(car_msg), 200

@app.route("/user/add", methods=["POST"])
def add_user_by_amount():
    data = request.get_json()
    if data is None:
        return jsonify({"error": "Pleace input [address] by string."}), 400
    address = data["address"]
    carrent = Car_Contract(contract_address)
    carrent.client.set_account_by_privkey(admin_privkey)
    new_user = carrent.add_user_by_amount(address)
    return jsonify(new_user), 200



@app.route("/user/is_user/<string:address>", methods=["POST", "GET"])
def is_user(address):
    carrent = Car_Contract(contract_address)
    isuser = carrent.is_user(address)
    return jsonify(isuser), 200

@app.route("/vehicle/new/<string:carowner_privkey>", methods=["GET", "POST"])
def new_vehicle(carowner_privkey):
    data = request.get_json()
    if data is None:
        return jsonify({"error": "Pleace input [chainNumber, number, brand, color, quality, price, day]s."}), 400
    chainNumber = int(data["chainNumber"])
    number = data["number"]
    brand = data["brand"]
    color = data["color"]
    quality = data["quality"]
    price = int(data["price"])
    day = int(data["day"])

    carrent = Car_Contract(contract_address)
    carrent.client.set_account_by_privkey(carowner_privkey)
    newvehicle = carrent.new_vehicle(chainNumber, number, brand, color, quality, price, day)
    return jsonify(newvehicle), 200

@app.route("/vehicle/sign/<string:user_privkey>/<int:chainNumber>", methods=["GET", "POST"])
def sign_vehicle(user_privkey, chainNumber):
    carrent = Car_Contract(contract_address)
    carrent.client.set_account_by_privkey(user_privkey)
    sign_msg = carrent.sign_vehicle(chainNumber)
    return jsonify(sign_msg), 200


@app.route("/vehicle/reback/<string:user_privkey>/<int:chainNumber>", methods=["GET", "POST"])
def reback_vehicle(user_privkey, chainNumber):
    carrent = Car_Contract(contract_address)
    carrent.client.set_account_by_privkey(user_privkey)
    reback_msg = carrent.reback_vehicle(chainNumber)
    return jsonify(reback_msg), 200

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=False)