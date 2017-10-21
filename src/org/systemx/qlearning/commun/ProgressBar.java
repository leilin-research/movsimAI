package org.systemx.qlearning.commun;

/**
 * Ascii progress meter. On completion this will reset itself, so it can be
 * reused <br />
 * <br />
 * 100% ################################################## |
 */
public class ProgressBar {
	private int oldDoneTotal;
	private int doneTotal;
	private int total;
	private boolean inverse = true;

	public ProgressBar(int total, String name) {
		System.out.print("Progress: [");
		this.total = total;
	}

	public void update(int done) {
		
		if(inverse){
			doneTotal = 50 * (done-total) / total;
		}else{
			doneTotal = 50 * done / total;
		}
		
		if (doneTotal != oldDoneTotal) {
			oldDoneTotal = doneTotal;
			if(done==total || oldDoneTotal >=49){
				System.out.print("=]");
			}else{
				System.out.print("=");
			}
		}
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	
	
}
