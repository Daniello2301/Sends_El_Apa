package co.edu.iudigital.sendelapa.controller;

import co.edu.iudigital.sendelapa.dto.SendDto;
import co.edu.iudigital.sendelapa.dto.TransportDto;
import co.edu.iudigital.sendelapa.exception.BadRequestException;
import co.edu.iudigital.sendelapa.exception.ErrorDto;
import co.edu.iudigital.sendelapa.exception.InternalServerErrorException;
import co.edu.iudigital.sendelapa.exception.RestException;
import co.edu.iudigital.sendelapa.model.Send;
import co.edu.iudigital.sendelapa.model.Transport;
import co.edu.iudigital.sendelapa.model.UserApp;
import co.edu.iudigital.sendelapa.repository.ISendRepository;
import co.edu.iudigital.sendelapa.repository.ITransportRepository;
import co.edu.iudigital.sendelapa.service.iface.ISendService;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sends")
@Api(value = "/sends", tags = {"sends"})
@SwaggerDefinition(tags = { @Tag(name = "sends", description = "Management send in API")})
public class SendController {

    private static final Logger log = LoggerFactory.getLogger(TransportController.class);

    @Autowired
    private ISendService sendService;

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Get All Transports", response = Transport.class, responseContainer = "List", produces = "application/json", httpMethod = "GET")
    @GetMapping
    @Secured({"ROLE_ADMIN"})
    @ResponseStatus(code = HttpStatus.OK)
    public List<SendDto> getAll() throws RestException {
        return sendService.getAll();
    }

    @ApiOperation(value = "Get one Send by Id", response = SendDto.class, produces = "application/json", httpMethod = "GET")
    @GetMapping("/send/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_USER","ROLE_ADMIN"})
    public ResponseEntity<SendDto> getById(@PathVariable Long id) throws RestException{
        return ResponseEntity.ok().body(sendService.getById(id));
    }

    @ApiOperation(value = "Get Sends from user logged", response = SendDto.class, produces = "application/json", httpMethod = "GET")
    @GetMapping("/user")
    @Secured({"ROLE_ADMIN"})
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<SendDto>> getByUserAppId(Authentication authentication) throws RestException{
        if(!authentication.isAuthenticated())
        {
            throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_AUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value()));
        }
        String username = authentication.getName();
        UserApp userLogged = userService.findByUsername(username);

        return ResponseEntity.ok().body(sendService.getByUserAppId(userLogged.getId()));
    }


    @ApiOperation(value = "Create new send", produces = "application/json", httpMethod = "POST")
    /*@Secured({"ROLE_USER", "ROLE_ADMIN"})*/
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public SendDto create(@RequestBody SendDto sendDto, Authentication authentication) throws RestException{
        try{
            if(!authentication.isAuthenticated()){
                throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_AUTHORIZED,
                        HttpStatus.UNAUTHORIZED.value()));
            }
            String userName = authentication.getName();
            UserApp userLogged = userService.findByUsername(userName);
            if(userLogged == null)
            {
                throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_AUTHORIZED,
                        HttpStatus.UNAUTHORIZED.value()));
            }
            if(sendDto.getUser() == null)
            {
                sendDto.setUser(userLogged.getId());
            }
            LocalDate newDate = LocalDate.now();
            sendDto.setDateCreate(newDate);
            sendDto.setDateUpdate(newDate);
            SendDto senSave = sendService.create(sendDto);


            return senSave;
        }catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error creating...", ex);
            throw new InternalServerErrorException(
                    ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ConstUtil.MESSAGE_GENERAL,
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

    }

    @ApiOperation(value = "Update Send by Id", produces = "application/json", httpMethod = "PUT")
    /*@Secured({"ROLE_USER", "ROLE_ADMIN"})*/
    @PutMapping(consumes = "application/json", path = "/send/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_TRANSPORT"})
    public ResponseEntity<SendDto> update(@PathVariable Long id, @RequestBody SendDto sendDto) throws RestException{
        LocalDate newDate = LocalDate.now();
        sendDto.setDateUpdate(newDate);
        SendDto senSave = sendService.create(sendDto);
        return ResponseEntity.ok().body(sendService.update(sendDto, id));
    }


    @ApiOperation(value = "Delete Send by Id", httpMethod = "DELETE")
    @DeleteMapping("/send/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN"})
    public void delete(@PathVariable Long id) throws RestException{
        sendService.delete(id);
    }


}
