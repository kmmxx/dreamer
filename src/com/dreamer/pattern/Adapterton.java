package com.dreamer.pattern;

public class Adapterton {

	public class ProductImp extends ProductAdapter {
		@Override
		public void consume() {

		}
	}

	public abstract class ProductAdapter implements Product {

		@Override
		public void produce() {
			// TODO Auto-generated method stub

		}

		@Override
		public void transport() {
			// TODO Auto-generated method stub

		}

		@Override
		public void save() {
			// TODO Auto-generated method stub

		}

		@Override
		public void consume() {
			// TODO Auto-generated method stub

		}

	}

	public interface Product {
		public void produce();

		public void transport();

		public void save();

		public void consume();
	}
}
