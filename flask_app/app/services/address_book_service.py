from app.dao.address_book_dao import AddressBookDAO
from app.utils.exceptions import NotFoundException

class AddressBookService:
    @staticmethod
    def create_address(user_id, data):
        data['user_id'] = user_id
        return AddressBookDAO.create(data)

    @staticmethod
    def get_addresses(user_id):
        return AddressBookDAO.get_by_user(user_id)
    
    @staticmethod
    def get_addresses_by_id(address_id):
        return AddressBookDAO.get_by_id(address_id)

    @staticmethod
    def update_address(address_id, user_id, updates):
        address = AddressBookDAO.get_by_id(address_id)
        if not address or address.user_id != user_id:
            raise NotFoundException("Address not found or unauthorized")
        return AddressBookDAO.update(address, updates)

    @staticmethod
    def delete_address(address_id, user_id):
        address = AddressBookDAO.get_by_id(address_id)
        if not address or address.user_id != user_id:
            raise NotFoundException("Address not found or unauthorized")
        AddressBookDAO.delete(address)
