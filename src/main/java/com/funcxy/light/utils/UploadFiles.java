package com.funcxy.light.utils;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by wtupc96 on 2017/3/10.
 *
 * @author Peter
 * @version 1.0
 */
public class UploadFiles {
    public static String upload(MultipartFile file, String path) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
            String filePath = path + ObjectId.get().toString() + "." + suffix;
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(bytes);
                stream.close();
                System.out.println("You successfully uploaded " + filePath + " into " + path);
                return filePath;
            } catch (Exception e) {
                System.out.println("You failed to upload " + " => " + e.getMessage());
            }
        } else {
            System.out.println("You failed to upload because the file was empty.");
        }
        return null;
    }
}
