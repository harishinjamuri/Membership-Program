from app.dao.product_dao import ProductDAO
from app.utils.discount_validation import check_product_discount_eligibility


class ProductService:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        items, total = ProductDAO.get_all(
            page=page, per_page=per_page, filters=filters or {}
        )

        items_dicts = [item.to_dict() for item in items]

        for item in items_dicts:
            eligible, discount_value = check_product_discount_eligibility(item)
            # print(f'item: {item.get("name")} Discount Id: {item.get("discount_id")}  eligible: {eligible}, discount_value: {discount_value}')
            if eligible:
                item["discount_applied"] = True
                item["discount_value"] = discount_value
                item["discount_id"] = item.get("discount_id")
            else:
                item["discount_applied"] = False
                item["discount_value"] = 0
        return items_dicts, total

    @staticmethod
    def get_by_id(product_id):
        return ProductDAO.get_by_id(product_id)

    @staticmethod
    def create(data):
        return ProductDAO.create(data)

    @staticmethod
    def update(product_id, data):
        return ProductDAO.update(product_id, data)

    @staticmethod
    def commit(product):
        return ProductDAO.commit(product)

    @staticmethod
    def delete(product_id):
        return ProductDAO.soft_delete(product_id)
