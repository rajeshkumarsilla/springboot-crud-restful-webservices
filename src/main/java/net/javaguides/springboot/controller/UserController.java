package net.javaguides.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	//get all users
	@GetMapping
	public List<User> getAllUsers()
	{
		System.out.println("in getAllUsers");
		return this.userRepository.findAll();
	}
	
	//get user by id
	@GetMapping("/{id}")
	public User getUserById(@PathVariable(value = "id") long userId)
	{
		System.out.println("in getUserById userId -> "+userId);
		return this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id -> "+userId));
	}
	
	//create user
	@PostMapping
	public User createUser(@RequestBody User user)
	{
		System.out.println("in createUser user -> "+user);
		return this.userRepository.save(user);
	}
	
	//update user by id
	@PutMapping("/{id}")
	public User updateUser(@RequestBody User user,@PathVariable(value = "id") long userId)
	{
		System.out.println("in updateUser user -> "+user+",userId -> "+userId);
		return userRepository.findById(userId)
	      .map(userExisting -> {
	    	  userExisting.setFirstName(user.getFirstName());
	    	  userExisting.setLastName(user.getLastName());
	    	  userExisting.setEmail(user.getEmail());
	    	  return userRepository.save(userExisting);
	      })
	      .orElseThrow(() -> new ResourceNotFoundException("User not found with id -> "+userId));
	}
	
	//delete user by id
	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable(value = "id") long userId)
	{
		System.out.println("in deleteUser userId -> "+userId);
		
		User existingUser = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id -> "+userId));
		//userRepository.deleteById(userId);
		this.userRepository.delete(existingUser);
		return ResponseEntity.ok().build();
	}
}
