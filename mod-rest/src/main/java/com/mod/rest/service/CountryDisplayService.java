package com.mod.rest.service;

import com.mod.rest.model.CountryDisplay;
import com.mod.rest.model.CountryLeader;
import com.mod.rest.model.DiscussionPointDIA;
import com.mod.rest.model.Test;
import com.mod.rest.repository.CountryDisplayRepository;
import com.mod.rest.repository.CountryLeaderRepository;
import com.mod.rest.repository.DiscussionPointDIARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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

    public ResponseEntity<byte[]> generatePDF(Long id) {
        List<String> sections = new LinkedList<String>(Arrays.asList("DP", "MM", "CV"));

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
        List<CountryLeader> countryLeaders = leaderRepo.getCountryLeaderByDisplayFileId(0, Integer.MAX_VALUE, id);
        List<DiscussionPointDIA> discussionPointDIAList = dpRepo.getDiscussionPointDIAByCountryDisplayFileId(id);

        lookupService.substituteLookupIds(discussionPointDIAList, "discussionPointField", "field", "ar");
        lookupService.substituteLookupIds(discussionPointDIAList, "countryLookup", "suggestedBy", "ar");

//http://localhost:8081/api/country-display/pdf/32769
        try {
            file = pdfService.generate(countryLeaders, file.toURI().getPath(), "CV");
            file = pdfService.generate(discussionPointDIAList, file.toURI().getPath(), "DP");
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
