from flask import jsonify
from app.utils.exceptions import BaseAppException

def register_error_handlers(app):
    @app.errorhandler(BaseAppException)
    def handle_custom_exceptions(error):
        response = {
            "success": False,
            "message": error.message
        }
        return jsonify(response), error.status_code

    @app.errorhandler(500)
    def handle_internal_server_error(error):
        return jsonify({
            "success": False,
            "message": "Internal server error"
        }), 500
