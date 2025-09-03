from app.models.address_book import AddressBook
from app.models.base import db

class AddressBookDAO:
    @staticmethod
    def create(data):
        address = AddressBook(**data)
        db.session.add(address)
        db.session.commit()
        return address

    @staticmethod
    def get_by_user(user_id):
        return AddressBook.query.filter_by(user_id=user_id).all()

    @staticmethod
    def get_by_id(address_id):
        return AddressBook.query.get(address_id)

    @staticmethod
    def update(address, updates):
        for key, value in updates.items():
            setattr(address, key, value)
        db.session.commit()
        return address

    @staticmethod
    def delete(address):
        db.session.delete(address)
        db.session.commit()
