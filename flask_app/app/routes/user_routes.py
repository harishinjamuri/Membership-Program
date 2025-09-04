from flask import Blueprint
from app.controllers.user_controller import UserController

user_bp = Blueprint("user", __name__, url_prefix="/users")

user_bp.get("/")(UserController.get_all)
user_bp.get("/<user_id>")(UserController.get_one)
user_bp.put("/<user_id>")(UserController.update)
user_bp.delete("/<user_id>")(UserController.delete)
