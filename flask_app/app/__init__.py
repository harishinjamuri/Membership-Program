import os
from flask import Flask
from flask_jwt_extended import JWTManager


from app.config import sqlite_db_name, JWT_SECRET_KEY
from app.models import db
from app.utils.logger import setup_logger
from app.routes import init_routes
from app.utils.error_handlers import register_error_handlers

basedir = os.path.abspath(os.path.dirname(__file__))
db_folder = os.path.abspath(os.path.join(basedir, '..', '..', 'database'))

# Make sure the database folder exists
os.makedirs(db_folder, exist_ok=True)

db_path = os.path.join(db_folder, sqlite_db_name)
sqlite_db_uri = f"sqlite:///{db_path}"

jwt = JWTManager()

def create_app():
    app = Flask(__name__)

    # Configuration
    app.config['SQLALCHEMY_DATABASE_URI'] = sqlite_db_uri
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    app.config['JWT_SECRET_KEY'] = JWT_SECRET_KEY

    # 🔒 JWT stored in cookies
    app.config['JWT_TOKEN_LOCATION'] = ['cookies']
    app.config['JWT_COOKIE_SECURE'] = False  # Set True in production with HTTPS
    app.config['JWT_ACCESS_COOKIE_NAME'] = 'access_token_cookie'
    app.config['JWT_COOKIE_CSRF_PROTECT'] = False  # Enable if CSRF protection is needed


    # Initialize extensions
    db.init_app(app)
    jwt.init_app(app)
    setup_logger(app)

    # Register blueprints or routes
    init_routes(app)

    register_error_handlers(app)

    with app.app_context():
        db.create_all()

    return app
