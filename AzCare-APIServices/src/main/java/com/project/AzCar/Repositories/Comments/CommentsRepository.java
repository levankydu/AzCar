package com.project.AzCar.Repositories.Comments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Comments.Comments;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {
	// lấy 5 cmt bởi id tạo mới nhất
	@Query(value = "SELECT * FROM tb_comments r where r.car_id = :id order by r.id desc limit 5;", nativeQuery = true)
	List<Comments> getAllCommentsByCarId(@Param("id") int id);
}
