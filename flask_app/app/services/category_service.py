from app.dao.category_dao import CategoryDAO

class CategoryService:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        return CategoryDAO.get_all(page, per_page, filters)

    @staticmethod
    def get_by_id(category_id):
        return CategoryDAO.get_by_id(category_id)

    @staticmethod
    def create(data):
        return CategoryDAO.create(data)

    @staticmethod
    def update(category_id, data):
        return CategoryDAO.update(category_id, data)

    @staticmethod
    def soft_delete(category_id):
        return CategoryDAO.soft_delete(category_id)
