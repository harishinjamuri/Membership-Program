import uuid
from datetime import datetime, timedelta

def calculate_end_date_from_current(duration_days: int) -> datetime:
    """
    Calculate the end_date by adding duration_days to the start_date.
    
    Args:
        start_date (datetime): The start date of the subscription.
        duration_days (int): Number of days for the subscription duration.
        
    Returns:
        datetime: Calculated end_date.
    """
    start_date = datetime.utcnow()
    if not start_date:
        raise ValueError("start_date must be a valid datetime object")
    
    return start_date, start_date + timedelta(days=duration_days)

def generate_subscription_number():
    """
    Generates a unique subscription number as a UUID4 string.
    """
    return str(uuid.uuid4())