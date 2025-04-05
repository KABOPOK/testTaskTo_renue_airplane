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
    Integer[] sa = new Integer[n];
    for (int i = 0; i < n; i++) {
      sa[i] = i;
    }

    int[] rank = new int[n];
    for (int i = 0; i < n; i++) {
      rank[i] = text.charAt(i);
    }

    for (int k = 1; k < n; k *= 2) {
      final int gap = k;
      final int[] currentRank = rank.clone();
      Arrays.sort(sa, (a, b) -> {
        // Compare by rank
        if (currentRank[a] != currentRank[b]) {
          return Integer.compare(currentRank[a], currentRank[b]);
        }
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
          newRank[sa[i]] = newRank[sa[i-1]];
        } else {
          newRank[sa[i]] = newRank[sa[i-1]] + 1;
        }
      }
      rank = newRank;
      if (newRank[sa[n-1]] == n-1) break;
    }
    int[] result = new int[n];
    for (int i = 0; i < n; i++) {
      result[i] = sa[i];
    }
    return result;
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
        return -1;  // Suffix is shorter than pattern
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