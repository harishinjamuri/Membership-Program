from app.dao.order_item_dao import OrderItemDAO

class OrderItemService:

    @staticmethod
    def get_all_for_order(order_id):
        return OrderItemDAO.get_all_for_order(order_id)

    @staticmethod
    def get_by_id(order_item_id):
        return OrderItemDAO.get_by_id(order_item_id)
    
    @staticmethod
    def get_by_ids(order_item_ids):
        return OrderItemDAO.get_by_ids(order_item_ids)

    @staticmethod
    def create(order_id, data):
        from app.services.order_service import OrderService

        item = OrderItemDAO.create(order_id, data)

        # Recalculate order totals
        order = OrderService.recalculate_order(order_id)

        return item

    @staticmethod
    def update(order_item_id, data):
        return OrderItemDAO.update(order_item_id, data)

    @staticmethod
    def delete(order_item_id):
        from app.services.order_service import OrderService
        
        item = OrderItemDAO.get_by_id(order_item_id)
        if not item:
            return None
        
        success = OrderItemDAO.delete(order_item_id)
        if success:
            OrderService.recalculate_order(item.order_id)
        return success