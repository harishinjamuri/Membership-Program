from flask import Blueprint
from app.controllers.product_controller import ProductController

product_bp = Blueprint('product', __name__, url_prefix='/products')

product_bp.get('/')(ProductController.get_all)
product_bp.get('/<product_id>')(ProductController.get_one)
product_bp.post('/')(ProductController.create)
product_bp.put('/<product_id>')(ProductController.update)
product_bp.delete('/<product_id>')(ProductController.delete)
