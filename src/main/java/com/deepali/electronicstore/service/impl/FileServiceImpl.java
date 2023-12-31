package com.deepali.electronicstore.service.impl;

import com.deepali.electronicstore.exception.BadApiRequest;
import com.deepali.electronicstore.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.io.File;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * @author Deepali
     * @implNote  uploads user image
     */
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        logger.info("Initializing uploadFile method of FileServiceImpl");
        //abc.png
        String originalFilename = file.getOriginalFilename();
        logger.info("Filename:{}",originalFilename);
        String filename= UUID.randomUUID().toString();
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension=filename+extension;
        String fullPathWithFileName = path +fileNameWithExtension;

        logger.info("full image path: {}",fullPathWithFileName);

        if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpeg")||extension.equalsIgnoreCase(".jpg"))
        {
            //file save
            logger.info("file extension is {}",extension);
            File folder=new File(path);
            if(!folder.exists())
            {
                //create the folder
                folder.mkdirs();
                System.out.println("folder created");

            }

            //upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;


        }
        else {

            throw new BadApiRequest("File with this "+extension + "not allowed");

        }

    }

    /**
     * @author Deepali
     * @implNote  fetch the path of file
     */
    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        logger.info("Initializing getResource method of FileServiceImpl");
        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        logger.info("Execution Completed of method getResource");
        return inputStream;
    }
}
