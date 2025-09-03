from flask import request, make_response
from flask_jwt_extended import (
    create_access_token,
    set_access_cookies,
    unset_jwt_cookies,
    jwt_required,
    get_jwt_identity
)
from datetime import timedelta
from app.services.auth_service import AuthService
from app.utils.response import success_response, error_response


class AuthController:

    @staticmethod
    def register():
        data = request.get_json()
        try:
            user = AuthService.register(
                name=data['name'],
                email=data['email'],
                password=data['password'],
                phone_number=data.get('phone_number'),
                date_of_birth=data.get('date_of_birth')
            )
            return success_response("User registered successfully", {"id": user.id}, 201)
        except Exception as e:
            return error_response(str(e), 400)

    @staticmethod
    def login():
        data = request.get_json()
        user = AuthService.login(data['email'], data['password'])
        if user:
            access_token = create_access_token(
                identity=user.id,
                expires_delta=timedelta(days=1)
            )
            AuthService.update_last_login(user.id)

            response = make_response(success_response("Login successful"))
            set_access_cookies(response, access_token)
            return response

        return error_response("Invalid email or password", 401)

    @staticmethod
    @jwt_required()
    def profile():
        user_id = get_jwt_identity()
        user = AuthService.get_user_by_id(user_id)

        user_data = {
            "id": user.id,
            "email": user.email,
            "name": user.name,
            "date_of_birth": str(user.date_of_birth) if user.date_of_birth else None,
            "phone_number": user.phone_number
        }
        return success_response("Access granted", user_data)

    @staticmethod
    @jwt_required()
    def logout():
        response = make_response(success_response("Logged out successfully"))
        unset_jwt_cookies(response)
        return response

    @staticmethod
    @jwt_required()
    def change_password():
        user_id = get_jwt_identity()
        data = request.get_json()

        current_password = data.get("current_password")
        new_password = data.get("new_password")

        if not current_password or not new_password:
            return error_response("Both current and new passwords are required", 400)

        user, error = AuthService.change_password(user_id, current_password, new_password)
        if error:
            return error_response(error, 400)

        return success_response("Password changed successfully", user.to_dict())
        
        