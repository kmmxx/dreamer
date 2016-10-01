package com.dreamer.pattern;

public class Factoryton {

	public interface Producer {
		public ProductKind1 getProductKind1();

		public ProductKind2 getProductKind2();
	}

	public class Producerk1 implements Producer {

		@Override
		public ProductKind1 getProductKind1() {
			// TODO Auto-generated method stub
			return new ProductKind11();
		}

		@Override
		public ProductKind2 getProductKind2() {
			// TODO Auto-generated method stub
			return new ProductKind21();
		}

	}

	public class Producerk2 implements Producer {

		@Override
		public ProductKind1 getProductKind1() {
			// TODO Auto-generated method stub
			return new ProductKind12();
		}

		@Override
		public ProductKind2 getProductKind2() {
			// TODO Auto-generated method stub
			return new ProductKind22();
		}

	}

	public class ProductKind11 implements ProductKind1 {
		public ProductKind11() {

		}
	}

	public class ProductKind12 implements ProductKind1 {

	}

	public class ProductKind21 implements ProductKind2 {

	}

	public class ProductKind22 implements ProductKind2 {

	}

	public interface ProductKind1 {

	}

	public interface ProductKind2 {

	}
}
