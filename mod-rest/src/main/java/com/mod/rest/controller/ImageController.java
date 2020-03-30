package com.mod.rest.controller;

import com.mod.rest.entity.ImageEntity;
import com.mod.rest.system.ResponseCode;
import com.mod.rest.system.Http;
import com.mod.rest.system.ResponseBuilder;
import com.mod.rest.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.mod.rest.system.Config;
import com.mod.rest.model.Image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by karim.omaya on 12/10/2019.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    private Config configUtil;

    @GetMapping("/view/{type}/{parentItemId}")
    @ResponseBody
    public ResponseEntity<byte[]> viewFile(@PathVariable String type, @PathVariable String parentItemId) {

        Image image = imageService.getImageByTypeAndParentItemId(type, parentItemId);
        HttpHeaders respHeaders = new HttpHeaders();
        Resource file = null;
        String originalPath = "";
        if (image != null) {
            originalPath = image.getPath();
        } else {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String dir = configUtil.getProperty("uploadDir");
            originalPath = s + "\\" + dir + "\\" + type + "\\default.png";
        }
//            originalPath = new PercentEscaper(":_.\\+ ", false).escape(originalPath);
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

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"null\"").body(null); // used to download file

    }

    @PostMapping("/upload/{SAMLart}/{id}/{type}")
    public ResponseBuilder<Image> save(@RequestParam("file") MultipartFile file, @PathVariable String SAMLart, @PathVariable String id,
                                       @PathVariable String type) {

        ResponseBuilder<Image> responseBuilder = new ResponseBuilder<Image>();
        responseBuilder.data(new Image()).status(ResponseCode.NO_DATA_SAVED).build();

        String location = imageService.store(file, id, type);

        if (location != null) {
            Image image = new Image();
            image.setParentItemId(id);
            image.setPath(location);
            image.setType(type);
            ImageEntity imageEntity = new ImageEntity(image);
            Http http = new Http(SAMLart, configUtil);
            imageEntity.create(http);
            responseBuilder.data(image);
        } else {
            responseBuilder.status(ResponseCode.NO_DATA_SAVED);
        }

        return responseBuilder.build();

    }

}
