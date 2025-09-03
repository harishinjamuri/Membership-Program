from flask import Blueprint
from app.controllers.membership_plans_controller import MembershipPlansController

membership_plans_bp = Blueprint('membership', __name__, url_prefix='/plans')

membership_plans_bp.get('/')(MembershipPlansController.get_all)
membership_plans_bp.get('/<string:plan_id>')(MembershipPlansController.get_one)
membership_plans_bp.post('/')(MembershipPlansController.create)
membership_plans_bp.put('/<string:plan_id>')(MembershipPlansController.update)
membership_plans_bp.delete('/<string:plan_id>')(MembershipPlansController.delete)
