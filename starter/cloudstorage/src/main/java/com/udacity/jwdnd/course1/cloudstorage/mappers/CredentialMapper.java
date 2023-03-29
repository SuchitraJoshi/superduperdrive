package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getMyCredentials(int userId);
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId} AND credentialid = #{credentialId}")
    Credential getCredentialById(int userId, int credentialId);

    @Insert("INSERT INTO CREDENTIALS (credentialUrl,credentialUsername,encryptionKey,encryptedPassword,userid) VALUES(#{credentialUrl},#{credentialUsername},#{encryptionKey},#{encryptedPassword},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET credentialUrl = #{credentialUrl}, credentialUsername = #{credentialUsername},encryptionKey = #{encryptionKey}, encryptedPassword = #{encryptedPassword} WHERE userid = #{userId} AND credentialid = #{credentialId}")
    int updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE userid = #{userId} AND credentialid = #{credentialId}")
    int deleteCredential(int userId, int credentialId);

}
