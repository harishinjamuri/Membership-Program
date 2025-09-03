from app.models.base import BaseModel, db
from app.constants import UserType, UserStatus

class User(BaseModel):
    __tablename__ = 'users'

    name = db.Column(db.String(100), nullable=False)
    email = db.Column(db.Text, unique=True, nullable=False)
    phone_number = db.Column(db.String(20))
    date_of_birth = db.Column(db.DateTime)
    last_login = db.Column(db.DateTime)
    status = db.Column(db.Enum(UserStatus), default= UserStatus.ACTIVE, nullable=False)
    password_hash = db.Column(db.String(255), nullable=False)
    user_type = db.Column(db.String(10), default=UserType.USER.value)  # ADMIN or USER

    @property
    def is_admin(self):
        return self.user_type == UserType.ADMIN.value

    def __repr__(self):
        return f"<User {self.email}>"