from app.models.category import Category
from app.models import db

class CategoryDAO:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        query = Category.query
        filters = filters or {}
        if filters.get("is_active"):
            query = query.filter(Category.is_active == filters["is_active"])

        pagination  = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_by_id(category_id):
        return Category.query.filter_by(id=category_id, is_active=True).first()

    @staticmethod
    def create(data):
        category = Category(**data)
        db.session.add(category)
        db.session.commit()
        return category

    @staticmethod
    def update(category_id, data):
        category = CategoryDAO.get_by_id(category_id)
        if not category:
            return None
        for key, value in data.items():
            if hasattr(category, key):
                setattr(category, key, value)
        db.session.commit()
        return category

    @staticmethod
    def soft_delete(category_id):
        category = CategoryDAO.get_by_id(category_id)
        if not category:
            return False
        category.is_active = False
        db.session.commit()
        return True
