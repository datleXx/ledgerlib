package com.ledgerlib.core;

import java.util.HashMap;
import java.util.Map;

public class CustomLRUCache<K, V> {
  private int capacity;
  private Map<K, Node<K, V>> map;
  private Node<K, V> head;
  private Node<K, V> tail;

  public CustomLRUCache(int capacity) {
    this.capacity = capacity;
    this.map = new HashMap<>();
    this.head = null;
    this.tail = null;
  }

  public V get(K key) {
    // fetch value from the map
    Node node = map.get(key);
    if (node == null) return null;

    // key exist, move the node up the linked list
    moveToHead(node);
    return (V) node.value;
  }

  public void put(K key, V value) {
    // If existing key value pair -> update value in both the list & map
    if (map.containsKey(key)) {
      Node node = map.get(key);
      node.value = value;
      moveToHead(node);
      return;
    }

    if (map.size() >= capacity) {
      Node removedNode = removeTail();
      if (removedNode != null) map.remove(removedNode.key);
    }

    // If not -> create new Node + insert into map
    Node nodeToInsert = new Node<>(key, value);
    addToHead(nodeToInsert);

    map.put(key, nodeToInsert);
  }

  // Helpers
  private void addToHead(Node node) {
    if (head == null) {
      head = tail = node;
      return;
    }
    node.next = head;
    head.previous = node;
    head = node;
  }

  private void moveToHead(Node node) {
    if (node == head) return;
    if (node.previous != null) node.previous.next = node.next;
    if (node == tail && tail.previous != null) tail = tail.previous;
    node.previous = null;
    head.previous = node;
    node.next = head;
    head = node;
  }

  private Node<K, V> removeTail() {
    if (tail == null) return null;
    Node nodeToRemove = tail;
    if (tail == head) {
      tail = null;
      head = null;
    } else {
      tail.previous.next = null;
      tail = tail.previous;
      nodeToRemove.previous = null;
    }
    return nodeToRemove;
  }

  private static class Node<K, V> {
    K key;
    V value;
    Node<K, V> next;
    Node<K, V> previous;

    public Node(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }
}
