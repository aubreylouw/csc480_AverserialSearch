package alouw.csc480.averserialsearch.implementations;

import alouw.csc480.adverserialsearch.interfaces.Tuple;

public class TupleFactory <L, R> {
	public static <L, R> Tuple<L, R> getNewTuple(L leftRef, R rightRef) {
		return new TupleImpl<L, R>(leftRef, rightRef);
	}
}

class TupleImpl <L, R> implements Tuple<L, R> {
	
	final L leftRef;
	final R rightRef;
	
	TupleImpl(L leftReference, R rightReference) {
		this.leftRef = leftReference;
		this.rightRef = rightReference;
	}

	@Override
	public L getLeftValue() {
		return this.leftRef;
	}

	@Override
	public R getRightValue() {
		return this.rightRef;
	}

}
