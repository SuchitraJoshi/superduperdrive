package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.models.FileListForm;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userId} AND fileid = #{fileId}")
    FileForm downloadFile (Integer userId, Integer fileId);

    @Select("SELECT filename FROM FILES WHERE userid = #{userId} AND filename = #{filename}")
    String selectFileByName(Integer userId, String filename);

    @Insert("INSERT INTO FILES (filename, contenttype,filesize,userid,filedata) " +
            "VALUES(#{fileName}, #{contentType},#{fileSize},#{userId},#{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(FileForm fileForm);

    @Delete("DELETE FROM FILES WHERE userid = #{userId} AND fileid = #{fileId}")
    Integer deleteFile(Integer userId, Integer fileId);

    @Select("SELECT fileid,filename,contenttype,filesize,userid FROM FILES WHERE userid = #{userid}")
    List<FileListForm> getMyFileNames(int userId);
}
