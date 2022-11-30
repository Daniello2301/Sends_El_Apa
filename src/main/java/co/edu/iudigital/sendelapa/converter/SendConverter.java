package co.edu.iudigital.sendelapa.converter;

import co.edu.iudigital.sendelapa.dto.SendDto;
import co.edu.iudigital.sendelapa.model.Send;

public class SendConverter {

    public static Send SendDtoToSend(SendDto sendDto){

        Send newSend = new Send();

        newSend.setId(sendDto.getId());
        newSend.setDescription(sendDto.getDescription());
        newSend.setLarge(sendDto.getLarge());
        newSend.setHeight(sendDto.getHeight());
        newSend.setWidth(sendDto.getWidth());
        newSend.setWeight(sendDto.getWeight());
        newSend.setStatus(sendDto.getStatus());
        newSend.setUbication(sendDto.getUbication());
        newSend.setDateCreate(sendDto.getDateCreate());
        newSend.setDateUpdate(sendDto.getDateUpdate());

        return newSend;
    }

    public static SendDto SendToSendDto(Send send){

        SendDto sDto = new SendDto();

        sDto.setId(send.getId());
        sDto.setDescription(send.getDescription());
        sDto.setLarge(send.getLarge());
        sDto.setHeight(send.getHeight());
        sDto.setWeight(send.getWeight());
        sDto.setWidth(send.getWidth());
        sDto.setStatus(send.getStatus());
        sDto.setUbication(send.getUbication());
        sDto.setDateCreate(send.getDateCreate());
        sDto.setDateUpdate(send.getDateUpdate());

        return sDto;
    }
}
