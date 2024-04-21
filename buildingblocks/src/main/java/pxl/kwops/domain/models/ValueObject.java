package pxl.kwops.domain.models;

import java.util.List;

public abstract class ValueObject {

    protected abstract List<Object> getEqualityComponents();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) return false;
        ValueObject other = (ValueObject) obj;
        return getEqualityComponents().equals(other.getEqualityComponents());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
