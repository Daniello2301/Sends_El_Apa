package co.edu.iudigital.sendelapa.service.iface;

import co.edu.iudigital.sendelapa.dto.SendDto;
import co.edu.iudigital.sendelapa.dto.TransportDto;
import co.edu.iudigital.sendelapa.exception.RestException;
import co.edu.iudigital.sendelapa.model.Transport;

import java.util.List;

public interface ITransportService {

    public List<TransportDto> getAll() throws RestException;

    public TransportDto getById(Long id) throws RestException;

    public Transport create(Transport transport) throws RestException;

    public void delete(Long id) throws RestException;

}
