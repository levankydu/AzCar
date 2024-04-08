package com.project.AzCar.Repositories.Reply;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Reply.Reply;
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

	@Query(value = "SELECT * FROM tb_replies r where r.comment_id = :comment_id order by r.id desc limit 2",nativeQuery = true)
	List<Reply> getAllReplyByCommentId(@Param("comment_id") int commentId);
	

	
}
