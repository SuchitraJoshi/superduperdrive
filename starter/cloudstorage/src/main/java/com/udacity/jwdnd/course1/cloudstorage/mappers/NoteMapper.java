package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getMyMessages(int userId);
    @Select("SELECT * FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId}")
    Note getNote(int userId, int noteId);
    @Insert("INSERT INTO NOTES (notetitle, notedescription,userid) VALUES(#{noteTitle}, #{noteDescription},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE userid = #{userId} AND noteid = #{noteId}")
    int updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId}")
    int deleteNote(int userId, int noteId);

}
