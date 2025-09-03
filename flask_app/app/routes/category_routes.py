from flask import Blueprint
from app.controllers.category_controller import CategoryController

category_bp = Blueprint('category', __name__, url_prefix='/categories')

category_bp.get('/')(CategoryController.get_all)
category_bp.get('/<category_id>')(CategoryController.get_one)
category_bp.post('/')(CategoryController.create)
category_bp.put('/<category_id>')(CategoryController.update)
category_bp.delete('/<category_id>')(CategoryController.delete)
