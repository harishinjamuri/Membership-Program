from flask import request, jsonify
from app.services.address_book_service import AddressBookService
from flask_jwt_extended import jwt_required, get_jwt_identity
from app.utils.response import success_response


class AddressBookController:

    @staticmethod
    @jwt_required()
    def create_address():
        user_id = get_jwt_identity()
        data = request.get_json()
        address = AddressBookService.create_address(user_id, data)
        return success_response("Address created", address.to_dict())

    @staticmethod
    @jwt_required()
    def get_addresses():
        user_id = get_jwt_identity()
        addresses = AddressBookService.get_addresses(user_id)
        return success_response("User addresses", [a.to_dict() for a in addresses])

    @staticmethod
    @jwt_required()
    def get_addresses_by_id(address_id):
        addresses = AddressBookService.get_addresses_by_id(address_id)
        return success_response("User addresses", [a.to_dict() for a in addresses])

    @staticmethod
    @jwt_required()
    def update_address(address_id):
        user_id = get_jwt_identity()
        data = request.get_json()
        updated = AddressBookService.update_address(address_id, user_id, data)
        return success_response("Address updated", updated.to_dict())

    @staticmethod
    @jwt_required()
    def delete_address(address_id):
        user_id = get_jwt_identity()
        AddressBookService.delete_address(address_id, user_id)
        return success_response("Address deleted")
