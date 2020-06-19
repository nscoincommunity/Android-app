package com.stocksexchange.android.utils.extensions

import com.stocksexchange.api.model.rest.SortOrder


/**
 * Searches this list for an element for which [compare] function returns zero
 * using binary search algorithm. If the element is present, [onPresent] callback
 * is invoked with the index of it and the function returns true. If the element
 * is absent, [onAbsent] callback is invoked with the insertion index and returns
 * false.
 *
 * By default, the list is assumed to be in an ascending order. You can specify
 * the order using [sortOrder] parameter.
 *
 * @param sortOrder The order of the list
 * @param onPresent The callback to invoke with the index of the element in case
 * it is present
 * @param onAbsent The callback to invoke with the insertion index of the element
 * in case it is absent
 * @param compare The callback that should return 0 if the passed in element
 * is the value being searched for, -1 if the passed in element is lesser
 * than the element being searched for, +1 if the passed in element is bigger
 * than the element being searched for
 *
 * @return true if present; false otherwise
 */
fun <T> List<T>.binarySearch(
    sortOrder: SortOrder = SortOrder.ASC,
    onPresent: ((Int) -> Unit)? = null,
    onAbsent: ((Int) -> Unit)? = null,
    compare: (T) -> Int
): Boolean {
    if(isEmpty()) {
        onAbsent?.invoke(0)
        return false
    }

    var lowIndex = 0
    var highIndex = lastIndex
    var insertionIndex: Int? = null
    var middleIndex: Int
    var comparisonResult: Int
    val calculateInsertionIndexDueToHighIndexChange: ((Int, Int, Int) -> Int) =
    { lowInd, highInd, middleInd ->
        if(highInd == -1) {
            0
        } else if(lowInd > highInd) {
            middleInd
        } else {
            highInd
        }
    }

    while(lowIndex <= highIndex) {
        middleIndex = (lowIndex + highIndex).ushr(1) // Safe from overflows
        comparisonResult = compare(get(middleIndex))

        if(comparisonResult < 0) {
            when(sortOrder) {
                SortOrder.ASC -> {
                    lowIndex = (middleIndex + 1)
                    insertionIndex = lowIndex
                }

                SortOrder.DESC -> {
                    highIndex = (middleIndex - 1)
                    insertionIndex = calculateInsertionIndexDueToHighIndexChange(
                        lowIndex,
                        highIndex,
                        middleIndex
                    )
                }
            }
        } else if(comparisonResult > 0) {
            when(sortOrder) {
                SortOrder.ASC -> {
                    highIndex = (middleIndex - 1)
                    insertionIndex = calculateInsertionIndexDueToHighIndexChange(
                        lowIndex,
                        highIndex,
                        middleIndex
                    )
                }

                SortOrder.DESC -> {
                    lowIndex = (middleIndex + 1)
                    insertionIndex = lowIndex
                }
            }
        } else {
            onPresent?.invoke(middleIndex)
            return true
        }
    }

    if(insertionIndex != null) {
        onAbsent?.invoke(insertionIndex)
    }

    return false
}


/**
 * Truncates this list to the specific size.
 *
 * @param count The size to truncate to
 *
 * @return The truncated list
 */
fun <T> List<T>.truncate(count: Int): List<T> {
    return if(count >= size) {
        this
    } else {
        take(count)
    }
}


/**
 * Retrieves an index to insert the [itemToInsert] parameter.
 *
 * @param itemToInsert The item to insert
 * @param sortOrder The sort order
 * @param shouldInsertAtEquality Whether to insert the item at
 * the index of the item that is equal
 *
 * @return The index to insert the item
 */
fun <T : Comparable<T>> List<T>.getIndexToInsertAt(
    itemToInsert: T,
    sortOrder: SortOrder,
    shouldInsertAtEquality: Boolean = true
): Int {
    if(isEmpty()) {
        return 0
    }

    val expectedComparisonResults: MutableList<Int> = mutableListOf(
        when(sortOrder) {
            SortOrder.ASC -> 1
            SortOrder.DESC -> -1
        }
    )

    if(shouldInsertAtEquality) {
        expectedComparisonResults.add(0)
    }

    for(index in indices) {
        if(get(index).compareTo(itemToInsert) in expectedComparisonResults) {
            return index
        }
    }

    return size
}


inline fun <T> List<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
    var index = 0

    for(item in this) {
        if(predicate(item)) {
            return index
        }

        index++
    }

    return null
}