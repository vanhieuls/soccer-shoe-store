//package com.dailycodework.shopping_cart.Data;
//
//import com.dailycodework.shopping_cart.Entity.User;
//import com.dailycodework.shopping_cart.Repository.UserRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
//    UserRepository userRepository;
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        createDefaultUserIfNotExist();
//    }
//
//    @Override
//    public boolean supportsAsyncExecution() {
//        return ApplicationListener.super.supportsAsyncExecution();
//    }
//    private void createDefaultUserIfNotExist(){
//        for(int i=1; i<=5; i++){
//            String defaultEmail = "user"+i+"@gmail.com";
//            if(userRepository.existsByEmail(defaultEmail)){
//                continue;
//            }
//            User user = new User();
//            user.setFirstName("The user");
//            user.setLastName("User"+i);
//            user.setEmail(defaultEmail);
//            user.setPassword("123456");
//
//            userRepository.save(user);
//        }
//    }
//}
