package com.lesserhydra.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {
	
	//Unsafe
	/*public static <E> E[] generateArray(int n, Supplier<E> elementSupplier) {
		@SuppressWarnings("unchecked")
		E[] result = (E[]) new Object[n];
		for (int i = 0; i < n; i++) {
			result[i] = elementSupplier.get();
		}
		return result;
	}*/
	
	/**
	 * Generates a fixed size, random access List using the given element supplier.
	 * @param n Number of elements to generate
	 * @param elementSupplier Supplier for generating elements
	 * @return The generated list
	 */
	public static <E> List<E> generateList(int n, Supplier<E> elementSupplier) {
		ArrayList<E> result = new ArrayList<E>(n);
		for (int i = 0; i < n; i++) {
			result.add(elementSupplier.get());
		}
		return result;
	}
	
	/**
	 * Generates an unmodifiable, random access List using the given element supplier.
	 * @param n Number of elements to generate
	 * @param elementSupplier Supplier for generating elements
	 * @return The generated list
	 */
	public static <E> List<E> generateUnmodifiableList(int n, Supplier<E> elementSupplier) {
		return Collections.unmodifiableList(generateList(n, elementSupplier));
	}
	
	public static <C extends Collection<E>, E> C generateCollection(int n, Supplier<E> elementSupplier, Supplier<C> collectionFactory) {
		return Stream.generate(elementSupplier)
				.limit(n)
				.collect(Collectors.toCollection(collectionFactory));
	}
	
}
