package org.example.userservice.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.client.OrderServiceClient;
import org.example.userservice.dto.UserDto;
import org.example.userservice.error.FeignErrorDecoder;
import org.example.userservice.jpa.UserEntity;
import org.example.userservice.jpa.UserRepository;
import org.example.userservice.vo.ResponseOrder;
import org.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;
    RestTemplate restTemplate;
    OrderServiceClient orderServiceClient;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           Environment env,
                           RestTemplate restTemplate,
                           OrderServiceClient orderServiceClient){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
    }

    // 인증 요청 시 요청 회원의 이메일 및 비밀번호를 체크해주는 함수
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null)
            throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPw(),
                true,true,true,true,
                new ArrayList<>());
    }

    // 회원 저장 기능
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        userDto.setCreatedAt(new Date());

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // 엄격한 매핑

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        // 암호화 시마다 해싱이 발생하여 같은 텍스트도 다시 암호화해주면 다른 값이 출력됩니다.
        userEntity.setEncryptedPw(passwordEncoder.encode(userDto.getPw()));

        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity,UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UsernameNotFoundException("user not found");

        UserDto userDto = new ModelMapper().map(userEntity,UserDto.class);

//        List<ResponseOrder> orders = new ArrayList<>();

//        RestTemplate 사용 - 주소, 요청 메서드, 파라미터, 반환 타입
//        String orderUrl = String.format(Objects.requireNonNull(env.getProperty("order-service.url")),userId); // 주소는 하드 코딩은 위험
//        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });
//
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        // Feign Client 사용
        // try ~ catch 구현
//        List<ResponseOrder> orderList = null;
//        try {
//            orderList = orderServiceClient.getOrders(userId);
//        }catch(FeignException ex) {
//            log.error(ex.getMessage());
//        }

        /* ErrorDecoder */
        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        userDto.setOrders(orderList);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null)
            throw new UsernameNotFoundException("email not found : "+email);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        return userDto;
    }
}
