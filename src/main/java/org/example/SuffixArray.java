package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuffixArray {
  private final String text;
  private final int[] suffixArray;
  private final int n;

  public SuffixArray(String text) {
    this.text = text;
    this.n = text.length();
    this.suffixArray = buildSuffixArray();
  }

  private int[] buildSuffixArray() {
    int[] sa = new int[n];
    for (int i = 0; i < n; i++) {
      sa[i] = i;
    }
    for (int i = 1; i < n; i++) {
      int key = sa[i];
      int j = i - 1;
      while (j >= 0 && compareSuffixes(sa[j], key) > 0) {
        sa[j + 1] = sa[j];
        j = j - 1;
      }
      sa[j + 1] = key;
    }
    return sa;
  }
  private int compareSuffixes(int pos1, int pos2) {
    while (pos1 < n && pos2 < n) {
      int cmp = text.charAt(pos1) - text.charAt(pos2);
      if (cmp != 0) {
        return cmp;
      }
      pos1++;
      pos2++;
    }
    if (pos1 < n) return 1;
    if (pos2 < n) return -1;
    return 0;
  }

  private int[] buildSuffixArraySelectionSort() {
    int[] sa = new int[n];
    for (int i = 0; i < n; i++) {
      sa[i] = i;
    }

    for (int i = 0; i < n - 1; i++) {
      int minIdx = i;
      for (int j = i + 1; j < n; j++) {
        if (compareSuffixes(sa[j], sa[minIdx]) < 0) {
          minIdx = j;
        }
      }

      if (minIdx != i) {
        int temp = sa[i];
        sa[i] = sa[minIdx];
        sa[minIdx] = temp;
      }
    }

    return sa;
  }

  public List<Integer> findSubstring(String pattern) {
    List<Integer> result = new ArrayList<>();

    if (pattern == null || pattern.isEmpty()) {
      return result;
    }

    int m = pattern.length();
    int low = 0;
    int high = n - 1;
    int start = -1;

    while (low <= high) {
      int mid = low + (high - low) / 2;
      int suffixPos = suffixArray[mid];

      int cmp = comparePatternWithSuffix(pattern, suffixPos);

      if (cmp == 0) {
        start = mid;
        high = mid - 1;
      } else if (cmp < 0) {
        high = mid - 1;
      } else {
        low = mid + 1;
      }
    }

    if (start == -1) {
      return result;
    }

    low = start;
    high = n - 1;
    int end = start;

    while (low <= high) {
      int mid = low + (high - low) / 2;
      int suffixPos = suffixArray[mid];

      int cmp = comparePatternWithSuffix(pattern, suffixPos);

      if (cmp == 0) {
        end = mid;
        low = mid + 1;
      } else if (cmp < 0) {
        high = mid - 1;
      } else {
        low = mid + 1;
      }
    }

    for (int i = start; i <= end; ++i) {
      result.add(suffixArray[i]);
    }

    return result;
  }

  private int comparePatternWithSuffix(String pattern, int suffixPos) {
    int m = pattern.length();

    for (int i = 0; i < m; ++i) {
      if (suffixPos + i >= n) {
        return -1;
      }

      int cmp = pattern.charAt(i) - text.charAt(suffixPos + i);
      if (cmp != 0) {
        return cmp;
      }
    }
    return 0;
  }

  public int[] getSuffixArray() {
    return Arrays.copyOf(suffixArray, n);
  }
}