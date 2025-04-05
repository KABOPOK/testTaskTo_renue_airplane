package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Suffix Array implementation for efficient substring search in O(m log n) time
 * where n is the length of the text and m is the length of the pattern.
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
   * Builds the suffix array using the prefix doubling algorithm.
   *
   * @return The suffix array
   */
  private int[] buildSuffixArray() {
    // Initialize suffix array with positions 0 to n-1
    Integer[] sa = new Integer[n];
    for (int i = 0; i < n; i++) {
      sa[i] = i;
    }

    // Initial character-based ranks
    int[] rank = new int[n];
    for (int i = 0; i < n; i++) {
      rank[i] = text.charAt(i);
    }

    // Sort the suffixes
    for (int k = 1; k < n; k *= 2) {
      // Create a comparator based on current k-distance
      final int gap = k;
      final int[] currentRank = rank.clone();  // Clone the rank to use in comparator

      Arrays.sort(sa, (a, b) -> {
        // Compare by rank
        if (currentRank[a] != currentRank[b]) {
          return Integer.compare(currentRank[a], currentRank[b]);
        }
        // If same rank, compare by rank of next part
        int rankA = (a + gap < n) ? currentRank[a + gap] : -1;
        int rankB = (b + gap < n) ? currentRank[b + gap] : -1;
        return Integer.compare(rankA, rankB);
      });

      // Update ranks
      int[] newRank = new int[n];
      newRank[sa[0]] = 0;

      for (int i = 1; i < n; i++) {
        if (currentRank[sa[i]] == currentRank[sa[i-1]] &&
                sa[i] + gap < n && sa[i-1] + gap < n &&
                currentRank[sa[i] + gap] == currentRank[sa[i-1] + gap]) {
          // Same rank as previous
          newRank[sa[i]] = newRank[sa[i-1]];
        } else {
          // Different rank
          newRank[sa[i]] = newRank[sa[i-1]] + 1;
        }
      }

      // Update rank for next iteration
      rank = newRank;

      // If all suffixes have unique ranks, we're done
      if (newRank[sa[n-1]] == n-1) break;
    }

    // Convert Integer[] to int[]
    int[] result = new int[n];
    for (int i = 0; i < n; i++) {
      result[i] = sa[i];
    }
    return result;
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