package com.dreamer.pattern;

public class Bridgeton {

	abstract public class Abstract {
		Implement imp;

		public void op() {
			imp.opImp();
		}
	}

	abstract public class Implement {

		public abstract void opImp();
	}
}
