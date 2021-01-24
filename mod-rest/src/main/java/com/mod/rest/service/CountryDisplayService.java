package com.mod.rest.service;

import com.mod.rest.dto.ActivityJointCommitteeDto;
import com.mod.rest.dto.CountryAdditionalDto;
import com.mod.rest.dto.HumanAidDto;
import com.mod.rest.dto.VisitsDto;
import com.mod.rest.model.*;
import com.mod.rest.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CountryDisplayService {
    @Autowired
    PDFServiceEx pdfService;
    @Autowired
    CountryDisplayRepository countryDisplayRepository;
    @Autowired
    DiscussionPointDIARepository dpRepo;
    @Autowired
    LookupService lookupService;
    @Autowired
    CountryLeaderRepository leaderRepo;
    @Autowired
    aspectsOfCooperationDIARepository acRepo;
    @Autowired
    RelatedPersonDIARepository rpRepo;
    @Autowired
    MediaMonitoringDIARepository mmRepo;
    @Autowired
    RegionalTalkingPointsDIARepository rdRepo;
    @Autowired
    JointCommitteeDIARepository jcRepo;
    @Autowired
    PreviousMeetingsDIARepository pmRepo;
    @Autowired
    TroopsDIARepository tsRepo;
    @Autowired
    LegalDocumentsDIARepository ldRepo;
    @Autowired
    ReportsDIARepository rptRepo;
    @Autowired
    OfficialMissionsDIARepository omRepo;
    @Autowired
    PurchasesAndContractsDIARepository pcRepo;
    @Autowired
    GeoStrategicalEventsDIARepository geRepo;
    @Autowired
    CooperationImportanceDIARepository ciRepo;
    @Autowired
    CountryAdditionalRepository caRepo;
//    @Autowired
//    ActivityJointCommitteeRepository ajRepo;
    @Autowired
    HumanAidDIARepository haRepo;
    @Autowired
    ActivityJointCommitteeRepository ajRepo;
    @Autowired
    VisitsDIARepository vRepo;

    public ResponseEntity<byte[]> generatePDF(Long id) {
        List<String> sections = new LinkedList<String>(Arrays.asList("DP", "CV", "AC", "RP", "MM", "RD", "JC",
                "PM", "TS", "LD", "RPT", "OM", "PC", "GE", "CI", "AV", "AG"));


        Optional<CountryDisplay> displayOptional = countryDisplayRepository.findById(id);

        if (!displayOptional.isPresent()) return null;

        CountryDisplay countryDisplay = displayOptional.get();
        List<String> sectionList = Arrays.asList(countryDisplay.getSections().split(","));
        sections.removeAll(sectionList);

        File file = new File("pdf-template/countryDisplay-template.html");
        for (String sec : sections) {
            try {
                file = pdfService.removeNodeByTagName(file.toURI().getPath(), sec);
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List <CountryLeader> countryLeaders = leaderRepo.getCountryLeaderByDisplayFileId(0, Integer.MAX_VALUE, id);
        List <DiscussionPointDIA> discussionPointDIAList = dpRepo.getDiscussionPointDIAByCountryDisplayFileId(id);
        List <AspectsOfCooperationDIA> aspectsOfCooperations = acRepo.getAspectsOfCooperationDIAByCountryDisplayFileId(id);
        List <RelatedPeopleDIA> relatedPeopleDIAS = rpRepo.getRelatedPeopleDIAByCountryDisplayFileId(id);
        List <MediaMonitoringDIA> mediaMonitoringDIAS = mmRepo.getMediaMonitoringDIAByCountryDisplayFileId(id);
        List <RegionalTalkingPointsDIA> regionalTalkingPointsDIAS = rdRepo.getRegionalTalkingPointsDIAByCountryDisplayFileId(id);
        List <JoinedCommitteeDIA> joinedCommitteeDIAS = jcRepo.getJointCommitteeDIAByCountryDisplayFileId(id);
        List <PreviousMeetingsDIA> previousMeetingsDIAS = pmRepo.getPreviousMeetingsDIAByCountryDisplayFileId(id);
        List <TroopsDIA> troopsDIAS = tsRepo.getTroopsDIAByCountryDisplayFileId(id);
        List <LegalDocumentsDIA> legalDocumentsDIAS = ldRepo.getLegalDocumentsDIAByCountryDisplayFileId(id);
        List <ReportsDIA> reportsDIAS = rptRepo.getReportsDIAByCountryDisplayFileId(id);
        List <OfficialMissionsDIA> officialMissionsDIA = omRepo.getOfficialMissionsDIAByCountryDisplayFileId(id);
        List <PurchasesAndContractsDIA> purchasesAndContractsDIAS = pcRepo.getPurchasesAndContractsDIAByCountryDisplayFileId(id);
        List <GeoStrategicalEventsDIA> geoStrategicalEventsDIAS = geRepo.getGeoStrategicalEventsDIAByCountryDisplayFileId(id);
        List <CooperationImportanceDIA> cooperationImportanceDIAS = ciRepo.getCooperationImportanceDIABycountryFileBasicId(countryDisplay.getCountryValue());
        List <CountryAdditionalDto> countryAdditionalDatasListCI = caRepo.findAllByTypeAndParentEntityId(0,0, Long.valueOf(countryDisplay.getCountryValue()), "cooperationImportance", 1,id );
//        List <ActivityJointCommittee> activityJointCommitteeList = ajRepo.findAllByCountryValueId(Long.valueOf(countryDisplay.getCountryValue()));
        List <HumanAidDto> humanAidDIAList = haRepo.getHumanAidDIAByType(0,Integer.MAX_VALUE, 2, id);
        List <HumanAidDto> humanAidDIAListMilitary = haRepo.getHumanAidDIAByType(0,Integer.MAX_VALUE, 1, id);
        List <CountryAdditionalDto> countryAdditionalDatasListAG = caRepo.findAllByTypeAndParentEntityId(0,0, Long.valueOf(countryDisplay.getCountryValue()), "humanAid", 1,id );
        List <ActivityJointCommitteeDto> ActivityJointCommitteeDto = ajRepo.getActivityJointCommitteeByCountryFileId(0,Integer.MAX_VALUE,id );
        List <VisitsDto> visitsDto = vRepo.getSelectedVisitsDIAByCountryFileId(0,Integer.MAX_VALUE,id );








//
// lookupService.substituteLookupIds(discussionPointDIAList, "discussionPointField", "field", "ar");
//        lookupService.substituteLookupIds(discussionPointDIAList, "countryLookup", "suggestedBy", "ar");
        // lookupService.substituteLookupIds(mediaMonitoringDIAS,  "countryLookup", "suggestedBy", "ar");
        // lookupService.substituteLookupIds(joinedCommitteeDIAS,  "countryLookup", "suggestedBy", "ar");



//http://localhost:8081/api/country-display/pdf/32769

        try {
//            file = pdfService.generate(countryLeaders, file.toURI().getPath(), "CV");
//            file = pdfService.generate(discussionPointDIAList, file.toURI().getPath(), "DP");
//            file = pdfService.generate(aspectsOfCooperations, file.toURI().getPath(), "AC");
//            file = pdfService.generate(relatedPeopleDIAS, file.toURI().getPath(), "RP");
//            file = pdfService.generate(mediaMonitoringDIAS, file.toURI().getPath(), "MM");
//            file = pdfService.generate(regionalTalkingPointsDIAS, file.toURI().getPath(), "RD");
//            file = pdfService.generate(joinedCommitteeDIAS, file.toURI().getPath(), "JC");
//            file = pdfService.generate(previousMeetingsDIAS, file.toURI().getPath(), "PM");
//            file = pdfService.generate(troopsDIAS, file.toURI().getPath(), "TS");
//            file = pdfService.generate(legalDocumentsDIAS, file.toURI().getPath(), "LD");
//            file = pdfService.generate(reportsDIAS, file.toURI().getPath(), "RPT");
//            file = pdfService.generate(officialMissionsDIA, file.toURI().getPath(), "OM");
//            file = pdfService.generate(purchasesAndContractsDIAS, file.toURI().getPath(), "PC");
//            file = pdfService.generate(geoStrategicalEventsDIAS, file.toURI().getPath(), "GE");
//            file = pdfService.generate(cooperationImportanceDIAS, file.toURI().getPath(), "CI");
 //           file = pdfService.generate(countryAdditionalDatasListCI, file.toURI().getPath(), "CI-additionalData");
//            file = pdfService.generate(humanAidDIAList, file.toURI().getPath(), "AG");
//            file = pdfService.generate(humanAidDIAListMilitary, file.toURI().getPath(), "AGMilitary");
//            file = pdfService.generate(countryAdditionalDatasListAG, file.toURI().getPath(), "AG-additionalData");
            file = pdfService.generate(ActivityJointCommitteeDto, file.toURI().getPath(), "AV");
            file = pdfService.generate(visitsDto, file.toURI().getPath(), "AVV");


        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
        HttpHeaders respHeaders = new HttpHeaders();

        respHeaders.setContentLength(bytes.length);
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf");
        return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);



        /*
            check selected sections: O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_countryDisplayFile
            get every section's data
            ex leader: education details - position details - additional data  (common)


         */


//        return null;
    }

}
