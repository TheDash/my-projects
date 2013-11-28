package objectives;

import dungeon.DGDungeon;


public interface Objective {

	public void completeObjective();
	
	public boolean hasRequirements();
	
	public boolean canObtainRequirements();
	
	public void obtainRequirements();
	
	public boolean isComplete();
}
