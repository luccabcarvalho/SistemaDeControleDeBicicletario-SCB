package com.example.aluguel_ms.aluguel.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AluguelModelTest {
    @Test
    void testGettersAndSetters() {
        Aluguel a = new Aluguel();
        a.setId(1);
        a.setCiclista(2);
        a.setBicicleta(3);
        a.setTrancaInicio(4);
        a.setTrancaFim(5);
        LocalDateTime now = LocalDateTime.now();
        a.setHoraInicio(now);
        a.setHoraFim(now.plusHours(2));
        a.setCobranca(6);
        assertEquals(1, a.getId());
        assertEquals(2, a.getCiclista());
        assertEquals(3, a.getBicicleta());
        assertEquals(4, a.getTrancaInicio());
        assertEquals(5, a.getTrancaFim());
        assertEquals(now, a.getHoraInicio());
        assertEquals(now.plusHours(2), a.getHoraFim());
        assertEquals(6, a.getCobranca());
    }

    @Test
    void testConstrutorPadrao() {
        Aluguel a = new Aluguel();
        assertNotNull(a);
    }
}
