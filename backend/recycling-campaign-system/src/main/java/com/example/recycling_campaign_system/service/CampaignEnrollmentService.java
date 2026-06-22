package com.example.recycling_campaign_system.service;

import com.example.recycling_campaign_system.model.Campaign;
import com.example.recycling_campaign_system.model.CampaignEnrollment;
import com.example.recycling_campaign_system.model.User;
import com.example.recycling_campaign_system.repository.CampaignEnrollmentRepository;
import com.example.recycling_campaign_system.repository.CampaignRepository;
import com.example.recycling_campaign_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignEnrollmentService {

    @Autowired
    private CampaignEnrollmentRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CampaignRepository campaignRepository;

    public EnrollResult enroll(Integer userId, Integer campaignId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return EnrollResult.userNotFound();
        }

        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        if (campaign.isEmpty()) {
            return EnrollResult.campaignNotFound();
        }

        if (campaign.get().getStartDate() == null || !campaign.get().getStartDate().isAfter(LocalDate.now())) {
            return EnrollResult.campaignNotUpcoming();
        }

        if (repository.existsByUserIdAndCampaignId(userId, campaignId)) {
            return EnrollResult.alreadyEnrolled();
        }

        CampaignEnrollment enrollment = new CampaignEnrollment();
        enrollment.setUser(user.get());
        enrollment.setCampaign(campaign.get());
        enrollment.setEnrolledAt(LocalDate.now());

        return EnrollResult.success(repository.save(enrollment));
    }

    public boolean unenroll(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<CampaignEnrollment> findByUser(Integer userId) {
        return repository.findByUserId(userId);
    }

    public List<CampaignEnrollment> findByCampaign(Integer campaignId) {
        return repository.findByCampaignId(campaignId);
    }

    public enum Status {
        SUCCESS, USER_NOT_FOUND, CAMPAIGN_NOT_FOUND, CAMPAIGN_NOT_UPCOMING, ALREADY_ENROLLED
    }

    public static class EnrollResult {
        private final Status status;
        private final CampaignEnrollment enrollment;

        private EnrollResult(Status status, CampaignEnrollment enrollment) {
            this.status = status;
            this.enrollment = enrollment;
        }

        public static EnrollResult success(CampaignEnrollment enrollment) {
            return new EnrollResult(Status.SUCCESS, enrollment);
        }

        public static EnrollResult userNotFound() {
            return new EnrollResult(Status.USER_NOT_FOUND, null);
        }

        public static EnrollResult campaignNotFound() {
            return new EnrollResult(Status.CAMPAIGN_NOT_FOUND, null);
        }

        public static EnrollResult campaignNotUpcoming() {
            return new EnrollResult(Status.CAMPAIGN_NOT_UPCOMING, null);
        }

        public static EnrollResult alreadyEnrolled() {
            return new EnrollResult(Status.ALREADY_ENROLLED, null);
        }

        public Status getStatus() {
            return status;
        }

        public CampaignEnrollment getEnrollment() {
            return enrollment;
        }
    }
}
