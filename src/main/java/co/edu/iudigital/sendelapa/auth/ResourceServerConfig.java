package co.edu.iudigital.sendelapa.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/**
 * Config Servidor de recursos
 * accesos de clientes a los recursos de nuestra app
 * si token es válido
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

    // protección del lado de oath2
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(configurationSource())
                .and()
                .authorizeRequests()
                //se parte desde rutas más específicas a mas generales o genéricas
                // urls abiertas sin autenticación ni autorización
                // pero lo haremos más sencillo con anotaciones @Secured
                .antMatchers(HttpMethod.POST, "/users/signup**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/users/signup**").permitAll()
                .antMatchers(HttpMethod.GET, "/usuarios/uploads/img/**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/user").permitAll()
                .antMatchers(HttpMethod.GET, "/transports").permitAll()
                //.antMatchers(HttpMethod.POST, "/empleados").hasRole("ADMIN")
                .anyRequest()
                .authenticated()// las rutas no especificadas, serán para usuarios autenticados
                .and()
                .httpBasic()
                .disable();
    }

    /**
     * Configuración de los cors
     * cors: croised
     * El Intercambio de Recursos de Origen Cruzado (CORS (en-US))
     * es un mecanismo que utiliza cabeceras HTTP adicionales para
     * permitir que un user agent (en-US) obtenga permiso para acceder
     * a recursos seleccionados desde un servidor, en un origen distinto
     * (dominio) al que pertenece.
     * @return
     */
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT", "PATCH", "OPTIONS"));
        config.setAllowCredentials(true);//permitir credenciales
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // configuramos el filtro en la prioridad mas alta de los filtros de spring
    // se aplique al servidor de auth para generar el token y validarlo para acceder al resto de servicios
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>(new CorsFilter(configurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);// dar un orden bajo: entre más bajo el orden, mayor la precedencia
        // como es el filtro más alto es sufiente para aplicar a todos los controllers
        return bean;
    }

}
