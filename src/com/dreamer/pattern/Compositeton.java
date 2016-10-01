package com.dreamer.pattern;

import java.util.Vector;

public class Compositeton {

	public abstract class View {

	}

	public class GlesFrame extends View {

		Vector vector = new Vector();

		public GlesFrame() {

		}

		public void addView(View view) {
			if (view == null)
				return;
			if (view instanceof GlesFrame) {
				return;
			}
			if (view instanceof GlesView) {
				((GlesView) view).frame = this;
				vector.add(view);
			}

		}

		public void removeView(View view) {
			if (view == null)
				return;
			if (view instanceof GlesFrame) {
				return;
			}
			if (view instanceof GlesView && vector.contains(view)) {
				((GlesView) view).frame = this;
				vector.remove(view);
			}
		}
		
	}

	public class GlesView extends View {
		GlesFrame frame;
	}
}
