package co.edu.iudigital.sendelapa.service.iface;

import co.edu.iudigital.sendelapa.dto.SendDto;
import co.edu.iudigital.sendelapa.exception.RestException;
import co.edu.iudigital.sendelapa.model.Send;


import java.util.List;

public interface ISendService {

    // Get All Sends
    public List<SendDto> getAll() throws RestException;


    // Get all sends by user logged
    public List<SendDto> getByUserAppId(Long id) throws RestException;

    // Create Send
    public SendDto create(SendDto sendDto) throws RestException;

    // Update Send
    public SendDto update(SendDto sendDto, Long id) throws RestException;

    // Get Send By Id
    public SendDto getById(Long id) throws RestException;

    //Activate or Deactivate Send
    public void delete(Long id) throws RestException;
}
