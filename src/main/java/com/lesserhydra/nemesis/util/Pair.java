package com.lesserhydra.nemesis.util;

/**
 * Represents a pair of elements.
 *
 * @param <L> Left element type
 * @param <R> Right element type
 */
public interface Pair<L, R> {
	
	/**
	 * Returns the left value in the pair.
	 * @return Left value
	 */
	public L getLeft();
	
	/**
	 * Returns the right value in the pair.
	 * @return Right value
	 */
	public R getRight();
	
	/**
	 * Optional operation that sets the left value to the given value. Throws an exception by default.
	 * @param value New left value
	 * @throws UnsupportedOperationException If the implementation does not support this opperation
	 */
	public default void setLeft(L value) {
		throw new UnsupportedOperationException("This pair implementation does not support setting of the left value.");
	}
	
	/**
	 * Optional operation that sets the right value to the given value. Throws an exception by default.
	 * @param value New right value
	 * @throws UnsupportedOperationException If the implementation does not support this opperation
	 */
	public default void setRight(R value) {
		throw new UnsupportedOperationException("This pair implementation does not support setting of the right value.");
	}
	
}
