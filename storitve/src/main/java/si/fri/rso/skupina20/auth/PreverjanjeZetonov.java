package si.fri.rso.skupina20.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.List;

public class PreverjanjeZetonov {

    private static String secretKey = System.getenv("JWT_SECRET");

    // Preveri žeton in vrne id uporabnika
    public static Boolean verifyToken(String token, List<String> roles){
        try{
            token = token.replace("Bearer ", "");
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWT.require(algorithm).build().verify(token);
            DecodedJWT jwt = JWT.decode(token);
            String tipUporabnika = jwt.getClaim("tipUporabnika").asString();

            if (roles.contains(tipUporabnika)) {
                return true;
            }

            return false;
        } catch (Exception e){
            return false;
        }
    }

    // Pridobi id uporabnika iz žetona
    public static int getUserId(String token){
        // Seznam vseh tipov uporabnikov, ki imajo dostop do storitve
        List roles = List.of("UPORABNIK", "ADMIN", "LASTNIK");
        if(verifyToken(token, roles)){
            token = token.replace("Bearer ", "");
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        }
        return -1;
    }

}
