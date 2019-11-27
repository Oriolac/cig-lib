package cat.udl.cig.ecc;

import cat.udl.cig.fields.IntegerPrimeOrderSubgroup;
import cat.udl.cig.fields.PrimeField;
import cat.udl.cig.fields.Ring;
import cat.udl.cig.fields.RingElement;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ECPrimeOrderSubgroupTest {


    private RingElement[] createRingElements(Ring ring) {
        RingElement[] ringElements = new RingElement[2];
        BigInteger bigOne = BigInteger.valueOf(1);
        ringElements[0] = ring.toElement(bigOne);
        ringElements[1] = ring.toElement(bigOne);
        return ringElements;
    }

    //private ArrayList<BigInteger> getCardElements(Ring ring) {

    //}

    @Test
    void getSize() {
        BigInteger EXPONENT = BigInteger.valueOf(10);
        BigInteger GENERATOR = BigInteger.valueOf(6);
        BigInteger MODULE = BigInteger.valueOf(11);
        BigInteger order = BigInteger.valueOf(11);
        Ring ring = (Ring) new PrimeField(MODULE);
        //GeneralEC curve = new GeneralEC(ring, createRingElements(ring), );
        //ECPrimeOrderSubgroup grup = new ECPrimeOrderSubgroup(curve, order );
    }


}