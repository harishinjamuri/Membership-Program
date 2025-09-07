from flask import Blueprint
from app.controllers.address_book_controller import AddressBookController

address_book_bp = Blueprint("address_book", __name__, url_prefix="/address")

address_book_bp.post("")(AddressBookController.create_address)
address_book_bp.get("")(AddressBookController.get_addresses)
address_book_bp.get("/<address_id>")(AddressBookController.get_addresses_by_id)
address_book_bp.put("/<address_id>")(AddressBookController.update_address)
address_book_bp.delete("/<address_id>")(AddressBookController.delete_address)
