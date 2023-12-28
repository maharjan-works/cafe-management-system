package com.maharjanworks.cafe.serviceImpl;

import com.maharjanworks.cafe.constants.CafeConstants;
import com.maharjanworks.cafe.model.User;
import com.maharjanworks.cafe.repository.UserRepository;
import com.maharjanworks.cafe.service.UserService;

import com.maharjanworks.cafe.utils.CafeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        this.logger.info("Inside signup {}", requestMap);
        try {
            if (validateSignupMap(requestMap)) {
                User user = this.userRepository.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    this.userRepository.save(this.getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Signup Success", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateSignupMap(Map<String,String> requestMap){
      if( requestMap.containsKey("firstName")
                && requestMap.containsKey("lastName")
                && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password"))
      {
          this.logger.info("Data validated {}",requestMap);
          return true;
      }
      return false;
    }


    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.getPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;


    }
}
