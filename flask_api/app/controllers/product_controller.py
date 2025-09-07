from flask import request
from flask_jwt_extended import jwt_required
from app.services.product_service import ProductService
from app.utils.response import success_response, error_response


class ProductController:

    @staticmethod
    @jwt_required()
    def get_all():
        page = request.args.get("page", default=1, type=int)
        per_page = request.args.get("per_page", default=10, type=int)

        filters = {
            "category_id": request.args.get("category_id"),
            "discounted": request.args.get("discounted"),
            "product": request.args.get("product"),
            "in_stock": request.args.get("product"),
            "exclusive": request.args.get("exclusive"),
        }

        filters = {k: v for k, v in filters.items() if v is not None}

        products, total = ProductService.get_all(page, per_page, filters=filters)
        return success_response(
            "Products retrieved",
            {
                "items": products,
                "total": total,
                "page": page,
                "per_page": per_page,
            },
        )

    @staticmethod
    @jwt_required()
    def get_one(product_id):
        product = ProductService.get_by_id(product_id)
        if not product:
            return error_response("Product not found", 404)
        return success_response("Product found", product.to_dict())

    @staticmethod
    @jwt_required()
    def create():
        data = request.get_json()
        product = ProductService.create(data)
        return success_response("Product created", product.to_dict())

    @staticmethod
    @jwt_required()
    def update(product_id):
        data = request.get_json()
        updated_product = ProductService.update(product_id, data)
        if not updated_product:
            return error_response("Product not found", 404)
        return success_response("Product updated", updated_product.to_dict())

    @staticmethod
    @jwt_required()
    def delete(product_id):
        deleted = ProductService.delete(product_id)
        if not deleted:
            return error_response("Product not found", 404)
        return success_response("Product deleted successfully")
