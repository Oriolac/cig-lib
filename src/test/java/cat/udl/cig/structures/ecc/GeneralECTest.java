package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigInteger;
import java.util.ArrayList;

abstract class GeneralECTest {

    GeneralEC generalEC;
    Ring ring;

    @BeforeEach
    void setUp() {
        generalEC = returnGeneralEC();
    }

    protected abstract GeneralEC returnGeneralEC();



}