package com.mod.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mod.rest.dto.*;
import com.mod.rest.model.*;
import com.mod.rest.repository.*;
import com.mod.rest.system.Utils;
import org.apache.commons.codec.binary.*;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    UnitService unitService;
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
    @Autowired
    CountryRepository cRepo;
    @Autowired
    HumanAidDIARepository haRepo;
    @Autowired
    ActivityJointCommitteeRepository ajRepo;
    @Autowired
    VisitsDIARepository vRepo;
    @Autowired
    TrainingAndCoursesDIARepository tcRepo;
    @Autowired
    HistoryOfCommonRelationDIARepository hcRepo;
    @Autowired
    JointCommitteeMeetingDIARepository jmRepo;
    @Autowired
    Environment env;
    @Autowired
    UnitRepository unitRepository;

    public ResponseEntity<byte[]> generatePDF(Long id) throws UnsupportedEncodingException {
        List<String> sections = new LinkedList<String>(
                Arrays.asList("DP", "CV", "AC", "RP", "MM", "RD", "JC",
                "PM", "TS", "LD", "RPT", "OM", "PC", "GE", "CI", "AV", "AG", "TC", "PD", "CB")
        );

        Optional<CountryDisplay> displayOptional = countryDisplayRepository.findById(id);
        if (!displayOptional.isPresent()) return null;

        CountryDisplay countryDisplay = displayOptional.get();
        List<String> sectionList = Arrays.asList(countryDisplay.getSections().split(","));
        sections.removeAll(sectionList);

        Optional<Country> country = cRepo.findById(Long.valueOf(countryDisplay.getCountryValue()));
        List countryList = Arrays.asList(country.get());

        country.get().setFlagImage(env.getProperty("domain-port")+"/api/country/view/"+country.get().getId()+"/2?countryCode="+country.get().getCountryCode());
        country.get().setMapImg(env.getProperty("domain-port")+"/api/country/view/"+country.get().getId()+"/3?countryCode="+country.get().getCountryCode());

        String doc = displayOptional.get().confidentialityDegree();
        country.get().setDegreeOfConf(doc);

        File file = new File("pdf-template/countryDisplay-template.html");
        for (String sec : sections) {
            try {
//                file = pdfService.removeNodeByTagName(file.toURI().getPath(), sec);
                file = pdfService.removeNodeByTagName(file.toURI().getPath(), sec + "-table");
            } catch (TransformerException | IOException e) {
                e.printStackTrace();
            }
        }

        List<CountryLeader> countryLeaders = leaderRepo.getCountryLeaderByDisplayFileId(0, Integer.MAX_VALUE, id);
        for (CountryLeader leaders : countryLeaders) {
            List<CountryAdditionalDto> leaderAdditionalData = caRepo.getSelectedLeaderAdditionalData(leaders.getId(), id);
            leaders.setCountryAdditionalDatas(leaderAdditionalData);
            String leaderImage = env.getProperty("domain-port")+"/api/country/leader/view/"+leaders.getId()+"/1?countryCode="+country.get().getCountryCode();
            List<LeaderPositionDto> leaderPositions = leaderRepo.getSelectedLeaderPosition(leaders.getId(), id);
            leaders.setCountryLeaderPositions(leaderPositions);
            leaders.setPicture(leaderImage);
        }

        List<DiscussionPointDIA> discussionPointDIAList = dpRepo.getDiscussionPointDIAByCountryDisplayFileId(id);
        if(discussionPointDIAList.size() > 0){
              lookupService.substituteLookupIds(discussionPointDIAList, "discussionPointField", "field", "ar");
        }
        List<AspectsOfCooperationDIA> aspectsOfCooperations = acRepo.getAspectsOfCooperationDIAByCountryDisplayFileId(id);
        List<RelatedPeopleDIA> relatedPeopleDIAS = rpRepo.getRelatedPeopleDIAByCountryDisplayFileId(id);

        List<MediaMonitoringDIA> mediaMonitoringDIAS = mmRepo.getMediaMonitoringDIAByCountryDisplayFileId(id);
        if(mediaMonitoringDIAS.size() > 0) {
            lookupService.substituteLookupIds(mediaMonitoringDIAS, "latestNewsType", "category", "ar");
        }

        List<RegionalTalkingPointsDIA> regionalTalkingPointsDIAS = rdRepo.getRegionalTalkingPointsDIAByCountryDisplayFileId(id);
        List<JoinedCommitteeDIA> joinedCommitteeDIAS = new ArrayList<>();

        List<JointCommitteeDto> joinedCommitteeDTO = jcRepo.getSelectedJointCommittees(0, Integer.MAX_VALUE, id);
        for (JointCommitteeDto committee : joinedCommitteeDTO) {

            List<MeetingJointCommitteeDto> meetingsJointCommitteeDIA = jmRepo.getSelectedCommitteeMeeting(id, committee.getJointCommitteeId());
            List<CountryAdditionalDto> additionalJointCommitteeDIA = caRepo.findAllByTypeAndParentEntityId(0, Integer.MAX_VALUE, committee.getJointCommitteeId(), "jointCommittee", 1, id);
            List<ActivityJointCommitteeDto> activityCommittee = ajRepo.getSelectedActivityJointCommittee(id, committee.getJointCommitteeId());

            JoinedCommitteeDIA target = new JoinedCommitteeDIA();
            BeanUtils.copyProperties(committee, target);

            target.setCountryAdditionalData(additionalJointCommitteeDIA);
            target.setActivityJointCommittees(activityCommittee);
            target.setMeetingsJointCommittees(meetingsJointCommitteeDIA);
            target.setCommitteeType(committee.getCommitteeTypeValue());
            joinedCommitteeDIAS.add(target);
        }

        List<PreviousMeetingsDIA> previousMeetingsDIAS = pmRepo.getPreviousMeetingsDIAByCountryDisplayFileId(id);
        for (PreviousMeetingsDIA meetings : previousMeetingsDIAS) {
            List<CountryAdditionalDto> meetingsAdditionalData = caRepo.getSelectedLeaderAdditionalData(meetings.getId(), id);
            meetings.setCountryAdditionalDataDIA(meetingsAdditionalData);
            List<MeetingsResultsDto> lastMeetingResults = pmRepo.getSelectedLastMeetingResults(0, Integer.MAX_VALUE, id, meetings.getId());
            if(lastMeetingResults.size() > 0) {
                  lookupService.substituteLookupIds(discussionPointDIAList, "discussionPointField", "field", "ar");
            }
            meetings.setMeetingsResultsDIA(lastMeetingResults);
        }
        List<TroopsDIA> troopsDIAS = tsRepo.getTroopsDIAByCountryDisplayFileId(id);
        List<LegalDocumentsDIA> legalDocumentsDIAS = ldRepo.getLegalDocumentsDIAByCountryDisplayFileId(id);
        if(legalDocumentsDIAS.size() > 0){
            lookupService.substituteLookupIds(legalDocumentsDIAS, "countryDocumentType", "documentType", "ar");
        }

        List<ReportsDIA> reportsDIAS = rptRepo.getReportsDIAByCountryDisplayFileId(id);
        if (reportsDIAS.size() > 0){
            lookupService.substituteLookupIds(reportsDIAS, "reportType", "reportType", "ar");
            // lookupService.substituteLookupIds(reportsDIAS, "reportFrom", "reportFrom", "ar");
            for (ReportsDIA report : reportsDIAS) {
                if(report.getReportFrom() == "1"){
                    String issuerName = unitRepository.getUnitByUnitCode(report.getReportIssuer()).get(0).getUnitNameByLanguage("ar");
                    report.setReportIssuer(issuerName);
                }else {
                    report.setReportIssuer(report.getReportExternalIssuer());
                }
            }
        }

        List<CountryAdditionalDto> countryAdditionalDatasListRPT = caRepo.findAllByTypeAndParentEntityId(0, Integer.MAX_VALUE, Long.valueOf(countryDisplay.getCountryValue()), "report", 1, id);
        List<CountryAdditionalDto> countryAdditionalDatasListPC = caRepo.findAllByTypeAndParentEntityId(0, Integer.MAX_VALUE, Long.valueOf(countryDisplay.getCountryValue()), "purchasesAndContracts", 1, id);
        List<OfficialMissionsDIA> officialMissionsDIA = omRepo.getOfficialMissionsDIAByCountryDisplayFileId(id);
        List<PurchasesAndContractsDIA> purchasesAndContractsDIAS = pcRepo.getPurchasesAndContractsDIAByCountryDisplayFileId(id);
        List<GeoStrategicalEventsDIA> geoStrategicalEventsDIAS = geRepo.getGeoStrategicalEventsDIAByCountryDisplayFileId(id);
        List<CooperationImportanceDIA> cooperationImportanceDIAS = ciRepo.getCooperationImportanceDIABycountryFileBasicId(countryDisplay.getCountryValue());
        List<CountryAdditionalDto> countryAdditionalDatasListCI = caRepo.findAllByTypeAndParentEntityId(0, Integer.MAX_VALUE, Long.valueOf(countryDisplay.getCountryValue()), "cooperationImportance", 1, id);
        List<HumanAidDto> humanAidDIAList = haRepo.getHumanAidDIAByType(0, Integer.MAX_VALUE, 2, id);
        List<HumanAidDto> humanAidDIAListMilitary = haRepo.getHumanAidDIAByType(0, Integer.MAX_VALUE, 1, id);
        List<CountryAdditionalDto> countryAdditionalDatasListAG = caRepo.findAllByTypeAndParentEntityId(0, Integer.MAX_VALUE, Long.valueOf(countryDisplay.getCountryValue()), "humanAid", 1, id);
        List<ActivityJointCommitteeDto> ActivityJointCommitteeDto = ajRepo.getActivityJointCommitteeByCountryFileId(0, Integer.MAX_VALUE, id);
        List<VisitsDIA> visitsDIAList = new ArrayList<>();

        List<VisitsDto> visitsDto = vRepo.getSelectedVisitsDIAByCountryFileId(0, Integer.MAX_VALUE, id);
        for (VisitsDto visit : visitsDto) {
            List<CountryAdditionalDto> visitAdditionalData = caRepo.findAllByTypeAndParentEntityId(0, 0, visit.getVisitId(), "visit", 1, id);
            VisitsDIA target = new VisitsDIA();
            BeanUtils.copyProperties(visit, target);
            target.setCountryAdditionalDataVisits(visitAdditionalData);
            visitsDIAList.add(target);
        }

        List<TrainingAndCoursesDto> trainingDtoList = tcRepo.getTrainingAndCoursesDIAByType(0, Integer.MAX_VALUE, "training", id);
        List<TrainingAndCoursesDto> coursesDtoList = tcRepo.getTrainingAndCoursesDIAByType(0, Integer.MAX_VALUE, "course", id);
        List<CountryAdditionalDto> countryAdditionalDatasListTC = caRepo.findAllByTypeAndParentEntityId(0, 0, Long.valueOf(countryDisplay.getCountryValue()), "trainingCourses", 1, id);
        List<CountryAdditionalDto> countryAdditionalDatasListGE = caRepo.findAllByTypeAndParentEntityId(0, 0, Long.valueOf(countryDisplay.getCountryValue()), "historicEvents", 1, id);
        List<CountryAdditionalDto> countryAdditionalDatasListRD = caRepo.findAllByTypeAndParentEntityId(0, 0, Long.valueOf(countryDisplay.getCountryValue()), "regionalTalkingPoints", 1, id);
        List<CountryAdditionalDto> countryAdditionalDatasListAV = caRepo.findAllByTypeAndParentEntityId(0, 0, Long.valueOf(countryDisplay.getCountryValue()), "visit", 1, id);
        List<CountryAdditionalDto> countryAdditionalDatasListOM = caRepo.findAllByTypeAndParentEntityId(0, 0, Long.valueOf(countryDisplay.getCountryValue()), "officialMissions", 1, id);
        List<PreviousMeetingsDto> previousMeetingsDtos = pmRepo.getLatestMeeting(id);
        List<CountryAdditionalDto> countryAdditionalDatasListCB = null;
        List<DiscussionPointsDto> stuckedPointsLists = null;
        if(previousMeetingsDtos.size() >0) {
            stuckedPointsLists = dpRepo.getSelectedStuckedPoints(0, Integer.MAX_VALUE, id, previousMeetingsDtos.get(0).getId());
            countryAdditionalDatasListCB = caRepo.getSelectedLastMeetingAdditionalData(0, Integer.MAX_VALUE, id, previousMeetingsDtos.get(0).getId());
        }

        if(countryList.size() > 0){
            lookupService.substituteLookupIds(countryList, "countryRelationshipLevel", "relationshiphLevel", "ar");
        }

        //   List <MeetingsResultsDto> lastMeetingResults = pmRepo.getSelectedLastMeetingResults(0,Integer.MAX_VALUE,id, previousMeetingsDtos.get(0).getId());
        List<HistoryOfCommonRelationDto> historyOfCommonRelationDtos = hcRepo.getSelectedHistoryOfCommonRel(0, Integer.MAX_VALUE, id);

//http://localhost:8081/api/country-display/pdf/32769
        try {
            file = pdfService.generate(countryList, file.toURI().getPath(), "cover-image");

            if (sectionList.contains("CV")){
                file = pdfService.generate(countryLeaders, file.toURI().getPath(), "CV");
            }
            if (sectionList.contains("PD")) {
                file = pdfService.generate(countryList, file.toURI().getPath(), "PD");
                file = pdfService.generate(visitsDto, file.toURI().getPath(), "PD-visits");
                file = pdfService.generate(historyOfCommonRelationDtos, file.toURI().getPath(), "PD-CR");
            }

            if (sectionList.contains("DP")){
                file = pdfService.generate(discussionPointDIAList, file.toURI().getPath(), "DP");
            }
            if (sectionList.contains("AC")) {
                file = pdfService.generate(aspectsOfCooperations, file.toURI().getPath(), "AC");
            }
            if (sectionList.contains("RP")) {
                file = pdfService.generate(relatedPeopleDIAS, file.toURI().getPath(), "RP");
            }
            if (sectionList.contains("MM")) {
                file = pdfService.generate(mediaMonitoringDIAS, file.toURI().getPath(), "MM");
            }
            if (sectionList.contains("RD")) {
                file = pdfService.generate(regionalTalkingPointsDIAS, file.toURI().getPath(), "RD");
                file = pdfService.generate(countryAdditionalDatasListRD, file.toURI().getPath(), "RD-additionalData");
            }
            if (sectionList.contains("JC")) {
                file = pdfService.generate(joinedCommitteeDIAS, file.toURI().getPath(), "JC");
            }
            if (sectionList.contains("PM")) {
                file = pdfService.generate(previousMeetingsDIAS, file.toURI().getPath(), "PM");
            }
            if (sectionList.contains("TS")) {
                file = pdfService.generate(troopsDIAS, file.toURI().getPath(), "TS");
            }
            if (sectionList.contains("LD")) {
                file = pdfService.generate(legalDocumentsDIAS, file.toURI().getPath(), "LD");
            }
            if (sectionList.contains("RPT")) {
                file = pdfService.generate(reportsDIAS, file.toURI().getPath(), "RPT");
                file = pdfService.generate(countryAdditionalDatasListRPT, file.toURI().getPath(), "RPT-additionalData");
            }

            if (sectionList.contains("OM")) {
                file = pdfService.generate(officialMissionsDIA, file.toURI().getPath(), "OM");
                file = pdfService.generate(countryAdditionalDatasListOM, file.toURI().getPath(), "OM-additionalData");
            }

            if (sectionList.contains("PC")) {
                file = pdfService.generate(purchasesAndContractsDIAS, file.toURI().getPath(), "PC");
                file = pdfService.generate(countryAdditionalDatasListPC, file.toURI().getPath(), "PC-additionalData");
            }

            if (sectionList.contains("GE")) {
                file = pdfService.generate(geoStrategicalEventsDIAS, file.toURI().getPath(), "GE");
                file = pdfService.generate(countryAdditionalDatasListGE, file.toURI().getPath(), "GE-additionalData");
            }

            if (sectionList.contains("CI")) {
                file = pdfService.generate(cooperationImportanceDIAS, file.toURI().getPath(), "CI");
                file = pdfService.generate(countryAdditionalDatasListCI, file.toURI().getPath(), "CI-additionalData");
            }

            if (sectionList.contains("AG")) {
                file = pdfService.generate(humanAidDIAList, file.toURI().getPath(), "AG");
                file = pdfService.generate(humanAidDIAListMilitary, file.toURI().getPath(), "AG-military");
                file = pdfService.generate(countryAdditionalDatasListAG, file.toURI().getPath(), "AG-additionalData");
            }

            if (sectionList.contains("AV")) {
                file = pdfService.generate(ActivityJointCommitteeDto, file.toURI().getPath(), "AV");
                file = pdfService.generate(visitsDIAList, file.toURI().getPath(), "AV-visits");
            }

            if (sectionList.contains("TC")) {
                file = pdfService.generate(trainingDtoList, file.toURI().getPath(), "TC");
                file = pdfService.generate(coursesDtoList, file.toURI().getPath(), "TC-courses");
                file = pdfService.generate(countryAdditionalDatasListTC, file.toURI().getPath(), "TC-additionalData");
            }

            if (sectionList.contains("CB")) {
                file = pdfService.generate(previousMeetingsDtos, file.toURI().getPath(), "CB");
                file = pdfService.generate(previousMeetingsDtos, file.toURI().getPath(), "CB-recommendation");
                file = pdfService.generate(countryAdditionalDatasListCB, file.toURI().getPath(), "CB-additionalData");
                file = pdfService.generate(stuckedPointsLists, file.toURI().getPath(), "SP");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentLength(bytes.length);
        respHeaders.setContentType(MediaType.APPLICATION_PDF);
        respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+URLEncoder.encode(countryDisplay.getDisplayFileTitle(), StandardCharsets.UTF_8.toString())+".pdf");


        /*
            check selected sections: O2MyCompanyDirectorateofInternationalAffairsDIAMOD_DIA_entity_countryDisplayFile
            get every section's data
            ex leader: education details - position details - additional data  (common)


         */

        if(countryDisplay.getS_WORKSPACEID() != null){

            String createDocumentUrl = "http://appworks-dev/otcs/cs.exe/api/v2/nodes/";
            String folderName = "المرفقات";
            String getSubNodesUrl = "http://appworks-dev/otcs/cs.exe/api/v2/nodes/"+countryDisplay.getS_WORKSPACEID()+"/nodes?where_name=" +
                    URLEncoder.encode(folderName, StandardCharsets.UTF_8.toString());
            String username = "Admin";
            String password = "Asset99a";
            GetMethod getMethod = new GetMethod(getSubNodesUrl);

            PostMethod method = new PostMethod(createDocumentUrl);
            String auth = username + ":" + password;
            byte[] encodedAuth = org.apache.commons.codec.binary.Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            Header tempHeader = new Header();
            tempHeader.setName(HttpHeaders.AUTHORIZATION);
            tempHeader.setValue(authHeader);
            try {
                getMethod.addRequestHeader(tempHeader);
                HttpClient client = new HttpClient();
                int getResponseCode = client.executeMethod(getMethod);
                System.out.println(getResponseCode);
                String response = getMethod.getResponseBodyAsString();
                System.out.println(response);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response);
                ArrayNode jsonArray = jsonNode.withArray("results");
    //            if (jsonArray.size() == 1) {
                    Long documentId = jsonArray.get(0).get("data").get("properties").get("id").asLong();
                    countryDisplay.setS_WORKSPACEID(documentId);
    //            }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                method.addRequestHeader(tempHeader);
                Part[] parts = {
                        new FilePart("file",
                                new ByteArrayPartSource(file.getName().replace(".html", ".pdf"),
                                        bytes)),
                        new StringPart("type", "144", StandardCharsets.UTF_8.name()),
                        new StringPart("parent_id", countryDisplay.getS_WORKSPACEID().toString(), StandardCharsets.UTF_8.name()),
                        new StringPart("name", file.getName().replace("template", URLEncoder.encode(countryDisplay.getDisplayFileTitle(), StandardCharsets.UTF_8.toString()) + "-").replace(".html",".pdf"), StandardCharsets.UTF_8.name()),
                };

                RequestEntity re = new MultipartRequestEntity(parts, new HttpMethodParams());
                method.setRequestEntity(re);
                HttpClient client = new HttpClient();
                int statusCode = client.executeMethod(method);
                String response = method.getResponseBodyAsString();
                System.out.println(response + "\n" + String.valueOf(statusCode));

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
        return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);

    }

}
