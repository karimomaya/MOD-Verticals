package com.mod.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mod.rest.model.Gifts;
import com.mod.rest.model.ReceivedGifts;
import com.mod.rest.model.SpentGift;
import com.mod.rest.repository.GiftsRepository;
import com.mod.rest.repository.ReceivedGiftsRepository;
import com.mod.rest.repository.SpentGiftRepository;
import com.mod.rest.service.ExcelWriterService;
import com.mod.rest.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/gift")
public class GiftController {
    @Autowired
    SessionService sessionService;
    @Autowired
    ExcelWriterService excelWriterService;
    @Autowired
    GiftsRepository giftsRepository;
    @Autowired
    ReceivedGiftsRepository receivedGiftsRepository;

    @Autowired
    SpentGiftRepository spentGiftRepository;

    @GetMapping("export-gifts/{reportType}/{startDate}/{endDate}/{receiverCounty}/{receiverName}/{giftType}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable("startDate") String startDate,
                                         @PathVariable("endDate") String endDate,
                                         @PathVariable("receiverCounty") String receiverCounty,
                                         @PathVariable("receiverName") String receiverName,
                                         @PathVariable("giftType") String giftType,
                                         @PathVariable("reportType") int reportType
    ) {
        HttpHeaders respHeaders = new HttpHeaders();
        File file = null;
        byte[] bytes = null;
        try {
            if(reportType == 1){
                List<Gifts> giftReports = giftsRepository.getGiftsRecordsBetweenPurshaseDate(startDate,endDate,giftType);
                file = excelWriterService.generate(giftReports);
            }
            if(reportType == 2){
                List<ReceivedGifts> receivedGifts = receivedGiftsRepository.getReceivedGiftsRecordsBetweenPurshaseDate(startDate,endDate);
                file = excelWriterService.generate(receivedGifts);
            }
            if(reportType == 3){
                if (receiverName != null){
                    List<SpentGift> spentGift = spentGiftRepository.getSpentGiftsRecordsBetweenPurshaseDate(startDate,endDate,receiverCounty,null);
                    file = excelWriterService.generate(spentGift);
                }else if (receiverCounty != null){
                    List<SpentGift> spentGift = spentGiftRepository.getSpentGiftsRecordsBetweenPurshaseDate(startDate,endDate,null,receiverName);
                    file = excelWriterService.generate(spentGift);
                }else if(receiverCounty != null && receiverName != null){
                    List<SpentGift> spentGift = spentGiftRepository.getSpentGiftsRecordsBetweenPurshaseDate(startDate,endDate,null,null);
                    file = excelWriterService.generate(spentGift);
                }else{
                    List<SpentGift> spentGift = spentGiftRepository.getSpentGiftsRecordsBetweenPurshaseDate(startDate,endDate,receiverCounty,receiverName);
                    file = excelWriterService.generate(spentGift);
                }
            }


            if(file == null){
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"null\"").body(null);
            }
            respHeaders.setContentLength(file.length());
            bytes = Files.readAllBytes(file.toPath());
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file
    }
}
