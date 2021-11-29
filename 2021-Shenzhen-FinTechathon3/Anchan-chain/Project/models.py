from flask_sqlalchemy import SQLAlchemy

from __init__ import app
db = SQLAlchemy(app)

class Enterprise(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(128), unique=True, nullable=False)
    password = db.Column(db.String(128), nullable=False, default="")
    account_pub = db.Column(db.String(1024), default="")
    account_priv = db.Column(db.String(1024), default="")
    account_addr = db.Column(db.String(1024), default="")
    contract_addr = db.Column(db.String(1024), default="")
    license_addr = db.Column(db.String(1024), default="")
    
    evaluation_addr = db.Column(db.String(1024), default="")
    started_evaluation = db.Column(db.Boolean, default=False)

    envelope_pub = db.Column(db.String(2048), default="")
    envelope_priv = db.Column(db.String(2048), default="")

    ent_name = db.Column(db.String(1024),default="")
    rep_name = db.Column(db.String(1024),default="")
    ent_addr = db.Column(db.String(1024),default="")
    ent_type = db.Column(db.String(1024),default="")
    ent_range = db.Column(db.String(1024),default="")
    
class Engineer(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(128), unique=True, nullable=False)
    eid = db.Column(db.String(1024), default="")
    field = db.Column(db.String(1024), default="")
    index = db.Column(db.String(1024), default="")
    agency = db.Column(db.Integer, db.ForeignKey('agency.id'))

class Agency(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(128), unique=True, nullable=False)
    password = db.Column(db.String(128), nullable=False, default="")
    account_pub = db.Column(db.String(1024), default="")
    account_priv = db.Column(db.String(1024), default="")
    account_addr = db.Column(db.String(1024), default="")
    contract_addr = db.Column(db.String(1024), default="")

    engineer_list_addr = db.Column(db.String(1024), default="")
    engineers = db.relationship('Engineer', backref='Agency', lazy=True)

    envelope_pub = db.Column(db.String(2048), default="")
    envelope_priv = db.Column(db.String(2048), default="")

class IPFSObject(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    hash = db.Column(db.String(1024), nullable = False)
    name = db.Column(db.String(1024), default="")
    secret = db.Column(db.String(1024), default="")
    idx = db.Column(db.Integer)
    audit = db.Column(db.Integer, db.ForeignKey('audit.id'))

class Audit(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(128), unique=True, nullable=False)
    password = db.Column(db.String(128), nullable=False, default="")
    account_pub = db.Column(db.String(1024), default="")
    account_priv = db.Column(db.String(1024), default="")
    account_addr = db.Column(db.String(1024), default="")
    files = db.relationship('IPFSObject', backref='Audit', lazy=True)

    envelope_pub = db.Column(db.String(2048), default="")
    envelope_priv = db.Column(db.String(2048), default="")

class Contracts(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(128), unique=True, nullable=False)
    addr = db.Column(db.String(1024), default="")

class Arbitrate(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    addr = db.Column(db.String(1024), default="")

def count_numbers():
    el = Enterprise.query.count()
    al = Agency.query.count()
    aal = Audit.query.count()
    eel = Engineer.query.count()
    return (el, al, aal, eel)

if __name__ == "__main__":
    db.drop_all()
    db.create_all()