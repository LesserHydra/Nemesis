package com.lesserhydra.nemesis.util;

import java.util.Spliterator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
	
	public static <T> Stream<T> supplierStream(Supplier<T> next, BooleanSupplier hasNext) {
		return StreamSupport.stream(new SupplierSpliterator<>(next, hasNext), false);
	}
	
	private static class SupplierSpliterator<T> implements Spliterator<T> {
		
		private final Supplier<T> next;
		private final BooleanSupplier hasNext;
		
		public SupplierSpliterator(Supplier<T> next, BooleanSupplier hasNext) {
			this.next = next;
			this.hasNext = hasNext;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super T> action) {
			if (!hasNext.getAsBoolean()) return false;
			action.accept(next.get());
			return true;
		}

		@Override
		public Spliterator<T> trySplit() {
			return null;
		}

		@Override
		public long estimateSize() {
			return Long.MAX_VALUE;
		}

		@Override
		public int characteristics() {
			return 0;
		}
		
	}
	
}
