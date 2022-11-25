package co.edu.iudigital.sendelapa.controller;

import co.edu.iudigital.sendelapa.converter.UserConverter;
import co.edu.iudigital.sendelapa.dto.UserDto;
import co.edu.iudigital.sendelapa.exception.*;
import co.edu.iudigital.sendelapa.model.UserApp;
import co.edu.iudigital.sendelapa.service.iface.IUserService;
import co.edu.iudigital.sendelapa.utilities.ConstUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/users")
@Api(value = "/users", tags = {"Users"})
@SwaggerDefinition(tags = {
        @Tag(name = "Users", description = "API Users Management")
})
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ApiOperation(value = "Create a new user/ Register new User",
            produces = "application/json",
            httpMethod = "POST")
    @PostMapping("/signup")
    public ResponseEntity<UserApp> create(@RequestBody UserApp user) throws RestException{
        try {
            UserApp userFound = userService.findByUsername( user.getUsername());
            if(userFound != null)
            {
                throw new BadRequestException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        ConstUtil.MESSAGE_ALREADY,
                        HttpStatus.INTERNAL_SERVER_ERROR.value()));
            }

            if(user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            UserApp userSaved = userService.create(user);

            // Send email
            /*String mensaje = "Su usuario: "+usuario.getUsername()+"; password: "+usuario.getPassword();
            String as		unto = "Registro en HelmeIUD";*/
            /*if(usuarioSaved != null) {
                emailService.sendEmail(mensaje, usuario.getUsername(), asunto);
            }*/
            return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
        }catch (BadRequestException ex){
            throw ex;
        }catch (Exception ex){
            log.error("Error creating a user", ex);
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ConstUtil.MESSAGE_GENERAL,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


    // Get User by Id
    @ApiOperation(value = "Get user by Id",
            response = UserDto.class,
            produces = "application/json",
            httpMethod = "GET")
    @GetMapping("/user/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<UserDto> show(@PathVariable Long id) throws RestException {
        UserApp user =  userService.getById(id);
        UserDto userDto = new UserDto();
        userDto = UserConverter.getMapValuesClient(user);

        return ResponseEntity.ok().body(userDto);
    }



    // Get All Users

    @ApiOperation(value = "Get users", produces = "application/json", httpMethod = "GET")
    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<UserDto>> index() throws RestException{

        //List Users using service
        List<UserApp> users = userService.getAll();

        // List Users Dto
        List<UserDto> usersDto = new ArrayList<>();
        //Converter users
        UserConverter.setMapValuesClient(users, usersDto);

        return ResponseEntity.ok().body(usersDto);
    }




    // Get user actually authenticated
    @ApiOperation(value = "Consult data from user login", response = UserDto.class, produces = "application/json", httpMethod = "GET")
    @GetMapping("/user")
    public ResponseEntity<UserDto> userInfo(Authentication authentication) throws RestException{

        if(!authentication.isAuthenticated())
        {
            throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_AUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value()));
        }
        String userName = authentication.getName();
        UserApp user = userService.findByUsername(userName);

        // Validate user exist or found
        if(user == null)
        {
            throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_AUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value()));
        }

        UserDto userDto = new UserDto();

        userDto = UserConverter.getMapValuesClient(user);

        return ResponseEntity.ok().body(userDto);
    }


    // Upload image from user
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile image, Authentication authentication) throws RestException{
        // Create Hash Map to response
        Map<String, Object> response = new HashMap<>();
        // Get user authenticated
        UserApp user = userService.findByUsername(authentication.getName());
        //Validate
        if(!image.isEmpty())
        {
            String imageName = UUID.randomUUID().toString()+"_"+image.getOriginalFilename().replace("","");
            Path path = Paths.get("uploads").resolve(imageName).toAbsolutePath();
            try {
                Files.copy(image.getInputStream(), path);
            } catch (IOException e) {
                response.put("Error", e.getMessage().concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String oldImage = user.getImage();

            if(oldImage != null && oldImage.length() > 0 && !oldImage.startsWith("http"))
            {
                Path oldPath = Paths.get("uploads").resolve(oldImage).toAbsolutePath();
                File oldFileImage = oldPath.toFile();
                if(oldFileImage.exists() && oldFileImage.canRead())
                {
                    oldFileImage.delete();
                }
            }
            user.setImage(imageName);
            userService.create(user);
            response.put("user", user);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }



    //Update User
    @ApiOperation(value = "Update a user", response = UserApp.class, produces = "application/json", httpMethod = "PUT")
    @PutMapping("/user")
    public ResponseEntity<UserApp> update(Authentication authentication, @RequestBody UserApp user) throws RestException{
        try{
            if(!authentication.isAuthenticated())
            {
                throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_AUTHORIZED,
                        HttpStatus.UNAUTHORIZED.value()));
            }
            UserApp userFound = userService.findByUsername(authentication.getName());
            if(userFound == null)
            {
                throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_FOUND,
                        HttpStatus.NOT_FOUND.value()));
            }
            userFound.setDocument(user.getDocument());
            userFound.setName(user.getName());
            userFound.setLastName(user.getLastName());
            userFound.setEmail(user.getEmail());
            userFound.setAddress(user.getAddress());
            userFound.setCity(user.getCity());
            userFound.setBirthDay(user.getBirthDay());
            userFound.setRoles(user.getRoles());
            if(user.getPassword() != null)
            {
                userFound.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userFound));
        } catch (BadRequestException ex) {
            throw ex;
        }catch (Exception ex){
            log.error("Error {}", ex);
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ConstUtil.MESSAGE_GENERAL,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


    // Delete User
    @ApiOperation(value = "Delete user by Id", response = UserDto.class, produces = "application/json", httpMethod = "PUT")
    @PutMapping("/user/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> delete(@PathVariable Long id) throws RestException{
        UserApp user = userService.getById(id);
        if(user == null)
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()));
        }
        user.setEnable(false);
        userService.create(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Disable");
        return ResponseEntity.ok().body(response);
    }


    // Get Image from user
    @GetMapping("/uploads/img/{name:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String name){
        Path path = Paths.get("uploads").resolve(name).toAbsolutePath();
        Resource resource = null;
        try{
            resource = new UrlResource(path.toUri());
            if(!resource.exists())
            {
                try {
                    path = Paths.get("uploads").resolve("default.png").toAbsolutePath();
                    resource = new UrlResource(path.toUri());
                }catch (MalformedURLException ex){
                    log.error("error {}", ex);
                }
            }
        } catch (MalformedURLException e) {
            log.error("error {}", e);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }
}
