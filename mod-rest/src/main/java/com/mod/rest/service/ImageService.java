package com.mod.rest.service;

import com.mod.rest.system.Config;
import com.mod.rest.model.Image;
import com.mod.rest.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by karim.omaya on 12/10/2019.
 */
@Service
public class ImageService {
    @Autowired
    private Config configUtil;
    @Autowired
    private ImageRepository imageRepository;

    public Image getImageByTypeAndParentItemId(String type, String parentItemId){
        System.out.println(type);
        System.out.println(parentItemId);
        List<Image> images = imageRepository.findOneByTypeAndParentItemId(type, parentItemId);
        if (images.size() == 0) return null;
        return images.get(0);
    }


    public String store(MultipartFile file, String id, String type) {

        if (type.equals("userImg")){
            List<Image> image = imageRepository.findOneByTypeAndParentItemId(type, id);
            if (image.size()>0){
                for (int i=0; i<image.size();i++){
                    File imgFile = new File(image.get(i).getPath());

                    if(imgFile.delete()){
                        System.out.println("Image is deleted try to add new image");
                    }
                    imageRepository.delete(image.get(i));
                }


            }
        }
        String dir = configUtil.getProperty("uploadDir");
        String location = null;
        try {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();

            String directory =  s+"\\"+ dir + "\\" + type + "\\" + id;

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
