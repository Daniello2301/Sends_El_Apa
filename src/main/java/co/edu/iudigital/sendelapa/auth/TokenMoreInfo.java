package co.edu.iudigital.sendelapa.auth;

import java.util.HashMap;
import java.util.Map;

import co.edu.iudigital.sendelapa.model.UserApp;
import co.edu.iudigital.sendelapa.service.iface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;


/**
 * Adicionar info del usuario al token
 * o cualquier otra
 * luego se registra en AuthorizationServerConfig
 *
 */
@Component
public class TokenMoreInfo implements TokenEnhancer{

    @Autowired
    private IUserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserApp usuario = userService.findByUsername(authentication.getName());
        Map<String, Object> info = new HashMap<>();
        info.put("id_usuario", usuario.getId()+"");
        info.put("nombre", usuario.getName());
        info.put("fecha_nacimiento", usuario.getBirthDay()+"");
        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
