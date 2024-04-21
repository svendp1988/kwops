package pxl.kwops.devops.domain;

import lombok.Getter;
import pxl.kwops.domain.models.Contracts;
import pxl.kwops.domain.models.ValueObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Getter
public class Percentage extends ValueObject {
    private final double value;

    public Percentage(double value) {
        Contracts.require(value >= 0 && value <= 1, "Percentage should be between 0 and 1.");

        this.value = value;
    }

    @Override
    protected List<Object> getEqualityComponents() {
        return List.of(getValue());
    }

    @Override
    public String toString() {
        NumberFormat percentageFormat = new DecimalFormat("0.##%", DecimalFormatSymbols.getInstance(Locale.GERMAN));
        return percentageFormat.format(value);
    }

}
