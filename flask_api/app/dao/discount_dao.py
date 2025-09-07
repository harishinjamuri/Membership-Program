from app.models.discount import Discount
from app.models.base import db
from app.utils.exceptions import NotFoundException


class DiscountDAO:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        query = Discount.query
        filters = filters or {}

        if filters.get("active"):
            query = query.filter(Discount.is_active == filters["active"])

        if filters.get("discount_type") is not None:
            query = query.filter(Discount.discount_type == filters["discount_type"])

        pagination = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_by_id(discount_id):
        return Discount.query.filter_by(id=discount_id, is_active=True).first()

    @staticmethod
    def get_by_code(code):
        return Discount.query.filter_by(code=code, is_active=True).first()

    @staticmethod
    def create(data):
        discount = Discount(**data)
        db.session.add(discount)
        db.session.commit()
        return discount

    @staticmethod
    def update(discount_id, data):
        discount = DiscountDAO.get_by_id(discount_id)
        if not discount:
            raise NotFoundException("Discount not found")
        for key, value in data.items():
            setattr(discount, key, value)
        db.session.commit()
        return discount

    @staticmethod
    def soft_delete(discount_id):
        discount = DiscountDAO.get_by_id(discount_id)
        if not discount:
            raise NotFoundException("Discount not found")
        discount.is_active = False
        db.session.commit()
        return True
