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

    public ResponseEntity<byte[]> generatePDF(Long id) throws UnsupportedEncodingException {
        List<String> sections = new LinkedList<String>(Arrays.asList("DP", "CV", "AC", "RP", "MM", "RD", "JC",
                "PM", "TS", "LD", "RPT", "OM", "PC", "GE", "CI", "AV", "AG", "TC", "PD", "CB"));

        Optional<CountryDisplay> displayOptional = countryDisplayRepository.findById(id);
        if (!displayOptional.isPresent()) return null;

        CountryDisplay countryDisplay = displayOptional.get();
        List<String> sectionList = Arrays.asList(countryDisplay.getSections().split(","));
        sections.removeAll(sectionList);

        File file = new File("pdf-template/countryDisplay-template.html");
        for (String sec : sections) {
            try {
                file = pdfService.removeNodeByTagName(file.toURI().getPath(), sec);
            } catch (TransformerException | IOException e) {
                e.printStackTrace();
            }
        }

        List<CountryLeader> countryLeaders = leaderRepo.getCountryLeaderByDisplayFileId(0, Integer.MAX_VALUE, id);
        for (CountryLeader leaders : countryLeaders) {
            List<CountryAdditionalDto> leaderAdditionalData = caRepo.getSelectedLeaderAdditionalData(leaders.getId(), id);
            leaders.setCountryAdditionalDatas(leaderAdditionalData);
            List<LeaderPositionDto> leaderPositions = leaderRepo.getSelectedLeaderPosition(leaders.getId(), id);
            leaders.setCountryLeaderPositions(leaderPositions);
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
        List<CountryAdditionalDto> countryAdditionalDatasListCB = caRepo.getSelectedLastMeetingAdditionalData(0, Integer.MAX_VALUE, id, previousMeetingsDtos.get(0).getId());
        Optional<Country> country = cRepo.findById(Long.valueOf(countryDisplay.getCountryValue()));
        List countryList = Arrays.asList(country.get());
        if(countryList.size() > 0){
            lookupService.substituteLookupIds(countryList, "countryRelationshipLevel", "relationshiphLevel", "ar");
        }
        List<DiscussionPointsDto> stuckedPointsLists = dpRepo.getSelectedStuckedPoints(0, Integer.MAX_VALUE, id, previousMeetingsDtos.get(0).getId());

        //   List <MeetingsResultsDto> lastMeetingResults = pmRepo.getSelectedLastMeetingResults(0,Integer.MAX_VALUE,id, previousMeetingsDtos.get(0).getId());
        List<HistoryOfCommonRelationDto> historyOfCommonRelationDtos = hcRepo.getSelectedHistoryOfCommonRel(0, Integer.MAX_VALUE, id);

//http://localhost:8081/api/country-display/pdf/32769
        try {
            if (sectionList.contains("CV")){
                file = pdfService.generate(countryLeaders, file.toURI().getPath(), "CV");
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
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "RD-additionalData");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
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
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "RPT-additionalData");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("OM")) {
                file = pdfService.generate(officialMissionsDIA, file.toURI().getPath(), "OM");
                file = pdfService.generate(countryAdditionalDatasListOM, file.toURI().getPath(), "OM-additionalData");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "OM-additionalData");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("PC")) {
                file = pdfService.generate(purchasesAndContractsDIAS, file.toURI().getPath(), "PC");
                file = pdfService.generate(countryAdditionalDatasListPC, file.toURI().getPath(), "PC-additionalData");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "PC-additionalData");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("GE")) {
                file = pdfService.generate(geoStrategicalEventsDIAS, file.toURI().getPath(), "GE");
                file = pdfService.generate(countryAdditionalDatasListGE, file.toURI().getPath(), "GE-additionalData");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "GE-additionalData");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("CI")) {
                file = pdfService.generate(cooperationImportanceDIAS, file.toURI().getPath(), "CI");
                file = pdfService.generate(countryAdditionalDatasListCI, file.toURI().getPath(), "CI-additionalData");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "CI-additionalData");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("AG")) {
                file = pdfService.generate(humanAidDIAList, file.toURI().getPath(), "AG");
                file = pdfService.generate(humanAidDIAListMilitary, file.toURI().getPath(), "AG-military");
                file = pdfService.generate(countryAdditionalDatasListAG, file.toURI().getPath(), "AG-additionalData");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "AG-military");
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "AG-additionalData");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("AV")) {
                file = pdfService.generate(ActivityJointCommitteeDto, file.toURI().getPath(), "AV");
                file = pdfService.generate(visitsDIAList, file.toURI().getPath(), "AV-visits");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "AV-visits");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("TC")) {
                file = pdfService.generate(trainingDtoList, file.toURI().getPath(), "TC");
                file = pdfService.generate(coursesDtoList, file.toURI().getPath(), "TC-courses");
                file = pdfService.generate(countryAdditionalDatasListTC, file.toURI().getPath(), "TC-additionalData");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "TC-courses");
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "TC-additionalData");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("CB")) {
                file = pdfService.generate(previousMeetingsDtos, file.toURI().getPath(), "CB");
                file = pdfService.generate(previousMeetingsDtos, file.toURI().getPath(), "CB-recommendation");
                file = pdfService.generate(countryAdditionalDatasListCB, file.toURI().getPath(), "CB-additionalData");
                file = pdfService.generate(stuckedPointsLists, file.toURI().getPath(), "SP");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "CB-recommendation");
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "CB-additionalData");
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "SP");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (sectionList.contains("PD")) {
                file = pdfService.generate(countryList, file.toURI().getPath(), "PD");
                file = pdfService.generate(visitsDto, file.toURI().getPath(), "PD-visits");
                file = pdfService.generate(historyOfCommonRelationDtos, file.toURI().getPath(), "PD-CR");
            }else {
                try {
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "PD-visits");
                    file = pdfService.removeNodeByTagName(file.toURI().getPath(), "PD-CR");
                } catch (TransformerException | IOException e) {
                    e.printStackTrace();
                }
            }













        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] bytes = pdfService.generatePDF(file.getAbsolutePath());
        HttpHeaders respHeaders = new HttpHeaders();

        respHeaders.setContentLength(bytes.length);
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf");



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
                        new StringPart("name", file.getName().replace(".html",".pdf"), StandardCharsets.UTF_8.name()),
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
