class BaseAppException(Exception):
    def __init__(self, message="Something went wrong", status_code=400):
        super().__init__(message)
        self.message = message
        self.status_code = status_code


class NotFoundException(BaseAppException):
    def __init__(self, message="Resource not found"):
        super().__init__(message, status_code=404)


class UnauthorizedException(BaseAppException):
    def __init__(self, message="Unauthorized access"):
        super().__init__(message, status_code=401)


class ForbiddenException(BaseAppException):
    def __init__(self, message="Forbidden"):
        super().__init__(message, status_code=403)


class BadRequestException(BaseAppException):
    def __init__(self, message="Bad request"):
        super().__init__(message, status_code=400)


class ConflictException(BaseAppException):
    def __init__(self, message="Conflict"):
        super().__init__(message, status_code=409)
