package club.someoneice.jod.tool

import java.util.LinkedList

class KeyInputHolder {
    val keySet = LinkedHashSet<Int>()

    fun getKeySet(): Set<Int> = keySet
    fun getLast(): Int = this.keySet.last()
    fun putKey(keyIndex: Int): Boolean = this.keySet.add(keyIndex)
    fun removeKey(keyIndex: Int): Boolean = this.keySet.remove(keyIndex)
    fun containsKey(keyIndex: Int): Boolean = this.keySet.contains(keyIndex)
    fun containsAny(keyInput: Set<Int>): Boolean = keyInput.any(this.keySet::contains)

    fun findLastIn(keyInput: Set<Int>): Int {
        val itor = LinkedList(this.keySet).descendingIterator()
        while (itor.hasNext()) {
            val key = itor.next()
            if (key in keyInput) {
                return key
            }
        }

        return -1
    }
}
