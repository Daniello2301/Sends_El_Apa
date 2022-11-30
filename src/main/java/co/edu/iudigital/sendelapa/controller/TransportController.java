 package co.edu.iudigital.sendelapa.controller;

import co.edu.iudigital.sendelapa.converter.TransportConverter;
import co.edu.iudigital.sendelapa.converter.UserConverter;
import co.edu.iudigital.sendelapa.dto.TransportDto;
import co.edu.iudigital.sendelapa.dto.UserDto;
import co.edu.iudigital.sendelapa.exception.*;
import co.edu.iudigital.sendelapa.model.Transport;
import co.edu.iudigital.sendelapa.model.UserApp;
import co.edu.iudigital.sendelapa.service.iface.ITransportService;
import co.edu.iudigital.sendelapa.service.iface.IUserService;
import co.edu.iudigital.sendelapa.utilities.ConstUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 @RestController
@RequestMapping("/transports")
@Api(value = "/transports", tags = {"transports"})
@SwaggerDefinition(tags = { @Tag(name = "Transports", description = "Transports modules management")})
public class TransportController {


    private static final Logger log = LoggerFactory.getLogger(TransportController.class);

    @Autowired
    private ITransportService transportService;

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Get All Transports", response = Transport.class, responseContainer = "List", produces = "application/json", httpMethod = "GET")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<TransportDto> getAll() throws RestException{
        return transportService.getAll();
    }

    @ApiOperation(value = "Get transport by id",
            response = TransportDto.class,
            produces = "application/json",
            httpMethod = "GET")
    @GetMapping("/transport/{id}")
    @Secured({"ROLE_TRANSPORT", "ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<TransportDto> getById(@PathVariable Long id) throws RestException{

        return ResponseEntity.ok().body(transportService.getById(id));
    }

    @ApiOperation(value = "Create new transport", produces = "application/json", httpMethod = "POST")
    @Secured({"ROLE_TRANSPORT", "ROLE_ADMIN"})
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Transport> create(@Valid @RequestBody Transport transport, Authentication authentication) throws RestException {
        try {
            if(!authentication.isAuthenticated())
            {
                throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_AUTHORIZED,
                        HttpStatus.UNAUTHORIZED.value()));
            }
            String username = authentication.getName();
            UserApp userFound = userService.findByUsername(username);
            if(userFound == null)
            {
                throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_AUTHORIZED,
                        HttpStatus.UNAUTHORIZED.value()));
            }
            if(transport.getUser() == null)
            {
                transport.setUser(userFound);
            }
            Transport transportSave = transportService.create(transport);
            return new ResponseEntity<>(transportSave, HttpStatus.CREATED);
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error creating...", ex);
            throw new InternalServerErrorException(
                    ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ConstUtil.MESSAGE_GENERAL,
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


    @ApiOperation(value = "Update transport",
            response = TransportDto.class,
            produces = "application/json",
            httpMethod = "PUT")
    @PutMapping("/transport")
    @Secured({"ROLE_TRANSPORT", "ROLE_ADMIN"})
    public ResponseEntity<TransportDto> update(@RequestBody Transport transport, Authentication authentication) throws RestException{
        try{
            if(!authentication.isAuthenticated())
            {
                throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_AUTHORIZED,
                        HttpStatus.UNAUTHORIZED.value()));
            }
            String userName = authentication.getName();
            UserApp userLogged = userService.findByUsername(userName);
            if(transport == null && userLogged == null) {
                throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_FOUND,
                        HttpStatus.NOT_FOUND.value()));
            }
            if(transport.getUser() == null)
            {
                transport.setUser(userLogged);
            }
            Transport transportSave = transportService.create(transport);
            TransportDto tDto = TransportConverter.TransportToTransportDto(transportSave);
            tDto.setId(transportSave.getId());

            return ResponseEntity.ok().body(tDto);
        }catch (BadRequestException ex){
            throw ex;
        }catch (Exception ex){
            log.error("Error {}", ex);
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ConstUtil.MESSAGE_GENERAL,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }



    @ApiOperation(value = "Disable transport by id",
            response = TransportDto.class,
            produces = "application/json",
            httpMethod = "put")
    @PutMapping("/transport/{id}")
    //@ResponseStatus(code = HttpStatus.OK)
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> delete(@PathVariable Long id) throws RestException {
        TransportDto transportDto =  transportService.getById(id);
        if(transportDto == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()));
        }
        Transport transport = new Transport();

        transport.setId(transportDto.getId());
        transport.setName(transportDto.getName());
        transport.setDescription(transportDto.getDescription());
        transport.setQuantity(transportDto.getQuantity());
        transport.setDateCreate(transportDto.getDateCreate());
        transport.setEnable(false);

        UserApp user = userService.getById(transportDto.getUser_id());
        if(user != null)
        {
            transport.setUser(user);
        }
        transportService.create(transport);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Disabled");
        return ResponseEntity.ok().body(response);
    }


}
