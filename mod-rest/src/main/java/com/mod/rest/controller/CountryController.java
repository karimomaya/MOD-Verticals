package com.mod.rest.controller;

import com.mod.rest.model.Country;
import com.mod.rest.model.Image;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/country")
public class CountryController {
    @Autowired
    CountryRepository countryRepository;

    @Autowired
    ImageService imageService;


    @Autowired
    private Config configUtil;

    @SuppressWarnings("Duplicates")
    @PostMapping("/upload/{countryId}/{type}")
    public ResponseBuilder<Country> save(@RequestParam("file") MultipartFile file, @PathVariable int type, @PathVariable long countryId, @RequestParam String countryCode) throws Exception {

        ResponseBuilder<Country> responseBuilder = new ResponseBuilder<Country>();
        responseBuilder.data(new Country()).status(ResponseCode.NO_DATA_SAVED).build();

        Optional<Country> countryOptional = countryRepository.findById(countryId);

        if (countryOptional.isPresent()) {
            Country country = countryOptional.get();

            // check if has file

            // delete file

            String fileId = Long.toString(countryId) + '-' + type;
            String location = imageService.store(file, fileId, "CountryImages");
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

                default:
                    throw new Exception("invalid type");
            }

            countryRepository.save(country);

            //delete default image if exists
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String dir = configUtil.getProperty("uploadDir");
            String originalPath = s + "\\" + dir + "\\" + "CountryImages" + "\\" + countryCode + "-" + type;
            File imageDir = new File(originalPath);
            if (imageDir.exists())
                Utils.deleteDirectory(imageDir);
        }


        return null;
    }


    @SuppressWarnings("Duplicates")
    @GetMapping("/view/{countryId}/{type}")
    @ResponseBody
    public ResponseEntity<byte[]> viewFile(@PathVariable int type, @PathVariable long countryId, @RequestParam String countryCode) throws Exception {

        Optional<Country> countryOptional = countryRepository.findById(countryId);

        if (countryOptional.isPresent()) {
            Country country = countryOptional.get();
            String originalPath;
            Resource file = null;
            HttpHeaders respHeaders = new HttpHeaders();

            System.out.println(countryCode);

            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String dir = configUtil.getProperty("uploadDir");
            originalPath = s + "\\" + dir + "\\" + "CountryImages" + "\\" + countryCode + "-" + type + "\\default.png";

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

                default:
                    throw new Exception("invalid type");
            }
            Path path = Paths.get(originalPath);
            try {
                file = new UrlResource(path.toUri());
                String mimeType = Files.probeContentType(path);
                respHeaders.setContentLength(file.contentLength());
                byte[] isr = Files.readAllBytes(file.getFile().toPath());

                respHeaders.setContentType(MediaType.parseMediaType(mimeType));

                respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//                respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename());
                respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getFilename());

                return new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file

    }

}
