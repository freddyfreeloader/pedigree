package de.pedigreeProject.utils;

import java.util.List;

/**
 * Changes the index of member in a List.
 */
public class IndexChanger {

    public IndexChanger() {}

    /**
     * Changes the index of an object in a List.
     * @param object to change the index
     * @param list the object belongs to
     * @param change the number of position to move right(>0) or left (<0)>
     * @param <T> the type of the object
     * @return true if index has changed
     */
    public <T> boolean changeIndex(T object, List<T> list, int change) {

        boolean indexHasChanged = false;

        if (list!=null && list.contains(object)) {
            int indexNew = list.indexOf(object) + change;
            if (indexNew >= 0 && indexNew < list.size()) {

                boolean isRemoved = list.remove(object);
                if (isRemoved) {
                    list.add(indexNew, object);
                    indexHasChanged = true;
                }
            }
        }
        return indexHasChanged;
    }
}
