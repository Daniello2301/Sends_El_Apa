package co.edu.iudigital.sendelapa.converter;

import co.edu.iudigital.sendelapa.dto.TransportDto;
import co.edu.iudigital.sendelapa.dto.UserDto;
import co.edu.iudigital.sendelapa.model.Transport;
import co.edu.iudigital.sendelapa.model.UserApp;

import java.util.List;
import java.util.stream.Collectors;

public class TransportConverter {

    public static void setMapValuesTransport(List<Transport> transports, List<TransportDto> transportsDto){
        transports.stream().map(transport -> {
            TransportDto cDto = TransportToTransportDto(transport);
            return cDto;
        }).forEach(cDto -> {
            transportsDto.add(cDto);
        });
    }

    public static TransportDto TransportToTransportDto(Transport transport){
        TransportDto tDto = new TransportDto();
        tDto.setId(transport.getId());
        tDto.setName(transport.getName());
        tDto.setDescription(transport.getDescription());
        tDto.setEnable(transport.getEnable());
        tDto.setQuantity(transport.getQuantity());
        tDto.setDateCreate(transport.getDateCreate());
        tDto.setUser_id(transport.getUser().getId());

        return tDto;
    }

}
