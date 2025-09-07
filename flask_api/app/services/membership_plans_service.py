from app.dao.membership_plans_dao import MembershipPlansDAO


class MembershipPlansService:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        return MembershipPlansDAO.get_all_plans(page, per_page, filters)

    @staticmethod
    def get_by_id(plan_id):
        return MembershipPlansDAO.get_by_id(plan_id)

    @staticmethod
    def create(data):
        return MembershipPlansDAO.create(data)

    @staticmethod
    def update(plan_id, data):
        return MembershipPlansDAO.update(plan_id, data)

    @staticmethod
    def soft_delete(plan_id):
        return MembershipPlansDAO.soft_delete(plan_id)
