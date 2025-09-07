package com.example.membership_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.constants.DiscountType;
import com.example.membership_api.constants.TierType;
import com.example.membership_api.dao.DiscountDAO;
import com.example.membership_api.dto.AppliesToDiscount;
import com.example.membership_api.model.Discount;
import com.example.membership_api.utils.DateUtils;

@Service
public class DiscountService {

    @Autowired
    private DiscountDAO discountDAO;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public Map<String, Object> getAll(int page, int perPage, Map<String, Object> filters) {
        return discountDAO.getAll(page, perPage, filters);
    }

    public Discount getById(String id) {
        return discountDAO.getById(id);
    }

    public Discount getByCode(String code) {
        return discountDAO.getByCode(code);
    }

    public Object[] create(Map<String, Object> data) {
        String code = (String) data.get("code");
        if (code != null && discountDAO.getByCode(code) != null ) {
            return new Object[]{"Discount Already Exist with Code " + code, false};
        }

        Discount d = new Discount();
        d.setCode((String) data.get("code"));
        d.setName((String) data.get("name"));
        d.setDescription((String) data.get("description"));

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> appliesToMap = (Map<String, Object>) data.get("applies_to");
            AppliesToDiscount appliesTo = new AppliesToDiscount();
            appliesTo.setType((String) appliesToMap.get("type"));
            appliesTo.setIds((List<String>) appliesToMap.get("ids"));
            d.setAppliesTo(appliesTo.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid applies_to format: " + e.getMessage());
        }

        try {
            String discountType = (String) data.get("discount_type");
            d.setDiscountType((DiscountType) DiscountType.valueOf(discountType.toUpperCase()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid discount_type: " + data.get("discount_type"));
        }

        LocalDateTime startDate = data.containsKey("start_date")
            ? DateUtils.parseSlashDateToDateTime((String) data.get("start_date"))
            : LocalDateTime.now();

        LocalDateTime endDate = data.containsKey("end_date")
            ? DateUtils.parseSlashDateToDateTime((String) data.get("end_date"))
            : LocalDate.of(9999, 12, 31).atStartOfDay();


        d.setStartDate(startDate);
        d.setEndDate(endDate);        
        d.setUsageCount(0);
        d.setValue((Double) data.get("value"));
        d.setMinOrderValue((Double) data.get("min_order_value"));
        d.setMaxDiscountAmount((Double) data.get("max_discount_amount"));
        d.setMinTier((Integer) TierType.valueOf((String) data.get("min_tier")).getLevel());

        return new Object[]{discountDAO.create(d), true};
    }

    public Discount update(String id, Map<String, Object> data) {
        Discount discount = getById(id);

        if (data.containsKey("start_date")) {
            String startDateStr = (String) data.get("start_date");
            LocalDateTime startDate = DateUtils.parseSlashDateToDateTime(startDateStr);
            discount.setStartDate(startDate);
        }

        if (data.containsKey("end_date")) {
            String endDateStr = (String) data.get("end_date");
            LocalDateTime endDate = DateUtils.parseSlashDateToDateTime(endDateStr);
            discount.setEndDate(endDate);
        }

        if (data.containsKey("min_tier")) {
            int tier = TierType.valueOf((String) data.get("min_tier")).getLevel();
            discount.setMinTier(tier);
        }

        if (data.containsKey("applies_to")) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> appliesToMap = (Map<String, Object>) data.get("applies_to");
                AppliesToDiscount appliesTo = new AppliesToDiscount();
                appliesTo.setType((String) appliesToMap.get("type"));
                appliesTo.setIds((List<String>) appliesToMap.get("ids"));
                discount.setAppliesTo(appliesTo.toString());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid applies_to format: " + e.getMessage());
            }
        }

        return discountDAO.update(discount);
    }

    public boolean incrementUsageCount(String discountId) {
        Discount discount = getById(discountId);
        discount.setUsageCount(discount.getUsageCount() + 1);
        discountDAO.update(discount);
        return true;
    }

    public Object[] validateDiscount(Map<String, Object> data) {
        String code = (String) data.get("code");
        Double orderTotal = data.get("order_total") instanceof Number
                ? ((Number) data.get("order_total")).doubleValue()
                : null;
        String userTierStr = (String) data.get("user_tier");

        if (orderTotal == null || userTierStr == null) {
            return new Object[]{false, "order_total and user_tier are required."};
        }

        Discount discount = getByCode(code);
        if (discount == null) {
            return new Object[]{false, "Discount Not Valid"};
        }

        int userTierLevel = TierType.valueOf(userTierStr).getLevel();

        if (discount.getMinTier() <= userTierLevel && orderTotal >= discount.getMinOrderValue()) {
            return new Object[]{true, "Discount Valid"};
        }

        return new Object[]{false, "Discount Not Valid"};
    }

    public boolean softDelete(String id) {
        return discountDAO.softDelete(id);
    }

}
