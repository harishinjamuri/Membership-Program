from app.utils.security import hash_password, verify_password
from app.dao.auth_dao import AuthDAO
from app import db
from datetime import datetime


class AuthService:

    @staticmethod
    def register(name, email, password, phone_number=None, date_of_birth=None):
        existing_user = AuthDAO.get_by_email(email)
        if existing_user:
            raise Exception("User already exists")

        hashed_password = hash_password(password)
        dob = None
        if date_of_birth:
            try:
                dob = datetime.strptime(date_of_birth, "%Y/%m/%d")  # or "%Y-%m-%d"
            except ValueError:
                raise Exception("Invalid date format. Use YYYY/MM/DD.")
            
        return AuthDAO.create_user(
            name=name,
            email=email,
            password_hash=hashed_password,
            phone_number=phone_number,
            date_of_birth=dob
        )

    @staticmethod
    def login(email, password):
        user = AuthDAO.get_by_email(email)
        if user and verify_password(user.password_hash, password):
            return user
        return None

    @staticmethod
    def update_last_login(user_id):
        user = AuthDAO.get_by_id(user_id)
        if user:
            user.last_login = datetime.utcnow()
            db.session.commit()

    @staticmethod
    def get_user_by_id(user_id):
        return AuthDAO.get_by_id(user_id)

    @staticmethod
    def change_password(user_id, current_password, new_password):
        user = AuthDAO.get_by_id(user_id)
        if not user:
            return None, "User not found"

        if not verify_password(user.password_hash, current_password):
            return None, "Incorrect current password"

        updated_user = AuthDAO.change_password(user_id, new_password)
        return updated_user, None