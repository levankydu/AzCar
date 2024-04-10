package com.project.AzCar.Services.Reviews;

import com.project.AzCar.Entities.Reviews.Reviews;
import com.project.AzCar.Entities.Cars.CarInfor;
import com.project.AzCar.Entities.Users.Users;
import com.project.AzCar.Repositories.Cars.CarRepository;
import com.project.AzCar.Repositories.Reviews.ReviewRepository;
import com.project.AzCar.Repositories.Users.UserRepository;
import com.project.AzCar.Services.Users.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ReviewService implements ReviewRepository {

	@Autowired
	private final ReviewRepository reviewRepository;
	private final CarRepository carInforRepository;
	private final UserServices userServices;
	@Autowired
	public ReviewService(ReviewRepository reviewRepository, CarRepository carInforRepository,
			UserServices usersRepository) {
		this.reviewRepository = reviewRepository;
		this.carInforRepository = carInforRepository;
		this.userServices = usersRepository;
	}

	// Lấy tất cả đánh giá cho một xe cụ thể dựa trên carId
	public List<Reviews> findAllReviewsByCarId(long i) {
		return reviewRepository.findByCarInforId(i);
	}

	// Lấy tất cả đánh giá của một người dùng cụ thể dựa trên userId
	public List<Reviews> findAllReviewsByUserId(Long userId) {
		return reviewRepository.findByUserId(userId);
	}
	
	// Lấy tất cả đánh giá theo thời gian gần nhất
	public List<Reviews> findRecentReviews()
	{
		
		return reviewRepository.findRecentReviews();
		
	}


	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public <S extends Reviews> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Reviews> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Reviews> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public Reviews getOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reviews getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reviews getReferenceById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Reviews> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Reviews> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Reviews> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reviews> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reviews> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Reviews> S save(S entity) {
		reviewRepository.save(entity);
		return null;
	}

	@Override
	public Optional<Reviews> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Reviews entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Iterable<? extends Reviews> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Reviews> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Reviews> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Reviews> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <S extends Reviews> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Reviews> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Reviews> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S extends Reviews, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reviews> findByCarInforId(Long carId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reviews> findByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reviews getReviewById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
