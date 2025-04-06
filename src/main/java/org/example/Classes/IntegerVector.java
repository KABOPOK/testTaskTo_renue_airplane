package org.example.Classes;

import java.util.Arrays;
public class IntegerVector {
  private int[] data;
  private int size;
  private static final int DEFAULT_CAPACITY = 10;
  private static final float GROWTH_FACTOR = 1.2f; // нам очень важна память

  public IntegerVector() {
    this(DEFAULT_CAPACITY);
  }
  public IntegerVector(int initialCapacity) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Initial capacity cannot be negative: " + initialCapacity);
    }
    this.data = new int[initialCapacity];
    this.size = 0;
  }

  public void add(int value) {
    ensureCapacity(size + 1);
    data[size++] = value;
  }

  public int get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    return data[index];
  }

  public void set(int index, int value) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    data[index] = value;
  }

  public int size() {
    return size;
  }

  public int capacity() {
    return data.length;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  private void ensureCapacity(int minCapacity) {
    if (minCapacity > data.length) {
      grow(minCapacity);
    }
  }
  private void grow(int minCapacity) {
    int newCapacity = Math.max((int)(data.length * GROWTH_FACTOR), minCapacity);
    data = Arrays.copyOf(data, newCapacity);
  }


  public void clear() {
    size = 0;
  }

  @Override
  public String toString() {
    if (size == 0) {
      return "[]";
    }

    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < size; i++) {
      sb.append(data[i]);
      if (i < size - 1) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

}
