package pxl.kwops.domain.models;

import java.util.List;

public abstract class Entity {

    protected abstract List<Object> getIdComponents();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) return false;
        Entity other = (Entity) obj;
        return this.getIdComponents().equals(other.getIdComponents());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
