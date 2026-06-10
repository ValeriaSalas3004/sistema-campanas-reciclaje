package com.example.recycling_campaign_system.Service;

import com.example.recycling_campaign_system.Model.*;
import com.example.recycling_campaign_system.Model.DTO.UserResponseDTO;
import com.example.recycling_campaign_system.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    private ReportRepository repo;
    @Autowired
    private UserService userService;
    @Autowired
    private WasteService wasteService;
    @Autowired
    private ZoneService zoneService;
    @Autowired
    private CampaignService campaignService;

    public List<Report> findAll(){
        return this.repo.findAll();
    }
    public Report addReport(Report report) {
        UserResponseDTO u = userService.findByIdUser(report.getUser().getId());
        Optional<WasteType> optional = wasteService.findById(report.getWasteType().getId());
        if (optional.isPresent()) {
            WasteType w = optional.get();
            RecollectionZone z = zoneService.findById(report.getRecoZone().getId());
            Campaign c = campaignService.findById(report.getCampaign().getId());
            if (u != null && w != null && z != null && c != null) {
                return repo.save(report);
            }
            return null;
        }

        return null;
    }

    public Report findByIdReport(Integer id){
        Optional<Report> optional=this.repo.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    public void deleteReport(Integer id){
        this.repo.deleteById(id);
    }

    public Report editReport(Integer id, Report reportEdit){
        Optional<Report> reportOp = this.repo.findById(id);
        if(reportOp.isPresent()){
            Report report = reportOp.get();
            report = reportEdit;
            return this.repo.save(report);
        }
        return null;
    }

    public List<Report> findByCampaignId(Integer id){
        return this.repo.findByCampaignId(id);
    }

    public List<Report> findByUserid(Integer id){
        return this.repo.findByUserId(id);
    }

    public List<Report> findByZoneId(Long id){
        return this.repo.findByRecoZoneId(id);
    }

    public List<Report> findByWasteId(Long id){
        return this.repo.findByWasteTypeId(id);
    }

}
