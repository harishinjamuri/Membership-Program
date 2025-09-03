from datetime import datetime
from app.dao.discount_usage_dao import DiscountUsageDAO

class DiscountUsageService:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        return DiscountUsageDAO.get_all(page=page, per_page=per_page, filters=filters or {})

    @staticmethod
    def get_by_id(usage_id):
        return DiscountUsageDAO.get_by_id(usage_id)

    @staticmethod
    def get_by_user(user_id, page=1, per_page=10):
        return DiscountUsageDAO.get_by_user(user_id, page, per_page)
    
    @staticmethod
    def get_user_usage_count(user_id):
        return DiscountUsageDAO.get_user_usage_count(user_id)
  
    @staticmethod
    def create( discount_id, user_id, applied_on, entity, discount_amount ):
        used_at = datetime.utcnow()
        data = { 
                "discount_id": discount_id, 
                "user_id": user_id, 
                "applied_on":applied_on,
                "entity": entity,
                "discount_amount": discount_amount, 
                "used_at": used_at 
            }
        
        return DiscountUsageDAO.create(data)