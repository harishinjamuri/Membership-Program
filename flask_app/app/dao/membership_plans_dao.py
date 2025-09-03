from app import db
from app.models.membership_plans import MemberShipPlans

class MembershipPlansDAO:

    @staticmethod
    def get_all_plans(page=1, per_page=10, filters=None):
        query = MemberShipPlans.query
        filters = filters or {}
        if filters.get("is_active"):
            query = query.filter(MemberShipPlans.is_active == filters["is_active"])
            
        pagination  = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_by_id(plan_id):
        return MemberShipPlans.query.get(plan_id)

    @staticmethod
    def create(data):
        plan = MemberShipPlans(
            name=data['name'],
            description=data['description'],
            price=data.get('price', 0.0)
        )
        plan.plan_type = data['plan_type']
        db.session.add(plan)
        db.session.commit()
        return plan

    @staticmethod
    def update(plan_id, data):
        plan = MemberShipPlans.query.get(plan_id)
        if not plan:
            return None

        for key, value in data.items():
            if hasattr(plan, key):
                setattr(plan, key, value)

        db.session.commit()
        return plan

    @staticmethod
    def soft_delete(plan_id):
        plan = MemberShipPlans.query.get(plan_id)
        if not plan:
            return False

        plan.is_active = False
        db.session.commit()
        return True
