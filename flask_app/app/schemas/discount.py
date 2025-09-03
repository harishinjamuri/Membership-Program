from pydantic import BaseModel, Field
from typing import List, Literal


class AppliesToModel(BaseModel):
    """
    Schema for the `applies_to` JSON field in Discount model.
    """
    type: Literal["product", "category", "plan","tier"]
    ids: List[str] = Field(default_factory=list)
