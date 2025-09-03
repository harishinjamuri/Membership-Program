from pydantic import BaseModel
from typing import List, Optional
from enum import Enum

class OrderItemModel(BaseModel):
    product_id: str
    quantity: int
    unit_price: float
    discount_value: float
    discount_applied: bool
    total_price: float


class OrderDiscountModel(BaseModel):
    id: str
    discount_code: str
    discount_amount: float


class OrderItemsModel(BaseModel):
    items: List[OrderItemModel] = []


class OrderDiscountsModel(BaseModel):
    discounts: List[OrderDiscountModel] = []
