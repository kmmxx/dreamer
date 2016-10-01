package com.dreamer.tool.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MFragmentManager {

	private static MFragmentManager mMFragmentManager;

	int root;

	public static MFragmentManager getInstance() {
		if (mMFragmentManager == null) {
			mMFragmentManager = new MFragmentManager();
		}
		return mMFragmentManager;
	}

	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;

	private MFragmentManager() {

	};

	public void setRootView(int id) {
		this.root = id;
	}

	public int getRootView() {
		return root;
	}

	public void openNewFragment(Fragment fragment) {
		int count = mFragmentManager.getBackStackEntryCount();
		while (count-- > 0) {
			mFragmentManager.popBackStack();
		}
		if (fragment != null) {
			getFragmentTransaction().replace(root, fragment);
			commit();
		}
	}

	public void openFragment(Fragment fragment, String repleaced) {
		if (fragment != null) {
			getFragmentTransaction().replace(root, fragment);
			getFragmentTransaction().addToBackStack(repleaced);
			commit();
		}
	}

	public void openSubFragment(String name, Fragment f) {
		if (f != null) {
			getFragmentTransaction().replace(root, f)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.addToBackStack(name).commitAllowingStateLoss();
			mFragmentTransaction = null;
		}
	}

	public void setTransactionAnimation(int animation) {
		getFragmentTransaction().setTransition(animation);
	}

	public void prepare(FragmentActivity f) {
		mFragmentManager = f.getSupportFragmentManager();
	}

	public FragmentTransaction getFragmentTransaction() {
		if (mFragmentTransaction == null) {
			mFragmentTransaction = mFragmentManager.beginTransaction();
		}
		return mFragmentTransaction;
	}

	public FragmentManager getFragmentManager() {
		return mFragmentManager;
	}

	public void add(Fragment fragment, String tag) {
		getFragmentTransaction().add(fragment, tag);
		commit();
	}

	public boolean remove(String tag) {
		Fragment f = mFragmentManager.findFragmentByTag(tag);
		if (f != null) {
			getFragmentTransaction().remove(f);
			commit();
			return true;
		}
		return false;
	}

	public void replace(int id, Fragment f) {
		if (f != null) {
			getFragmentTransaction().replace(id, f);
			commit();
		}

	}

	public void commit() {
		mFragmentTransaction.commit();
		mFragmentTransaction = null;
	}

	public Fragment getFragment(String tag) {
		// TODO Auto-generated method stub
		return mFragmentManager.findFragmentByTag(tag);
	}

}
