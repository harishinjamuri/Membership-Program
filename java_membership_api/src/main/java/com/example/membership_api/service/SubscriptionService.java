package com.example.membership_api.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.constants.TierType;
import com.example.membership_api.dao.MembershipPlanDAO;
import com.example.membership_api.dao.MembershipTierDAO;
import com.example.membership_api.dao.SubscriptionDAO;
import com.example.membership_api.model.MembershipPlan;
import com.example.membership_api.model.MembershipTier;
import com.example.membership_api.model.Subscription;
import com.example.membership_api.model.TierBenefits;
import com.example.membership_api.utils.DateUtils;
import com.example.membership_api.utils.JsonUtils;
import com.example.membership_api.utils.SubscriptionUtils;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SubscriptionService {

    @Autowired
    private SubscriptionDAO subscriptionDAO;

    @Autowired
    private MembershipTierDAO membershipTierDAO;

    @Autowired
    private MembershipPlanDAO membershipPlanDAO;

    @Autowired
    private TierBenefitService tierBenefitService;

    private static final Logger logger = LoggerFactory.getLogger(TierBenefitService.class);

    public Map<String, Object> getAll(int page, int perPage, Map<String, Object> filters) {
        return subscriptionDAO.getAll(page, perPage, filters);
    }

    public Subscription getById(String subscriptionId) {
        return subscriptionDAO.getById(subscriptionId);
    }

    public Subscription create(String userId, Map<String, Object> data) {
        String planId = (String) data.get("plan_id");

        logger.info("TierType: {} {}", TierType.BRONZE.name(), TierType.BRONZE.getLevel());

        List<MembershipTier> bronzeTiers = membershipTierDAO.getActiveByTier(TierType.BRONZE.getLevel());
        logger.info("bronze_tiers: {}", bronzeTiers.stream().map(MembershipTier::toMap).toList());
        
        Subscription subscription = new Subscription();
        
        if (!bronzeTiers.isEmpty()) {
            MembershipTier defaultTier = bronzeTiers.get(0);
            String defaultTierId = defaultTier.getId();

            var benefits = tierBenefitService.getByTierId(defaultTierId);
            String serializedBenefits = benefits.stream().map(b -> b.getId()).collect(Collectors.toList()).toString();

            MembershipPlan plan = membershipPlanDAO.getById(planId);
            logger.info("default_tier_id: {} benefits: {} plan_details: {}", defaultTierId, benefits, plan.toMap());

            LocalDateTime[] dates = DateUtils.calculateEndDateFromCurrent(plan.getDurationDays());
            LocalDateTime startDate = dates[0];
            LocalDateTime endDate = dates[1];

            subscription.setUserId(userId);
            subscription.setPlanId(planId);
            subscription.setTierId(defaultTierId);
            subscription.setCurrentTier(TierType.BRONZE.getLevel());
            subscription.setBenefits(serializedBenefits);
            subscription.setSubscriptionNumber(SubscriptionUtils.generateSubscriptionNumber().toUpperCase());
            subscription.setTotalSaved(0.0);
            subscription.setTotalSpent(0.0);
            subscription.setStartDate(startDate);
            subscription.setEndDate(endDate);
        }

        logger.info("subscription: {}", subscription.toString());
        return subscriptionDAO.create(subscription);
    }

    public Subscription update(String subscriptionId, Map<String, Object> data) {
        System.out.printf("Subscription Update: %s%n", data.toString());
        Map<String, Object> mutableData = (data instanceof HashMap) ? data : new HashMap<>(data);

        if (mutableData.containsKey("tierId")) {
            String tierId = mutableData.get("tierId").toString();
            System.out.printf("tierId: %s",tierId);
            List<TierBenefits> tierBenefits = tierBenefitService.getByTierId(tierId);            
                        List<String> serializedBenefits = tierBenefits.stream()
                .map(TierBenefits::getId)
                .collect(Collectors.toList());

            String jsonBenefits = JsonUtils.toJson(serializedBenefits); 

            // List<String> serializedBenefits = tierBenefits.stream()
            //     .map(b -> b.getId())
            //     .collect(Collectors.toList());
            
            System.out.printf("serializedBenefits: %s",jsonBenefits);
            mutableData.put("benefits", jsonBenefits);
        }

        if (mutableData.containsKey("planId")) {
            String planId = mutableData.get("planId").toString();
            MembershipPlan plan = membershipPlanDAO.getById(planId);
            LocalDateTime[] dates = DateUtils.calculateEndDateFromCurrent(plan.getDurationDays());
            mutableData.put("start_date", dates[0]);
            mutableData.put("end_date", dates[1]);
        }
        System.out.printf("mutableData: %s%n",mutableData.toString());
        return subscriptionDAO.update(subscriptionId, mutableData);
    }


    public Subscription save(Subscription subscription) {
        return subscriptionDAO.save(subscription);
    }

    public Subscription getActiveSubscriptionByUser(String userId) {
        return subscriptionDAO.getActiveByUser(userId);
    }

    public Subscription extendSubscription(String subscriptionId, Integer extendDays, Map<String, Object> additionalData) {
        Subscription subscription = subscriptionDAO.getById(subscriptionId);
        if (subscription == null || subscription.isExpired() && !subscription.getIsActive()) {
            return null;
        }

        if (extendDays != null) {
            subscription.setEndDate(subscription.getEndDate().plusDays(extendDays));
        }

        List<String> allowedFields = Arrays.asList("auto_renew", "tier_id", "plan_id", "benefits");
        for (String field : allowedFields) {
            if (additionalData != null && additionalData.containsKey(field)) {
                SubscriptionUtils.setField(subscription, field, additionalData.get(field));
            }
        }

        return subscriptionDAO.save(subscription);
    }

    public boolean softDelete(String subscriptionId) {
        return subscriptionDAO.softDelete(subscriptionId);
    }
}
