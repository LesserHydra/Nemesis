package com.lesserhydra.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Represents a collection of elements with weighted probability.
 *
 * @param <E> Element type
 */
public class RandomBag<E> implements Iterable<E> {
	
	private final NavigableMap<Double, E> map = new TreeMap<>();
	private final Random random;
	private double total = 0;
	
	/**
	 * Constructs a new RandomBag with a new Random instance.
	 */
	public RandomBag() { this(new Random()); }
	
	/**
	 * Constructs a new RandomBag with a given Random instance.
	 * @param random the Random instance to use
	 */
	public RandomBag(Random random) { this.random = random; }
	
	/**
	 * Adds an element with the given weight.
	 * @param weight How likely this element is to be chosen relative to others
	 * @param element The element to add
	 */
	public void add(double weight, E element) {
		if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");
		if (element == null) throw new IllegalArgumentException("Element may not be null.");
		total += weight;
		map.put(total, element);
	}
	
	/**
	 * Returns a random element.
	 * @return A random element
	 */
	public E next() {
		if (map.isEmpty()) throw new IllegalStateException("Cannot get from an empty RandomBag.");
		double value = random.nextDouble() * total;
		return map.ceilingEntry(value).getValue();
	}
	
	/**
	 * Checks if there are no elements.
	 * @return True if collection is empty
	 */
	public boolean isEmpty() { return map.isEmpty(); }
	
	/**
	 * Returns the number of contained elements.
	 * @return the number of elements
	 */
	public int size() { return map.size(); }
	
	@Override
	public Iterator<E> iterator() {
		return Collections.unmodifiableCollection(map.values()).iterator();
	}
	
	public Stream<E> stream() {
		return map.values().stream();
	}
	
}
