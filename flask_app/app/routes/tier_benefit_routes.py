from flask import Blueprint
from app.controllers.tier_benefit_controller import TierBenefitController

tier_benefit_bp = Blueprint('tier_benefit', __name__, url_prefix='/benefits')

tier_benefit_bp.get('/')(TierBenefitController.get_all)
tier_benefit_bp.get('/<string:benefit_id>')(TierBenefitController.get_one)
tier_benefit_bp.get('/tier/<string:tier_id>')(TierBenefitController.get_by_tier)
tier_benefit_bp.post('/')(TierBenefitController.create)
tier_benefit_bp.put('/<string:benefit_id>')(TierBenefitController.update)
tier_benefit_bp.delete('/<string:benefit_id>')(TierBenefitController.delete)
