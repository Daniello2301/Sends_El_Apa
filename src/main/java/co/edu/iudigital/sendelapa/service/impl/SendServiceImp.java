package co.edu.iudigital.sendelapa.service.impl;

import co.edu.iudigital.sendelapa.converter.SendConverter;
import co.edu.iudigital.sendelapa.dto.SendDto;
import co.edu.iudigital.sendelapa.exception.ErrorDto;
import co.edu.iudigital.sendelapa.exception.InternalServerErrorException;
import co.edu.iudigital.sendelapa.exception.NotFoundException;
import co.edu.iudigital.sendelapa.exception.RestException;
import co.edu.iudigital.sendelapa.model.Send;
import co.edu.iudigital.sendelapa.model.Transport;
import co.edu.iudigital.sendelapa.model.UserApp;
import co.edu.iudigital.sendelapa.repository.ISendRepository;
import co.edu.iudigital.sendelapa.repository.ITransportRepository;
import co.edu.iudigital.sendelapa.repository.IUserRepository;
import co.edu.iudigital.sendelapa.service.iface.ISendService;
import co.edu.iudigital.sendelapa.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SendServiceImp implements ISendService {


    private final ISendRepository sendRepository;

    private final ITransportRepository transportRepository;

    private final IUserRepository userRepository;

    @Autowired
    public SendServiceImp(ISendRepository sendRepository, ITransportRepository transportRepository, IUserRepository userRepository) {
        this.sendRepository = sendRepository;
        this.transportRepository = transportRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<SendDto> getAll() throws RestException {
        List<Send> sends = sendRepository.findAll();
        if(sends == null)
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        List<SendDto> sendDtos = new ArrayList<>();
        for (Send send: sends)
        {
            SendDto sDto = SendConverter.SendToSendDto(send);
            sDto.setUser(send.getUser().getId());
            sDto.setTransport(send.getTransport().getId());

            sendDtos.add(sDto);
        }
        return sendDtos;
    }

    @Override
    public List<SendDto> getByUserAppId(Long id) throws RestException {

        List<Send> sends = sendRepository.findByUserAppId(id);
        if(sends == null)
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        List<SendDto> sendsDto = new ArrayList<>();
        for (Send send: sends)
        {
            SendDto sDto = SendConverter.SendToSendDto(send);
            sDto.setUser(send.getUser().getId());
            sDto.setTransport(send.getTransport().getId());

            sendsDto.add(sDto);
        }
        return sendsDto;
    }

    @Override
    public SendDto create(SendDto sendDto) throws RestException {
        if(sendDto == null)
        {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        if(sendDto.getUser() == null && sendDto.getTransport() == null)
        {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        Send sendCreated = SendConverter.SendDtoToSend(sendDto);

        UserApp userFound = userRepository.findById(sendDto.getUser()).get();

        sendCreated.setUser(userFound);

        Transport tFound = transportRepository.findById(sendDto.getTransport()).get();
        tFound.setQuantity(tFound.getQuantity() - 1);
        transportRepository.save(tFound);

        sendCreated.setTransport(tFound);

        Send sendSave = sendRepository.save(sendCreated);
        sendDto.setId(sendSave.getId());

        return sendDto;
    }

    @Override
    public SendDto update(SendDto sendDto, Long id) throws RestException {
        Send sendFound = sendRepository.findById(id).orElse(null);
        UserApp userFound = userRepository.findById(sendDto.getUser()).orElse(null);
        Transport transportFound = transportRepository.findById(sendDto.getTransport()).get();
        if(sendFound == null)
        {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()));
        }
        Send sendUpdate = SendConverter.SendDtoToSend(sendDto);

        sendUpdate.setTransport(transportFound);
        sendUpdate.setUser(userFound);

        sendRepository.save(sendUpdate);
        sendDto.setId(sendUpdate.getId());
        return sendDto;
    }

    @Override
    public SendDto getById(Long id) throws RestException {

        Send sendFound = sendRepository.findById(id).get();
        if(sendFound == null)
        {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()));
        }
        SendDto sendResponse = SendConverter.SendToSendDto(sendFound);
        sendResponse.setTransport(sendFound.getTransport().getId());
        sendResponse.setUser(sendFound.getUser().getId());

        return sendResponse;
    }

    @Override
    public void delete(Long id) throws RestException {
        Send sendFound = sendRepository.findById(id).orElse(null);
        if(sendFound == null)
        {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()));
        }
        sendRepository.delete(sendFound);
    }

}
