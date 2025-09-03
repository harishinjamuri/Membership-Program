from functools import wraps
from flask_jwt_extended import get_jwt_identity, verify_jwt_in_request
from flask import jsonify
from app.services.user_service import UserService

def admin_required():
    def wrapper(fn):
        @wraps(fn)
        def decorated_function(*args, **kwargs):
            verify_jwt_in_request()
            current_user_id = get_jwt_identity()
            if not UserService.is_admin(current_user_id):
                return jsonify({"message": "Admins only"}), 403

            return fn(*args, **kwargs)
        return decorated_function
    return wrapper
