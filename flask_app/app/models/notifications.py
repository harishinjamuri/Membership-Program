from app.models.base import BaseModel, db
from app.constants import NotificationType

class Notifications(BaseModel):
    __tablename__ = 'notifications'

    user_id  = db.Column(db.String(36), nullable=False)
    is_read = db.Column(db.Boolean, default=False)
    notification_type  = db.Column(db.Enum(NotificationType), nullable=False)
    title  = db.Column(db.String(50), nullable=False)
    message  = db.Column(db.Text, nullable=False)
    details = db.Column(db.JSON, nullable=True)

    def __repr__(self):
        return f"<Notifications {self.title}>"
