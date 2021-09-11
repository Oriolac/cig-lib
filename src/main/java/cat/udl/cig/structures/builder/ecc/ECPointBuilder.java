package cat.udl.cig.structures.builder.ecc;

import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.builder.GroupElementBuilder;
import cat.udl.cig.structures.builder.RingElementBuilder;
import cat.udl.cig.structures.ecc.GeneralEC;
import cat.udl.cig.structures.ecc.GeneralECPoint;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public class ECPointBuilder implements GroupElementBuilder {

    private final GeneralEC EC;
    private GeneralECPoint point;

    public ECPointBuilder(@NotNull final GeneralEC ec) {
        EC = ec;
    }

    public RingElementBuilder getRingElementBuilder() {
        return this.EC.getRing().buildElement();
    }

    public ECPointBuilder setXAndLift(@NotNull final RingElement ringElement) {
        // TODO: It only gets the first point
        ArrayList<? extends GeneralECPoint> points = EC.liftX(ringElement);
        if (points.size()>=1) {
            this.point = points.get(0);
        }
        return this;
    }

    public ECPointBuilder setXYCoordinates(@NotNull RingElement x, @NotNull RingElement y) {
        GeneralECPoint point = new GeneralECPoint(this.EC, x, y);
        this.point = (point.isInfinity()) ? null : point;
        return this;
    }

    public ECPointBuilder infinityPoint() {
        point = EC.getMultiplicativeIdentity();
        return this;
    }

    @Override
    public Optional<? extends GeneralECPoint> build() {
        if (point == null)
            return Optional.empty();
        return Optional.of(point);
    }
}
