from app.models.product import Product
from app.models import db


class ProductDAO:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        query = Product.query
        filters = filters or {}

        if filters.get("category_id"):
            query = query.filter(Product.category_id == filters["category_id"])

        if filters.get("discounted") is not None:
            if filters["discounted"]:
                query = query.filter(Product.discount_id.isnot(None))
            else:
                query = query.filter(Product.discount_id.is_(None))

        if filters.get("in_stock") is not None:
            if filters["in_stock"]:
                query = query.filter(Product.stock_quantity > 0)
            else:
                query = query.filter(Product.stock_quantity <= 0)

        if filters.get("exclusive") is not None:
            query = query.filter(
                Product.is_membership_exclusive == filters["exclusive"]
            )

        if filters.get("product"):
            name_filter = filters["product"]
            query = query.filter(Product.name.ilike(f"%{name_filter}%"))

        pagination = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_by_id(product_id):
        return Product.query.filter_by(id=product_id).first()

    @staticmethod
    def create(data):
        product = Product(**data)
        db.session.add(product)
        db.session.commit()
        return product

    @staticmethod
    def update(product_id, data):
        product = ProductDAO.get_by_id(product_id)
        if not product:
            return None
        for key, value in data.items():
            if hasattr(product, key):
                setattr(product, key, value)
        db.session.commit()
        return product

    @staticmethod
    def commit(product):
        db.session.add(product)
        db.session.commit()

    @staticmethod
    def soft_delete(product_id):
        # Assuming product does not have is_active, so we actually delete or mark in some way
        product = ProductDAO.get_by_id(product_id)
        if not product:
            return False
        db.session.delete(product)
        db.session.commit()
        return True
