package com.example.membership_api.utils;

import com.example.membership_api.constants.DiscountApplicableType;
import com.example.membership_api.constants.DiscountType;
import com.example.membership_api.model.Discount;
import com.example.membership_api.model.Product;
import com.example.membership_api.service.DiscountService;


public class DiscountValidation {

    private static DiscountService discountService = new DiscountService(); // Or inject this properly

    public static DiscountResult checkProductDiscountEligibility(Product product) {
        if (product == null || product.getDiscountId() == null) {
            return new DiscountResult(false, 0.0);
        }

        Discount discount = discountService.getById(product.getDiscountId());

        if (discount == null) {
            return new DiscountResult(false, 0.0);
        }

        if (DiscountApplicableType.PRODUCT.equals(discount.getAppliesToType())
            && discount.getApplicableIds() != null
            && discount.getApplicableIds().contains(product.getId())
            && discount.isCurrentlyActive()) {

            return applyDiscount(discount.getDiscountType(), product.getPrice(), discount.getValue());
        }

        return new DiscountResult(false, 0.0);
    }

    private static DiscountResult applyDiscount(DiscountType discountType, double price, double discountValue) {
        if (discountType == DiscountType.FIXED_AMOUNT) {
            return new DiscountResult(true, discountValue);
        } else if (discountType == DiscountType.PERCENTAGE) {
            double value = Math.round(price * (discountValue / 100) * 100.0) / 100.0; // round to 2 decimals
            return new DiscountResult(true, value);
        }
        return new DiscountResult(false, 0.0);
    }

    public static class DiscountResult {
        private final boolean eligible;
        private final double discountValue;

        public DiscountResult(boolean eligible, double discountValue) {
            this.eligible = eligible;
            this.discountValue = discountValue;
        }

        public boolean isEligible() {
            return eligible;
        }

        public double getDiscountValue() {
            return discountValue;
        }
    }
}
