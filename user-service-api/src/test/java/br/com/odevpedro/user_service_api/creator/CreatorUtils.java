package br.com.odevpedro.user_service_api.creator;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


public final class CreatorUtils {
    private static final PodamFactory factory = new PodamFactoryImpl();

    private CreatorUtils() {
        throw new UnsupportedOperationException("Classe utilitária");
    }

    //não sabemos qual é o tipo da classe que vamos retornar
    //clazz com "z" para não ter impedimentos do compildador
    public static <T> T generateMock(final Class<T> clazz) {
            return factory.manufacturePojo(clazz);
        }
}
