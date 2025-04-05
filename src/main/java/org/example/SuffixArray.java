package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Suffix Array implementation for efficient substring search in O(m log n) time
 * where n is the length of the text and m is the length of the pattern.
 * Building the suffix array takes O(n^2 log n) time with minimal extra space.
 */
public class SuffixArray {
  private final String text;
  private final int[] suffixArray;
  private final int n;

  /**
   * Constructs a suffix array for the given text.
   *
   * @param text The input text
   */
  public SuffixArray(String text) {
    this.text = text;
    this.n = text.length();
    this.suffixArray = buildSuffixArray();
  }

  /**
   * Builds the suffix array using direct comparison of suffixes.
   * Time complexity: O(n^2 log n) with minimal extra space.
   *
   * @return The suffix array
   */
  private int[] buildSuffixArray() {
    // Initialize suffix array with positions 0 to n-1
    int[] sa = new int[n];
    for (int i = 0; i < n; i++) {
      sa[i] = i;
    }

    // Sort the suffix array using insertion sort (O(n^2))
    // Could be replaced with a more efficient sort if needed
    for (int i = 1; i < n; i++) {
      int key = sa[i];
      int j = i - 1;

      // Move elements of sa[0..i-1] that are greater than key
      // to one position ahead of their current position
      while (j >= 0 && compareSuffixes(sa[j], key) > 0) {
        sa[j + 1] = sa[j];
        j = j - 1;
      }
      sa[j + 1] = key;
    }

    return sa;
  }

  /**
   * Compares two suffixes of the text.
   * Time complexity: O(n) in worst case.
   *
   * @param pos1 Starting position of first suffix
   * @param pos2 Starting position of second suffix
   * @return Negative if suffix1 < suffix2, 0 if equal, positive if suffix1 > suffix2
   */
  private int compareSuffixes(int pos1, int pos2) {
    while (pos1 < n && pos2 < n) {
      int cmp = text.charAt(pos1) - text.charAt(pos2);
      if (cmp != 0) {
        return cmp;
      }
      pos1++;
      pos2++;
    }

    // If we've reached the end of one suffix but not the other
    if (pos1 < n) return 1;  // First suffix is longer
    if (pos2 < n) return -1; // Second suffix is longer
    return 0;                // Both suffixes are identical
  }

  /**
   * Alternate buildSuffixArray implementation using selection sort.
   * This has O(n^2 log n) time complexity with minimal extra space.
   *
   * @return The suffix array
   */
  private int[] buildSuffixArraySelectionSort() {
    // Initialize suffix array with positions 0 to n-1
    int[] sa = new int[n];
    for (int i = 0; i < n; i++) {
      sa[i] = i;
    }

    // Selection sort (O(n²))
    for (int i = 0; i < n - 1; i++) {
      int minIdx = i;
      for (int j = i + 1; j < n; j++) {
        if (compareSuffixes(sa[j], sa[minIdx]) < 0) {
          minIdx = j;
        }
      }

      // Swap the found minimum element with the element at i
      if (minIdx != i) {
        int temp = sa[i];
        sa[i] = sa[minIdx];
        sa[minIdx] = temp;
      }
    }

    return sa;
  }

  /**
   * Finds all occurrences of a pattern in the text in O(m log n) time.
   *
   * @param pattern The pattern to search for
   * @return List of starting positions of the pattern in the text
   */
  public List<Integer> findSubstring(String pattern) {
    List<Integer> result = new ArrayList<>();

    if (pattern == null || pattern.isEmpty()) {
      return result;
    }

    int m = pattern.length();

    // Binary search for the lower bound
    int low = 0;
    int high = n - 1;
    int start = -1;

    while (low <= high) {
      int mid = low + (high - low) / 2;
      int suffixPos = suffixArray[mid];

      int cmp = comparePatternWithSuffix(pattern, suffixPos);

      if (cmp == 0) {
        // Match found
        start = mid;
        high = mid - 1;  // Continue searching for the first occurrence
      } else if (cmp < 0) {
        high = mid - 1;
      } else {
        low = mid + 1;
      }
    }

    // If no match found
    if (start == -1) {
      return result;
    }

    // Find the upper bound (last occurrence)
    low = start;
    high = n - 1;
    int end = start;

    while (low <= high) {
      int mid = low + (high - low) / 2;
      int suffixPos = suffixArray[mid];

      int cmp = comparePatternWithSuffix(pattern, suffixPos);

      if (cmp == 0) {
        // Match found
        end = mid;
        low = mid + 1;  // Continue searching for the last occurrence
      } else if (cmp < 0) {
        high = mid - 1;
      } else {
        low = mid + 1;
      }
    }

    // Collect all occurrences
    for (int i = start; i <= end; ++i) {
      result.add(suffixArray[i]);
    }

    return result;
  }

  /**
   * Compare a pattern with a suffix of the text.
   *
   * @param pattern The pattern
   * @param suffixPos The starting position of the suffix
   * @return Negative if pattern < suffix, 0 if equal, positive if pattern > suffix
   */
  private int comparePatternWithSuffix(String pattern, int suffixPos) {
    int m = pattern.length();

    for (int i = 0; i < m; ++i) {
      if (suffixPos + i >= n) {
        return -1;  // Suffix is shorter than pattern
      }

      int cmp = pattern.charAt(i) - text.charAt(suffixPos + i);
      if (cmp != 0) {
        return cmp;
      }
    }
    return 0;  // Pattern matches the beginning of this suffix
  }

  /**
   * Gets the constructed suffix array.
   *
   * @return The suffix array
   */
  public int[] getSuffixArray() {
    return Arrays.copyOf(suffixArray, n);
  }
}