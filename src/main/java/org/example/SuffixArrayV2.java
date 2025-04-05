package org.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SuffixArrayV2 {
  private final String text;
  private final int[] suffixArray;
  private final int[] rowNumber;
  private final int n;

  /**
   * Constructs a suffix array from the text and corresponding row numbers.
   *
   * @param text The concatenated text string
   * @param rowNumber Array indicating which row each character belongs to
   */
  public SuffixArrayV2(String text, int[] rowNumber) {
    this.text = text;
    this.n = text.length();
    this.rowNumber = rowNumber;
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
   * Finds all row numbers where the pattern appears.
   *
   * @param pattern The pattern to search for
   * @return Set of row numbers containing the pattern
   */
  public Set<Integer> findRowsWithPattern(String pattern) {
    Set<Integer> matchingRows = new HashSet<>();

    if (pattern == null || pattern.isEmpty()) {
      return matchingRows;
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
      return matchingRows;
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

    // Collect all unique row numbers where the pattern appears
    for (int i = start; i <= end; i++) {
      int position = suffixArray[i];
      int row = rowNumber[position];
      matchingRows.add(row);
    }

    return matchingRows;
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

    for (int i = 0; i < m; i++) {
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
   * Main method for testing the functionality.
   */
  public static void main(String[] args) {
    // Example with your specific case
    String mergedText = "hello buy banana";
    int[] rowNumbers = new int[mergedText.length()];

    // Map each character to its row
    // "hello" is from row 4
    for (int i = 0; i < 5; i++) {
      rowNumbers[i] = 4;
    }

    // "buy" is from row 5 (including the space)
    for (int i = 5; i < 9; i++) {
      rowNumbers[i] = 5;
    }

    // "banana" is from row 6
    for (int i = 9; i < mergedText.length(); i++) {
      rowNumbers[i] = 6;
    }

    SuffixArrayV2 sa = new SuffixArrayV2(mergedText, rowNumbers);

    // Test pattern search
    String[] patterns = {"hello", "buy", "banana", "a"};
    for (String pattern : patterns) {
      Set<Integer> rows = sa.findRowsWithPattern(pattern);
      System.out.println("Pattern '" + pattern + "' found in rows: " + rows);
    }
  }
}