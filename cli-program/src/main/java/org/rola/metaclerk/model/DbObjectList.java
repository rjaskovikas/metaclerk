package org.rola.metaclerk.model;

import java.util.ArrayList;

public class DbObjectList<T extends DbObject> extends ArrayList<T> {
    @Override
    public String toString() {
        String result = "{" + T.getStaticTypeName() + "List (size=" + size() + "):";
        for (T obj: this) {
            result += ",\n[" + obj + "]";
        }
        return result + "}";
    }

    public T findByName(String ObjectName) {
        for (T obj: this)
            if (ObjectName.equalsIgnoreCase(obj.getName()))
                return obj;
        return null;
    }

    public void fillWasFound(@SuppressWarnings("SameParameterValue") boolean value) {
        this.forEach(obj->obj.setWasFound(value));
    }
}
