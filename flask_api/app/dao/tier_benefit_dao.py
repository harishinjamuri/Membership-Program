from app import db
from app.models.tier_benefits import TierBenefits


class TierBenefitDAO:

    @staticmethod
    def get_all(is_active, page=1, per_page=10):
        items = (
            TierBenefits.query.filter_by(is_active=True)
            .order_by(TierBenefits.sort_order)
            .all()
        )
        return items

    @staticmethod
    def get_by_id(benefit_id):
        return TierBenefits.query.get(benefit_id)

    @staticmethod
    def get_by_tier_id(tier_id):
        return (
            TierBenefits.query.filter_by(tier_id=tier_id)
            .order_by(TierBenefits.sort_order)
            .all()
        )

    @staticmethod
    def create(data):
        benefit = TierBenefits(**data)
        db.session.add(benefit)
        db.session.commit()
        return benefit

    @staticmethod
    def update(benefit, data):
        for key, value in data.items():
            if hasattr(benefit, key):
                setattr(benefit, key, value)
        db.session.commit()
        return benefit

    @staticmethod
    def soft_delete(benefit_id):
        benefit = TierBenefits.query.get(benefit_id)
        if not benefit:
            return False
        benefit.is_active = False
        db.session.commit()
        return True
