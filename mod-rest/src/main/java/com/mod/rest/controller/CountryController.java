package com.mod.rest.controller;

import com.mod.rest.model.Country;
import com.mod.rest.model.CountryLeader;
import com.mod.rest.repository.CountryLeaderRepository;
import com.mod.rest.repository.CountryRepository;
import com.mod.rest.service.ImageService;
import com.mod.rest.system.Config;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.system.ResponseCode;
import com.mod.rest.system.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/country")
public class CountryController {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CountryLeaderRepository countryLeaderRepository;

    @Autowired
    ImageService imageService;


    @Autowired
    private Config configUtil;

    @SuppressWarnings("Duplicates")
    @PostMapping("/upload/{countryId}/{type}")
    public ResponseBuilder<Country> save(@RequestParam("file") MultipartFile file, @PathVariable int type,
                                         @PathVariable long countryId, @RequestParam String countryCode) throws Exception {

        ResponseBuilder<Country> responseBuilder = new ResponseBuilder<Country>();
        responseBuilder.data(new Country()).status(ResponseCode.NO_DATA_SAVED).build();
        countryCode = URLDecoder.decode(countryCode, StandardCharsets.UTF_8.toString());

        Optional<Country> countryOptional = countryRepository.findById(countryId);

        if (countryOptional.isPresent()) {
            Country country = countryOptional.get();


            String location = store(file, Integer.toString(type), countryCode, "PrimaryData");
//            java reflection

            switch (type) {
                case 1:
                    imageService.delete(country.getCountryLogoImage());
                    country.setCountryLogoImage(location);
                    break;

                case 2:
                    imageService.delete(country.getFlagImage());
                    country.setFlagImage(location);
                    break;

                case 3:
                    imageService.delete(country.getMapImg());
                    country.setMapImg(location);
                    break;

                case 4:
                    imageService.delete(country.getMODLogoImg());
                    country.setMODLogoImg(location);
                    break;

                case 5:
                    imageService.delete(country.getArmedforcesLogo());
                    country.setArmedforcesLogo(location);
                    break;

                default:
                    throw new Exception("invalid type");
            }

            countryRepository.save(country);

        }


        return null;
    }


    @SuppressWarnings("Duplicates")
    @PostMapping("/leader/upload/{leaderId}/{type}")
    public ResponseBuilder<Country> saveLeaderImages(@RequestParam("file") MultipartFile file, @PathVariable int type,
                                                     @PathVariable long leaderId, @RequestParam String countryCode) throws Exception {
        countryCode = URLDecoder.decode(countryCode, StandardCharsets.UTF_8.toString());

        ResponseBuilder<CountryLeader> responseBuilder = new ResponseBuilder<>();
        responseBuilder.data(new CountryLeader()).status(ResponseCode.NO_DATA_SAVED).build();

        Optional<CountryLeader> countryLeaderOptional = countryLeaderRepository.findById(leaderId);

        if (countryLeaderOptional.isPresent()) {
            CountryLeader countryLeader = countryLeaderOptional.get();

            String location = store(file, Integer.toString(type), countryCode, "Leader");

            if (type == 1) {
                imageService.delete(countryLeader.getPicture());
                countryLeader.setPicture(location);
            } else {
                throw new Exception("invalid type");
            }

            countryLeaderRepository.save(countryLeader);
        }


        return null;
    }


    @SuppressWarnings("Duplicates")
    @GetMapping("/view/{countryId}/{type}")
    @ResponseBody
    public ResponseEntity<byte[]> viewFile(@PathVariable int type, @PathVariable long countryId, @RequestParam String countryCode) throws Exception {

        Optional<Country> countryOptional = countryRepository.findById(countryId);
        countryCode = URLDecoder.decode(countryCode, StandardCharsets.UTF_8.toString());
        if (countryOptional.isPresent()) {
            Country country = countryOptional.get();
            String originalPath;
            Resource file;
            HttpHeaders respHeaders = new HttpHeaders();

            System.out.println(countryCode);

            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String dir = configUtil.getProperty("uploadDir");
            originalPath = s + "\\" + dir + "\\" + "CountryImages" + "\\" + countryCode + "\\" + "PrimaryData" + "\\" + type + "\\default.png";

            switch (type) {

                case 1:
                    if (country.getCountryLogoImage() != null) {
                        originalPath = country.getCountryLogoImage();
                    }

                    break;

                case 2:
                    if (country.getFlagImage() != null) {
                        originalPath = country.getFlagImage();
                    }
                    break;

                case 3:

                    if (country.getMapImg() != null) {
                        originalPath = country.getMapImg();
                    }
                    break;

                case 4:
                    if (country.getMODLogoImg() != null) {
                        originalPath = country.getMODLogoImg();
                    }
                    break;

                case 5:
                    if (country.getArmedforcesLogo() != null) {
                        originalPath = country.getArmedforcesLogo();
                    }
                    break;

                default:
                    throw new Exception("invalid type");
            }
            Path path = Paths.get(originalPath);
            if (Files.exists(path)) {
                try {
                    file = new UrlResource(path.toUri());
                    String mimeType = Files.probeContentType(path);
                    respHeaders.setContentLength(file.contentLength());
                    byte[] isr = Files.readAllBytes(file.getFile().toPath());

                    respHeaders.setContentType(MediaType.parseMediaType(mimeType));

                    respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                    respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getFilename());

                    return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file

    }


    @SuppressWarnings("Duplicates")
    @GetMapping("/leader/view/{leaderId}/{type}")
    @ResponseBody
    public ResponseEntity<byte[]> viewLeaderImage(@PathVariable int type, @PathVariable long leaderId, @RequestParam String countryCode) throws Exception {

        Optional<CountryLeader> countryLeaderOptional = countryLeaderRepository.findById(leaderId);
        countryCode = URLDecoder.decode(countryCode, StandardCharsets.UTF_8.toString());

        if (countryLeaderOptional.isPresent()) {
            CountryLeader countryLeader = countryLeaderOptional.get();
            String originalPath;
            Resource file;
            HttpHeaders respHeaders = new HttpHeaders();

            System.out.println(countryCode);

            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String dir = configUtil.getProperty("uploadDir");
            originalPath = s + "\\" + dir + "\\" + "CountryImages" + "\\" + countryCode + "\\" + "Leader" + "\\" + type + "\\default.png";

            if (type == 1) {
                if (countryLeader.getPicture() != null) {
                    originalPath = countryLeader.getPicture();
                }
            } else {
                throw new Exception("invalid type");
            }

            Path path = Paths.get(originalPath);
            if (Files.exists(path)) {
                try {
                    file = new UrlResource(path.toUri());
                    String mimeType = Files.probeContentType(path);
                    respHeaders.setContentLength(file.contentLength());
                    byte[] isr = Files.readAllBytes(file.getFile().toPath());

                    respHeaders.setContentType(MediaType.parseMediaType(mimeType));

                    respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                    respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getFilename());

                    return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file

    }

    @SuppressWarnings("Duplicates")
    private String store(MultipartFile file, String id, String countryCode, String category) {


        //delete default image if exists
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String dir = configUtil.getProperty("uploadDir");

        String directory = s + "\\" + dir + "\\" + "CountryImages" + "\\" + countryCode + "\\" + category + "\\" + id;

        File imageDir = new File(directory);
        if (imageDir.exists())
            Utils.deleteDirectory(imageDir);


        String location;
        try {

            Path path = Paths.get(directory);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Directory created");
            }

            System.out.println(path.toFile().getAbsolutePath());

            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");


            String fileName = df.format(date) + file.getOriginalFilename();

            Path locationToStore = path.resolve(fileName);


            Files.copy(file.getInputStream(), locationToStore);
            location = locationToStore.toFile().getAbsolutePath();


        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
        return location;
    }

}
