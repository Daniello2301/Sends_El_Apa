package co.edu.iudigital.sendelapa.converter;

import co.edu.iudigital.sendelapa.dto.UserDto;
import co.edu.iudigital.sendelapa.model.UserApp;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static void setMapValuesClient(List<UserApp> users, List<UserDto> usersDto){
        users.stream().map(user -> {
            UserDto cDto = getMapValuesClient(user);
            return cDto;
        }).forEach(cDto -> {
            usersDto.add(cDto);
        });
    }

    public static UserDto getMapValuesClient(UserApp user){
        UserDto uDto = new UserDto();
        uDto.setId(user.getId());
        uDto.setName(user.getName());
        uDto.setLastName(user.getLastName());
        uDto.setEmail(user.getEmail());
        uDto.setBirthDay(user.getBirthDay());
        uDto.setImage(user.getImage());
        uDto.setRoles(user.getRoles()
                .stream().map(r -> r.getName())
                .collect(Collectors.toList())
        );
        uDto.setUsername(user.getUsername());

        return uDto;
    }
}
