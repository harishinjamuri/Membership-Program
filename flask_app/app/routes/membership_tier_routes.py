from flask import Blueprint
from app.controllers.membership_tier_controller import MembershipTierController

membership_tier_bp = Blueprint('membership_tier', __name__, url_prefix='/tiers')

membership_tier_bp.get('/')(MembershipTierController.get_all)
membership_tier_bp.get('/<string:tier_id>')(MembershipTierController.get_one)
membership_tier_bp.post('/')(MembershipTierController.create)
membership_tier_bp.put('/<string:tier_id>')(MembershipTierController.update)
membership_tier_bp.delete('/<string:tier_id>')(MembershipTierController.delete)
