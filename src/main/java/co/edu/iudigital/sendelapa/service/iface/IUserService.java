package co.edu.iudigital.sendelapa.service.iface;

import co.edu.iudigital.sendelapa.exception.RestException;
import co.edu.iudigital.sendelapa.model.UserApp;

import java.util.List;

public interface IUserService   {
    public UserApp findByUsername(String username);

    public List<UserApp> getAll() throws RestException;

    public UserApp getById(Long id) throws RestException;

    public UserApp create(UserApp user) throws RestException;


}
