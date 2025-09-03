from app import db
from app.models.membership_tiers import MemberShipTiers
from app.constants import TierType

class MembershipTierDAO:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        query = MemberShipTiers.query
        filters = filters or {}

        if filters.get("is_active"):
            query = query.filter(MemberShipTiers.is_active == filters["is_active"])
        
        if filters.get("tier_level"):
            query = query.filter(MemberShipTiers.tier_level == filters["tier_level"])

        pagination  = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_eligible_tier(data):
        monthly_orders = data.get('monthly_orders')
        monthly_spend = data.get('monthly_spend')
        total_orders = data.get('total_orders')
        total_spend = data.get('total_spend')

        query = MemberShipTiers.query

        if monthly_orders:
            query = query.filter(MemberShipTiers.min_monthly_orders <= monthly_orders)

        if monthly_spend:
            query = query.filter(MemberShipTiers.min_monthly_spend <= monthly_spend)

        if total_orders:
            query = query.filter(MemberShipTiers.min_total_orders <= total_orders)

        if total_spend:
            query = query.filter(MemberShipTiers.min_total_spend <= total_spend)

        tiers = query.order_by(MemberShipTiers.tier_level.desc()).first()
        return tiers

    @staticmethod
    def get_by_id(tier_id):
        return MemberShipTiers.query.get(tier_id)
    
    @staticmethod
    def get_active_by_tier(tier_level):
        return MemberShipTiers.query.filter_by(is_active=True, tier_level=tier_level).all()

    @staticmethod
    def create(data):
        tier = MemberShipTiers(
            name=data['name'],
            description=data['description'],
            min_monthly_orders=data.get('min_monthly_orders', 0),
            min_monthly_spend=data.get('min_monthly_spend', 0.0),
            min_total_orders=data.get('min_total_orders', 0),
            min_total_spend=data.get('min_total_spend', 0.0)
        )

        tier.tier=data.get('tier', TierType.BRONZE)

        db.session.add(tier)
        db.session.commit()
        return tier

    @staticmethod
    def update(tier_id, data):
        tier = MemberShipTiers.query.get(tier_id)
        if not tier:
            return None
        for key, value in data.items():
            if hasattr(tier, key):
                setattr(tier, key, value)
        db.session.commit()
        return tier

    @staticmethod
    def soft_delete(tier_id):
        tier = MemberShipTiers.query.get(tier_id)
        if not tier:
            return False
        tier.is_active = False
        db.session.commit()
        return True
