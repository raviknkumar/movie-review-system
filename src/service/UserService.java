package service;

import config.ApplicationConfig;
import exception.MovieReviewException;
import entities.Review;
import enums.Role;
import entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * service
 * used to handle business logic regarding users
 */
public class UserService {
    List<User> users = new ArrayList<>();
    Scanner scanner;

    public UserService() {
        scanner =new Scanner(System.in);
    }

    public User addUser() throws MovieReviewException {
        System.out.println("To Add User, please enter the Name:");
        User user = new User();
        String name = scanner.nextLine();
        user.setName(name);
        user.setPassword(ApplicationConfig.getDefaultPassword());
        user.setRole(ApplicationConfig.getDefaultRole());

        if(findByUserName(name) != null){
            throw new MovieReviewException("user with name: "+ name + " already exists!");
        }

        users.add(user);
        return user;
    }

    public User addUser(User user) {
        users.add(user);
        return user;
    }

    public User findByUserName(String userName) throws MovieReviewException {
        for (User user : users){
            if(user.getName().equalsIgnoreCase(userName)){
                return user;
            }
        }
        throw new MovieReviewException("No User found with the name: "+ userName);
    }

    public List<User> getAll(){
        return users;
    }

    public List<User> findByRole(Role role){
        if(role == null)
            return users;

        return users.stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

    public void listAll() {
        System.out.println("Listing All Users");
        for (User user: users){
            System.out.println(user);
        }
    }

    public void promoteUserIfApplicable(User user, List<Review> reviews) {
        Role role = user.getRole();
        Role newRole = Role.getRoleByReviewCount(reviews.size());
        if(role != newRole){
            System.out.println("Promoting User: "+ user + " to the role:"+ newRole);
            user.setRole(newRole);
        }
    }
}
