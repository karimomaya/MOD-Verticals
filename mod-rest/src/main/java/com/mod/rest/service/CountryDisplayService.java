package com.mod.rest.service;

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

    public ResponseEntity<byte[]> generatePDF(Long id) {
        List<String> sections = new LinkedList<String>(Arrays.asList("DP", "CV", "AC", "RP", "MM", "RD", "JC"));


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

        lookupService.substituteLookupIds(discussionPointDIAList, "discussionPointField", "field", "ar");
        lookupService.substituteLookupIds(discussionPointDIAList, "countryLookup", "suggestedBy", "ar");
        // lookupService.substituteLookupIds(mediaMonitoringDIAS,  "countryLookup", "suggestedBy", "ar");
        // lookupService.substituteLookupIds(joinedCommitteeDIAS,  "countryLookup", "suggestedBy", "ar");
//http://localhost:8081/api/country-display/pdf/32769
        try {
            file = pdfService.generate(countryLeaders, file.toURI().getPath(), "CV");
            file = pdfService.generate(discussionPointDIAList, file.toURI().getPath(), "DP");
            file = pdfService.generate(aspectsOfCooperations, file.toURI().getPath(), "AC");
            file = pdfService.generate(relatedPeopleDIAS, file.toURI().getPath(), "RP");
            file = pdfService.generate(mediaMonitoringDIAS, file.toURI().getPath(), "MM");
            file = pdfService.generate(regionalTalkingPointsDIAS, file.toURI().getPath(), "RD");
            file = pdfService.generate(joinedCommitteeDIAS, file.toURI().getPath(), "JC");

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
