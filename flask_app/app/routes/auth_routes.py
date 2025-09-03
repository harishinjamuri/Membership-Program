from flask import Blueprint
from app.controllers.auth_controller import AuthController

auth_bp = Blueprint("auth", __name__, url_prefix="auth")

auth_bp.post('/register')(AuthController.register)
auth_bp.post('/login')(AuthController.login)
auth_bp.post('/logout')(AuthController.logout)
auth_bp.post('/change-password')(AuthController.change_password)
auth_bp.get('/profile')(AuthController.profile)
