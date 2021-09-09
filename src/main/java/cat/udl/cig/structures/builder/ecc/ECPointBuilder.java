package cat.udl.cig.structures.builder.ecc;

import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.builder.GroupElementBuilder;
import cat.udl.cig.structures.builder.RingElementBuilder;
import cat.udl.cig.structures.ecc.GeneralEC;
import cat.udl.cig.structures.ecc.GeneralECPoint;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Optional;

public class ECPointBuilder implements GroupElementBuilder {

    private final GeneralEC EC;
    private GeneralECPoint point;

    public ECPointBuilder(@Nonnull final GeneralEC ec) {
        EC = ec;
    }

    public RingElementBuilder getRingElementBuilder() {
        return this.EC.getRing().buildElement();
    }

    public ECPointBuilder setXAndLift(@Nonnull final RingElement ringElement) {
        // TODO: It only gets the first point
        ArrayList<? extends GeneralECPoint> points = EC.liftX(ringElement);
        if (points.size()>=1) {
            this.point = points.get(0);
        }
        return this;
    }

    @Override
    public Optional<? extends GeneralECPoint> build() {
        if (point == null)
            return Optional.empty();
        return Optional.of(point);
    }
}
