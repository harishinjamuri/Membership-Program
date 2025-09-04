from app.models.base import BaseModel, db


class AddressBook(BaseModel):
    __tablename__ = "address_book"

    name = db.Column(db.Text, nullable=False)
    user_id = db.Column(db.String(36), nullable=False)
    pincode = db.Column(db.String(36), nullable=False)
    address = db.Column(db.Text, nullable=False)
    city = db.Column(db.String(36), nullable=False)
    state = db.Column(db.String(36), nullable=False)
    country = db.Column(db.String(36), nullable=False)
    is_default = db.Column(db.Boolean, default=True)

    def __repr__(self):
        return f"<AddressBook {self.address}>"
