from flask import Blueprint

from .auth_routes import auth_bp
from .user_routes import user_bp
from .membership_plans_routes import membership_plans_bp
from .membership_tier_routes import membership_tier_bp
from .tier_benefit_routes import tier_benefit_bp
from .address_book_routes import address_book_bp
from .subscription_routes import subscription_bp
from .discount_routes import discount_bp 
from .discount_usage_routes import discount_usage_bp
from .category_routes import category_bp
from .product_routes import product_bp
from .order_item_routes import order_item_bp
from .order_routes import order_bp
from .notification_routes import notification_bp
from .user_tier_metrics_routes import utm_bp


def init_routes(app):
    all_routes = Blueprint('api', __name__)


    all_routes.register_blueprint(auth_bp)
    all_routes.register_blueprint(user_bp)
    all_routes.register_blueprint(membership_plans_bp)
    all_routes.register_blueprint(membership_tier_bp)
    all_routes.register_blueprint(tier_benefit_bp)
    all_routes.register_blueprint(address_book_bp)
    all_routes.register_blueprint(subscription_bp)
    all_routes.register_blueprint(discount_bp)
    all_routes.register_blueprint(discount_usage_bp)
    all_routes.register_blueprint(category_bp)
    all_routes.register_blueprint(product_bp)
    all_routes.register_blueprint(order_item_bp)
    all_routes.register_blueprint(order_bp)
    all_routes.register_blueprint(notification_bp)
    all_routes.register_blueprint(utm_bp)


    app.register_blueprint(all_routes, url_prefix='/api')
