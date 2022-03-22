package de.frederik.unitTests.jUnitTests.utils;

import de.pedigreeProject.utils.IndexChanger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class IndexChangerTest {

    private static final List<Object> list = new ArrayList<>();
    private static final Object o1 = new Object();
    private static final Object o2 = new Object();
    private static final Object o3 = new Object();
    private static final Object o4 = new Object();

    static IndexChanger indexChanger;

    @BeforeAll
    static void initChanger() {
        indexChanger = new IndexChanger();
    }

    @BeforeEach
    void setUp() {
        list.clear();
        list.add(o1);
        list.add(o2);
        list.add(o3);
    }

    @Test
    void changeIndex_Increment_OneIndexToRight() {
        indexChanger.changeIndex(o1, list, 1);
        assertSame(list.get(0), o2);
        assertSame(list.get(1), o1);
        assertSame(list.get(2), o3);
    }

    @Test
    void changeIndex_Increment_TwoIndexToRight() {
        indexChanger.changeIndex(o1, list, 2);
        assertSame(list.get(0), o2);
        assertSame(list.get(1), o3);
        assertSame(list.get(2), o1);
    }

    @Test
    void changeIndex_Decrement_OneIndexToLeft() {
        indexChanger.changeIndex(o3, list, -1);
        assertSame(list.get(0), o1);
        assertSame(list.get(1), o3);
        assertSame(list.get(2), o2);
    }

    @Test
    void changeIndex_Decrement_TwoIndexToLeft() {
        indexChanger.changeIndex(o3, list, -2);
        assertSame(list.get(0), o3);
        assertSame(list.get(1), o1);
        assertSame(list.get(2), o2);
    }

    @Test
    void changeIndex_Increment_OutOfBounds() {
        indexChanger.changeIndex(o3, list, 1);
        assertSame(list.get(0), o1);
        assertSame(list.get(1), o2);
        assertSame(list.get(2), o3);
    }

    @Test
    void changeIndex_Decrement_OutOfBounds() {
        indexChanger.changeIndex(o1, list, -1);
        assertSame(list.get(0), o1);
        assertSame(list.get(1), o2);
        assertSame(list.get(2), o3);
    }

    @Test
    void changeIndex_ObjectNotInList() {
        indexChanger.changeIndex(o4, list, 1);
        assertSame(list.get(0), o1);
        assertSame(list.get(1), o2);
        assertSame(list.get(2), o3);
    }

    @Test
    void changeIndex_ObjectIsNull() {
        indexChanger.changeIndex(null, list, 1);
        assertSame(list.get(0), o1);
        assertSame(list.get(1), o2);
        assertSame(list.get(2), o3);
    }

    @Test
    void changeIndex_ListIsNull() {
        indexChanger.changeIndex(o1, null, 1);
        assertSame(list.get(0), o1);
        assertSame(list.get(1), o2);
        assertSame(list.get(2), o3);
    }

    @Test
    void changeIndex_ListAndObjectIsNull() {
        indexChanger.changeIndex(null, null, 1);
        assertSame(list.get(0), o1);
        assertSame(list.get(1), o2);
        assertSame(list.get(2), o3);
    }
}