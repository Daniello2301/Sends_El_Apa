package co.edu.iudigital.sendelapa.service.impl;

import co.edu.iudigital.sendelapa.converter.SendConverter;
import co.edu.iudigital.sendelapa.converter.TransportConverter;
import co.edu.iudigital.sendelapa.dto.SendDto;
import co.edu.iudigital.sendelapa.dto.TransportDto;
import co.edu.iudigital.sendelapa.exception.ErrorDto;
import co.edu.iudigital.sendelapa.exception.InternalServerErrorException;
import co.edu.iudigital.sendelapa.exception.NotFoundException;
import co.edu.iudigital.sendelapa.exception.RestException;
import co.edu.iudigital.sendelapa.model.Send;
import co.edu.iudigital.sendelapa.model.Transport;
import co.edu.iudigital.sendelapa.repository.ITransportRepository;
import co.edu.iudigital.sendelapa.repository.IUserRepository;
import co.edu.iudigital.sendelapa.service.iface.ITransportService;
import co.edu.iudigital.sendelapa.utilities.ConstUtil;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransportImp implements ITransportService {


    private final ITransportRepository transportRepository;

    private final IUserRepository userRepository;

    @Autowired
    public TransportImp(ITransportRepository transportRepository, IUserRepository userRepository) {
        this.transportRepository = transportRepository;
        this.userRepository = userRepository;
    }



    @Override
    @Transactional(readOnly = true)
    public List<TransportDto> getAll() throws RestException {

        List<Transport> transports = transportRepository.findAll();
        if(transports == null)
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }
        List<TransportDto> transportsDto = new ArrayList<>();

        for (Transport transport: transports)
        {

            transportsDto.add(TransportConverter.TransportToTransportDto(transport));
        }

        return transportsDto;
    }

    @Override
    public TransportDto getById(Long id) throws RestException {

        Transport transportFound = transportRepository.findById(id).orElse(null);

        TransportDto tDto = TransportConverter.TransportToTransportDto(transportFound);

        return tDto;
    }

    @Override
    @Transactional
    public Transport create(Transport transport) throws RestException {

        if(transport == null)
        {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        return transportRepository.save(transport);
    }

    @Override
    public void delete(Long id) throws RestException {
        Transport transportFound = transportRepository.findById(id).get();
        if(transportFound == null){
            throw new NotFoundException(ErrorDto
                    .getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "Not Fund Data",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }
    }
}
