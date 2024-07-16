package com.uniroma3.prog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uniroma3.prog.model.Review;
import com.uniroma3.prog.model.User;
import com.uniroma3.prog.repository.ReviewRepository;
import com.uniroma3.prog.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }
    @Transactional
    public List<Review> getUserReview(String userName){
    	List<Review> result = new ArrayList<>();
    	Iterable<Review> iterable = this.reviewRepository.findAll();
    	for(Review review : iterable) {
    		if(review.getNomeUtente().equals(userName)) {
    			result.add(review);
    		}
    	}
    	return result;
    }
    
    

}
