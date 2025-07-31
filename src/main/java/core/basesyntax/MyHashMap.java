package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;

    MyHashMap() {
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key,V value,Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * 2];
        for (Node<K, V> node : table) {
            if (node == null) {
                continue;
            }
            while (node != null) {
                int index = node.key == null ? 0 :
                        (int) (node.key.hashCode() & 0x7FFFFFFF % newTable.length);
                newTable[index] = new Node<>(node.key, node.value, newTable[index]);
                node = node.next;
            }
        }
        table = newTable;
    }

    @Override
    public void put(K key, V value) {
        if (size > DEFAULT_LOAD_FACTOR * table.length) {
            resize();
        }
        int index = key == null ? 0 : (int) (key.hashCode() & 0x7FFFFFFF % table.length);
        if (table[index] == null) {
            table[index] = new Node<>(key,value,null);
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            Node<K, V> prevNode = currentNode;
            boolean isAdded = false;

            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    isAdded = true;
                    break;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }

            if (!isAdded) {
                if (prevNode != null) {
                    prevNode.next = new Node<>(key,value,null);
                    size++;
                }
            }
        }

    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : (int) (key.hashCode() & 0x7FFFFFFF % table.length);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
