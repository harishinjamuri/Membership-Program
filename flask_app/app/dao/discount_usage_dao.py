from app.models.discount_usage import DiscountUsage
from app.models import db

class DiscountUsageDAO:

    @staticmethod
    def get_by_filter(page=1, per_page=10, filters=None):
        query = DiscountUsage.query
        filters = filters or {}

        if filters.get("user_id"):
            query = query.filter(DiscountUsage.user_id == filters["user_id"])

        if filters.get("discount_id"):
            query = query.filter(DiscountUsage.discount_id == filters["discount_id"])

        pagination = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        return DiscountUsageDAO.get_by_filter(page=page, per_page=per_page, filters=filters)

    @staticmethod
    def get_by_id(usage_id):
        return DiscountUsage.query.filter_by(id=usage_id).first()

    @staticmethod
    def get_by_user(user_id, page=1, per_page=10):
        return DiscountUsageDAO.get_by_filter(page=page, per_page=per_page,filters= {"user_id":user_id})
    
    @staticmethod
    def get_user_usage_count(user_id):
        return DiscountUsage.query.filter(DiscountUsage.user_id == user_id).count()

    @staticmethod
    def create(data):
        usage = DiscountUsage(**data)
        db.session.add(usage)
        db.session.commit()
        return usage