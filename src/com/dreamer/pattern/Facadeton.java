package com.dreamer.pattern;

public class Facadeton {

	public class Facade {

		public Facade() {
			SubSystem1 ss1 = new SubSystem1();
			SubSystem2 ss2 = new SubSystem2();
			ss1.op();
			ss2.op();
		}
	}

	public class SubSystem1 {

		public void op() {
			// TODO Auto-generated method stub

		}

	}

	public class SubSystem2 {
		public void op() {
			// TODO Auto-generated method stub

		}
	}
}
