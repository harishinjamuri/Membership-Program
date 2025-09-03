from app.models.base import BaseModel, db

class Category(BaseModel):
    __tablename__ = 'category'

    name = db.Column(db.String(100), unique=True, nullable=False)
    description = db.Column(db.Text, nullable=False)
    is_active = db.Column(db.Boolean, default=True)
    
    def __repr__(self):
        return f"<Category {self.name}>"
