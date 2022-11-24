package co.edu.iudigital.sendelapa.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
/**
 * proceso de autenticación por OAuth2: login, etc.
 * todo lo relacionado con el login, autenticación con oauth2, creación del token
 * validación del token, toda esa parte
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

    // en la clase de spring security config lo creamos como un bean
    // entonces podemos usarlo e inyectarlo aqui o cualquier lado
    // igual como lo inyectamos en el main para encriptar las contraseñas
    // aleatorias
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // el que configuramos en el spring security config
    // lo inyectamos aqui con todo aquellso cque configuramos allá en la configuracion
    // del spring security , con lo del userdetailsservice, usuarios, roles, etc.
    // para que este servidor de autorización lo pueda usar para lo que es
    // el proceso de login
    @Autowired
    @Qualifier("authenticacionManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenMoreInfo tokenMoreInfo;

    /*permisos para rutas de acceso*/
    // ruta de login debe ser publica (servicio de autenticación)
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")//permisos usuarios anónimos o no
                .checkTokenAccess("isAuthenticated()");//chequea o valida el token; permiso a endpoint que valida token// acceden solo usuarios autenticados
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()// tipo de almacenamiento
                .withClient("security")// creamos cliente
                .secret(passwordEncoder.encode("555555"))//contraseña y codificamos
                .scopes("read", "write")// scope: permisos que va tener la app
                .authorizedGrantTypes("password", "refresh_token")//tipo de concesión del token, como se va a obtener (hay otros mas)
                // refresh token obtiene token de acceso renovado y poder continuar en los recursos antes que caduque el token
                .accessTokenValiditySeconds(3600)//tiempo de validez o cuando caduca
                .refreshTokenValiditySeconds(3600);// tiempo para el refresh token
        // aqui puedes crear credenciales y demas parametros para más apps
    }

    /**
     * Primero configuramos el endpoint de autorización
     * este lo que hace es realizar todo ese proceso de autenticación y también
     * valida el token y firma, todo ese proceso
     * +
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancer = new TokenEnhancerChain();
        tokenEnhancer.setTokenEnhancers(Arrays.asList(tokenMoreInfo, accessTokenConverter()));
        endpoints.authenticationManager(authenticationManager)// registramos el autenticationManager
                .tokenStore(tokenStore())//opcional pero lo hacemos explicitamente
                .accessTokenConverter(accessTokenConverter())// registramos el accesstokenconverter y creamos el método
                // el cual manipula varias cosas relacionadas con el token, por ejemplo, almecenamiento de datos
                // de autenticación del usuario: roles, usuario, etc u otra información, recuerden que el token
                // tiene información del usuario, por lo cual no debemos en el almacenar en él información sensible
                // porque llega alguien, lo tiene, y lo descifra, como por ejemplo jwt.io
                // este elemento traduce los valores del token codificados, verifica validez del token
                .tokenEnhancer(tokenEnhancer);

    }

    /**
     * se encarga de crear el token y almacenarlo
     * pero JWtAccess trabaja con el, entonces se pone opcional
     * pero lo hacemos explicitamente
     * @return
     */
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    // ponemos de tipo de dato siempre la interface genérica
    // retorna un bean
    // traduce la información del token
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();// por defacto tiene un token storag
    }


}
