package com.project.AzCar.Repositories.Reviews;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.AzCar.Entities.Reviews.Reviews;
@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {
	  // Tìm tất cả đánh giá cho một xe cụ thể dựa trên carId
	@Query(value ="Select * from tb_reviews r where r.car_id = :id",nativeQuery= true)
    List<Reviews> findByCarInforId(@Param("id")Long carId);
    
    // Tìm tất cả đánh giá của một người dùng cụ thể dựa trên userId
    List<Reviews> findByUserId(Long userId);
    
    // Tìm theo thời gian comment gần đây nhất
    @Query(value = "SELECT * FROM tb_reviews r ORDER BY r.review_date DESC",nativeQuery = true)
    List <Reviews> findRecentReviews();
    
    
}
