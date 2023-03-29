package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.models.FileListForm;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<FileListForm> getFileList(int userId) {

        return fileMapper.getMyFileNames(userId);
    }

    public void deleteFile(int userId, Integer fileId) {
        fileMapper.deleteFile(userId,fileId);
    }

    public FileForm getFile(int userId, Integer fileId) {
        return fileMapper.downloadFile(userId,fileId);
    }

    public void storeFile(MultipartFile multipartFile, int userId) throws Exception{

        String fileName= multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        //Blob fileData = null;
        Long fileSize =multipartFile.getSize();
        if(fileMapper.selectFileByName(userId,fileName)!= null){
            throw new Exception("File already exists.");
        }
        try(InputStream inputStream=multipartFile.getInputStream()){
            byte[] fileBytes= new byte[inputStream.available()];
            inputStream.read();
           //fileData  = new javax.sql.rowset.serial.SerialBlob(fileBytes);
            FileForm newFileForm = new FileForm(userId,fileName,contentType,fileBytes, fileSize.toString());
            System.out.println("save filesize :" + fileBytes.toString());
            int fileId = fileMapper.insertFile(newFileForm);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
