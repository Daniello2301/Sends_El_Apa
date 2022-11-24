package co.edu.iudigital.sendelapa.service.iface;

import co.edu.iudigital.sendelapa.model.UserApp;

public interface IUserService   {
    public UserApp findByUsername(String username);
}
