package cat.udl.cig.structures.ecc;

import java.math.BigInteger;

public interface ECSubgroup {

    public BigInteger computeOrder(GeneralECPoint point);
}
