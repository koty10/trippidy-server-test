package dev.koten.trippidy.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public class CategoryDto implements Serializable {
    private String id;
    private String name;
    private Collection<ItemDto> items;

    public CategoryDto(String id, String name, Collection<ItemDto> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    public CategoryDto() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<ItemDto> getItems() {
        return items;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(Collection<ItemDto> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDto entity = (CategoryDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.items, entity.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "items = " + items + ")";
    }
}